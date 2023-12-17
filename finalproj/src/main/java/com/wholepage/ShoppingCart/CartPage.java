package com.wholepage.ShoppingCart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.wholepage.MainInterface;
import com.wholepage.Model.CartItem;
import com.wholepage.Model.Order;
import com.wholepage.Model.OrderManager;
import com.wholepage.Model.User;

public class CartPage extends JFrame {

    private JTable cartTable;
    private JButton checkoutButton;
    private User loggedInUser;
    private List<CartItem> cartItems;
    private MainInterface mainInterface;
    private OrderManager orderManager;

    public CartPage(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        loggedInUser = mainInterface.getLoggedInUser();
        cartItems = loggedInUser.getCart().getItems();
        initOrderManager(mainInterface.getOrderManager());
        initComponents();
        loadCart();
        mainInterface.getPendingOrdersPage().loadPendingOrders(orderManager.getPendingOrders());

    }

    public CartPage(JTable cartTable) {
        this.cartTable = cartTable;
        initComponents();
    }

    public void initOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    private void checkout() {
        if (orderManager == null) {
            System.out.println("Order manager not initialized");
            return;
        }

        List<Order> pendingOrders = orderManager.getPendingOrders(); // Error occurs here
        if (pendingOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no items in the cart to checkout.");
            return;
        }
    }
    

    private void initComponents() {
        setTitle("Cart Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BorderLayout());

        // Create a panel for the table and add it to the frame
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(tablePanel, BorderLayout.CENTER);

        // Create a table for the items in the cart
        String[] columnNames = {"Name", "Description", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(model);

        // Add the table to the panel
        JScrollPane scrollPane = new JScrollPane(cartTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the button and add it to the frame
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        add(buttonPanel, BorderLayout.SOUTH);

        // Create a button for checking out
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new CheckoutListener());
        buttonPanel.add(checkoutButton);

        pack();
        setLocationRelativeTo(null);
    }

    private void loadCart() {
        DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
        model.setRowCount(0);

        for (CartItem cartItem : cartItems) {
            model.addRow(new Object[]{cartItem.getItem().getName(), cartItem.getItem().getDescription(), cartItem.getItem().getPrice(), cartItem.getQuantity()});
        }
    }

    private class CheckoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Thank you for your purchase!");
            loggedInUser.getCart().clearCart();
            mainInterface.updateCartLabel();
    
            loggedInUser.placeOrder();
            mainInterface.updateCartLabel();
            
            // Null check added here
            if (mainInterface.getPendingOrdersPage() != null) {
                mainInterface.getPendingOrdersPage().loadPendingOrders(loggedInUser.getPendingOrders());
            }
            
            dispose();
        }
    }
    

}
