package com.btec.fpt.campus_expense_manager.entities;

import java.io.Serializable;

public class Goals implements Serializable {
    private int id;
    private String goalName;
    private double goalAmount;
    private double currentAmount;
    private boolean isAchieved;

    // Default constructor
    public Goals() {}

    // Constructor with parameters
    public Goals(String goalName, double goalAmount) {
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.currentAmount = 0;
        this.isAchieved = false;
    }

    // Full constructor
    public Goals(int id, String goalName, double goalAmount, double currentAmount, boolean isAchieved) {
        this.id = id;
        this.goalName = goalName;
        this.goalAmount = goalAmount;
        this.currentAmount = currentAmount;
        this.isAchieved = isAchieved;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public void setAchieved(boolean achieved) {
        isAchieved = achieved;
    }

    // Method to check if goal is achieved
    public boolean checkGoalAchieved() {
        this.isAchieved = currentAmount >= goalAmount;
        return this.isAchieved;
    }

    // Method to update current amount
    public void updateCurrentAmount(double amount) {
        this.currentAmount += amount;
        checkGoalAchieved();
    }

    @Override
    public String toString() {
        return "Goal: " + goalName + ", Target: $" + goalAmount + 
               ", Current: $" + currentAmount + 
               ", Achieved: " + (isAchieved ? "Yes" : "No");
    }
}
