package com.wholepage.Model;

import java.sql.*;
import java.util.List;

import com.wholepage.Model.User.UserType;

public class Database {
    private Connection conn;

    public Database(String url, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addUser(String username, String password, String firstName, String lastName, String email, String address, String role) {
        String query = "INSERT INTO users (username, password, first_name, last_name, email, address, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, address);
            stmt.setString(7, role);
    
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false;
    }

    public User authenticate(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                UserType userType = UserType.valueOf(resultSet.getString("role"));
                return new User(username, password, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    
    public void saveOrder(User user, List<CartItem> items, double totalPrice) {
        String query = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            stmt.setDouble(2, totalPrice);
            stmt.setString(3, "pending");
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                for (CartItem item : items) {
                    String itemQuery = "INSERT INTO order_items (order_id, item_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement itemStmt = conn.prepareStatement(itemQuery)) {
                        itemStmt.setInt(1, orderId);
                        itemStmt.setString(2, item.getItem().getId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int createOrder(User user, double totalPrice) {
        int userId = user.getId();
        int orderId = -1;

        String insertOrderQuery = "INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = conn.prepareStatement(insertOrderQuery)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, totalPrice);
            stmt.setString(3, "pending");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                orderId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderId;
    }
}