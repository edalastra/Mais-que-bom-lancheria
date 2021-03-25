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


    public boolean insert(Item item) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlInsert = "INSERT INTO item(description, maker, category, price, cost)" +
                "VALUES(?,?,?,?,?)";

        try(PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlInsert)) {
            pstmt.setString(1, item.getDescription());
            pstmt.setString(2, item.getMaker());
            pstmt.setString(3, item.getCategory());
            pstmt.setBigDecimal(4,item.getPrice());
            pstmt.setBigDecimal(5, item.getCost());

            int rs = pstmt.executeUpdate();

            if(rs == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throw throwables;
        } finally {
            postgresConnection.desconnect();
        }

        return false;
    }


    public boolean update(Item item) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlInsert = "UPDATE item SET description = ?, maker = ?, category = ?, price = ?, cost = ? " +
                "WHERE id = ?";

        try(PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlInsert)) {
            pstmt.setString(1, item.getDescription());
            pstmt.setString(2, item.getMaker());
            pstmt.setString(3, item.getCategory());
            pstmt.setBigDecimal(4,item.getPrice());
            pstmt.setBigDecimal(5, item.getCost());
            pstmt.setInt(6, item.getId());

            int rs = pstmt.executeUpdate();

            if(rs == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throw throwables;
        } finally {
            postgresConnection.desconnect();
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
        postgresConnection.desconnect();
        return items;
    }

    public boolean delete(Item item) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlDelete = "DELETE FROM item WHERE id = ?";

        try(PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlDelete)) {
            pstmt.setInt(1, item.getId());

            int rs = pstmt.executeUpdate();

            if(rs == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throw throwables;
        } finally {
            postgresConnection.desconnect();
        }

        return false;

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
