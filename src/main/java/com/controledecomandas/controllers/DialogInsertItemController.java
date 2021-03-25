package com.controledecomandas.controllers;

import com.controledecomandas.models.Item;
import com.controledecomandas.textFieldsValidators.CurrencyField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class DialogInsertItemController implements Initializable {

    @FXML
    private Label labelMain;

    @FXML
    private TextField textFieldItemDescription;

    @FXML
    private TextField textFieldItemMaker;

    @FXML
    private CurrencyField moneyFieldItemCost;

    @FXML
    private CurrencyField moneyFieldItemPrice;

    @FXML
    private ToggleGroup category;

    @FXML
    private RadioButton radioButtonComida;

    @FXML
    private RadioButton radioButtonBebida;

    private boolean update;

    private Item item;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;



    @FXML
    public void handleButtonConfirm() {
        item.setDescription(textFieldItemDescription.getText());
        item.setMaker(textFieldItemMaker.getText());
        RadioButton radio = (RadioButton) category.getSelectedToggle();
        item.setCategory(radio.getText().toLowerCase(Locale.ROOT));
        item.setCost(new BigDecimal(moneyFieldItemCost.getAmount()));
        item.setPrice(new BigDecimal(moneyFieldItemPrice.getAmount()));
        buttonConfirmCheck = true;
        dialogStage.close();
        try {
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

    }

    public boolean isButtonConfirmClicked() {
        return buttonConfirmCheck;
    }


    @FXML
    public void  handleButtonCancel() {
        dialogStage.close();

    }

    public void setItem(Item item) {
        this.item = item;
        if(update) {
            textFieldItemDescription.setText(item.getDescription());
            textFieldItemMaker.setText(item.getMaker());
            moneyFieldItemCost.setAmount(item.getCost().doubleValue());
            moneyFieldItemPrice.setAmount(item.getPrice().doubleValue());
            radioButtonComida.setSelected(item.getCategory().equals("comida"));
            radioButtonBebida.setSelected(item.getCategory().equals("bebida"));
        }
    }


    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmCheck() {
        return buttonConfirmCheck;
    }

    public void setButtonConfirmCheck(boolean buttonConfirmCheck) {
        this.buttonConfirmCheck = buttonConfirmCheck;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public Item getItem() {
        return item;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(update) {
            labelMain.setText("Alterar Item");
        }
    }
}
