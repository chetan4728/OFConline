package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.Package_model;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PackageAdapter extends BaseAdapter {
    Context context;
    ArrayList<Package_model> Datalist;

    public PackageAdapter(Context context, ArrayList<Package_model> datalist) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Package_model cat = Datalist.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.pkg_list_item, null);
        }

        TextView pakage_name =  (TextView)view.findViewById(R.id.pakage_name);
        TextView pkgvalid = (TextView)view.findViewById(R.id.pkgvalid);
        TextView pkgLimit = (TextView)view.findViewById(R.id.pkgLimit);
        TextView pkgprice = (TextView)view.findViewById(R.id.pkgprice);
        pakage_name.setText(cat.getPkg_name());
        pkgvalid.setText(cat.getPkg_validity()+" Day's");
        pkgLimit.setText(cat.getPkg_limit());
        pkgprice.setText("\u20B9"+cat.getPkg_price());




        return view;
    }




}
