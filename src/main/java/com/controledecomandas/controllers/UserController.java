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
    private Label labelUserSalary;

    @FXML
    private Label labelUserName;

    @FXML
    private Label labelUserTelephone;

    @FXML
    private Label labelUserEmail;

    @FXML
    private Label labelUserAddress;

    @FXML
    private Label labelUserAccess;

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
        userList = userDao.list();
        ObservableList obsUserList = FXCollections.observableArrayList(userList);
        tableViewUsers.setItems(obsUserList);

    }
    private void selectUserTableView(User user){
        labelUserName.setText(user.getFirstName() + " " + user.getLastName());
        labelUserEmail.setText(user.getEmail());
        labelUserTelephone.setText(user.getTelephone());
        labelUserAddress.setText(user.getAddress() + " - " + user.getZipcode());
        labelUserSalary.setText("R$ " + user.getSalary());
        labelUserAccess.setText(user.getAccess() ? "Gerente" : "Funcionário");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTableViewUsers();
        tableViewUsers.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.selectUserTableView((User) newValue)
        );
    }


}
