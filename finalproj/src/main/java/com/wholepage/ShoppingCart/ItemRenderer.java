package com.wholepage.ShoppingCart;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.wholepage.Model.Item;

public class ItemRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Item) {
            Item item = (Item) value;
            return super.getTableCellRendererComponent(table, item.getName(), isSelected, hasFocus, row, column);
        } else {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
