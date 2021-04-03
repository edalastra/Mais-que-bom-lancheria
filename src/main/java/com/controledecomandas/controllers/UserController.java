package com.controledecomandas.controllers;

import com.controledecomandas.controllers.Dialogs.DialogInsertUserController;
import com.controledecomandas.controllers.Dialogs.DialogOrderController;
import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.Order;
import com.controledecomandas.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, String> columnName;

    @FXML
    private TableColumn<User, String> columnEmail;

    @FXML
    private TableColumn<User, String> columnTelephone;

    @FXML
    private TableColumn<User, String> columnAddress;

    @FXML
    private TableColumn<User, String> columnAccess;

    private List<User> userList;

    private UserDao userDao = new UserDao();

    @FXML
    public void handleButtonInsert() throws IOException {
        User user = new User();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneInsertUserDialog(user, false);

        if(buttonConfirmedClicked) {
            boolean updated = userDao.insert(user);
            if(updated) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Usuário inserido com sucesso!");
                alert.show();
                loadTableViewUsers();
            }

        }
    }


    private boolean showFXMLAnchorPaneInsertUserDialog(User user, boolean update) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrderController.class.getResource("/fxml/FXMLInsertUser.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        String windowTitle = "Novo item";
        if (update) {
            windowTitle = "Editar item";
        }
        dialogStage.setTitle(windowTitle);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogInsertUserController dialogController = loader.getController();
        dialogController.setDialogStage(dialogStage);
        dialogController.setUpdate(update);
        dialogController.setUser(user);

        dialogStage.showAndWait();
        return dialogController.isButtonConfirmClicked();
    }

    private void loadTableViewUsers() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        columnAccess.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        userList = userDao.list();
        ObservableList obsUserList = FXCollections.observableArrayList(userList);
        tableViewUsers.setItems(obsUserList);

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableViewUsers();
    }


}
