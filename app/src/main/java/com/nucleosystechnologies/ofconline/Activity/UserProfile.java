package com.nucleosystechnologies.ofconline.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.CategoryListAdapter;
import com.nucleosystechnologies.ofconline.Adapter.PackageAdapter;
import com.nucleosystechnologies.ofconline.Adapter.SpinnerAdapter;
import com.nucleosystechnologies.ofconline.Model.AddressModel;
import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.Model.Package_model;
import com.nucleosystechnologies.ofconline.Model.SubCategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.nucleosystechnologies.ofconline.Utility.CircleTransform;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.nucleosystechnologies.ofconline.Utility.API.IMG_PATH;

public class UserProfile extends AppCompatActivity {

     AppSharedPreferences appSharedPreferences;
    EditText firstname,lasttname,mobile,mobile2,zipcode,address,email,email2,password;
    Spinner country,state,city;
    ArrayList<AddressModel> Countrymodel;
    ArrayList<AddressModel> Statemodel;
    ArrayList<AddressModel> Citymodel;
    ArrayList<CategoryModel> Datalist;
     int c_id,s_is,cc_id;
    TextView country_id,state_text,city_text,profile;
    int PICK_IMAGE_REQUEST = 1010;
    String Country_id,state_id,city_id;
    File file;
    ImageView image;
    RoundedBitmapDrawable roundDrawable;
    Button choose, upload;
    String URL ="http://ofconline.in/Builders/upload_aid_file";
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Bundle bundle = getIntent().getExtras();
        appSharedPreferences =  new AppSharedPreferences(this);
        firstname = (EditText)findViewById(R.id.firstname);
        lasttname = (EditText)findViewById(R.id.lasttname);
        mobile = (EditText)findViewById(R.id.mobile);
        mobile2 = (EditText)findViewById(R.id.mobile2);
        country = (Spinner)findViewById(R.id.country);
        state = (Spinner)findViewById(R.id.state);
        city = (Spinner)findViewById(R.id.city);
        zipcode = (EditText)findViewById(R.id.zipcode);
        address = (EditText)findViewById(R.id.address);
        email = (EditText)findViewById(R.id.email);
        email2 = (EditText)findViewById(R.id.email2);
        password = (EditText)findViewById(R.id.password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Please Update Your Profile");

        Button udpatebtn = (Button)findViewById(R.id.udpatebtn);
        Countrymodel =  new ArrayList<>();
        Statemodel=  new ArrayList<>();
        image = (ImageView)findViewById(R.id.profile_pic);
        profile = (TextView)findViewById(R.id.profile);


        Citymodel =  new ArrayList<>();
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_id = (TextView)view.findViewById(R.id.cat_name);
                Country_id = String.valueOf(country_id.getTag());
                State(Country_id);
                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_text = (TextView)view.findViewById(R.id.cat_name);
                state_id = String.valueOf(state_text.getTag());
                city(state_id);
                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                final Intent chooserIntent = Intent.createChooser(galleryIntent, "select");
                startActivityForResult(chooserIntent, 1010);
            }
        });


        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_text = (TextView)view.findViewById(R.id.cat_name);
                city_id = String.valueOf(city_text.getTag());

                //Toast.makeText(UserProfile.this, ""+Country_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });





        udpatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(firstname.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Firstname", Toast.LENGTH_SHORT).show();
                }
                else    if(lasttname.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Lastname", Toast.LENGTH_SHORT).show();
                }
                else    if(mobile.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
                }



                else    if(email.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                }

                else    if(password.getText().toString().isEmpty())
                {
                    Toast.makeText(UserProfile.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updatedata();
                }

            }
        });
        getuserdata();
        country();


    }
    public void getuserdata()
    {
        image.setBackground(null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getuser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {

                                    Picasso.with(getApplicationContext()).load(API.PROFILE_PATH+jsonArray.getJSONObject(i).getString("user_profile")).placeholder(R.drawable.place).resize(400, 400)
                                            .onlyScaleDown().transform(new CircleTransform())
                                            .centerInside().into(image);




                                    password.setText(jsonArray.getJSONObject(i).getString("user_pass"));
                                    firstname.setText(jsonArray.getJSONObject(i).getString("first_name"));
                                    lasttname.setText(jsonArray.getJSONObject(i).getString("last_name"));
                                    mobile.setText(jsonArray.getJSONObject(i).getString("mobile"));
                                    mobile2.setText(jsonArray.getJSONObject(i).getString("alt_contact"));
                                    email.setText(jsonArray.getJSONObject(i).getString("user_email"));
                                    email2.setText(jsonArray.getJSONObject(i).getString("alt_email"));
                                    zipcode.setText(jsonArray.getJSONObject(i).getString("zipcode"));
                                    address.setText(jsonArray.getJSONObject(i).getString("adrs"));

                                    appSharedPreferences.editor.putString(appSharedPreferences.userprofile,API.PROFILE_PATH+jsonArray.getJSONObject(0).getString("user_profile"));
                                    appSharedPreferences.editor.commit();

                                    State(jsonArray.getJSONObject(i).getString("country"));
                                    city(jsonArray.getJSONObject(i).getString("state"));
                                    c_id = jsonArray.getJSONObject(i).getInt("country");
                                    s_is = jsonArray.getJSONObject(i).getInt("state");
                                 //   Toast.makeText(UserProfile.this, ""+jsonArray.getJSONObject(i).getInt("city"), Toast.LENGTH_SHORT).show();
                                    cc_id = jsonArray.getJSONObject(i).getInt("city");
                                    Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
                                    image.setImageBitmap(getclip(bitmap));
                                }



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
                params.put("mast_id",appSharedPreferences.pref.getString(AppSharedPreferences.mast_id,""));

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void updatedata()
    {
      //  Toast.makeText(this, "sdfs", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.Updateuser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(UserProfile.this, "Profile updateed Successfully", Toast.LENGTH_SHORT).show();
                        appSharedPreferences.editor.putString(appSharedPreferences.isSignup,"1");
                        appSharedPreferences.editor.commit();
                        Intent intent = new Intent(UserProfile.this, SellerDashboard.class);
                        startActivity(intent);
                        finish();
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
                params.put("first_name",firstname.getText().toString());
                params.put("last_name",lasttname.getText().toString());
                params.put("user_email",email.getText().toString());
                params.put("alt_email",email2.getText().toString());
                params.put("mobile",mobile.getText().toString());
                params.put("alt_contact",mobile2.getText().toString());
                params.put("user_pass",password.getText().toString());
                params.put("adrs",address.getText().toString());
                params.put("zipcode",zipcode.getText().toString());
                params.put("country",Country_id.toString());
                params.put("state",state_id.toString());
                params.put("city",city_id.toString());
                params.put("mast_id",appSharedPreferences.pref.getString(appSharedPreferences.mast_id,""));

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }

    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public  void country()
    {

     //  final ProgressDialog progressDialog = new ProgressDialog(UserProfile.this);
      // progressDialog.setMessage("Loading...");
     //  progressDialog.show();
     //  progressDialog.setCancelable(false);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.getCountry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    AddressModel model =  new AddressModel();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    Countrymodel.add(model);





                                }
                                SpinnerAdapter spinnerAdapter =  new SpinnerAdapter(getApplicationContext(),R.layout.category_spinner_item,Countrymodel);
                                country.setAdapter(spinnerAdapter);
                                // categoryList.setAdapter(categoryAdapter);
                                //progressDialog.hide();
                              //  country.setSelection(c_id-1);

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    if(c_id == jsonArray.getJSONObject(i).getInt("id"))
                                    {
                                        country.setSelection(i);
                                    }

                                }


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
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public  void State(final String id)
    {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.statelist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    AddressModel model =  new AddressModel();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    Statemodel.add(model);
                                }
                                SpinnerAdapter spinnerAdapter =  new SpinnerAdapter(getApplicationContext(),R.layout.category_spinner_item,Statemodel);
                                state.setAdapter(spinnerAdapter);
                                // categoryList.setAdapter(categoryAdapter);
                                //state.setSelection(s_is-1);
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    if(s_is == jsonArray.getJSONObject(i).getInt("id"))
                                    {
                                        state.setSelection(i);
                                    }

                                }

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
                params.put("country_id",id);

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public  void city(final String id)
    {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.city,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    AddressModel model =  new AddressModel();
                                    model.setId(jsonArray.getJSONObject(i).getInt("id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    Citymodel.add(model);

                                }
                                SpinnerAdapter spinnerAdapter =  new SpinnerAdapter(getApplicationContext(),R.layout.category_spinner_item,Citymodel);
                                city.setAdapter(spinnerAdapter);

                                // categoryList.setAdapter(categoryAdapter);
                               // city.setSelection(2499);
                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    if(cc_id == jsonArray.getJSONObject(i).getInt("id"))
                                    {
                                      city.setSelection(i);
                                    }

                                }


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
                params.put("state_id",id);

                return params;
            }
        };
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {


                //getting image from gallery
                file = new File(getRealPathFromURI(filePath));

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                image.setBackground(null);


                image.setImageBitmap(getclip(bitmap));

                final ProgressDialog  progressDialog = new ProgressDialog(UserProfile.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();


                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String content_type = getMimeType(file.getPath());

                        String file_path = file.getAbsolutePath();

                        OkHttpClient client = new OkHttpClient();
                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type), file);

                        RequestBody request_body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("type", content_type)
                                .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                                .addFormDataPart("mast_id",appSharedPreferences.pref.getString(appSharedPreferences.mast_id,""))
                                .build();
                        Log.d("bodyyyyy", String.valueOf(request_body));

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url("http://ofconline.in/Builders/upload_image_pic")
                                .post(request_body)
                                .build();
                        try {
                            okhttp3.Response response = client.newCall(request).execute();

                            if (!response.isSuccessful()) {
                                throw new IOException("Error : " + response);
                            }

                            progressDialog.dismiss();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                thread.start();


// Alternatively, draw it to an canvas (e.g. in onDraw where a Canvas is available).
// setBounds since there's no View handling size and positioning.
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);


        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    public static Bitmap getclip(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
