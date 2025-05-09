package com.mycompany.mycontacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class Person {

    private String name;
    private String mobilePhone;
    private String work;
    private String email;
    private String userId;
    private boolean valid = true;

    public Person(String name, String mobilePhone, String work, String email) {
        setName(name);
        setMobilePhone(mobilePhone);
        setWork(work);
        setEmail(email);
        setUserId(String.valueOf(getUserIdByEmail(email)));
        System.out.println("User ID: " + getUserId());
    }

    public Person(String name, String mobilePhone, String email) {
        setName(name);
        setMobilePhone(mobilePhone);
        setEmail(email);
    }

    public Person(String name) {
        setName(name);
    }

    public Person() {
    }

    public boolean isValid() {
        return valid;
    }

    public String getName() {
        return name;
    }

    public static int getUserIdByEmail(String email) {
        int userId = -1;
        String query = "SELECT id FROM Users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setName(String name) {
        boolean localValid = true;
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The name cannot be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else if (name.length() > 15) {
            JOptionPane.showMessageDialog(null, "The name cannot be greater than 15 letters", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else {
            for (char x : name.toCharArray()) {
                if (!Character.isLetter(x) && x != ' ') {
                    JOptionPane.showMessageDialog(null, "The name cannot contain numbers or symbols", "ERROR", JOptionPane.ERROR_MESSAGE);
                    localValid = false;
                    break;
                }
            }
        }
        if (localValid) {
            this.name = name;
        }
        valid = valid && localValid;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        boolean localValid = true;
        if (mobilePhone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The mobile phone cannot be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else if (mobilePhone.trim().length() != 11) {
            JOptionPane.showMessageDialog(null, "The mobile phone must be 11 digits", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else if (!mobilePhone.startsWith("010") && !mobilePhone.startsWith("011") && !mobilePhone.startsWith("012") && !mobilePhone.startsWith("015")) {
            JOptionPane.showMessageDialog(null, "The mobile phone must start with 010, 011, 012, or 015", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        }

        for (char x : mobilePhone.toCharArray()) {
            if (!Character.isDigit(x)) {
                JOptionPane.showMessageDialog(null, "The mobile phone cannot contain letters or symbols", "ERROR", JOptionPane.ERROR_MESSAGE);
                localValid = false;
                break;
            }
        }

        if (localValid) {
            this.mobilePhone = mobilePhone.trim();
        }
        valid = valid && localValid;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        boolean localValid = true;
        if (work.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The work phone cannot be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else if (work.trim().length() < 6 || work.trim().length() > 10) {
            JOptionPane.showMessageDialog(null, "The work phone is invalid", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        }

        for (char x : work.toCharArray()) {
            if (!Character.isDigit(x)) {
                JOptionPane.showMessageDialog(null, "The work phone cannot contain letters or symbols", "ERROR", JOptionPane.ERROR_MESSAGE);
                localValid = false;
                break;
            }
        }

        if (localValid) {
            this.work = work.trim();
        }
        valid = valid && localValid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        boolean localValid = true;
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$";
        if (email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "The email cannot be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(null, "The email is invalid", "ERROR", JOptionPane.ERROR_MESSAGE);
            localValid = false;
        } else {
            this.email = email.trim();
        }
        valid = valid && localValid;
    }
}