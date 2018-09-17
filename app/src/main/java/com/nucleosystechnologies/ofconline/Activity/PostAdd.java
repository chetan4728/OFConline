package com.nucleosystechnologies.ofconline.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nucleosystechnologies.ofconline.Adapter.CategoryListAdapter;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.nucleosystechnologies.ofconline.Utility.AppSharedPreferences;
import com.nucleosystechnologies.ofconline.Utility.ImageUploader;
import com.nucleosystechnologies.ofconline.Utility.VolllyRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostAdd extends AppCompatActivity {
    AppSharedPreferences sharedPreferences;

    ArrayList<String> MenuName;
    ArrayList<Integer> ImageList;




    private final static int FILECHOOSER_RESULTCODE=1;


    ProgressDialog pDialog;

    WebView  webView;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    ArrayList<CategoryModel> Datalist;
    private static final String TAG = SellerDashboard.class.getSimpleName();
    private WebSettings webSettings;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    ImageView imageView;
    Spinner category;
    File file;
    ImageView image;
    Button choose, upload;
    int PICK_IMAGE_REQUEST = 1010;
    String URL ="http://ofconline.in/Builders/upload_aid_file";
    Bitmap bitmap;
    ProgressDialog progressDialog;
    Boolean upload_flag = false;
    Boolean cat_flag = false;
     EditText name;
    String cat_id,getname;
    CategoryListAdapter categoryListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_add);

         name = (EditText)findViewById(R.id.name);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Add Advertiesmnet");

        category = (Spinner) findViewById(R.id.category);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

            }
        }

        Datalist = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, API.CATEGORY,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("status").equals("200"))
                            {
                                JSONArray jsonArray = obj.getJSONArray("data");

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    CategoryModel model =  new CategoryModel();
                                    model.setCategory_id(jsonArray.getJSONObject(i).getInt("category_id"));
                                    model.setName(jsonArray.getJSONObject(i).getString("name"));
                                    model.setDescription(jsonArray.getJSONObject(i).getString("description"));
                                    model.setImg(jsonArray.getJSONObject(i).getString("img"));
                                    Datalist.add(model);
                                }
                                CategoryListAdapter categoryListAdapter =  new CategoryListAdapter(getApplicationContext(),R.layout.category_spinner_item,Datalist);
                                category.setAdapter(categoryListAdapter);
                                // categoryList.setAdapter(categoryAdapter);


                            }
                            else if (obj.getString("status").equals("400"))
                            {

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //  Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        VolllyRequest.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);





        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView callImage = (TextView)view.findViewById(R.id.cat_name);




                if(position>0)
                {
                    cat_flag = true;
                    cat_id = String.valueOf(callImage.getTag());

                }
                else
                {
                   // Toast.makeText(PostAdd.this, "Please Select Category", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        sharedPreferences =  new AppSharedPreferences(this);
        if(sharedPreferences.pref.getString("isSignup","").isEmpty())
        {
            Intent intent = new Intent(PostAdd.this, UserProfile.class);
            startActivity(intent);
        }



        image = (ImageView)findViewById(R.id.image);
        choose = (Button)findViewById(R.id.choose);
        upload = (Button)findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cat_flag==false) {
                    Toast.makeText(PostAdd.this, "Please Select Category", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().isEmpty()) {
                    Toast.makeText(PostAdd.this, "Please Put Aid Name", Toast.LENGTH_SHORT).show();
                }else if(upload_flag == false)
                 {

                     Toast.makeText(PostAdd.this, "Please Upload Aid", Toast.LENGTH_SHORT).show();
                }
                else {

                    long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB > 2) {
                        Toast.makeText(PostAdd.this, "File size Too large upload less than 2 mb", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = new ProgressDialog(PostAdd.this);
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
                                        .addFormDataPart("name", String.valueOf(name.getText()))
                                        .addFormDataPart("category_id", cat_id)
                                        .addFormDataPart("mast_id", sharedPreferences.pref.getString(sharedPreferences.mast_id, ""))
                                        .build();
                                // Log.d("bodyyyyy", String.valueOf(request_body));

                                Request request = new Request.Builder()
                                        .url("http://ofconline.in/Builders/get_file_sample")
                                        .post(request_body)
                                        .build();
                                try {
                                    Response response = client.newCall(request).execute();

                                    if (!response.isSuccessful()) {
                                        throw new IOException("Error : " + response);
                                    } else {
                                        backgroundThreadShortToast(getApplicationContext(), "Adervtiement uploaded succssfully wait's for admns approval");
                                        progressDialog.dismiss();

                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        thread.start();


                    }
                }
            }
        });


        //opening image chooser option
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_PICK);
                final Intent chooserIntent = Intent.createChooser(galleryIntent, "select");
                startActivityForResult(chooserIntent, 1010);
            }
        });










        MenuName = new ArrayList<>();
        ImageList = new ArrayList<>();

        MenuName.add("Home");
        MenuName.add("Package");
        MenuName.add("Advertisement");
        MenuName.add("Setting");
        MenuName.add("Logout");
        ImageList.add(R.drawable.seller_home);
        ImageList.add(R.drawable.seller_package);
        ImageList.add(R.drawable.seller_advertiesment);
        ImageList.add(R.drawable.seller_setting);
        ImageList.add(R.drawable.seller_logout);
        pDialog = new ProgressDialog(this);







    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);


        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {


                //getting image from gallery
                 file = new File(getRealPathFromURI(filePath));
                //getting image from gallery
                file = new File(getRealPathFromURI(filePath));
                try {
                    file = new Compressor(getApplicationContext()).compressToFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                upload_flag = true;
                //Setting image to ImageView
                image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


    public void backgroundThreadShortToast(final Context context,

                                           final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(PostAdd.this,AddSuccess.class);
                    startActivity(i);
                    finish();

                }
            });
        }
    }

}
