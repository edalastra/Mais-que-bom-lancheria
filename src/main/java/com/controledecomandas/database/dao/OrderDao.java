package com.controledecomandas.database.dao;

import com.controledecomandas.database.PostgresConnection;
import com.controledecomandas.models.Bartable;
import com.controledecomandas.models.Order;
import com.controledecomandas.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDao {

    public boolean insert(Order order, int workerId, int bartable_id) throws Exception {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlInsertOrder = "INSERT INTO orders(bartable_id, open_at ) values(?, default);";
        String sqlInsertWorkerBartable = "INSERT INTO bartable_worker(user_id, bartable_id) VALUES(?,?)";

        try (PreparedStatement pstmtWorkerBartable = postgresConnection.createPrepedStatement(sqlInsertWorkerBartable)) {
            pstmtWorkerBartable.setInt(1, workerId);
            pstmtWorkerBartable.setInt(2, bartable_id);

            pstmtWorkerBartable.executeUpdate();
            System.out.println(workerId);
            try (PreparedStatement pstmtOrder = postgresConnection.createPrepedStatement(sqlInsertOrder)) {

                pstmtOrder.setInt(1, bartable_id);
                if (pstmtOrder.executeUpdate() == 1) {
                    return true;
                }

            } catch (SQLException throwables) {
                throw new Exception("Erro ao cadastrar comanda!");
            }
        } catch (SQLException throwables) {

            throw new Exception("Já existe uma comanda registrada para esta mesa!");

        } finally {
            postgresConnection.desconnect();
        }
        return false;
    }

    public List<Order> list()  {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlQuery = "SELECT u.id as user_id, u.first_name, u.last_name, o.id, o.open_at, b.id as bartable_id, b.capacity as bartable_capacity FROM orders o " +
                "JOIN bartable b ON b.id = o.bartable_id " +
                "JOIN bartable_worker bw ON bw.bartable_id = b.id " +
                "JOIN users u ON u.id = bw.user_id " +
                "WHERE close_at is NULL";

        PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlQuery);
        List<Order> orders = new ArrayList<>();

        try(ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setOpenAt(rs.getTimestamp("open_at"));
                Bartable bartable = new Bartable();
                bartable.setId(rs.getInt("bartable_id"));
                bartable.setCapacity(rs.getInt("bartable_capacity"));
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                order.setBartable(bartable);
                order.setWorker(user);
                orders.add(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            postgresConnection.desconnect();
        }

        return orders;
    }

    public List<Order> listByWorker(User user) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlQuery = "SELECT o.id, o.open_at, b.id as bartable_id, b.capacity as bartable_capacity FROM orders o " +
                "JOIN bartable b ON b.id = o.bartable_id " +
                "JOIN bartable_worker bw ON bw.bartable_id = b.id " +
                "JOIN users u ON u.id = bw.user_id " +
                "WHERE u.id = ? AND close_at is NULL;";

        PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlQuery);

        pstmt.setInt(1, user.getId());
        ResultSet rs = pstmt.executeQuery();
        List<Order> orders = new ArrayList<>();

        while (rs.next()) {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setOpenAt(rs.getTimestamp("open_at"));
            Bartable bartable = new Bartable();
            bartable.setId(rs.getInt("bartable_id"));
            bartable.setCapacity(rs.getInt("bartable_capacity"));
            order.setBartable(bartable);
            orders.add(order);
        }

        postgresConnection.desconnect();
        return orders;
    }

    public boolean associateItem(int itemId, int orderId, int quantity) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlIsert = "INSERT INTO order_item(order_id, item_id, quantity) " +
                "VALUES(?,?,?)";

        try (PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlIsert)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantity);

            int rs = pstmt.executeUpdate();
            if (rs == 1) {
                return true;
            }

        } catch (SQLException throwables) {
            throw new SQLException("Item já inserido na comanda");
        } finally {
            postgresConnection.desconnect();
        }
        return false;
    }

    public boolean updateItem(int itemId, int orderId, int quantity) {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlUpdate = "UPDATE order_item SET quantity = ? WHERE order_id = ? and item_id = ?;";
        try (PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlUpdate)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, orderId);
            pstmt.setInt(3, itemId);
            int rs = pstmt.executeUpdate();
            if (rs == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            postgresConnection.desconnect();
        }
        return false;
    }

    public boolean removeItem(int itemId, int orderId) {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlDelete = "DELETE FROM order_item WHERE item_id = ? AND order_id = ?";
        try (PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlDelete)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, orderId);
            int rs = pstmt.executeUpdate();
            if (rs == 1) {
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            postgresConnection.desconnect();
        }
        return false;
    }

    public void migrateOrderItem(Order order) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlQuery = "SELECT o.open_at, o.close_at, o.id as order_id, i.id as item_id, u.id as user_id, oi.quantity, b.id as bartable_id FROM orders o " +
                "JOIN order_item oi ON oi.order_id = o.id " +
                "JOIN item i ON oi.item_id = i.id " +
                "JOIN bartable b ON b.id = o.bartable_id " +
                "JOIN bartable_worker bw ON bw.bartable_id = b.id " +
                "JOIN users u ON u.id = bw.user_id " +
                "WHERE o.id = ? ";

        String sqlInsert = "INSERT INTO order_item_history(order_id, item_id, user_id, quantity, consumption_time)" +
                "VALUES(?,?,?,?,?)";

        String sqlDeleteOrderItem = "DELETE FROM order_item WHERE order_id = ? AND item_id = ?";


        try(PreparedStatement pstmtQuery = postgresConnection.createPrepedStatement(sqlQuery)) {
            pstmtQuery.setInt(1, order.getId());
            ResultSet rs = pstmtQuery.executeQuery();
            PreparedStatement pstmtInsert = postgresConnection.createPrepedStatement(sqlInsert);
            PreparedStatement pstmtDelete = postgresConnection.createPrepedStatement(sqlDeleteOrderItem);
            while (rs.next()) {
                pstmtInsert.setInt(1, rs.getInt("order_id"));
                pstmtInsert.setInt(2, rs.getInt("item_id"));
                pstmtInsert.setInt(3, rs.getInt("user_id"));
                pstmtInsert.setInt(4, rs.getInt("quantity"));



                Time time = compareTwoTimeStamps(rs.getTimestamp("open_at"), order.getCloseAt());

                pstmtInsert.setTime(5, time);
                pstmtInsert.executeUpdate();
                pstmtDelete.setInt(1, rs.getInt("order_id"));
                pstmtDelete.setInt(2, rs.getInt("item_id"));
                pstmtDelete.executeUpdate();
            }

        } catch (SQLException throwables) {
            throw throwables;
        }
    }


    public boolean close(Order order) {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        Date today = new Date();
        order.setCloseAt(new Timestamp(today.getTime()));

        String sqlQueryBartable = "DELETE FROM bartable_worker WHERE bartable_id = (" +
                "SELECT b.id FROM orders o " +
                "JOIN bartable b ON b.id = o.bartable_id " +
                "WHERE o.id = ?) AND user_id = ( " +
                "SELECT u.id FROM orders o " +
                "JOIN bartable b ON b.id = o.bartable_id " +
                "JOIN bartable_worker bw ON bw.bartable_id = b.id " +
                "JOIN users u ON u.id = bw.user_id " +
                "WHERE o.id = ?) ;";
        String sqlDelete = "UPDATE orders SET close_at = ? WHERE id = ?";

        try (PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlDelete)) {
            migrateOrderItem(order);
            PreparedStatement pstmtD = postgresConnection.createPrepedStatement(sqlQueryBartable);
            pstmt.setTimestamp(1, order.getCloseAt());
            pstmt.setInt(2, order.getId());
            pstmtD.setInt(1, order.getId());
            pstmtD.setInt(2, order.getId());
            int rs = pstmt.executeUpdate();
            int rs2 = pstmtD.executeUpdate();
            if (rs * rs2 == 1) {
                return true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            postgresConnection.desconnect();
        }
        return false;
    }


    public static Time compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime)
    {
        long milliseconds1 = currentTime.getTime();
        long milliseconds2 = oldTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        int seconds = (int) (diff / 1000) % 60 ;
        int minutes = (int) ((diff / (1000*60)) % 60);
        int hours   = (int) ((diff / (1000*60*60)) % 24);
        Time time = new Time(hours, minutes, seconds);
        return time;
    }
}

