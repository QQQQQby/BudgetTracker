package com.byqi.budgettracker.ui;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.byqi.budgettracker.R;
import com.byqi.budgettracker.adapter.CategoryAdapter;
import com.byqi.budgettracker.model.Category;
import com.byqi.budgettracker.model.Transaction;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    TextView total_view;
    Button clear_btn, transaction_btn, add_btn;
    ListView categoryListView;
    List<Category> categoryList;
    CategoryAdapter categoryAdapter;
    List<Transaction> transactionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total_view = (TextView) findViewById(R.id.total_view);

        clear_btn = (Button) findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(view -> showClearDialog());

        transaction_btn = (Button) findViewById(R.id.transaction_btn);
        transaction_btn.setOnClickListener(view -> startTransactionActivity());

        add_btn = (Button) findViewById(R.id.add_btn);
        add_btn.setOnClickListener(view -> showAddTransactionDialog());

        transactionList = readTransactionList();
        if (transactionList == null)
            transactionList = new ArrayList<>();

        setCategoryListFromTransactionList();
        updateTotalCost();
        categoryAdapter = new CategoryAdapter(MainActivity.this,
                R.layout.category_item_view, categoryList);
        categoryListView = (ListView) findViewById(R.id.category_list_view);
        categoryListView.setAdapter(categoryAdapter);


    }

    private void startTransactionActivity() {
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        String prefix = "transaction_";
        int i = 0;
        for (Transaction transaction : transactionList) {
            intent.putExtra(prefix + i, transaction);
            i++;
        }
        startActivity(intent);
    }

    private void showClearDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getResources().getString(R.string.clear_all))
                .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> {
                })
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    transactionList.clear();
                    categoryAdapter.clear();
                    updateTotalCost();
                    saveTransactionList();
                })
                .create()
                .show();
    }

    private void showAddTransactionDialog() {
        View view = View.inflate(MainActivity.this, R.layout.add_transaction_dialog_view, null);
        EditText category_text = (EditText) view.findViewById(R.id.transaction_category_edit_text);
        EditText name_text = (EditText) view.findViewById(R.id.transaction_name_edit_text);
        EditText cost_text = (EditText) view.findViewById(R.id.transaction_cost_edit_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view)
                .setTitle(getResources().getString(R.string.add_transaction))
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .setPositiveButton(getResources().getString(R.string.add), null)
                .create();
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((v) -> {
            dialog.dismiss();
        });
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((v) -> {
            int n = categoryAdapter.getCount();
            String categoryName, transactionName;
            double cost;
            try {
                categoryName = String.valueOf(category_text.getText());
                transactionName = String.valueOf(name_text.getText());
                String costString = String.valueOf(cost_text.getText());
                cost = Double.parseDouble(costString);
                if (categoryName.equals("") || transactionName.equals("")||cost <= 0.0 || (costString.contains(".") && costString.length() - costString.indexOf('.') >= 4))
                    throw new Exception();
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.invalid_input),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            transactionList.add(0, new Transaction(categoryName, transactionName, cost));
            updateTotalCost();
            for (int i = 0; i < n; i++) {
                if (categoryAdapter.getItem(i).getName().equals(categoryName)) {
                    categoryList.get(i).addTransaction(transactionName, cost);
                    categoryAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                    saveTransactionList();
                    return;
                }
            }
            Category category = new Category(categoryName);
            category.addTransaction(categoryName, cost);
            categoryAdapter.insert(category, 0);
            sortCategoryList();
            dialog.dismiss();
            saveTransactionList();
        });

    }

    private void updateTotalCost() {
        double cost = 0.0;
        for (Transaction transaction : transactionList)
            cost += transaction.getCost();
        total_view.setText(getResources().getString(R.string.total_prefix) + String.format("%.2f", cost));
    }

    private void saveTransactionList() {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = getApplicationContext().openFileOutput(getString(R.string.transaction_file_name),
                    Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(transactionList);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Transaction> readTransactionList() {
        List<Transaction> obj = null;
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = getApplicationContext().openFileInput(getString(R.string.transaction_file_name));
            ois = new ObjectInputStream(fis);
            obj = (List<Transaction>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private void setCategoryListFromTransactionList() {
        HashMap<String, Category> categoryHashMap = new HashMap<>();
        for (Transaction transaction : transactionList) {
            String name = transaction.getCategoryName();
            if (categoryHashMap.containsKey(name)) {
                categoryHashMap.get(name).addTransaction(transaction);
            } else {
                Category category = new Category(name);
                category.addTransaction(transaction);
                categoryHashMap.put(name, category);
            }
        }
        categoryList = new ArrayList<>();
        for (HashMap.Entry<String, Category> entry : categoryHashMap.entrySet())
            categoryList.add(entry.getValue());
        sortCategoryList();
    }

    private void sortCategoryList(){
        categoryList.sort((o1, o2) -> {
            char[] chars1 = o1.getName().toCharArray();
            char[] chars2 = o2.getName().toCharArray();
            int i = 0;
            while (i < chars1.length && i < chars2.length) {
                if (chars1[i] > chars2[i]) {
                    return 1;
                } else if (chars1[i] < chars2[i]) {
                    return -1;
                } else {
                    i++;
                }
            }
            if (i == chars1.length) {
                return -1;
            }
            if (i == chars2.length) {
                return 1;
            }
            return 0;
        });
    }
}

