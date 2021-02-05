package com.byqi.budgettracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byqi.budgettracker.R;
import com.byqi.budgettracker.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

    private List<Transaction> transactionList;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_view, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.categoryView.setText(transaction.getCategoryName());
        holder.nameView.setText(transaction.getName());
        holder.timestampView.setText(dateFormat.format(new Date(transaction.getTimestamp())));
        holder.costView.setText(String.format(Locale.getDefault(), "$%.2f", transaction.getCost()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}

class TransactionViewHolder extends RecyclerView.ViewHolder {
    public final TextView categoryView, nameView, timestampView, costView;

    public TransactionViewHolder(View view) {
        super(view);
        categoryView = (TextView) view.findViewById(R.id.transaction_category_view);
        nameView = (TextView) view.findViewById(R.id.transaction_name_view);
        timestampView = (TextView) view.findViewById(R.id.transaction_timestamp_view);
        costView = (TextView) view.findViewById(R.id.transaction_cost_view);
    }
}