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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class DialogOrderController implements Initializable {


    @FXML
    private NumberField textFieldBartableId;

    private boolean buttonConfirmCheck = false;

    @FXML
    private Button buttonCancel;

    private Stage dialogStage;

    private Order order;

    @FXML
    public void handleButtonConfirm() {
        if(textFieldBartableId.validate()) {
            BartableDao bartableDao = new BartableDao();
            try {
                Bartable bartable = bartableDao.getById(Integer.parseInt(textFieldBartableId.getText()));
                order.setBartable(bartable);
                buttonConfirmCheck = true;
                dialogStage.close();
            } catch (Exception e) {
                textFieldBartableId.setText("");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }

    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @FXML
    public void  handleButtonCancel() {
        dialogStage.close();

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
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator("Digite o número da mesa!");
        textFieldBartableId.getValidators().add(requiredFieldValidator);
    }
}
