package com.controledecomandas.database.dao;

import com.controledecomandas.database.PostgresConnection;
import com.controledecomandas.models.Item;
import com.controledecomandas.models.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    public boolean associateToOrder(int itemId, int orderId, int quantity) {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlIsert = "INSERT INTO order_item(order_id, item_id, quantity) " +
                "VALUES(?,?,?)";

        try(PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlIsert)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantity);

            int rs = pstmt.executeUpdate();
            if (rs == 1) {
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public List<Item> listByOrder(int orderId) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlQuery = "SELECT i.id, i.description, oi.quantity, i.category, i.price FROM item i\n" +
                "JOIN order_item oi ON oi.item_id = i.id\n" +
                "JOIN orders o ON o.id = oi.order_id\n" +
                "WHERE o.id = ?;";

        List<Item> items = new ArrayList<>();

        PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlQuery);
            pstmt.setInt(1,orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setDescription(rs.getString("description"));
                item.setQuantity(rs.getInt("quantity"));
                item.setCategory(rs.getString("category"));
                item.setPrice(rs.getBigDecimal("price"));
                items.add(item);
            }

        return items;
    }


    public List<Item> list() {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlQuery = "SELECT * FROM item";
        Statement stmt = postgresConnection.createStatement();

        List<Item> itemList = new ArrayList<>();

        try(ResultSet rs = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setDescription(rs.getString("description"));
                item.setCategory(rs.getString("category"));
                item.setCost(rs.getBigDecimal("cost"));
                item.setMaker(rs.getString("maker"));

                itemList.add(item);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return itemList;
    }
}
