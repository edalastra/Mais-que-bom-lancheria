package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.Order;
import com.controledecomandas.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class DialogUpdatePasswordController {
    @FXML
    private PasswordField passFieldOld;

    @FXML
    private PasswordField passFieldNew;

    @FXML
    private PasswordField passFieldConfirm;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;

    private User user;

    private UserDao userDao = new UserDao();

    @FXML
    public void handleButtonConfirm() {
        try {
            if(!passFieldConfirm.getText().equals(passFieldNew.getText())) {
                throw new Exception("Repita corretamente a nova senha!");
            }
            userDao.updatePassword(user,passFieldOld.getText(), passFieldNew.getText());
            buttonConfirmCheck = true;
            dialogStage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

    }

        @FXML
    public void handleButtonCancel() {
        dialogStage.close();

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

}
