package com.nucleosystechnologies.ofconline.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.CategoryAdapter;
import com.nucleosystechnologies.ofconline.Adapter.SubCategoryAdapter;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.Model.SubCategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SubCatFragment extends Fragment {




    ArrayList<SubCategoryModel> Datalist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        Bundle bundle = this.getArguments();
        final int category_id = bundle.getInt("category_id");

        final String cat_id = String.valueOf(category_id).toString();


        view = inflater.inflate(R.layout.fragment_sub_cat, container, false);
        final ListView subcategory = (ListView)view.findViewById(R.id.subcategory);





        final ProgressDialog pDialog = new ProgressDialog(getActivity());

        pDialog.setMessage("Showing Data");
        pDialog.show();
        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                       // Log.i("reponse",response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    SubCategoryModel model =  new SubCategoryModel();
                                    model.setMast_id(jsonArray.getJSONObject(i).getInt("mast_id"));
                                    model.setFirst_name(jsonArray.getJSONObject(i).getString("first_name"));
                                    model.setLast_name(jsonArray.getJSONObject(i).getString("last_name"));
                                    model.setAdrs(jsonArray.getJSONObject(i).getString("adrs"));
                                    model.setCountry(jsonArray.getJSONObject(i).getString("country_name"));
                                    model.setState(jsonArray.getJSONObject(i).getString("state"));
                                    model.setCity(jsonArray.getJSONObject(i).getString("city"));
                                    model.setZipcode(jsonArray.getJSONObject(i).getString("zipcode"));
                                    Datalist.add(model);
                                }

                                SubCategoryAdapter subCategoryAdapter =  new SubCategoryAdapter(getActivity(),Datalist);
                                subcategory.setAdapter(subCategoryAdapter);

                                pDialog.hide();
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
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("category_id", cat_id);
                return params;
            }
        };
        VolllyRequest.getInstance(getActivity()).addToRequestQueue(stringRequest);
        return view;
    }

    public void call()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("23423423"));
        startActivity(callIntent);
    }

}
