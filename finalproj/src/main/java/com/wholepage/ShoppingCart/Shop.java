package com.wholepage.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

import com.wholepage.Model.Item;

public class Shop {
    private List<Item> items;

    public Shop() {
        items = new ArrayList<>();
        // Add items to the shop
        items.add(new Item("Item 1", "Description 1", 9.99));
        items.add(new Item("Item 2", "Description 2", 19.99));

        // Add more items as needed
    }

    public List<Item> getItems() {
        return items;
    }
}
