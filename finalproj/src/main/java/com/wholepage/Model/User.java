package com.wholepage.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class User {

    public enum UserType {
        MANAGER, EMPLOYEE, CUSTOMER
    }

    private int id;
    private String username;
    private String password;
    private UserType userType;
    private Cart<CartItem> cart;
    private List<Order> orders;
    private Date date;
    private OrderManager orderManager;

    List<CartItem> cart2 = new ArrayList<CartItem>();
    List<Order> pendingOrders = new ArrayList<>();

    public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.cart = new Cart<CartItem>();
        this.orders = new ArrayList<>();
        this.orderManager = orderManager;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.cart = new Cart<CartItem>();
        this.orders = new ArrayList<>();
        this.orderManager = orderManager;
    }

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.cart = new Cart<CartItem>();
        this.orders = new ArrayList<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public Cart<CartItem> getCart() {
        return cart;
    }

    public void setCart(Cart<CartItem> cart) {
        this.cart = cart;
    }

    public void addToCart(Item item, int quantity) {
        CartItem cartItem = new CartItem(item, quantity);
        cart.addItem(cartItem);
    }

    public void removeFromCart(Item item) {
        CartItem cartItem = new CartItem(item, 0);
        cart.removeItem(cartItem);
    }

    private double calculateCartTotal() {
        double total = 0;
        List<Order> orders = getPendingOrders(); // get the pending orders for the user
        for (Order order : orders) {
            for (CartItem item : order.getItems()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        return total;
    }
    

    public int getNextOrderId() {
        // get the highest order ID currently in the system
        int maxOrderId = 0;
        for (Order order : orders) {
            if (order.getId() > maxOrderId) {
                maxOrderId = order.getId();
            }
        }

        // return the next available order ID
        return maxOrderId + 1;
    }

    public Order placeOrder() {
        double totalPrice = calculateCartTotal();
        int orderId = getNextOrderId();
        Order order = new Order(orderId, id, new ArrayList<>(cart.getItems()), totalPrice, "Pending", new Date(System.currentTimeMillis()));
        orderManager.placeOrder(this, cart); // Use OrderManager to place the order
        return order;
    }
    

    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : pendingOrders) {
            if (order.getStatus().equals("Pending")) {
                orders.add(order);
            }
        }
        return orders;
    }

    public List<CartItem> getCartItems() {
        return cart.getItems();
    }
}
