package com.controledecomandas;

import com.controledecomandas.database.CreateDatabase;
import com.controledecomandas.database.PostgresConnection;
import com.controledecomandas.database.dao.UserDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            //primaryStage.setMaximized(true);
            primaryStage.setTitle("Controle de comandas");
            primaryStage.setScene(scene);

            primaryStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main (String [] args){
        launch(args);

    }


}