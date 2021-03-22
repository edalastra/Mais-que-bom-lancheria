package com.controledecomandas.database;

import java.sql.*;
import java.util.Properties;

public class PostgresConnection {

    private Connection connection;

    public boolean connect() {
        try {
            String url = "jdbc:postgresql://localhost:5432/mqb";
            Properties props = new Properties();
            props.setProperty("user","postgres");
            props.setProperty("password","root");
            this.connection = DriverManager.getConnection(url, props);
            System.out.println("connectou");
        } catch (SQLException e ) {
            System.out.println(e);
            return false;
        }

        return true;
    }

    public boolean desconnect()  {
        try {
            if(this.connection.isClosed() == false) {
                this.connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return true;
    }

    public Statement createStatement() {
        try {
            return this.connection.createStatement();
        } catch (SQLException e) {
            return null;
        }
    }

    public PreparedStatement createPrepedStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }


    public Connection getConnection(){
        return this.connection;
    }
}
