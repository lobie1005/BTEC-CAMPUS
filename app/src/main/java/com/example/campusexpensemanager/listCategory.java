package com.example.campusexpensemanager;

public class listCategory {
    private String categoryName;
    private String type; // 0 for income, 1 for expense

    public listCategory(String category, String type) {
        this.categoryName = category;
        this.type = type;
    }

    public String getCategory() {
        return categoryName;
    }

    public String getType() {
        return type;
    }
}
