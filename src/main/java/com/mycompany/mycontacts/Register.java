package com.mycompany.mycontacts;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Register extends JFrame implements ActionListener {

    RoundedTextField userNameField;
    RoundedTextField phoneField;
    RoundedTextField workField;
    RoundedTextField emailField;
    RoundedPasswordField passwordField;
    RoundedPasswordField confirmPasswordField;
    RoundedButton signButton;

    Register() {

        // ImageIcon
        ImageIcon editIcon = new ImageIcon("addperson.png");

        // Title
        JLabel titleLabel = new JLabel("Create Your Account");
        titleLabel.setBounds(80, 35, 400, 50);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
        titleLabel.setForeground(new Color(44, 44, 44));


        // textfield
        userNameField = new RoundedTextField();
        userNameField.setBounds(200, 135, 210, 30);
        userNameField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        userNameField.setBackground(new Color(236, 236, 236));

        emailField = new RoundedTextField();
        emailField.setBounds(200, 195, 210, 30);
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        emailField.setBackground(new Color(236, 236, 236));

        phoneField = new RoundedTextField();
        phoneField.setBounds(200, 255, 210, 30);
        phoneField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        phoneField.setBackground(new Color(236, 236, 236));

        workField = new RoundedTextField();
        workField.setBounds(200, 315, 210, 30);
        workField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        workField.setBackground(new Color(236, 236, 236));

        passwordField = new RoundedPasswordField();
        passwordField.setBounds(200, 375, 210, 30);
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordField.setBackground(new Color(236, 236, 236));

        confirmPasswordField = new RoundedPasswordField();
        confirmPasswordField.setBounds(200, 435, 210, 30);
        confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        confirmPasswordField.setBackground(new Color(236, 236, 236));


        // label
        JLabel labelName = new JLabel();
        labelName.setText("Username:");
        labelName.setBounds(45, 130, 220, 40);
        labelName.setForeground(new Color(55, 54, 53));
        labelName.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelEmail = new JLabel();
        labelEmail.setText("Email:");
        labelEmail.setBounds(45, 190, 220, 40);
        labelEmail.setForeground(new Color(55, 54, 53));
        labelEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));


        JLabel labelPhone = new JLabel();
        labelPhone.setText("Phone Number:");
        labelPhone.setBounds(45, 250, 220, 40);
        labelPhone.setForeground(new Color(55, 54, 53));
        labelPhone.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelWork = new JLabel();
        labelWork.setText("Work Number:");
        labelWork.setBounds(45, 310, 220, 40);
        labelWork.setForeground(new Color(55, 54, 53));
        labelWork.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelPassword = new JLabel();
        labelPassword.setText("Password:");
        labelPassword.setBounds(45, 370, 220, 40);
        labelPassword.setForeground(new Color(55, 54, 53));
        labelPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelConfirmPassword = new JLabel();
        labelConfirmPassword.setText("Confirm Password:");
        labelConfirmPassword.setBounds(45, 430, 220, 40);
        labelConfirmPassword.setForeground(new Color(55, 54, 53));
        labelConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));

        // button
        signButton = new RoundedButton();
        signButton.setBounds(170, 520, 150, 57);
        signButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        signButton.setText("Register");
        signButton.setBackground(new Color(41, 110, 69));
        signButton.setForeground(Color.white);
        signButton.setFocusable(false);
        signButton.setBorderPainted(false);
        signButton.addActionListener(this);
        signButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signButton.setFocusPainted(false);

        // panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBounds(5, 5, 475, 650);
        panel.setLayout(null);
        panel.add(userNameField);
        panel.add(phoneField);
        panel.add(workField);
        panel.add(emailField);
        panel.add(labelName);
        panel.add(labelPhone);
        panel.add(labelWork);
        panel.add(labelEmail);
        panel.add(signButton);
        panel.add(labelPassword);
        panel.add(labelConfirmPassword);
        panel.add(passwordField);
        panel.add(confirmPasswordField);
        panel.add(titleLabel);
        

        // behindPanel

        GradientPanel behindPanel = new GradientPanel();
        behindPanel.setBackground(Color.white);
        behindPanel.setBounds(0, 0, 495, 670);
        behindPanel.setLayout(null);

        // Frame
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500, 700);
        this.add(panel);
        this.add(behindPanel);
        this.setTitle("Add Personal information");
        this.setIconImage(editIcon.getImage());
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null); 
    }

    @Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == signButton) {

        String name = userNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String work = workField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || work.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } else if (!password.equals(confirmPassword)) {
            javax.swing.JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Connection conn = DBConnection.getConnection();
                String query = "INSERT INTO Users (username, email, phone, work, password) VALUES (?, ?, ?, ?, ?)";
                java.sql.PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, work);
                ps.setString(5, password);
                ps.executeUpdate();

                javax.swing.JOptionPane.showMessageDialog(this, "Registration successful", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new Login();

            } catch (SQLException ex) {
                ex.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(this, "Error connecting to database", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
    public static void main(String[] args) {
        new Register();
    }
}