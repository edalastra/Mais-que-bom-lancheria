package com.controledecomandas.database.dao;

import com.controledecomandas.database.PostgresConnection;
import com.controledecomandas.models.Item;
import com.controledecomandas.models.User;
import com.controledecomandas.utils.GenerateHash;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Integer.parseInt;

public class UserDao {


    public int countAdminUsers() throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();


        String sqlQuery = " SELECT COUNT(*) as c FROM users WHERE is_admin";
        Statement stmt = postgresConnection.createStatement();
        ResultSet result = stmt.executeQuery(sqlQuery);
        int qnt = 0;
        while (result.next())
            qnt = result.getInt("c");
        if(connected) {
            postgresConnection.desconnect();
        }

        return qnt;
    }

    public boolean insert(User user) {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlInsert = "INSERT INTO users " +
                "(email, password, is_admin, first_name, last_name, telephone, salary, address, zipcode)" +
                "VALUES(?,?,?,?,?,?,?,?,?);";

        PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlInsert);

        try {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setBoolean(3, user.getAccess());
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            pstmt.setString(6, user.getTelephone());
            pstmt.setBigDecimal(7, user.getSalary());
            pstmt.setString(8, user.getAddress());
            pstmt.setString(9, user.getZipcode());

            int result = pstmt.executeUpdate();
            if(result == 1) {
                return true;
            }else {
                System.out.println("ERROUUU");
            }

        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(pstmt != null) {
                try {
                    pstmt.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            postgresConnection.desconnect();
        }
        return false;

    }

    public boolean update(User user) throws SQLException {
        PostgresConnection postgresConnection = new PostgresConnection();
        boolean connected = postgresConnection.connect();

        String sqlInsert = "UPDATE users SET first_name = ?, last_name = ?, telephone = ?, is_admin = ?, salary = ?, zipcode = ?, address = ?" +
                "WHERE id = ?";

        try(PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlInsert)) {
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getTelephone());
            pstmt.setBoolean(4, user.getAccess());
            pstmt.setBigDecimal(5, user.getSalary());
            pstmt.setString(6, user.getZipcode());
            pstmt.setString(7, user.getAddress());
            pstmt.setInt(8, user.getId());


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

    public User login(String email, String password) throws Exception {

        PostgresConnection postgresConnection = new PostgresConnection();
        postgresConnection.connect();

        String sqlQuery = "SELECT * FROM  users  WHERE email=?";
        PreparedStatement pstmt = postgresConnection.createPrepedStatement(sqlQuery);
        User user = new User();

            pstmt.setString(1,email);
            ResultSet result = pstmt.executeQuery();
            if(!result.next()
                    || !GenerateHash.generate(password).equals(result.getString("password"))) {
                postgresConnection.desconnect();

                throw new Exception("Usuário ou senha incorretos!");
            }
            user.setId(result.getInt("id"));
            user.setEmail(result.getString("email"));
            user.setAccess(result.getBoolean("is_admin"));
            user.setFirstName(result.getString("first_name"));
            user.setLastName(result.getString("last_name"));
            user.setTelephone(result.getString("telephone"));
            user.setSalary(result.getBigDecimal("salary"));
            user.setAddress(result.getString("address"));
            user.setZipcode(result.getString("zipcode"));

            user.setId(result.getInt("id"));
            postgresConnection.desconnect();


        return user;
    }

}
