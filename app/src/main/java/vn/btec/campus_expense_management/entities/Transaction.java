package vn.btec.campus_expense_management.entities;


public class Transaction {

    private int id;
    private double amount;
    private String description;
    private String date;
    private int type; // 0: income, 1: expense
    private int userId;
    private String category;

    public Transaction(int id, double amount, String description, String date, int type, int userId, String category) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.userId = userId;
        this.category = category;
    }

    public Transaction() {
        this.id = 0;
        this.amount = 0;
        this.description = "";
        this.date = "";
        this.type = 0;
        this.userId = 0;
        this.category = "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}