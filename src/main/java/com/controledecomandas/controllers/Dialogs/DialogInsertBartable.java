package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.BartableDao;
import com.controledecomandas.models.Bartable;
import com.controledecomandas.models.Order;
import com.controledecomandas.textFieldsValidators.NumberField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DialogInsertBartable implements Initializable {

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
        if(numberFieldCapacity.validate()) {
            bartable.setCapacity(numberFieldCapacity.getValue());
            buttonConfirmCheck = true;
            dialogStage.close();
        }

    }


    @FXML
    public void  handleButtonCancel() {
        dialogStage.close();
        buttonConfirmCheck = false;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RequiredFieldValidator requiredFieldValidator =  new RequiredFieldValidator("Digite um valor");
        numberFieldCapacity.getValidators().add(requiredFieldValidator);
    }
}
