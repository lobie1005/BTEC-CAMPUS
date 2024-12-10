package vn.btec.campusexpensemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.models.Item;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {
    private Context context;
    private List<Item> itemList;

    public ItemAdapter(@NonNull Context context, List<Item> itemList) {
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

        Item currentItem = itemList.get(position);

        TextView nameTextView = listItem.findViewById(R.id.item_text);
        nameTextView.setText(currentItem.getText());

        TextView descriptionTextView = listItem.findViewById(R.id.item_time);
        descriptionTextView.setText(currentItem.getText());

        return listItem;
    }
}
