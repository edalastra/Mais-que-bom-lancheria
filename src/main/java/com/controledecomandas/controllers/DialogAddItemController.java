package com.controledecomandas.controllers;

import com.controledecomandas.database.dao.BartableDao;
import com.controledecomandas.database.dao.ItemDao;
import com.controledecomandas.models.Bartable;
import com.controledecomandas.models.Item;
import com.controledecomandas.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DialogAddItemController implements Initializable {


    @FXML
    private ListView<Item> listViewItems;

    @FXML
    private TextField textFieldQuantity;

    @FXML
    private Label labelItem;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;

    private ItemDao itemDao = new ItemDao();

    private Item item;

    private List<Item> itemList;



    @FXML
    public void handleButtonConfirm() {
        System.out.println("confirm");
        item.setQuantity(Integer.parseInt(textFieldQuantity.getText()));

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
        itemList = itemDao.list();
        ObservableList obsItemList = FXCollections.observableArrayList(itemList);
        listViewItems.setItems(obsItemList);

        listViewItems.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.selectItemListView((Item) newValue)
            );
    }

    private void selectItemListView(Item item){
        if (item != null) {
            this.item.setId(item.getId());
            labelItem.setText(item.getDescription());
        }
    }
}
