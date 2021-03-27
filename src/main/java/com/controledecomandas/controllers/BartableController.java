package com.controledecomandas.controllers;

import com.controledecomandas.controllers.Dialogs.DialogInsertBartable;
import com.controledecomandas.controllers.Dialogs.DialogOrderController;
import com.controledecomandas.database.dao.BartableDao;
import com.controledecomandas.models.Bartable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class BartableController implements Initializable {
    @FXML
    private ListView<Bartable> listViewBartable;

    List<Bartable> bartableList;

    BartableDao bartableDao = new BartableDao();

    @FXML
    public void handelButtonInsert() throws IOException {
        Bartable bartable = new Bartable();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneDialog(bartable, false);

        if(buttonConfirmedClicked) {
            try {
                boolean updated = bartableDao.insert(bartable);
                if(updated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Mesa inserida com sucesso!");
                    alert.show();
                    loadListViewBartable();
                }

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro! " + e.getMessage());
                alert.show();
            }
        }
    }

    @FXML
    public void handelButtonUpdate() throws IOException {
        Bartable bartable = listViewBartable.getSelectionModel().getSelectedItem();
        boolean buttonConfirmedClicked = this.showFXMLAnchorPaneDialog(bartable, true);
        if(bartable != null) {
        if(buttonConfirmedClicked) {
            try {
                boolean updated = bartableDao.update(bartable);
                if (updated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Mesa alterad com sucesso!");
                    alert.show();
                    loadListViewBartable();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erro! " + e.getMessage());
                alert.show();
            }
        }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Selecione uma messa na lista para poder altera-la");
            alert.show();
        }
    }

    @FXML
    public void handelButtonDelete() throws IOException {
        Bartable bartable = listViewBartable.getSelectionModel().getSelectedItem();

        if (bartable != null) {
            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
                    "Você tem certeza que deseja excluir a mesa " + bartable.getId() + "?",
                    ButtonType.YES, ButtonType.CANCEL);
            alertConfirmation.showAndWait();
            if (alertConfirmation.getResult() == ButtonType.YES) {
                try {
                    boolean updated = bartableDao.delete(bartable);
                    if (updated) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Mesa alterada com sucesso!");
                        alert.show();
                        loadListViewBartable();
                    }
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Erro! " + e.getMessage());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Selecione uma messa na lista para poder exluí-la");
                alert.show();
            }
        }
    }

    private boolean showFXMLAnchorPaneDialog(Bartable bartable, boolean update) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DialogOrderController.class.getResource("/fxml/FXMLDialogInsertBartable.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        String windowTitle = "Nova Mesa";
        if (update) {
            windowTitle = "Editar mesa";
        }
        dialogStage.setTitle(windowTitle);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        DialogInsertBartable dialogController = loader.getController();
        dialogController.setDialogStage(dialogStage);
        dialogController.setUpdate(update);
        dialogController.setBartable(bartable);

        dialogStage.showAndWait();
        return dialogController.isButtonConfirmClicked();
    }


    private void loadListViewBartable() {

        bartableList = bartableDao.list();
        ObservableList obsBartableList = FXCollections.observableArrayList(bartableList);
        listViewBartable.setItems(obsBartableList);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadListViewBartable();
    }

}
