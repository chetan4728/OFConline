package com.nucleosystechnologies.ofconline.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Activity.Dashboard;
import com.nucleosystechnologies.ofconline.Activity.LoginActivity;
import com.nucleosystechnologies.ofconline.Activity.OTPConfirmation;
import com.nucleosystechnologies.ofconline.Activity.OTPScreen;
import com.nucleosystechnologies.ofconline.Activity.Services;
import com.nucleosystechnologies.ofconline.Activity.SplashScreen;
import com.nucleosystechnologies.ofconline.Activity.SubCategory;
import com.nucleosystechnologies.ofconline.Fragments.SubCatFragment;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.Model.SubCategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.nucleosystechnologies.ofconline.Utility.CircleTransform;
import com.nucleosystechnologies.ofconline.Utility.TouchImageView;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;
import com.squareup.picasso.Picasso;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.nucleosystechnologies.ofconline.Utility.API.IMG_PATH;

public class SubCategoryAdapter extends BaseAdapter {
    Context context;
    Activity activity;
    ArrayList<SubCategoryModel> Datalist;
    AppSharedPreferences sharedPreferences;
    String name,email,mast_id;
    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> datalist,Activity activity) {
        this.context = context;
        this.Datalist = datalist;
        this.activity = activity;
        sharedPreferences =  new AppSharedPreferences(context.getApplicationContext());
         name = sharedPreferences.pref.getString(sharedPreferences.FirstName,"")+" "+sharedPreferences.pref.getString(sharedPreferences.LastName,"");
         email = sharedPreferences.pref.getString(sharedPreferences.Email,"");
        mast_id =  sharedPreferences.pref.getString(sharedPreferences.mast_id,"");
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



        final SubCategoryModel cat = Datalist.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sub_category_list_item, null);
        }
        final TextView name = (TextView) view.findViewById(R.id.name);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView address1 = (TextView) view.findViewById(R.id.address1);
        name.setText(cat.getFirst_name() + " " + cat.getLast_name());
        address.setText(cat.getAdrs() + " ");
        address1.setText(cat.getCountry() + " " + cat.getState() + " " + cat.getCity() + " " + cat.getZipcode());
        Button call = (Button) view.findViewById(R.id.call);
        final Button email = (Button) view.findViewById(R.id.email);

        final ImageView cat_img = (ImageView) view.findViewById(R.id.cat_img);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    OtpAlertMsg(view, "sms",cat.getCategory_name());

            }
        });




        cat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhoom(view,API.PROFILE_PATH+cat.getImg_upload());

            }
        });


        email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent callIntent =new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:8686861878"));
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(callIntent);


            }


        });





        //Log.i("IMAGE",API.IMG_PATH+cat.getImg());
        Picasso.with(context).load(API.PROFILE_PATH+cat.getImg_upload()).placeholder(R.drawable.place).into(cat_img);

        return view;
    }


    public void zhoom(View view, final String url)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());



        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.zoomer, null);
        //builder.setCancelable(false);
        builder.setView(promptsView);
        final AlertDialog dialog = builder.create();
        final TouchImageView zhoomer = (TouchImageView) promptsView.findViewById(R.id.zhoomer);

        Picasso.with(context).load(url).placeholder(R.drawable.place).into(zhoomer);
        dialog.show();
    }

    public void OtpAlertMsg(View view, final String type,final String categiry_name)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());



        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.enquiry_view, null);
        builder.setCancelable(false);
        builder.setView(promptsView);
        final AlertDialog dialog = builder.create();
        final EditText msg = (EditText)promptsView.findViewById(R.id.msg);


        Button editbtn = (Button)promptsView.findViewById(R.id.editbtn);
        Button confirm = (Button)promptsView.findViewById(R.id.confirm);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(msg.getText().toString().isEmpty())
{
    Toast.makeText(context, "Please Enter Query", Toast.LENGTH_SHORT).show();
}
else
{
    StringRequest stringRequest = new StringRequest(Request.Method.POST, API.SENDSMS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Toast.makeText(context, "Message Sanded Successfully"+response, Toast.LENGTH_LONG).show();

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
            params.put("msg", msg.getText().toString());
            params.put("name", String.valueOf(name));
            params.put("mast_id", String.valueOf(mast_id));
            params.put("type", String.valueOf(type));
            params.put("categiry_name", String.valueOf(categiry_name));


            return params;
        }
    };
    VolllyRequest.getInstance(context).addToRequestQueue(stringRequest);

    dialog.dismiss();
}



            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
// Set up the buttons

        dialog.show();
    }


}
