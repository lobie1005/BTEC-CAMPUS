package vn.btec.campusexpensemanagement.models;

public class BalanceInfor {
    private String email;

    private String fullName;
    private String firstName;
    private String lastName;
    private double balance;
    private double totalIncomes;
    private double totalExpense;
    private String incommingExplain;

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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getTotalIncome() {
        return totalIncomes;
    }

    public void setTotalIncome(double totalIncomes) {
        this.totalIncomes = totalIncomes;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public void setIncommingExplain(String incommingExplain) {
        this.incommingExplain = incommingExplain;
    }
}
