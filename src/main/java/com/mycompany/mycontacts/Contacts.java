package com.mycompany.mycontacts;

import javax.swing.*;

public class Contacts {
    private String firstName;
    private String lastName;
    private String email;
    private String mobilePhone;
    private String homePhone;
    private String address;
    public static boolean valid = true;

    public Contacts(String firstName, String lastName, String email, String mobilePhone, String homePhone,
            String address) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setMobilePhone(mobilePhone);
        setHomePhone(homePhone);
        setAddress(address);
    }

    public Contacts(String firstName, String lastName, String email, String mobilePhone, String homePhone) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setMobilePhone(mobilePhone);
        setHomePhone(homePhone);
    }

    public Contacts(String firstName, String lastName, String email, String mobilePhone) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setMobilePhone(mobilePhone);
    }

    public void setFirstName(String firstName) {

        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The First name cannot empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else if (firstName.trim().length() > 10) {
            JOptionPane.showMessageDialog(null, "The First name cannot be greater than 10 letters", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else {
            for (char x : firstName.toCharArray()) {
                if (!Character.isLetter(x)) {
                    JOptionPane.showMessageDialog(null, "The First name cannot contain numbers or symbols", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                }
            }
            if (valid) {
                this.firstName = firstName.trim();
            }
        }
    }


    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {

        if (lastName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The last name cannot empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else if (lastName.trim().length() > 10) {
            JOptionPane.showMessageDialog(null, "The last name cannot be greater than 10 letters", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else {
            for (char x : lastName.toCharArray()) {
                if (!Character.isLetter(x)) {
                    JOptionPane.showMessageDialog(null, "The last name cannot contain numbers or symbols", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    valid = false;
                    break;
                }
            }
            if (valid) {
                this.lastName = lastName.trim();
            }
        }
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }


    public void setEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-/#/%]{3,15}+@[a-zA-Z]{5}+.[a-zA-Z]{3}+$";

        if (email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "The email can not be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (email.matches(emailRegex)) {
            this.email = email.trim();
        } else {
            JOptionPane.showMessageDialog(null, "The Email is invalid", "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {

        if (mobilePhone.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The mobile phone cannot be empty", "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else if (mobilePhone.trim().length() != 11) {
            JOptionPane.showMessageDialog(null, "The mobile phone must be 11 number", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else if (!mobilePhone.startsWith("010") && !mobilePhone.startsWith("011") && !mobilePhone.startsWith("012")
                && !mobilePhone.startsWith("015")) {
            JOptionPane.showMessageDialog(null, "This number is not an egyptian number so it is invalid", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        for (char x : mobilePhone.toCharArray()) {
            if (!Character.isDigit(x)) {
                JOptionPane.showMessageDialog(null, "The mobile phone cannot be Symbols or letter", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                valid = false;
                break;
            }
        }
        if (valid) {
            this.mobilePhone = mobilePhone.trim();
        }
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {

        if (homePhone.trim().length() != 10) {
            JOptionPane.showMessageDialog(null, "The home phone must be 10 number", "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else if (!homePhone.startsWith("0")) {
            JOptionPane.showMessageDialog(null, "This number is not an home egyptian number so it is invalid", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        for (char x : homePhone.toCharArray()) {
            if (!Character.isDigit(x)) {
                JOptionPane.showMessageDialog(null, "The home phone cannot be Symbols or letter", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                valid = false;
                break;
            }
        }
        if (valid) {
            this.homePhone = homePhone.trim();
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        if (address.trim().length() > 20) {
            JOptionPane.showMessageDialog(null, "The address is cannot be Symbols and must be less than 25 letter",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            valid = false;
        } else {
            this.address = address;
        }

    }

}