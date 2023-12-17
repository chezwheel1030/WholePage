package com.wholepage;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.wholepage.Model.DatabaseHelper;
import com.wholepage.Model.OrderManager;
import com.wholepage.Model.User;

public class App {
    private static MainInterface mainInterface;
    
    public static void main(String[] args) {
        // Set up the database connection parameters
        String url = "jdbc:postgresql://localhost:5432/mydatabase";
        String user = "postgres";
        String password = "edcr12";

        // Create a DatabaseHelper instance with the connection parameters
        DatabaseHelper dbHelper = new DatabaseHelper(url, user, password);
        JTable ordersTable = new JTable(); // Initialize this as necessary
        OrderManager orderManager = new OrderManager(mainInterface, dbHelper, ordersTable);
        User shopUser = new User("username", "password", User.UserType.CUSTOMER);

        // Pass the DatabaseHelper, OrderManager, and User instances to the MainInterface constructor
        SwingUtilities.invokeLater(() -> {
        MainInterface mainInterface = new MainInterface(shopUser, dbHelper);
        mainInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainInterface.setVisible(true);
    });

        
    }
}
