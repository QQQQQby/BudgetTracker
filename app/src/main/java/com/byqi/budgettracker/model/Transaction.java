package com.byqi.budgettracker.model;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String category, name;
    private double cost;
    private long timestamp;

    public Transaction(String category, String name, double cost) {
        this.category = category;
        this.name = name;
        this.cost = cost;
        timestamp = System.currentTimeMillis();
    }

    public String getCategoryName() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
