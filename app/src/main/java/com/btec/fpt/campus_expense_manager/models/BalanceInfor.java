package com.btec.fpt.campus_expense_manager.models;

public class BalanceInfor {
    private String email;

    private String fullName;
    private String firstName;
    private String lastName;
    private double balance;

    public BalanceInfor(String email, String fullName, String firstName, String lastName, double balance) {
        this.email = email;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getBalanceInfor() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
