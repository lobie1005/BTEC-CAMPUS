package vn.btec.campusexpensemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.entities.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;
    private final NumberFormat currencyFormat;
    private final SimpleDateFormat dateFormat;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        this.transactions = transactions;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivTransactionIcon;
        private final TextView tvTransactionTitle;
        private final TextView tvTransactionDate;
        private final TextView tvTransactionAmount;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTransactionIcon = itemView.findViewById(R.id.ivTransactionIcon);
            tvTransactionTitle = itemView.findViewById(R.id.tvTransactionTitle);
            tvTransactionDate = itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionAmount = itemView.findViewById(R.id.tvTransactionAmount);
        }

        public void bind(Transaction transaction) {
            // Set title (description)
            tvTransactionTitle.setText(transaction.getDescription());

            // Set date
            tvTransactionDate.setText(dateFormat.format(transaction.getDate()));

            // Set amount with proper formatting and color
            double amount = transaction.getAmount();
            String formattedAmount = currencyFormat.format(Math.abs(amount));
            if (transaction.getType() == 1) { // Income
                tvTransactionAmount.setTextColor(itemView.getContext().getColor(R.color.income_green));
                tvTransactionAmount.setText("+" + formattedAmount);
            } else { // Expense
                tvTransactionAmount.setTextColor(itemView.getContext().getColor(R.color.expense_red));
                tvTransactionAmount.setText("-" + formattedAmount);
            }

            // Set icon based on category
            // You can add more category-specific icons here
            String category = transaction.getCategory().toLowerCase();
            int iconResId;
            switch (category) {
                case "food":
                    iconResId = R.drawable.ic_food;
                    break;
                case "transport":
                    iconResId = R.drawable.ic_transport;
                    break;
                case "entertainment":
                    iconResId = R.drawable.ic_entertainment;
                    break;
                case "utilities":
                    iconResId = R.drawable.ic_utilities;
                    break;
                case "health":
                    iconResId = R.drawable.ic_health;
                    break;
                default:
                    iconResId = R.drawable.ic_misc;
                    break;
            }
            ivTransactionIcon.setImageResource(iconResId);
        }
    }
}
