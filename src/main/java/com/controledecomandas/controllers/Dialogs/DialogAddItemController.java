package com.controledecomandas.controllers.Dialogs;

import com.controledecomandas.database.dao.ItemDao;
import com.controledecomandas.models.Item;
import com.controledecomandas.textFieldsValidators.NumberField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DialogAddItemController implements Initializable {


    @FXML
    private ListView<Item> listViewItems;

    @FXML
    private NumberField textFieldQuantity;

    @FXML
    private Label labelItem;

    private boolean buttonConfirmCheck = false;

    private Stage dialogStage;

    private ItemDao itemDao = new ItemDao();

    private Item item;

    private List<Item> itemList;

    private boolean isUpdate = false;


    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    @FXML
    public void handleButtonConfirm() {

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
        if(isUpdate) {
            this.textFieldQuantity.setText(String.valueOf(item.getQuantity()));
            Item itemFiltred = itemList.stream().filter(i -> i.getId() == item.getId()).collect(Collectors.toList()).get(0);
            int index = itemList.indexOf(itemFiltred);
            listViewItems.scrollTo(index);
            listViewItems.getSelectionModel().select(index);
            listViewItems.setMouseTransparent( true );
            listViewItems.setFocusTraversable( false );
        }
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
