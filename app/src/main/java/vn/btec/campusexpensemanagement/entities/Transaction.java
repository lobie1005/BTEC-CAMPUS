package vn.btec.campusexpensemanagement.entities;

public class Transaction {

    private int id;
    private int userId;
    private double amount;
    private String description;
    private String date;
    private int type;
    private String category;

    public static final int EXPENSE_TYPE = 1; // Define a constant for expense type
    public static final int INCOME_TYPE = 2; // Define a constant for income type

    public Transaction(int id, int userId, double amount, String description, String date, int type, String category) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    public Transaction() {
        this.id = 0;
        this.userId = 0;
        this.amount = 0.0;
        this.description = "";
        this.date = "";
        this.type = 0;
        this.category = "";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
