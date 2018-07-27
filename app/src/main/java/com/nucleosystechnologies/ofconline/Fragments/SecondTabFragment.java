package com.nucleosystechnologies.ofconline.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.AdvertiseAdapter;
import com.nucleosystechnologies.ofconline.Adapter.CategoryAdapter;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondTabFragment extends Fragment {


    ArrayList<Addvertise_model> Datalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_second_tab, container, false);
        final ListView add = (ListView)view.findViewById(R.id.add);
        Datalist =  new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.ADDVERTISE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                //Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    Addvertise_model model =  new Addvertise_model();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setAdd_name(jsonArray.getJSONObject(i).getString("name"));
                                    model.setImg(jsonArray.getJSONObject(i).getString("image"));

                                    Datalist.add(model);
                                }

                                AdvertiseAdapter advertiseAdapter = new AdvertiseAdapter(getActivity(),Datalist);
                                add.setAdapter(advertiseAdapter);


                            }
                            else if (obj.getString("status").equals("400"))
                            {

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //  Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        VolllyRequest.getInstance(getActivity()).addToRequestQueue(stringRequest);


        return view;
    }

}
