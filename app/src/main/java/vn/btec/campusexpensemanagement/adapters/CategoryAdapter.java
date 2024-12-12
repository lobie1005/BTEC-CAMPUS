<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/adapters/CategoryAdapter.java
package vn.btec.campusexpensemanagement.adapters;
=======
package com.btec.fpt.campus_expense_manager.adapters;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/adapters/CategoryAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/adapters/CategoryAdapter.java
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.entities.Category;
=======
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.entities.Category;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/adapters/CategoryAdapter.java

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface CategoryActionListener {
        void onEditCategory(Category category);
        void onDeleteCategory(Category category);
    }

    private List<Category> categories;
    private CategoryActionListener actionListener;

    public CategoryAdapter(List<Category> categories, CategoryActionListener listener) {
        this.categories = categories;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageButton btnEdit, btnDelete;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnEdit = itemView.findViewById(R.id.btnEditCategory);
            btnDelete = itemView.findViewById(R.id.btnDeleteCategory);
        }

        void bind(Category category) {
            tvCategoryName.setText(category.getName());

            btnEdit.setOnClickListener(v -> actionListener.onEditCategory(category));
            btnDelete.setOnClickListener(v -> actionListener.onDeleteCategory(category));
        }
    }
}
