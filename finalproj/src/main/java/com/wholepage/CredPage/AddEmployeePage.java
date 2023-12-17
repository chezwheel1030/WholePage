package com.wholepage.CredPage;

import javax.swing.*;
import com.wholepage.Model.DatabaseHelper;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEmployeePage extends JFrame {
    private JTextField nameField;
    private JTextField positionField;
    private JTextField salaryField;
    private DatabaseHelper dbHelper;

    public AddEmployeePage(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        initComponents();
    }

    private void initComponents() {
        setTitle("Add Employee");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        panel.add(nameField);

        panel.add(new JLabel("Position:"));
        positionField = new JTextField(20);
        panel.add(positionField);

        panel.add(new JLabel("Salary:"));
        salaryField = new JTextField(20);
        panel.add(salaryField);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(new AddEmployeeButtonListener());
        panel.add(addButton);

        add(panel, BorderLayout.CENTER);
    }

    private class AddEmployeeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String position = positionField.getText();
            double salary = Double.parseDouble(salaryField.getText());

            boolean success = dbHelper.addEmployee(name, position, salary);
            if (success) {
                JOptionPane.showMessageDialog(null, "Employee added successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add employee.");
            }
        }
    }
}
