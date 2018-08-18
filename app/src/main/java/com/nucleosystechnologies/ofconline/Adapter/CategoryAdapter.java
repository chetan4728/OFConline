package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.Activity.SubCategory;
import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.nucleosystechnologies.ofconline.Utility.API;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.nucleosystechnologies.ofconline.Utility.API.IMG_PATH;

public class CategoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<CategoryModel> Datalist;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> datalist) {
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
        final CategoryModel cat = Datalist.get(i);
        View convertview;



        if(view==null)
        {
            LayoutInflater inflater =  (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertview = inflater.inflate(R.layout.top_rated_category_item,null);
            convertview.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            convertview.setTag(cat.getCategory_id());
        }else {
            convertview = view;
        }

        ImageView cat_img = (ImageView) convertview.findViewById(R.id.full_image_view);
        TextView title =(TextView)convertview.findViewById(R.id.title);


        title.setText(cat.getName());
        convertview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(context, SubCategory.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();

                bundle.putInt("Category_id",cat.getCategory_id());
                bundle.putString("cate_name",cat.getName());
                i.putExtras(bundle);
                context.startActivity(i);


            }
        });

      //Log.i("IMAGE",API.IMG_PATH+cat.getImg());
        Picasso.with(context).load(IMG_PATH+cat.getImg()).placeholder(R.drawable.place).resize(400, 400)
                .onlyScaleDown()
                .centerInside().into(cat_img);

        return convertview;
    }
}
