package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdvertiseAdapter extends BaseAdapter {
    Context context;
    ArrayList<Addvertise_model> Datalist;

    public AdvertiseAdapter(Context context, ArrayList<Addvertise_model> datalist) {
        this.context = context;
        this.Datalist = datalist;
    }

    @Override
    public int getCount() {
        return Datalist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Addvertise_model add = Datalist.get(i);

        if(view==null)
        {
            LayoutInflater inflater =  (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.advertise_item_list,null);
        }



        return view;
    }
}
