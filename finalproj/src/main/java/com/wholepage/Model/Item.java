package com.wholepage.Model;

public class Item {
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    
    public Item(String string, String name, double price) {
        this.id = string;
        this.name = name;
        this.price = price;
    }

    public Item(int newId, String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
