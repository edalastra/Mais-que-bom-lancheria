package com.controledecomandas.controllers;

import com.controledecomandas.controllers.Dialogs.DialogInsertUserController;
import com.controledecomandas.controllers.Dialogs.DialogOrderController;
import com.controledecomandas.database.dao.UserDao;
import com.controledecomandas.models.User;
import com.controledecomandas.models.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable {


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

    private User user;

    private UserDao userDao = new UserDao();

    @FXML
    public void handleButtonUpdate() throws IOException {
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneUpdateUserDialog(user, true);

        if(buttonConfirmedClicked) {
            try {
                boolean updated = userDao.update(user);
                if(updated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Informações alterado com sucesso!");
                    alert.show();
                    loadUser();
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro! " + e.getMessage());
                alert.show();
            }
        }
    }

    private boolean showFXMLAnchorPaneUpdateUserDialog(User user, boolean update) throws IOException {
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

    private void loadUser() {
        user = UserSession.getInstace(new User()).getUser();
        labelUserName.setText(user.getFirstName() + " " + user.getLastName());
        labelUserEmail.setText(user.getEmail());
        labelUserTelephone.setText(user.getTelephone());
        labelUserAddress.setText(user.getAddress() + " - " + user.getZipcode());
        labelUserSalary.setText("R$ " + user.getSalary());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadUser();
    }
}
