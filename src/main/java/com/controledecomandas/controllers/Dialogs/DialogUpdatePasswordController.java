package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.User;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogUpdatePasswordController implements Initializable {
    @FXML
    private JFXPasswordField passFieldOld;

    @FXML
    private JFXPasswordField passFieldNew;

    @FXML
    private JFXPasswordField passFieldConfirm;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;

    private User user;

    private UserDao userDao = new UserDao();

    @FXML
    public void handleButtonConfirm() {
        boolean validate =
                passFieldOld.validate() &&
                passFieldNew.validate() &&
                passFieldConfirm.validate();
        if(validate) {
            try {
                if (!passFieldConfirm.getText().equals(passFieldNew.getText())) {
                    throw new Exception("Repita corretamente a nova senha!");
                }
                userDao.updatePassword(user, passFieldOld.getText(), passFieldConfirm.getText());
                buttonConfirmCheck = true;
                dialogStage.close();

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

        @FXML
    public void handleButtonCancel() {
        dialogStage.close();
        buttonConfirmCheck = false;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmClicked() {
        return buttonConfirmCheck;
    }

    public void setButtonConfirmCheck(boolean buttonConfirmCheck) {
        this.buttonConfirmCheck = buttonConfirmCheck;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Este campo precisa ser prenchido!");
        passFieldOld.getValidators().add(requiredFieldValidator);
        passFieldNew.getValidators().add(requiredFieldValidator);
        passFieldConfirm.getValidators().add(requiredFieldValidator);
    }
}
