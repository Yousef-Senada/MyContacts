package com.mycompany.mycontacts;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class EditPersonal extends JFrame implements ActionListener {

    RoundedTextField nameField;
    RoundedTextField phoneField;
    RoundedTextField workField;
    RoundedTextField emailField;
    RoundedButton updateButton;
    RoundedButton cancelButton;
    private int userId;

    EditPersonal(int userId) {
        this.userId = userId;

        // ImageIcon
        ImageIcon editIcon = new ImageIcon("edit.png");

        // textField
        nameField = new RoundedTextField();
        nameField.setBounds(130, 125, 210, 30);
        nameField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        nameField.setBackground(new Color(236, 236, 236));

        phoneField = new RoundedTextField();
        phoneField.setBounds(130, 185, 210, 30);
        phoneField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        phoneField.setBackground(new Color(236, 236, 236));

        workField = new RoundedTextField();
        workField.setBounds(130, 245, 210, 30);
        workField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        workField.setBackground(new Color(236, 236, 236));

        emailField = new RoundedTextField();
        emailField.setBounds(130, 305, 210, 30);
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 15));
        emailField.setBackground(new Color(236, 236, 236));

        // label
        JLabel labelName = new JLabel();
        labelName.setText("Name:");
        labelName.setBounds(55, 120, 220, 40);
        labelName.setForeground(new Color(55, 54, 53));
        labelName.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelPhone = new JLabel();
        labelPhone.setText("Phone:");
        labelPhone.setBounds(50, 180, 220, 40);
        labelPhone.setForeground(new Color(55, 54, 53));
        labelPhone.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelWork = new JLabel();
        labelWork.setText("Work:");
        labelWork.setBounds(57, 240, 220, 40);
        labelWork.setForeground(new Color(55, 54, 53));
        labelWork.setFont(new Font("Tahoma", Font.PLAIN, 18));

        JLabel labelEmail = new JLabel();
        labelEmail.setText("Email:");
        labelEmail.setBounds(55, 300, 220, 40);
        labelEmail.setForeground(new Color(55, 54, 53));
        labelEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));

        // button
        updateButton = new RoundedButton();
        updateButton.setBounds(80, 400, 130, 57);
        updateButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        updateButton.setText("Update");
        updateButton.setBackground(new Color(41, 110, 69));
        updateButton.setForeground(Color.white);
        updateButton.setFocusable(false);
        updateButton.setBorderPainted(false);
        updateButton.addActionListener(this);

        cancelButton = new RoundedButton();
        cancelButton.setBounds(280, 400, 130, 57);
        cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        cancelButton.setText("Cancel");
        cancelButton.setBackground(new Color(130, 30, 53));
        cancelButton.setForeground(Color.white);
        cancelButton.setFocusable(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(this);

        // panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        panel.setBounds(5, 5, 475, 550);
        panel.setLayout(null);
        panel.add(nameField);
        panel.add(phoneField);
        panel.add(workField);
        panel.add(emailField);
        panel.add(labelName);
        panel.add(labelPhone);
        panel.add(labelWork);
        panel.add(labelEmail);
        panel.add(updateButton);
        panel.add(cancelButton);

        // behindPanel
        GradientPanel behindPanel = new GradientPanel();
        behindPanel.setBackground(Color.white);
        behindPanel.setBounds(0, 0, 495, 570);
        behindPanel.setLayout(null);

        // Frame
        this.setLayout(null);
        this.setSize(500, 600);
        this.add(panel);
        this.add(behindPanel);
        this.setTitle("Edit Personal information");
        this.setIconImage(editIcon.getImage());
        this.setResizable(false);
        this.setVisible(true);
    }

    @SuppressWarnings("unused")
    public void actionPerformed(ActionEvent e) {
        // update Button
        if (e.getSource() == updateButton) {
            Person person = new Person();
            person.setName(nameField.getText());
            person.setEmail(emailField.getText());
            person.setMobilePhone(phoneField.getText());
            person.setWork(workField.getText());

            if (person.isValid()) {
                try {
                    Connection conn = DBConnection.getConnection();
                    String query = "UPDATE Users SET username = ?, email = ?, phone = ?, work = ? WHERE id = ?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, person.getName());
                    ps.setString(2, person.getEmail());
                    ps.setString(3, person.getMobilePhone());
                    ps.setString(4, person.getWork());
                    ps.setInt(5, userId);
                    ps.executeUpdate();

                    GUI.label.setText(person.getName());
                    GUI.label2.setText(person.getMobilePhone());
                    GUI.label4.setText(person.getWork());
                    GUI.label6.setText(person.getEmail());

                    JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();

                    ps.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating profile", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // cancel Button
        if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }
}