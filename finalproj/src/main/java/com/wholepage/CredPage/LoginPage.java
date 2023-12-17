package com.wholepage.CredPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.wholepage.MainInterface;
import com.wholepage.Model.DatabaseHelper;
import com.wholepage.Model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private AccountOptionsPage accountOptionsPage;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    private User loggedInUser;
    private MainInterface mainInterface;
    private DatabaseHelper dbHelper;

    public LoginPage(DatabaseHelper dbHelper, AccountOptionsPage accountOptionsPage) {
        this.dbHelper = dbHelper;
        this.accountOptionsPage = accountOptionsPage;
        initComponents();
    }

    public LoginPage(DatabaseHelper dbHelper, MainInterface mainInterface) {
        this.dbHelper = dbHelper;
        this.mainInterface = mainInterface;
        initComponents();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void initComponents() {
        setTitle("Log In");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 1, 0, 10));

        JLabel titleLabel = new JLabel("Log In to WholePage");
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        contentPane.add(titleLabel);

        JPanel usernamePanel = new JPanel(new BorderLayout());
        JLabel usernameLabel = new JLabel("Username:");
        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernameField = new JTextField();
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        contentPane.add(usernamePanel);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        JLabel passwordLabel = new JLabel("Password:");
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordField = new JPasswordField();
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        contentPane.add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButton = new JButton("Log In");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                User user = dbHelper.login(username, password);
                if (user != null) {
                    if (accountOptionsPage != null) {
                        accountOptionsPage.onLoginSuccess(user);
                    } else if (mainInterface != null) {
                        mainInterface.setUser(user);
                        mainInterface.updateCartLabel();
                    }
                    setVisible(false);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password.");
                }
            }
        });

        buttonPanel.add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        contentPane.add(buttonPanel);

        pack();
        setLocationRelativeTo(null);
    }

}
