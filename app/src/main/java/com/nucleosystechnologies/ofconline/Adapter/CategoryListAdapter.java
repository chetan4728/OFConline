package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter {

    ArrayList<CategoryModel> data;

    public CategoryListAdapter(Context context, int textViewResourceId, ArrayList<CategoryModel> data) {
        super(context, textViewResourceId, data);
        this.data=data;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_spinner_item, parent, false);
        final TextView label=(TextView)row.findViewById(R.id.cat_name);

            label.setText(data.get(position).getName());
            label.setTag(data.get(position).getCategory_id());



        return row;
    }
}
