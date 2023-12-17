package com.wholepage.Model;

import java.util.ArrayList;
import java.util.List;

public class Cart<T extends CartItem> {
    private List<T> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public List<T> getItems() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }

    public void clearCart() {
        items.clear();
    }

    public void clear() {
        items.clear();
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getItem().getPrice() * item.getQuantity();
        }
        return total;
    }
    
    
    

    public double getSubtotal() {
        double subtotal = 0.0;
        for (T item : items) {
            subtotal += item.getPrice();
        }
        return subtotal;
    }
}
