package com.byqi.budgettracker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.byqi.budgettracker.R;
import com.byqi.budgettracker.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private int resourceId;


    public CategoryAdapter(@NonNull Context context, int resourceId, @NonNull List<Category> categoryList) {
        super(context, resourceId, categoryList);
        this.resourceId = resourceId;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView nameView = (TextView) view.findViewById(R.id.category_name_view);
        TextView costView = (TextView) view.findViewById(R.id.category_cost_view);
        Category category = getItem(position);
        nameView.setText(category.getName());
        costView.setText(String.format("$%.2f", category.getCost()));
        return view;
    }
}
