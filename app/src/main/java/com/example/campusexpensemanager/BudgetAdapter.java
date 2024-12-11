package com.example.campusexpensemanager;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.DatabaseHandler1;
import com.example.campusexpensemanager.incomeModel;
import com.example.campusexpensemanager.R;

import java.util.Calendar;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.viewholder> {
    private Context context;
    private List<incomeModel> incomeModelList;
    private DatabaseHandler1 databaseHandler1;
    private BudgetFragment budgetFragment;

    public BudgetAdapter(Context context, List<incomeModel> incomeModelList, DatabaseHandler1 databaseHandler1, BudgetFragment budgetFragment) {
        this.context = context;
        this.incomeModelList = incomeModelList;
        this.databaseHandler1 = databaseHandler1;
        this.budgetFragment = budgetFragment;
    }



    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_budget_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        incomeModel model = incomeModelList.get(position);
        holder.tv_incomeAmount.setText( model.getAmount()+"VND" );
        holder.tv_incomeType.setText(model.getType());
        holder.tv_incomeNote.setText(model.getNote());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(model.getDate()));
        String formattedDate = DateFormat.format("dd/MM/yyyy", calendar).toString();

        holder.tv_incomeDate.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> showIncomeDialog(context, model));
    }

    public void showIncomeDialog(Context context, incomeModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View customLayout = LayoutInflater.from(context).inflate(R.layout.update_budget, null);
        EditText etu_income = customLayout.findViewById(R.id.et_update_incomeAmount);
        EditText etu_type = customLayout.findViewById(R.id.et_update_incomeType);
        EditText etu_note = customLayout.findViewById(R.id.et_update_incomeNote);

        etu_income.setText(model.getAmount());
        etu_type.setText(model.getType());
        etu_note.setText(model.getNote());

        Button btn_update = customLayout.findViewById(R.id.btn_update);
        Button btn_delete = customLayout.findViewById(R.id.btn_delete);

        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_delete.setOnClickListener(v -> {
            String id = model.getId();
            boolean isDeleted = databaseHandler1.delete(id);
            if (isDeleted) {
                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
                budgetFragment.refreshData1(); // Refresh the fragment data after deletion
            } else {
                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
            }
            alertDialog.dismiss();
        });

        btn_update.setOnClickListener(v -> {
            String id = model.getId();
            String amount = etu_income.getText().toString();
            String type = etu_type.getText().toString();
            String note = etu_note.getText().toString();
            long date = System.currentTimeMillis();

            if (amount.isEmpty()) {
                etu_income.setError("Empty amount");
                return;
            } else if (type.isEmpty()) {
                etu_type.setError("Empty Type");
                return;
            } else if (note.isEmpty()) {
                etu_note.setError("Empty note");
                return;
            } else {
                databaseHandler1.update(id, amount, type, note, String.valueOf(date));
                alertDialog.dismiss();
                budgetFragment.refreshData1(); // Refresh the fragment data after update
            }
        });
    }

    @Override
    public int getItemCount() {
        return incomeModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        private TextView tv_incomeDate, tv_incomeType, tv_incomeNote, tv_incomeAmount;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tv_incomeDate = itemView.findViewById(R.id.tv_incomeDate);
            tv_incomeType = itemView.findViewById(R.id.tv_incomeType);
            tv_incomeNote = itemView.findViewById(R.id.tv_incomeNote);
            tv_incomeAmount = itemView.findViewById(R.id.tv_incomeAmount);
        }
    }
}
