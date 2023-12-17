package com.wholepage.ShoppingCart;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class BuyButtonRenderer extends DefaultTableCellRenderer {
    
    private JButton buyButton;
    
    public BuyButtonRenderer() {
        buyButton = new JButton("Add to Cart");
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return buyButton;
    }
}
