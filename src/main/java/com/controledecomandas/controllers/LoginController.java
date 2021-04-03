package com.controledecomandas.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.controledecomandas.controllers.Dialogs.DialogInsertUserController;
import com.controledecomandas.controllers.Dialogs.DialogOrderController;
import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.User;
import com.controledecomandas.models.UserSession;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    private JFXTextField loginEmail;

    @FXML
    private JFXPasswordField loginPassword;

    @FXML
    private Label labelInsertAdmin;


    @FXML
    public void login() {
        try {
            if(loginEmail.validate() && loginPassword.validate()){
                User user = userDao.login(loginEmail.getText(), loginPassword.getText());
                UserSession.getInstace(user);
                Stage stage = (Stage) anchorPane.getScene().getWindow();
                Parent home = (AnchorPane) FXMLLoader.load(getClass().getResource("/fxml/FXMLHome.fxml"));
                Scene scene = new Scene(home, 900, 700);
                stage.setScene(scene);
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void handleLabelAdmin() throws IOException {
        User user = new User();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneInsertUserDialog(user, false);

        if(buttonConfirmedClicked) {
            boolean insert = userDao.insert(user);
            if(insert) {
                labelInsertAdmin.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Usuário inserido com sucesso!");
                alert.show();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
        requiredFieldValidator.setMessage("Este campo não pode estar vazio");
        loginEmail.getValidators().add(requiredFieldValidator);
        loginPassword.getValidators().add(requiredFieldValidator);
        try {
            labelInsertAdmin.setVisible(userDao.countAdminUsers() == 0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
