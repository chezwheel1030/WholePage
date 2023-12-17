package com.wholepage.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.wholepage.Model.User.UserType;

public class DatabaseHelper {
    
    private Connection conn;
    private Statement statement;

    public DatabaseHelper(String url, String user, String password) {
        try {
            conn = DriverManager.getConnection(url, user, password);
            statement = conn.createStatement(); // Initialize the statement object
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createUser(String username, String password, UserType userType) {
        boolean success = false;
        try {
            String query = "INSERT INTO users (username, password, user_type) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, userType.toString());
            preparedStatement.executeUpdate();
            System.out.println("User created successfully.");
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean addEmployee(String name, String position, double salary) {
        try {
            String query = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, position);
            preparedStatement.setDouble(3, salary);
    
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public User getUser(String username, String password) {
        User user = null;
        try {
            String query = "SELECT user_type FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                UserType userType = UserType.valueOf(resultSet.getString("user_type"));
                user = new User(username, password, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean validateManagerKey(String key) {
        String sql = "SELECT * FROM manager_keys WHERE key = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean insertManagerKey(String key) {
        String sql = "INSERT INTO manager_keys (key) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, key);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    public List<Item> getItems() {
        List<Item> items = new ArrayList<Item>();

        try {
            String sql = "SELECT * FROM items";
            ResultSet rs = statement.executeQuery(sql); // Use the statement object
            while (rs.next()) {
                Item item = new Item(rs.getString("name"), rs.getString("description"), rs.getDouble("price"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error getting items from database: " + e.getMessage());
        }

        return items;
    }


    public Connection getConnection() {
        return conn;
    }

    
    
    public User login(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;
    
        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
    
            if (rs.next()) {
                int id = rs.getInt("id");
                UserType userType = UserType.valueOf(rs.getString("user_type"));
    
                user = new User(username, password, userType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    
    public boolean isUserManager(User user) {
        try {
            String query = "SELECT * FROM users WHERE id = ? AND user_type = 'MANAGER'";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    } 
    
    public void addOrder(Order order) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO orders (user_id, total_price, status) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, order.getUserId());
            stmt.setDouble(2, order.getTotalPrice());
            stmt.setString(3, order.getStatus());
            stmt.executeUpdate();
    
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
    
                for (CartItem item : order.getItems()) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)");
                    stmt2.setInt(1, orderId);
                    stmt2.setInt(2, Integer.parseInt(item.getItem().getId()));
                    stmt2.setInt(3, item.getQuantity());
                    stmt2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateOrderStatus(Order order, String newStatus) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE orders SET status = ? WHERE id = ?");
            stmt.setString(1, newStatus);
            stmt.setInt(2, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>();
    
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Pending WHERE status = 'Pending';");
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int userId = resultSet.getInt("user_id");
                double totalPrice = resultSet.getDouble("total_price");
                String status = resultSet.getString("status");
                Order order = new Order(orderId, userId, null, totalPrice, status, null);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting orders from database: " + e.getMessage());
        }
    
        return orders;
    }
    
    
    

   public void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}


