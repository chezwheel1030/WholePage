package com.wholepage.CredPage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.wholepage.MainInterface;
import com.wholepage.Model.DatabaseHelper;
import com.wholepage.Model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountOptionsPage extends JFrame implements OnLoginSuccessListener {
    private JButton loginButton;
    private JButton createAccountButton;
    private DatabaseHelper dbHelper;
    private MainInterface parentFrame;

    public AccountOptionsPage(DatabaseHelper dbHelper,MainInterface parentFrame) {
        this.parentFrame = parentFrame;
        this.dbHelper = dbHelper;
        initComponents();
    }

    public void showLogin() {
        LoginPage loginPage = new LoginPage(dbHelper, this);
        loginPage.setVisible(true);
        setVisible(false); // Hide the current window
    }

    private void initComponents() {
        setTitle("Account Options");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 1, 0, 10));

        JLabel titleLabel = new JLabel("Welcome to WholePage!");
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        contentPane.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Please log in or create an account to continue.");
        contentPane.add(subtitleLabel);

        loginButton = new JButton("Log In");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginPage loginPage = new LoginPage(dbHelper, AccountOptionsPage.this);
                loginPage.setVisible(true);
                setVisible(false); // Hide the current window
            }
        });
        contentPane.add(loginButton);

        createAccountButton = new JButton("Create Account");
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CreateAccountPage createAccountPage = new CreateAccountPage(dbHelper, AccountOptionsPage.this, null);
                createAccountPage.setVisible(true);
                setVisible(false); // Hide the current window
            }
        });
        contentPane.add(createAccountButton);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void onLoginSuccess(User user) {
        MainInterface mainInterface = new MainInterface(user, dbHelper);
        mainInterface.updateCartLabel();
        setVisible(false); // Hide the current window
        mainInterface.setVisible(true); // Show the main window
    }
    
    


    public void close() {
        this.dispose();
    }


}
