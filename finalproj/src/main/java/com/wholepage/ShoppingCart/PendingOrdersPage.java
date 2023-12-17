package com.wholepage.ShoppingCart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import com.wholepage.MainInterface;
import com.wholepage.Model.Order;
import com.wholepage.Model.OrderManager;

public class PendingOrdersPage extends JFrame {

    private JTable ordersTable;
    private MainInterface mainInterface;
    private JTable pendingOrdersTable;
    private JButton processOrderButton;
    private List<Order> orders;
    private OrderManager orderManager;
    

    public PendingOrdersPage(JTable ordersTable) {
        initComponents();
        this.ordersTable = ordersTable;
        this.orders = new ArrayList<>();
    }

    public PendingOrdersPage(OrderManager orderManager) {
        this.orderManager = orderManager;
        initComponents();
        this.ordersTable = new JTable();
        this.orders = new ArrayList<>();
    }
    

    public void loadPendingOrders(List<Order> orders) {
        if (orders == null) {
            return;
        }
        this.orders = orders;
        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
        model.setRowCount(0);
        for (Order order : orders) {
            model.addRow(new Object[]{order.getOrderNumber(), order.getDate(), order.getTotalPrice()});
        }
    }
    

    private void initComponents() {
        setTitle("Pending Orders");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        // Create a panel for the table and add it to the frame
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(tablePanel, BorderLayout.CENTER);

        // Create a table for the pending orders
        String[] columnNames = {"Order ID", "Customer Name", "Order Date", "Total Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        pendingOrdersTable = new JTable(model);
        pendingOrdersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the table to the panel
        JScrollPane scrollPane = new JScrollPane(pendingOrdersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons and add it to the frame
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        add(buttonPanel, BorderLayout.SOUTH);

        // Create a button for processing orders
        processOrderButton = new JButton("Process Order");
        buttonPanel.add(processOrderButton);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTable ordersTable = new JTable();
            PendingOrdersPage pendingOrdersPage = new PendingOrdersPage(ordersTable);
            pendingOrdersPage.setVisible(true);
        });
    }
    
}
