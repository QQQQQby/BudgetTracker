package com.byqi.budgettracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.byqi.budgettracker.R;
import com.byqi.budgettracker.model.Transaction;
import com.byqi.budgettracker.adapter.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends Activity {

    ImageButton return_btn;
    RecyclerView transactionRecyclerView;
    List<Transaction> transactionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transation);

        initTransactionList();
        if (transactionList.size()==0)
            ((TextView)findViewById(R.id.no_transactions_view)).setVisibility(View.VISIBLE);

        return_btn = (ImageButton) findViewById(R.id.return_btn);
        return_btn.setOnClickListener(view -> finish());

        TransactionAdapter adapter = new TransactionAdapter(transactionList);

        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(this);
        transactionLayoutManager.setOrientation(RecyclerView.VERTICAL);

        transactionRecyclerView = (RecyclerView) findViewById(R.id.transaction_recycler_view);
        transactionRecyclerView.setLayoutManager(transactionLayoutManager);
        transactionRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        transactionRecyclerView.setAdapter(adapter);
    }

    private void initTransactionList() {
        transactionList = new ArrayList<>();
        Intent intent = getIntent();
        int i = 0;
        String prefix = "transaction_";
        while (intent.hasExtra(prefix + i)) {
            transactionList.add((Transaction) intent.getSerializableExtra(prefix + i));
            i++;
        }
    }
}
