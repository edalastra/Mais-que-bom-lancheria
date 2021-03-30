package com.controledecomandas.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.User;
import com.controledecomandas.models.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    private UserDao userDao;

    public LoginController() {
        this.userDao = new UserDao();
    }

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField loginEmail;

    @FXML
    private PasswordField loginPassword;

    @FXML
    public void login() {
        try {
            User user = userDao.login(loginEmail.getText(), loginPassword.getText());
            System.out.println(user.getAccess());
            UserSession.getInstace(user);
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            Parent home = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/FXMLHome.fxml"));
            Scene scene = new Scene(home, 900, 700);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if(userDao.countAdminUsers() == 0) {
                AnchorPane insertUser = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/FXMLInsertUser.fxml"));
                anchorPane.getChildren().setAll(insertUser);
            }
        } catch (SQLException | IOException throwables) {
            System.out.println(throwables);
        }
    }

}
