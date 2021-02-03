package com.byqi.budgettracker.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<Transaction> transactionList;


    public Category(String name) {
        this.name = name;
        transactionList = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    public void addTransaction(String name, double cost) {
        transactionList.add(new Transaction(this.name, name, cost));
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        double res = 0.0;
        for (Transaction transaction : transactionList)
            res += transaction.getCost();
        return res;
    }

}
