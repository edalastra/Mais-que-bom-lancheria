package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.Item;
import com.controledecomandas.models.User;
import com.controledecomandas.textFieldsValidators.CurrencyField;
import com.controledecomandas.textFieldsValidators.NumberField;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.StringLengthValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class DialogInsertUserController implements Initializable {

    @FXML
    private JFXTextField textFieldUserFirstName;

    @FXML
    private JFXTextField textFieldUserEmail;

    @FXML
    private NumberField textFieldUserZipcode;

    @FXML
    private JFXTextField textFieldUserLastName;

    @FXML
    private NumberField textFieldUserTelephone;

    @FXML
    private JFXTextField textFieldUserAddress;

    @FXML
    private JFXPasswordField passFieldUserConfirm;

    @FXML
    private JFXPasswordField passFieldUser;

    @FXML
    private JFXRadioButton radioBtnWorker;

    @FXML
    private CurrencyField salary;

    @FXML
    private JFXRadioButton radioBtnAdmin;

    @FXML
    private ToggleGroup access;


    private boolean update;

    private User user;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;

    @FXML
    public void handleButtonConfirm() {

        boolean validFields = textFieldUserFirstName.validate()
                && textFieldUserLastName.validate()
                && textFieldUserEmail.validate()
                && textFieldUserTelephone.validate()
                && textFieldUserZipcode.validate()
                && textFieldUserAddress.validate()
                && passFieldUser.validate()
                && passFieldUserConfirm.validate();

        if(validFields) {
            user.setEmail(textFieldUserEmail.getText());
            user.setPassword(passFieldUserConfirm.getText());
            RadioButton radio = (RadioButton) access.getSelectedToggle();
            user.setAccess(radio.getText().equals("Gerente"));
            user.setFirstName(textFieldUserFirstName.getText());
            user.setLastName(textFieldUserLastName.getText());
            user.setTelephone(textFieldUserTelephone.getText());
            user.setSalary(new BigDecimal(salary.getAmount()));
            user.setAddress(textFieldUserAddress.getText());
            user.setZipcode(textFieldUserZipcode.getText());
            buttonConfirmCheck = true;
            dialogStage.close();
        }

    }

    public void setUser(User user) {
        this.user = user;
        if(update) {
            textFieldUserEmail.setDisable(true);
            salary.setDisable(!user.getAccess());
            passFieldUser.getParent().setVisible(false);
            passFieldUserConfirm.getParent().setVisible(false);
            radioBtnAdmin.setSelected(true);
            radioBtnAdmin.setSelected(!user.getAccess());
            radioBtnWorker.setDisable(!user.getAccess());
            radioBtnAdmin.setDisable(!user.getAccess());

            textFieldUserFirstName.setText(user.getFirstName());
            textFieldUserLastName.setText(user.getLastName());
            textFieldUserEmail.setText(user.getEmail());
            salary.setText("R$ " + user.getSalary());
            textFieldUserTelephone.setText(user.getTelephone());
            textFieldUserZipcode.setText(user.getZipcode());
            textFieldUserAddress.setText(user.getAddress());


        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDao userDao = new UserDao();
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        requiredFieldValidator.setMessage("Este campo não pode estar vazio");
        textFieldUserFirstName.getValidators().add(requiredFieldValidator);
        textFieldUserLastName.getValidators().add(requiredFieldValidator);
        textFieldUserEmail.getValidators().add(requiredFieldValidator);
        textFieldUserTelephone.getValidators().add(requiredFieldValidator);
        textFieldUserZipcode.getValidators().add(requiredFieldValidator);
        textFieldUserAddress.getValidators().add(requiredFieldValidator);

        salary.getValidators().add(requiredFieldValidator);

        if(!update) {
            passFieldUser.getValidators().add(requiredFieldValidator);
            passFieldUserConfirm.getValidators().add(requiredFieldValidator);
        }

        RegexValidator telephoneValidator = new RegexValidator("Telefone inválido");
        telephoneValidator.setRegexPattern("^[0-9]{11,12}$");
        textFieldUserTelephone.getValidators().add(telephoneValidator);

        RegexValidator zipcodeValidator = new RegexValidator("CEP inválido");
        zipcodeValidator.setRegexPattern("^[0-9]{8}$");
        textFieldUserZipcode.getValidators().add(zipcodeValidator);

        RegexValidator emailValidator = new RegexValidator("Digite um email válido");
        emailValidator.setRegexPattern("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        textFieldUserEmail.getValidators().add(emailValidator);




        try {
            if(userDao.countAdminUsers() == 0) {
                //radioBtnWorker.setSelected(false);
                radioBtnAdmin.setSelected(true);
                radioBtnWorker.setDisable(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isButtonConfirmClicked() {
        return buttonConfirmCheck;
    }

    public void setButtonConfirmCheck(boolean buttonConfirmCheck) {
        this.buttonConfirmCheck = buttonConfirmCheck;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
