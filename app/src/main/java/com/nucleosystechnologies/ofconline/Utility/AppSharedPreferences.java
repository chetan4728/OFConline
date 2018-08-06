package com.nucleosystechnologies.ofconline.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class AppSharedPreferences {



    private Context context;
    public  SharedPreferences pref;
    public Editor editor;

    public static final String mypreference = "mypref";
    public static final String Email = "email";
    public static final String FirstName = "FirstName";
    public static final String LastName = "LastName";
    public static final String Mobile = "Mobile";
    public static final String mast_id = "mast_id";

    public AppSharedPreferences(Context context)
    {
         this.context=context;
         pref = context.getSharedPreferences("ApplicationPrefernce", Context.MODE_PRIVATE);
         editor = pref.edit();


    }


}
