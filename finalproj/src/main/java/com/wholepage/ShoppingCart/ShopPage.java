package com.wholepage.ShoppingCart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.wholepage.MainInterface;
import com.wholepage.Model.Item;
import com.wholepage.Model.User;

public class ShopPage extends JFrame {

    private JTable itemTable;
    private JButton addToCartButton;
    private JButton viewCartButton;
    private JButton backButton;
    private User loggedInUser;
    private List<Item> items;
    private MainInterface mainInterface;

    public List<Item> getItems() {
        return items;
    }

    public void refreshItems() {
        items = mainInterface.getDbHelper().getItems();
        loadItems();
    }
    

    public ShopPage(MainInterface mainInterface) {
        this.mainInterface = mainInterface;
        loggedInUser = mainInterface.getLoggedInUser();
        items = mainInterface.getDbHelper().getItems();
        initComponents();
        loadItems();
    }

    private void initComponents() {
        setTitle("Shop Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BorderLayout());
        

        // Create a panel for the table and add it to the frame
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(tablePanel, BorderLayout.CENTER);

        // Create a table for the items in the shop
        String[] columnNames = {"Name", "Description", "Price", "Add to Cart"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        itemTable = new JTable(model);
        itemTable.getColumnModel().getColumn(3).setCellEditor(new BuyButtonE(new JCheckBox(), loggedInUser));
        itemTable.getColumnModel().getColumn(3).setCellRenderer(new BuyButtonRenderer());


        // Add the table to the panel
        JScrollPane scrollPane = new JScrollPane(itemTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons and add it to the frame
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        add(buttonPanel, BorderLayout.SOUTH);

        // Create a button for adding items to the cart
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new AddToCartListener());
        buttonPanel.add(addToCartButton);

        // Create a button for viewing the cart
        viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(new ViewCartListener());
        buttonPanel.add(viewCartButton);

        // Create a button for going back to the MainInterface
        backButton = new JButton("Back");
        backButton.addActionListener(new BackToMainInterfaceListener());
        buttonPanel.add(backButton);

        pack();
        setLocationRelativeTo(null);
    }

    private void loadItems() {
        DefaultTableModel model = (DefaultTableModel) itemTable.getModel();
        model.setRowCount(0);
    
        for (Item item : items) {
            model.addRow(new Object[]{item.getName(), item.getDescription(), item.getPrice(), "Add to Cart"});
        }
    }
    
    
    

    private class AddToCartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = itemTable.getSelectedRow();
    
            if (selectedRow != -1) {
                Item selectedItem = items.get(selectedRow);
                loggedInUser.addToCart(selectedItem, 1);
                JOptionPane.showMessageDialog(null, "Item added to cart.");
                mainInterface.updateCartLabel();
            } else {
                JOptionPane.showMessageDialog(null, "No item selected.");
            }
        }
    }
    


    private class ViewCartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            CartPage cartPage = new CartPage(mainInterface);
            cartPage.setVisible(true);
        }
    }

    private class BackToMainInterfaceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ShopPage.this.dispose(); // Close the ShopPage
            mainInterface.setVisible(true); // Show the MainInterface
        }
    }

    private class BuyButtonE extends DefaultCellEditor {
        protected JButton button;
        private Item currentItem;
        private User loggedInUser;
        private boolean isPushed;
    
        public BuyButtonE(JCheckBox checkBox, User loggedInUser) {
            super(checkBox);
            this.loggedInUser = loggedInUser;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }
    
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentItem = items.get(row);
            button.setText("Add to Cart");
            isPushed = true;
            return button;
        }
    
        public Object getCellEditorValue() {
            if (isPushed) {
                loggedInUser.addToCart(currentItem, 1);
                JOptionPane.showMessageDialog(null, "Item added to cart.");
                mainInterface.updateCartLabel();
            }
            isPushed = false;
            return "Add to Cart";
        }
    
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
}
