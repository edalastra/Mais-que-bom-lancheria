package com.controledecomandas.controllers;

import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class InsertUserController implements Initializable {

    @FXML
    private TextField inputUserFirstName;

    @FXML
    private TextField inputUserEmail;

    @FXML
    private TextField inputUserZipcode;

    @FXML
    private TextField inputUserLastName;

    @FXML
    private TextField inputUserTelephone;

    @FXML
    private TextField inputUserAddress;

    @FXML
    private TextField inputUserSalary;

    @FXML
    private TextField inputUserPassword;

    @FXML
    private ToggleGroup access;

    @FXML
    private RadioButton radioBtnWorker;

    @FXML
    private RadioButton radioBtnAdmin;

    @FXML
    private TextField inputUserCPassword;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void registerUser(ActionEvent event) throws IOException {
        User user = new User();
        user.setEmail(inputUserEmail.getText());
        user.setPassword(inputUserPassword.getText());
        RadioButton radio = (RadioButton) access.getSelectedToggle();
        user.setAccess(radio.getText().equals("Gerente"));
        user.setFirstName(inputUserFirstName.getText());
        user.setLastName(inputUserLastName.getText());
        user.setTelephone(inputUserTelephone.getText());
        user.setSalary(new BigDecimal(inputUserSalary.getText()));
        user.setAddress(inputUserAddress.getText());
        user.setZipcode(inputUserZipcode.getText());

        UserDao userDao = new UserDao();
        boolean isCreated = userDao.insert(user);

        if(isCreated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("Usuário " + user.getFirstName() + " criado com sucesso!");
            alert.showAndWait();
            AnchorPane insertUser = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/FXMLLogin.fxml"));
            anchorPane.getChildren().setAll(insertUser);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Ooops, ocorreu um erro inesperdao!");
            alert.setContentText("Não foi possível criar o usuário!");
            alert.showAndWait();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserDao userDao = new UserDao();
        try {
            if(userDao.countAdminUsers() == 0) {
                //radioBtnWorker.setSelected(false);
                radioBtnAdmin.setSelected(true);
                radioBtnWorker.setDisable(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
