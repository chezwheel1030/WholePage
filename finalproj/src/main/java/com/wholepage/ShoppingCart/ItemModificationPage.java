package com.wholepage.ShoppingCart;

import java.awt.event.*;
import javax.swing.*;

import com.wholepage.Model.Item;

import java.util.HashMap;

public class ItemModificationPage extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JTextField itemNameField;
    private JTextField itemQuantityField;
    private JTextField itemPriceField;
    private JButton updateButton;
    private JButton addButton;
    private JButton deleteButton;

    private HashMap<Integer, Item> items; // The data structure that stores the items
    private Item currentItem; // The currently selected item

    public ItemModificationPage(HashMap<Integer, Item> items) {
        this.items = items;
        initComponents();
    }

    private void initComponents() {
        // ... (same as before)

        // Add event listeners
        searchButton.addActionListener(new SearchItemListener());
        updateButton.addActionListener(new UpdateItemListener());
        addButton.addActionListener(new AddItemListener());
        deleteButton.addActionListener(new DeleteItemListener());

        pack();
        setLocationRelativeTo(null);
    }

    // Event listeners for buttons
    private class SearchItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchStr = searchField.getText().trim();
            for (Item item : items.values()) {
                if (item.getName().equalsIgnoreCase(searchStr)) {
                    currentItem = item;
                    itemNameField.setText(item.getName());
                    itemQuantityField.setText(String.valueOf(item.getQuantity()));
                    itemPriceField.setText(String.valueOf(item.getPrice()));
                    break;
                }
            }
        }
    }

    private class UpdateItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentItem != null) {
                currentItem.setName(itemNameField.getText());
                currentItem.setQuantity(Integer.parseInt(itemQuantityField.getText()));
                currentItem.setPrice(Double.parseDouble(itemPriceField.getText()));
                JOptionPane.showMessageDialog(null, "Item updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No item selected.");
            }
        }
    }

    private class AddItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = itemNameField.getText();
            int quantity = Integer.parseInt(itemQuantityField.getText());
            double price = Double.parseDouble(itemPriceField.getText());

            int newId = items.size() + 1; // Generate a new ID (not ideal, just for demonstration)
            Item newItem = new Item(newId, name, quantity, price);
            items.put(newId, newItem);
            JOptionPane.showMessageDialog(null, "Item added successfully.");
        }
    }

    private class DeleteItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentItem != null) {
                items.remove(currentItem.getId());
                itemNameField.setText("");
                itemQuantityField.setText("");
                itemPriceField.setText("");
                JOptionPane.showMessageDialog(null, "Item deleted successfully.");
                currentItem = null;
            } else {
                JOptionPane.showMessageDialog(null, "No item selected.");
            }
        }
    }
    
}
