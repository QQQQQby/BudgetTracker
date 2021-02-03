package com.byqi.budgettracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.byqi.budgettracker.R;
import com.byqi.budgettracker.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private int resourceId;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TransactionAdapter(Context context, int resourceId, List<Transaction> transactionList) {
        super(context, resourceId, transactionList);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView categoryView = (TextView) view.findViewById(R.id.transaction_category_view);
        TextView nameView = (TextView) view.findViewById(R.id.transaction_name_view);
        TextView timestampView = (TextView) view.findViewById(R.id.transaction_timestamp_view);
        TextView costView = (TextView) view.findViewById(R.id.transaction_cost_view);
        Transaction transaction = getItem(position);
        categoryView.setText(transaction.getCategoryName());
        nameView.setText(transaction.getName());
        timestampView.setText(dateFormat.format(new Date(transaction.getTimestamp())));
        costView.setText(String.format("$%.2f",transaction.getCost()));
        return view;
    }
}
