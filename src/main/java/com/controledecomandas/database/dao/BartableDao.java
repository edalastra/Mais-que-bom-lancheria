package com.controledecomandas.database.dao;

import com.controledecomandas.database.PostgresConnection;
import com.controledecomandas.models.Bartable;
import com.controledecomandas.models.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BartableDao {

    public List<Bartable> list() {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlQuery = "SELECT * FROM bartable";
        Statement stmt = postgresConnection.createStatement();

        List<Bartable> bartableList = new ArrayList<>();

        try(ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                Bartable bartable = new Bartable();
                bartable.setId(rs.getInt("id"));
                bartable.setCapacity(rs.getInt("capacity"));

                bartableList.add(bartable);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return bartableList;
    }

    public Bartable getById(int id) throws Exception {

        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();
        Bartable bartable = new Bartable();

        String sqlQuery = "SELECT * FROM bartable WHERE id = ?";
        try(PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlQuery)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                bartable.setId(rs.getInt("id"));
                bartable.setCapacity(rs.getInt("capacity"));
            } else {
                throw new Exception("Mesa não encontrada! Digite outra ou cadastre uma nova");
            }


        } catch (SQLException throwables) {

        } finally {
            postgresConnection.desconnect();
        }

        return bartable;
    }


}
