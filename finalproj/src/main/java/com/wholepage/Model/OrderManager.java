package com.wholepage.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.wholepage.MainInterface;

public class OrderManager {
    private MainInterface mainInterface;
    private List<Order> pendingOrders;
    private List<Order> completedOrders;
    private DatabaseHelper dbHelper;
    private Date date;
    private List<Order> orders;
    private JTable ordersTable;

    public OrderManager(MainInterface mainInterface, DatabaseHelper dbHelper, JTable ordersTable) {
        this.mainInterface = mainInterface;
        this.dbHelper = dbHelper;
        this.pendingOrders = new ArrayList<>();
        this.completedOrders = new ArrayList<>();
        this.ordersTable = ordersTable;
    }
    

    public OrderManager() {
        this.orders = new ArrayList<>();
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public List<Order> getCompletedOrders() {
        return completedOrders;
    }

    public void placeOrder(User user, Cart cart) {
        Order newOrder = new Order(0, user.getId(), cart.getItems(), cart.getTotalPrice(), "Pending", date);
        dbHelper.addOrder(newOrder);
        pendingOrders.add(newOrder);
        cart.clearCart();
        if (mainInterface.getPendingOrdersPage() != null) {
            mainInterface.getPendingOrdersPage().loadPendingOrders(pendingOrders);
        }
    }
    

    public void markOrderAsCompleted(Order order) {
        dbHelper.updateOrderStatus(order, "Completed");
        pendingOrders.remove(order);
        completedOrders.add(order);
    }

    public void loadOrders() {
        List<Order> orders = dbHelper.getPendingOrders();
        for (Order order : orders) {
            if (order.getStatus().equals("Pending")) {
                pendingOrders.add(order);
            } else if (order.getStatus().equals("Completed")) {
                completedOrders.add(order);
            }
        }
        System.out.println("Pending orders loaded: " + pendingOrders); // Add this line
    }
    
    
    public void loadPendingOrders(List<Order> orders) {
        if (this.orders == null) {
            this.orders = new ArrayList<Order>();
        }
        this.orders = orders;
        DefaultTableModel model = (DefaultTableModel) ordersTable.getModel();
        model.setRowCount(0);
        for (Order order : orders) {
            model.addRow(new Object[]{order.getOrderNumber(), order.getDate(), order.getTotalPrice(), order.getStatus()});

        }
    }
}
