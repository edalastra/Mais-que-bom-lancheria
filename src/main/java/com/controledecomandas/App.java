package com.controledecomandas;

import com.controledecomandas.database.CreateDatabase;
import com.controledecomandas.database.PostgresConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class App extends Application{

    @Override
    public void start (Stage primaryStage){


        try {
            PostgresConnection postgresConnection = new PostgresConnection();
            CreateDatabase createDatabase = new CreateDatabase(postgresConnection);
            createDatabase.createTables();

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXMLLogin.fxml"));
            Scene scene = new Scene (root,900,600);

            primaryStage.setTitle("Controle de comandas");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/icons/hamburguer-icon.png"))));

            primaryStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main (String [] args){
        launch(args);

    }


}