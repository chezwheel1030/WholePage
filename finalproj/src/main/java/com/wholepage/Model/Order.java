package com.wholepage.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private Cart cart;
    private int userId;
    private List<CartItem> items;
    private double totalPrice;
    private String status;
    private int orderNumber;
    private DatabaseHelper dbHelper;
    private Date date;
    private List<Order> pendingOrders = new ArrayList<>();


    public Order(int id, int userId, List<CartItem> items, double totalPrice, String status, Date date) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void User() {
        this.cart = new Cart();
    }

    // Getters and setters for the class fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    

    public void placeOrder() {
        Order newOrder = new Order(0, this.getUserId(), cart.getItems(), cart.getTotalPrice(), "Pending", date);
        dbHelper.addOrder(newOrder);
        pendingOrders.add(newOrder);
        cart.clearCart();
    }

    public int getOrderNumber() {
        return orderNumber;
    }
    

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
