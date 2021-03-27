package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.BartableDao;
import com.controledecomandas.models.Bartable;
import com.controledecomandas.models.Order;
import com.controledecomandas.textFieldsValidators.NumberField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogInsertBartable {

    @FXML
    private NumberField numberFieldCapacity;

    private boolean buttonConfirmCheck = false;

    @FXML
    private Button buttonCancel;

    private Stage dialogStage;

    private Bartable bartable;

    private boolean update;

    @FXML
    public void handleButtonConfirm() {
        bartable.setCapacity(numberFieldCapacity.getValue());
        buttonConfirmCheck = true;
        dialogStage.close();
    }


    @FXML
    public void  handleButtonCancel() {
        dialogStage.close();

    }

    public Bartable getBartable() {
        return bartable;
    }

    public void setBartable(Bartable bartable) {
        this.bartable = bartable;
        if(update) {
            numberFieldCapacity.setValue(bartable.getCapacity());
        }
    }

    public void setUpdate(boolean update) {
        this.update = update;
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
