package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class ListCategoryActivity extends AppCompatActivity {

    private LinearLayout listCategory;
    private Button addCategoryButton;
    private Button editCategoryButton;
    private ArrayList<Category> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_category); // Replace with your actual layout name

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listCategory = findViewById(R.id.item_category);

        // Mock category data
        categories = new ArrayList<>();
        categories.add(new Category("Income", "Đóng tiền nhà"));
        categories.add(new Category("Expense", "Chi phí hàng ngày"));

        // Populate the list dynamically
        populateCategoryList();
    }

    private void populateCategoryList() {
        listCategory.removeAllViews(); // Clear previous views

        for (Category category : categories) {
            View categoryView = getLayoutInflater().inflate(R.layout.item_category, null); // Inflate individual item layout

            TextView itemText = categoryView.findViewById(R.id.item_text);
            TextView categoryDescription = categoryView.findViewById(R.id.categoryDescription);

            // Set data
            itemText.setText(category.getName());
            categoryDescription.setText(category.getCateType());

            // Add the view to the list
            listCategory.addView(categoryView);
        }
    }


    // Mock Category class
    public static class Category {
        private String cateType;
        private String cateName;

        public Category(String name, String type) {
            this.cateName = name;
            this.cateType = type;
        }

        public String getName() {
            return cateName;
        }

        public String getCateType() {
            return cateType;
        }
    }
}