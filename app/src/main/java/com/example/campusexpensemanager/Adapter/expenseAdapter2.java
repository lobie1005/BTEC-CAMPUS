package com.example.campusexpensemanager.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.DataBase.ExpensesData;
import com.example.campusexpensemanager.ExpenseFragment;
import com.example.campusexpensemanager.Model.Expenses;
import com.example.campusexpensemanager.R;

import java.util.Calendar;
import java.util.List;

public class expenseAdapter2 extends RecyclerView.Adapter<expenseAdapter2.viewholder> {
    private Context context;
    private List<Expenses> expenseModelList;
    private ExpensesData expensesData;
    private ExpenseFragment expenseFragment;

    public expenseAdapter2(Context context, List<Expenses> expenseModelList, ExpensesData expensesData, ExpenseFragment expenseFragment) {
        this.context = context;
        this.expenseModelList = expenseModelList;
        this.expensesData = expensesData;
        this.expenseFragment = expenseFragment; // Initialize the fragment reference
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_expense_item2, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Expenses model = expenseModelList.get(position);
        holder.tv_Amount.setText( model.getAmount()+"VND");
        holder.tv_Type.setText(model.getName());
        holder.tv_Note.setText(model.getNote());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(model.getDate()));
        String formattedDate = DateFormat.format("dd/MM/yyyy", calendar).toString();

        holder.tv_Date.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> showExpenseDialog(context, model));
    }

    public void showExpenseDialog(Context context, Expenses model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View customLayout = LayoutInflater.from(context).inflate(R.layout.expense_update_item, null);
        EditText edt_amount = customLayout.findViewById(R.id.edt_UpdateAmount);
        EditText edt_type = customLayout.findViewById(R.id.edt_UpdateType);
        EditText edt_note = customLayout.findViewById(R.id.edt_UpdateNote);

        edt_amount.setText(model.getAmount());
        edt_type.setText(model.getName());
        edt_note.setText(model.getNote());

        Button btn_update = customLayout.findViewById(R.id.btn_update);
        Button btn_delete = customLayout.findViewById(R.id.btn_delete);
        ImageView iv_cancel = customLayout.findViewById(R.id.cancelButton);

        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        iv_cancel.setOnClickListener(v -> alertDialog.dismiss());

        btn_delete.setOnClickListener(v -> {
            String id = model.getId();
            boolean isDeleted = expensesData.delete(id);

            if (isDeleted) {
                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
                expenseModelList.remove(model);
                notifyDataSetChanged();
                updateUI();
            } else {
                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();
            }

            alertDialog.dismiss();
        });

        btn_update.setOnClickListener(v -> {
            String id = model.getId();
            String amount = edt_amount.getText().toString();
            String type = edt_type.getText().toString();
            String note = edt_note.getText().toString();
            long date = System.currentTimeMillis();

            if (amount.isEmpty()) {
                edt_amount.setError("Empty amount");
                return;
            } else if (type.isEmpty()) {
                edt_type.setError("Empty Type");
                return;
            } else if (note.isEmpty()) {
                edt_note.setError("Empty note");
                return;
            } else {
                expensesData.update(id, amount, type, note, String.valueOf(date));
                alertDialog.dismiss();
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (expenseFragment != null) {
            expenseFragment.refreshData(); // Refresh data in the fragment
        }
    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {
        private TextView tv_Date, tv_Type, tv_Note, tv_Amount;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_Date = itemView.findViewById(R.id.tv_expenseDate);
            tv_Type = itemView.findViewById(R.id.tv_expenseName);
            tv_Note = itemView.findViewById(R.id.tv_expenseNote);
            tv_Amount = itemView.findViewById(R.id.tv_expenseAmount);
        }
    }
}
