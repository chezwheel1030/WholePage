package com.wholepage.CredPage;

import javax.swing.*;
import com.wholepage.Model.DatabaseHelper;
import com.wholepage.Model.User;
import com.wholepage.Model.User.UserType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountPage extends JFrame {

    private User loggedInUser;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<UserType> userTypeComboBox;
    private DatabaseHelper dbHelper;
    private JTextField managerKeyField;
    private JLabel managerKeyLabel;
    private AccountOptionsPage callback;

    public CreateAccountPage(DatabaseHelper dbHelper, AccountOptionsPage callback, User loggedInUser) {
        this.dbHelper = dbHelper;
        this.callback = callback;
        this.loggedInUser = loggedInUser;
        initComponents();
    }

    private void initComponents() {
        setTitle("Create Account");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(100, 50, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(190, 50, 100, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(100, 90, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(190, 90, 100, 25);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(50, 130, 130, 25);
        panel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBounds(190, 130, 100, 25);
        panel.add(confirmPasswordField);

        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeLabel.setBounds(100, 170, 80, 25);
        panel.add(userTypeLabel);

        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        if (dbHelper.isUserManager(loggedInUser)) {
            String managerKey = JOptionPane.showInputDialog(null, "Enter the manager key:");
            if (dbHelper.validateManagerKey(managerKey)) {
                AddEmployeePage addEmployeePage = new AddEmployeePage(dbHelper);
                addEmployeePage.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid manager key.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Only managers can add employees.");
        }
        }
    });
        panel.add(addEmployeeButton);


        userTypeComboBox = new JComboBox<>(UserType.values());
        userTypeComboBox.setBounds(190, 170, 100, 25);
        panel.add(userTypeComboBox);

        managerKeyLabel = new JLabel("Manager Key:");
        managerKeyLabel.setBounds(100, 210, 80, 25);
        panel.add(managerKeyLabel);

        managerKeyField = new JTextField(20);
        managerKeyField.setBounds(190, 210, 100, 25);
        panel.add(managerKeyField);

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(145, 250, 120, 25);
        createAccountButton.addActionListener(new CreateAccountButtonListener());
        panel.add(createAccountButton);

        add(panel, BorderLayout.CENTER);
    }
    
    private class CreateAccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
            UserType userType = (UserType) userTypeComboBox.getSelectedItem();
            String managerKey = managerKeyField.getText();

            if (!managerKey.isEmpty()) {
                boolean validKey = dbHelper.validateManagerKey(managerKey);
                if (validKey) {
                    userType = UserType.MANAGER;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid manager key.");
                    return;
                }
            } else {
                userType = UserType.CUSTOMER;
            }

            // Create the user account with the username, password, and userType
            boolean success = dbHelper.createUser(username, password, userType);
            if (success) {
                JOptionPane.showMessageDialog(null, "Account created successfully.");
                callback.showLogin();
            } else {
                JOptionPane.showMessageDialog(null, "Account creation failed.");
            }
        }
    }
}
