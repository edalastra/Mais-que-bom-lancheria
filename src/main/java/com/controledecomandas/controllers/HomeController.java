package com.controledecomandas.controllers;

import com.controledecomandas.database.dao.OrderDao;
import com.controledecomandas.models.*;
import com.controledecomandas.utils.FxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private BorderPane borderPane;
    private Pane view;


    @FXML
    public void toggleOrders() {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("FXMLTableViewOrders");
        borderPane.setCenter(view);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
