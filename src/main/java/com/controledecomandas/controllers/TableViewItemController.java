package com.controledecomandas.controllers;

import com.controledecomandas.controllers.Dialogs.DialogInsertItemController;
import com.controledecomandas.controllers.Dialogs.DialogOrderController;
import com.controledecomandas.database.dao.ItemDao;
import com.controledecomandas.models.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableViewItemController implements Initializable {
    @FXML
    private TableView<Item> tableViewFoods;

    @FXML
    private TableColumn<Item, String> columnDescription;

    @FXML
    private TableColumn<Item, String> columnCategory;

    @FXML
    private Label labelDescription;

    @FXML
    private Label labelMaker;

    @FXML
    private Label labelPrice;

    @FXML
    private Label labelCost;

    List<Item> itemList = new ArrayList<>();

    ItemDao itemDao = new ItemDao();

    @FXML
    public void handleButtonInsert() throws IOException {
        Item item = new Item();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneAddItemDialog(item, false);
        if (buttonConfirmedClicked) {
            try {
                boolean inserted = itemDao.insert(item);
                if (inserted) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Item inserido com sucesso!");
                    alert.show();
                }
                loadTableItem();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro! " + e.getMessage());
                alert.show();
            }
        }
    }

    @FXML
    public void handleButtonUpdate() throws IOException {
        Item item = tableViewFoods.getSelectionModel().getSelectedItem();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneAddItemDialog(item, true);
        if (item != null) {
            if(buttonConfirmedClicked) {
                try {
                    boolean updated = itemDao.update(item);
                    if(updated) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Item alterado com sucesso!");
                        alert.show();
                    }
                    loadTableItem();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Erro! " + e.getMessage());
                    alert.show();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Selecione um item na tabela para poder altera-lo");
            alert.show();
        }
    }

    @FXML
    public void handleButtonDelete() {
        Item item = tableViewFoods.getSelectionModel().getSelectedItem();
        if (item != null) {
            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "Você tem certeza que deseja excluir o item " + item.getDescription() + "?", ButtonType.YES, ButtonType.CANCEL);
            alertConfirmation.showAndWait();
            if (alertConfirmation.getResult() == ButtonType.YES) {
                try {
                    boolean deleted = itemDao.delete(item);
                    if (deleted) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Item deletado com sucesso!");
                        alert.show();
                    }
                    loadTableItem();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Erro! " + e.getMessage());
                    alert.show();
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Selecione um item na tabela para poder altera-lo");
            alert.show();
        }
    }

    private boolean showFXMLAnchorPaneAddItemDialog(Item item, boolean update) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrderController.class.getResource("/fxml/FXMLDialogInsertItem.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        String windowTitle = "Novo item";
        if (update) {
            windowTitle = "Editar item";
        }
        dialogStage.setTitle(windowTitle);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogInsertItemController dialogController = loader.getController();
        dialogController.setDialogStage(dialogStage);
        dialogController.setUpdate(update);
        dialogController.setItem(item);

        dialogStage.showAndWait();
        return dialogController.isButtonConfirmClicked();
    }


    private void loadTableItem() {
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        itemList = itemDao.list();
        ObservableList obsItemList = FXCollections.observableArrayList(itemList);
        tableViewFoods.setItems(obsItemList);
    }

    private void selectItemTableView(Item item) {
        if(item != null) {
            labelDescription.setText(item.getDescription());
            labelMaker.setText(item.getMaker());
            labelPrice.setText("R$ " + item.getPrice());
            labelCost.setText("R$ " + item.getCost());
        } else {
            labelDescription.setText("");
            labelMaker.setText("");
            labelPrice.setText("");
            labelCost.setText("");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableItem();

        tableViewFoods.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectItemTableView((Item) newValue)
        );

    }
}
