package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.Item;
import com.controledecomandas.models.User;
import com.controledecomandas.textFieldsValidators.CurrencyField;
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
    private TextField textFieldUserFirstName;

    @FXML
    private TextField textFieldUserEmail;

    @FXML
    private TextField textFieldUserZipcode;

    @FXML
    private TextField textFieldUserLastName;

    @FXML
    private TextField textFieldUserTelephone;

    @FXML
    private TextField textFieldUserAddress;

    @FXML
    private CurrencyField textFieldUserSalary;

    @FXML
    private PasswordField passwordFieldUser;

    @FXML
    private PasswordField passwordFieldUserConfirm;

    @FXML
    private ToggleGroup access;

    @FXML
    private RadioButton radioBtnWorker;

    @FXML
    private RadioButton radioBtnAdmin;

    @FXML
    private TextField inputUserCPassword;


    private boolean update;

    private User user;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;

    @FXML
    public void handleButtonConfirm() {
        user.setEmail(textFieldUserEmail.getText());
        user.setPassword(passwordFieldUser.getText());
        RadioButton radio = (RadioButton) access.getSelectedToggle();
        user.setAccess(radio.getText().equals("Gerente"));
        user.setFirstName(textFieldUserFirstName.getText());
        user.setLastName(textFieldUserLastName.getText());
        user.setTelephone(textFieldUserTelephone.getText());
        user.setSalary(new BigDecimal(textFieldUserSalary.getAmount()));
        user.setAddress(textFieldUserAddress.getText());
        user.setZipcode(textFieldUserZipcode.getText());

        buttonConfirmCheck = true;
        dialogStage.close();

    }

    public void setUser(User user) {
        this.user = user;
        if(update) {
            textFieldUserEmail.setDisable(true);
            textFieldUserSalary.setDisable(!user.getAccess());
            passwordFieldUser.getParent().setVisible(false);
            passwordFieldUserConfirm.getParent().setVisible(false);
            radioBtnAdmin.setSelected(true);
            radioBtnAdmin.setSelected(!user.getAccess());
            radioBtnWorker.setDisable(!user.getAccess());
            radioBtnAdmin.setDisable(!user.getAccess());

            textFieldUserFirstName.setText(user.getFirstName());
            textFieldUserLastName.setText(user.getLastName());
            textFieldUserEmail.setText(user.getEmail());
            textFieldUserSalary.setText("R$ " + user.getSalary());
            textFieldUserTelephone.setText(user.getTelephone());
            textFieldUserZipcode.setText(user.getZipcode());
            textFieldUserAddress.setText(user.getAddress());

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDao userDao = new UserDao();
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
