package com.controledecomandas.controllers;

import com.controledecomandas.database.dao.OrderDao;
import com.controledecomandas.models.*;
import com.controledecomandas.utils.FxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    AnchorPane anchorPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Group groupNavsAdmin;

    private Pane view;


    @FXML
    public void toggleOrders() {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPage("FXMLTableViewOrders");
        borderPane.setCenter(view);
    }

    @FXML
    public void toggleFoods() {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPage("FXMLTableViewItem");
        borderPane.setCenter(view);
    }

    @FXML
    public void toggleMyAccount() {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPage("FXMLMyAccount");
        borderPane.setCenter(view);
    }

    @FXML
    public void toggleBartable() {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPage("FXMLBartable");
        borderPane.setCenter(view);
    }

    @FXML
    public void toggleUsers() {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPage("FXMLUsers");
        borderPane.setCenter(view);
    }

    @FXML
    public void toggleReport() {
        FxmlLoader object = new FxmlLoader();
        AnchorPane view = object.getPage("FXMLReport");
        borderPane.setCenter(view);
    }



    @FXML
    public void handleButtonLogout() throws IOException {
        UserSession.getInstace(new User()).cleanUserSession();
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Parent login = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/FXMLLogin.fxml"));
        Scene scene = new Scene(login, 900, 700);
        stage.setScene(scene);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleOrders();
        User user = UserSession.getInstace(new User()).getUser();
        if(!user.getAccess()) {
            groupNavsAdmin.setDisable(true);
            groupNavsAdmin.setVisible(true);
        }
    }
}
