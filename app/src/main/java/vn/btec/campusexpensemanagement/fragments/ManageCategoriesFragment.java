<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/ManageCategoriesFragment.java
package vn.btec.campusexpensemanagement.fragments;
=======
package com.btec.fpt.campus_expense_manager.fragments;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/ManageCategoriesFragment.java

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/ManageCategoriesFragment.java
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.adapters.CategoryAdapter;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Category;
=======
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.adapters.CategoryAdapter;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/ManageCategoriesFragment.java
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ManageCategoriesFragment extends Fragment implements CategoryAdapter.CategoryActionListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private FloatingActionButton fabAddCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_categories, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        initializeViews(view);
        setupRecyclerView();
        setupFabAddCategory();

        return view;
    }

    private void initializeViews(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        fabAddCategory = view.findViewById(R.id.fabAddCategory);
    }

    private void setupRecyclerView() {
        List<Category> categories = databaseHelper.getAllCategoryByEmail(
            databaseHelper.getCurrentUserEmail()
        );

        categoryAdapter = new CategoryAdapter(categories, this);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewCategories.setAdapter(categoryAdapter);
    }

    private void setupFabAddCategory() {
        fabAddCategory.setOnClickListener(v -> showAddCategoryDialog());
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add New Category");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        EditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        EditText etCategoryType = dialogView.findViewById(R.id.etCategoryType);

        builder.setView(dialogView);
        builder.setPositiveButton("Add", (dialog, which) -> {
            String categoryName = etCategoryName.getText().toString().trim();
            String categoryType = etCategoryType.getText().toString().trim();

            if (!categoryName.isEmpty() && !categoryType.isEmpty()) {
                // Insert category
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/ManageCategoriesFragment.java
                try {
                    boolean result = databaseHelper.insertCategory(
                        categoryName, 
                        categoryType,
                        databaseHelper.getCurrentUserEmail()
                    );

                    if (result) {
                        setupRecyclerView(); // Refresh the list
                        Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error adding category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Category name and type cannot be empty", Toast.LENGTH_SHORT).show();
=======
                long result = databaseHelper.insertCategory(
                    categoryName, 
                    databaseHelper.getCurrentUserEmail()
                );

                if (result != -1) {
                    setupRecyclerView(); // Refresh the list
                    Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/ManageCategoriesFragment.java
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public void onEditCategory(Category category) {
        showEditCategoryDialog(category);
    }

    @Override
    public void onDeleteCategory(Category category) {
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Category")
            .setMessage("Are you sure you want to delete this category?")
            .setPositiveButton("Delete", (dialog, which) -> {
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/ManageCategoriesFragment.java
                try {
                    boolean result = databaseHelper.deleteCategory(category.getCateId());
                    if (result) {
                        setupRecyclerView(); // Refresh the list
                        Toast.makeText(requireContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete category", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error deleting category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
=======
                int result = databaseHelper.deleteCategory(category.getCategoryId());
                if (result > 0) {
                    setupRecyclerView(); // Refresh the list
                    Toast.makeText(requireContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to delete category", Toast.LENGTH_SHORT).show();
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/ManageCategoriesFragment.java
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void showEditCategoryDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Category");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        EditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
        EditText etCategoryType = dialogView.findViewById(R.id.etCategoryType);

        // Pre-fill existing category details
        etCategoryName.setText(category.getName());
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/ManageCategoriesFragment.java
        etCategoryType.setText(category.getType());
=======
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/ManageCategoriesFragment.java

        builder.setView(dialogView);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String newCategoryName = etCategoryName.getText().toString().trim();
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/ManageCategoriesFragment.java
            String newCategoryType = etCategoryType.getText().toString().trim();

            if (!newCategoryName.isEmpty() && !newCategoryType.isEmpty()) {
                // Update category
                try {
                    boolean result = databaseHelper.updateCategory(
                        category.getCateId(),
                        newCategoryName,
                        newCategoryType,
                        databaseHelper.getCurrentUserEmail()
                    );

                    if (result) {
                        setupRecyclerView(); // Refresh the list
                        Toast.makeText(requireContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Failed to update category", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Error updating category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Category name and type cannot be empty", Toast.LENGTH_SHORT).show();
=======

            if (!newCategoryName.isEmpty()) {
                // Update category
                int result = databaseHelper.updateCategory(
                    category.getCategoryId(), 
                    newCategoryName, 
                    databaseHelper.getCurrentUserEmail()
                );

                if (result > 0) {
                    setupRecyclerView(); // Refresh the list
                    Toast.makeText(requireContext(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to update category", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/ManageCategoriesFragment.java
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
