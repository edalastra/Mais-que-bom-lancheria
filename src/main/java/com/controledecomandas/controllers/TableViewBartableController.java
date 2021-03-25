package com.controledecomandas.controllers;

import com.controledecomandas.database.dao.BartableDao;
import com.controledecomandas.models.Bartable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TableViewBartableController implements Initializable {
    @FXML
    private VBox tableViewBartables;

    @FXML
    private TableView<Bartable> tableViewBartable;

    @FXML
    private TableColumn<Bartable, Integer> columnBartableId;

    @FXML
    private TableColumn<Bartable, Integer> columnBartableCapacity;

    List<Bartable> bartableList;

    BartableDao bartableDao = new BartableDao();


    private void loadTableViewBartable() {
        columnBartableId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnBartableCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        bartableList = bartableDao.list();
        ObservableList obsBartableList = FXCollections.observableArrayList(bartableList);
        tableViewBartable.setItems(obsBartableList);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableViewBartable();
    }

}
