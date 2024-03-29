package com.controledecomandas.controllers;

import com.controledecomandas.controllers.Dialogs.DialogAddItemController;
import com.controledecomandas.controllers.Dialogs.DialogOrderController;
import com.controledecomandas.database.dao.ItemDao;
import com.controledecomandas.database.dao.OrderDao;
import com.controledecomandas.models.*;
import com.controledecomandas.textFieldsValidators.CurrencyField;
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
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TableViewOrderController implements Initializable {

    @FXML
    private TableView<Item> tableViewItems;

    @FXML
    private TableColumn<Order, Integer> columnOrderId;

    @FXML
    private TableColumn<Order, BartableController> columnOrderBartable;

    @FXML
    private TableView<Order> tableViewOrders;

    @FXML
    private TableColumn<Item, Integer> columnItemQuantity;

    @FXML
    private TableColumn<Item, String> columnItemDescription;

    @FXML
    private TableColumn<Item, String> columnItemCategory;

    @FXML
    private TableColumn<Item, Integer> columnTotalPrice;

    @FXML
    private TableColumn<Item, BigDecimal> columnItemPrice;

    @FXML
    private TableColumn<Order, String> columnResponsible;

    private List<Order> orders;

    private List<Item> items;

    private OrderDao orderDao = new OrderDao();

    private ItemDao itemDao = new ItemDao();


    @FXML
    private Button buttonCloseOrder;


    @FXML
    public void handleButtonInsertOrder() throws IOException, SQLException {
        Order order = new Order();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneInsertOrderDialog(order);

        if (buttonConfirmedClicked) {
            try {
                boolean inserted = orderDao.insert(order, UserSession.getInstace(new User()).getUser().getId(), order.getBartable().getId());
                loadTableOrders();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleButtonCloseOrder() throws IOException {
        Order order = tableViewOrders.getSelectionModel().getSelectedItem();

        if (order != null) {
            Alert alertConfirmation =
                    new Alert(Alert.AlertType.CONFIRMATION,
                            !items.isEmpty() ? "Valor total da comanda: R$" + items
                                    .stream()
                                    .map(Item::getTotalPrice)
                                    .reduce(BigDecimal::add)
                                    .get() : "",
                            ButtonType.YES, ButtonType.CANCEL);
            alertConfirmation.showAndWait();
            if (alertConfirmation.getResult() == ButtonType.YES) {
                try {
                    boolean inserted = orderDao.close(order);
                    loadTableOrders();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Selecione uma comanda na tabela");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleButtonAddItem() throws IOException, SQLException {
        Order order = tableViewOrders.getSelectionModel().getSelectedItem();
        Item item = new Item();
        if(order != null) {
            boolean buttonConfirmedClicked = this.showFXMLAnchorPaneAddItemDialog(item, false);
            if(buttonConfirmedClicked) {
                try {
                    boolean inserted = orderDao.associateItem(item.getId(), order.getId(), item.getQuantity());
                    selectOrderTableView(order);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Erro! " + e.getMessage());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Selecione uma comanda");
            alert.show();
        }
    }

    @FXML
    public void handleButtonUpdateItem() throws IOException, SQLException {
        Item item = tableViewItems.getSelectionModel().getSelectedItem();
        Order order = tableViewOrders.getSelectionModel().getSelectedItem();
        if (item != null && order != null) {
            boolean buttonConfirmedClicked = showFXMLAnchorPaneAddItemDialog(item, true);
            if(buttonConfirmedClicked) {
                orderDao.updateItem(item.getId(), order.getId(), item.getQuantity());
                selectOrderTableView(order);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemoveItem() throws IOException, SQLException {
        Item item = tableViewItems.getSelectionModel().getSelectedItem();
        Order order = tableViewOrders.getSelectionModel().getSelectedItem();
        if (item != null) {
            Alert alert =
                    new Alert(Alert.AlertType.CONFIRMATION,
                "Você tem certeza que deseja remover o item " + item.getQuantity() + " X " + item.getDescription() + " da comanda " + order.getId() + "?",
                    ButtonType.YES, ButtonType.CANCEL);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                orderDao.removeItem(item.getId(), order.getId());
                selectOrderTableView(order);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na tabela!");
            alert.show();
        }
    }


    public void loadTableOrders() throws IOException, SQLException {
        columnOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        columnOrderBartable.setCellValueFactory(new PropertyValueFactory<Order, BartableController>("bartable"));
        User session = UserSession.getInstace(new User()).getUser();
        if( session.getAccess() ) {
            orders = orderDao.list();
            columnResponsible.setCellValueFactory(new PropertyValueFactory<>("worker"));
        } else {
            orders = orderDao.listByWorker(session);
        }

        ObservableList<Order> obsOrders;
        obsOrders = FXCollections.observableArrayList(orders);
        tableViewOrders.setItems(obsOrders);

        columnItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnItemCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));


    }


    private void selectOrderTableView(Order order) throws SQLException {
        if (order != null) {
            ItemDao itemDao = new ItemDao();
             items = itemDao.listByOrder(order.getId());
            ObservableList obsItems = FXCollections.observableArrayList(items);
            tableViewItems.setItems(obsItems);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTableOrders();

        } catch (IndexOutOfBoundsException | IOException | SQLException e) {
            System.out.println(e);
        }
        boolean access = UserSession.getInstace(new User()).getUser().getAccess();

        buttonCloseOrder.setVisible(access);
        columnResponsible.setVisible(access);


        tableViewOrders.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        this.selectOrderTableView((Order) newValue);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
        );

    }

    public boolean showFXMLAnchorPaneInsertOrderDialog(Order order) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrderController.class.getResource("/fxml/FXMLDialogOrder.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Nova Comanda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogOrderController dialogController = loader.getController();
        dialogController.setDialogStage(dialogStage);
        dialogController.setOrder(order);

        dialogStage.showAndWait();
        return dialogController.isButtonConfirmClicked();
    }


    public boolean showFXMLAnchorPaneAddItemDialog(Item item, boolean update) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrderController.class.getResource("/fxml/FXMLDialogAddItem.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        String windowTitle = "Adicionar item a comanda";
        if (update) {
            windowTitle = "Editar item na comanda";
        }
        dialogStage.setTitle(windowTitle);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogAddItemController dialogController = loader.getController();
        dialogController.setDialogStage(dialogStage);
        dialogController.setUpdate(update);
        dialogController.setItem(item);

        dialogStage.showAndWait();
        return dialogController.isButtonConfirmClicked();
    }
}
