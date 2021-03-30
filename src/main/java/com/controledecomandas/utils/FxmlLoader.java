package com.controledecomandas.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {

    private AnchorPane view;

    public AnchorPane getPage(String fileName) {
        try {
            view = new FXMLLoader().load(getClass().getResource("/fxml/" + fileName + ".fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
