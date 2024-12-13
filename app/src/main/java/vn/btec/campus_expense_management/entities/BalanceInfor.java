package vn.btec.campus_expense_management.entities;
import vn.btec.campus_expense_management.DataStatic;

public class BalanceInfor {

    private int userId;
    private double income;
    private double expense;
    private double balance;

    public BalanceInfor(int userId, double balance, double income, double expense) {
        this.balance = balance;
        this.userId = userId;
        this.income = income;
        this.expense = expense;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}