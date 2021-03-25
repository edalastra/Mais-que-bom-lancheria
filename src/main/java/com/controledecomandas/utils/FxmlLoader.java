package com.controledecomandas.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {

    private Pane view;

    public Pane getPage(String fileName) {
        try {

            view = new FXMLLoader().load(getClass().getResource("/fxml/" + fileName + ".fxml"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return view;
    }

}
