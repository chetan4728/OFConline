package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.nucleosystechnologies.ofconline.Activity.AdvertiseListing;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.SubCategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Addvertise_model> Datalist;

    public AddListAdapter(Context context, ArrayList<Addvertise_model> datalist) {
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
        final Addvertise_model cat = Datalist.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.add_listing_item, null);
        }

        ImageView add_img =  (ImageView)view.findViewById(R.id.add_img);
        TextView add_name = (TextView)view.findViewById(R.id.add_name);
        TextView cat_name = (TextView)view.findViewById(R.id.cat_name);
        TextView date = (TextView)view.findViewById(R.id.date);
        final Switch status = (Switch)view.findViewById(R.id.status);
        add_name.setText(cat.getAdd_name());
        cat_name.setText(cat.getCat());
        date.setText(cat.getCreated_date());

        //Toast.makeText(context, ""+cat.getStatus(), Toast.LENGTH_SHORT).show();
        if(cat.getStatus()==1)
        {
            status.setChecked(true);
        }
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(status.isChecked()==true)
                {
                    updateStatus(cat.getId(),1);
                    Toast.makeText(context, "Active", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateStatus(cat.getId(),0);
                    Toast.makeText(context, "Inactive", Toast.LENGTH_SHORT).show();
                }

            }
        });

        LinearLayout delme = (LinearLayout)view.findViewById(R.id.delme);
        delme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Datalist.remove(i);
                notifyDataSetChanged();
                del(cat.getId());
            }
        });

          //Log.i("IMAGE",API.ADVERTISE_IMG+cat.getImg());
       Picasso.with(context).load(API.ADVERTISE_IMG+cat.getImg()).placeholder(R.drawable.place).into(add_img);

        return view;
    }

    public void del(final int i)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.DELADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      //  Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("adv_id", String.valueOf(i).toString());

                return params;
            }
        };
        VolllyRequest.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void updateStatus(final int i,final  int status)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.UpdateAdd,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("adv_id", String.valueOf(i).toString());
                params.put("status", String.valueOf(status).toString());
                return params;
            }
        };
        VolllyRequest.getInstance(context).addToRequestQueue(stringRequest);

    }


}
