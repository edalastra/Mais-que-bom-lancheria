package com.controledecomandas.controllers;

import com.controledecomandas.database.dao.ItemDao;
import com.controledecomandas.database.dao.OrderDao;
import com.controledecomandas.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    @FXML
    private TableView<Item> tableViewItems;

    @FXML
    private TableColumn<Order, Integer> columnOrderId;

    @FXML
    private TableColumn<Order, Bartable> columnOrderBartable;

    @FXML
    private TableView<Order> tableViewOrders;

    @FXML
    private TableColumn<Item, Integer> columnItemQuantity;

    @FXML
    private TableColumn<Item, String> columnItemDescription;

    @FXML
    private TableColumn<Item, String> columnItemCategory;

    @FXML
    private TableColumn<Item, BigDecimal> columnItemPrice;

    private OrderDao orderDao = new OrderDao();

    private ItemDao itemDao = new ItemDao();


    @FXML
    public void handleButtonInsertOrder() throws IOException, SQLException {
        Order order = new Order();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneInsertOrderDialog(order);

        if (buttonConfirmedClicked) {
            try {
                boolean inserted = orderDao.insert(order, UserSession.getInstace(new User()).getUser().getId(), order.getBartable().getId());
                if (inserted) {
                    System.out.println("tudo certo");
                }
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
    public void handleButtonAddItem() throws IOException, SQLException {
        int orderId = tableViewOrders.getSelectionModel().getSelectedItem().getId();
        Item item = new Item();

        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneAddItemDialog(item);
        if(buttonConfirmedClicked) {
            boolean inserted = itemDao.associateToOrder(item.getId(), orderId, item.getQuantity());
            if(inserted){
                System.out.println("tudo certo");
            }
            loadTableOrders();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erro");
            alert.show();
        }
    }


    public void loadTableOrders() throws IOException, SQLException {
        columnOrderId.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        columnOrderBartable.setCellValueFactory(new PropertyValueFactory<Order, Bartable>("bartable"));

        List<Order> orders = orderDao.listByWorker(UserSession.getInstace(new User()).getUser().getId());
        ObservableList<Order> obsOrders;
        obsOrders = FXCollections.observableArrayList(orders);
        tableViewOrders.setItems(obsOrders);

        columnItemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnItemCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        columnItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


    private void selectOrderTableView(Order order) throws SQLException {
        if (order != null) {
            ItemDao itemDao = new ItemDao();
            List<Item> items = itemDao.listByOrder(order.getId());
            ObservableList obsItems = FXCollections.observableArrayList(items);
            tableViewItems.setItems(obsItems);
        }
    }

    private void selectItemTableView(Item order) throws SQLException {
        if (order != null) {
//            ItemDao itemDao = new ItemDao();
//            List<Item> items = itemDao.listByOrder(order.getId());
//            ObservableList obsItems = FXCollections.observableArrayList(items);
//            tableViewItems.setItems(obsItems);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTableOrders();

        } catch (IndexOutOfBoundsException | IOException | SQLException e) {
            System.out.println(e);
        }

        tableViewOrders.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        this.selectOrderTableView((Order) newValue);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
        );

        tableViewItems.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        this.selectItemTableView((Item) newValue);
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


    public boolean showFXMLAnchorPaneAddItemDialog(Item item) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrderController.class.getResource("/fxml/FXMLDialogAddItem.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Adicionar item a comanda");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogAddItemController dialogController = loader.getController();
        dialogController.setDialogStage(dialogStage);
        dialogController.setItem(item);

        dialogStage.showAndWait();
        return dialogController.isButtonConfirmClicked();
    }
}
