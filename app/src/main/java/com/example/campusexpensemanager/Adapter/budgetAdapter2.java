package com.example.campusexpensemanager.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Model.Expenses;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.incomeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class budgetAdapter2 extends RecyclerView.Adapter<budgetAdapter2.viewholder> {

    public Context context;
    private List<incomeModel> incomeModelList = new ArrayList<>();

    public budgetAdapter2(Context context, List<incomeModel> incomeModelList) {
        this.context = context;
        this.incomeModelList = incomeModelList;
    }

    @NonNull
    @Override
    public budgetAdapter2.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_income_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull budgetAdapter2.viewholder holder, int position) {
        incomeModel model = incomeModelList.get(position);
        holder.tv_incomeAmount.setText(model.getAmount()+ " VND");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(model.getDate()));
        String formattedDate = DateFormat.format("dd/MM/yyyy", calendar).toString();

        holder.tv_incomeDate.setText(formattedDate);
        holder.tv_incomeJob.setText(model.getType());
    }

    @Override
    public int getItemCount() {
        return incomeModelList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView tv_incomeJob, tv_incomeAmount, tv_incomeDate;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tv_incomeAmount = itemView.findViewById(R.id.tv_incomeAmount);
            tv_incomeJob = itemView.findViewById(R.id.tv_incomeJob);
            tv_incomeDate = itemView.findViewById(R.id.tv_incomeDate);
        }
    }
}
