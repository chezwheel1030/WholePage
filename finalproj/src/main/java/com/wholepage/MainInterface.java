package com.wholepage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Box;
import javax.swing.BoxLayout;

import com.wholepage.CredPage.AccountOptionsPage;
import com.wholepage.CredPage.LoginPage;
import com.wholepage.Model.DatabaseHelper;
import com.wholepage.Model.OrderManager;
import com.wholepage.Model.User;
import com.wholepage.Model.User.UserType;
import com.wholepage.ShoppingCart.CartPage;
import com.wholepage.ShoppingCart.PendingOrdersPage;
import com.wholepage.ShoppingCart.ShopPage;

public class MainInterface extends JFrame {

    private JButton shoppingButton;
    private JButton accountButton;
    private JButton logoutButton;
    private User loggedInUser;
    private JButton pendingOrdersButton;
    private DatabaseHelper dbHelper;
    private JButton cartButton;
    private PendingOrdersPage pendingOrdersPage;
    private JTable orderTable;
    private JTable cartTable;
    private OrderManager orderManager;

    public MainInterface(User loggedInUser, DatabaseHelper dbHelper) {
        this.loggedInUser = loggedInUser;
        this.dbHelper = dbHelper;
        orderManager = new OrderManager(this, dbHelper, orderTable);
        initComponents();
    } 

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }    

    public void setUser(User user) {
        this.loggedInUser = user;
    }

    public PendingOrdersPage getPendingOrdersPage() {
        return pendingOrdersPage;
    }

    public void setPendingOrdersPage(PendingOrdersPage pendingOrdersPage) {
        this.pendingOrdersPage = pendingOrdersPage;
    }
    

    public void showPendingOrdersPage() {
        PendingOrdersPage pendingOrdersPage = new PendingOrdersPage(orderTable);
        setVisible(false);
        pendingOrdersPage.setVisible(true);
    }

    public void showShopPage() {
        ShopPage shopPage = new ShopPage(this);
        setVisible(false);
        shopPage.setVisible(true);
    }

    public void showAccountOptionsPage() {
        AccountOptionsPage accountOptionsPage = new AccountOptionsPage(dbHelper, this);
        setVisible(false);
        accountOptionsPage.setVisible(true);
    }    
    
    
    public void updateCartLabel() {
        if (loggedInUser == null) {
            cartButton.setText("Cart");
        } else {
            int itemCount = loggedInUser.getCart().getItems().size();
            String cartButtonText = "Cart (" + itemCount + ")";
            cartButton.setText(cartButtonText);
        }
    }

    public void initComponents() {
        setTitle("Main Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));
        setLayout(new BorderLayout());
    
        int numRows = loggedInUser != null && (loggedInUser.getUserType() == UserType.EMPLOYEE || loggedInUser.getUserType() == UserType.MANAGER) ? 6 : 5;
        JPanel buttonPanel = new JPanel(new GridLayout(numRows, 1, 0, 10));
    
        
        shoppingButton = new JButton("Shopping");
        styleButton(shoppingButton);
        shoppingButton.addActionListener(new ShoppingButtonListener());
        buttonPanel.add(shoppingButton);
    
        cartButton = new JButton("Cart");
        styleButton(cartButton);
        cartButton.addActionListener(new CartButtonListener());
        buttonPanel.add(cartButton);
        buttonPanel.add(Box.createVerticalStrut(3));

        if (loggedInUser != null && (loggedInUser.getUserType() == UserType.EMPLOYEE || loggedInUser.getUserType() == UserType.MANAGER)) {
            pendingOrdersButton = new JButton("Pending Orders");
            pendingOrdersButton.addActionListener(new PendingOrdersListener());
            styleButton(pendingOrdersButton);
            buttonPanel.add(pendingOrdersButton);
    
            // Initialize PendingOrdersPage with the OrderManager instance
            pendingOrdersPage = new PendingOrdersPage(orderManager);
        }
    
        accountButton = new JButton("Account");
        styleButton(accountButton);
        accountButton.addActionListener(new AccountButtonListener());
        buttonPanel.add(accountButton);
    
        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(new LogoutButtonListener());
        buttonPanel.add(logoutButton);
    
        add(buttonPanel, BorderLayout.CENTER);
    
        pack();
        setLocationRelativeTo(null);
    }
    
    
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(55, 150, 198));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private class ShoppingButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loggedInUser == null) {
                LoginPage loginPage = new LoginPage(dbHelper, MainInterface.this);
                loginPage.setVisible(true);
            } else {
                showShopPage();
            }
        }
    }

    private class CartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loggedInUser == null) {
                LoginPage loginPage = new LoginPage(dbHelper, MainInterface.this);
                loginPage.setVisible(true);
            } else {
                CartPage cartPage = new CartPage(cartTable);
                cartPage.setVisible(true);
            }
        }
    }
    
    

    private class AccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAccountOptionsPage();
        }
    }

    private class PendingOrdersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PendingOrdersPage pendingOrdersPage = new PendingOrdersPage(orderTable);
            pendingOrdersPage.setVisible(true);
        }
    }

    private class LogoutButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        loggedInUser = null;
        JOptionPane.showMessageDialog(null, "Logout successful.");
        updateCartLabel();
    }
}

            
    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
    
            
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String url = "jdbc:postgresql://localhost:5432/mydatabase";
            String user = "postgres";
            String password = "edcr12";
            DatabaseHelper dbHelper = new DatabaseHelper(url, user, password);
            
            MainInterface mainInterface = new MainInterface(null, dbHelper);

            mainInterface.setVisible(true);
        });
    }


    public Object getProducts() {
        return null;
    }
}
