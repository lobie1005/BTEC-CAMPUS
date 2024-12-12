<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/adapters/ItemAdapter.java
package vn.btec.campusexpensemanagement.adapters;
=======
package com.btec.fpt.campus_expense_manager.adapters;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/adapters/ItemAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/adapters/ItemAdapter.java
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.models.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> itemList;

    public ItemAdapter(@NonNull Context context, List<Item> itemList) {
=======
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.models.ItemModel;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<ItemModel> {
    private Context context;
    private List<ItemModel> itemList;

    public ItemAdapter(@NonNull Context context, List<ItemModel> itemList) {
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/adapters/ItemAdapter.java
        super(context, 0, itemList);
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/adapters/ItemAdapter.java
        Item currentItem = itemList.get(position);

        TextView nameTextView = listItem.findViewById(R.id.item_text);
        nameTextView.setText(currentItem.getText());

        TextView descriptionTextView = listItem.findViewById(R.id.item_time);
        descriptionTextView.setText(currentItem.getText());
=======
        ItemModel currentItem = itemList.get(position);

        TextView nameTextView = listItem.findViewById(R.id.textViewName);
        nameTextView.setText(currentItem.getName());

        TextView descriptionTextView = listItem.findViewById(R.id.textViewDescription);
        descriptionTextView.setText(currentItem.getDescription());
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/adapters/ItemAdapter.java

        return listItem;
    }
}
