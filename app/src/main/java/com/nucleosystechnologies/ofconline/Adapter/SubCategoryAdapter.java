package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.Model.SubCategoryModel;
import com.nucleosystechnologies.ofconline.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.nucleosystechnologies.ofconline.Utility.API.IMG_PATH;

public class SubCategoryAdapter extends BaseAdapter {
    Context context;
    ArrayList<SubCategoryModel> Datalist;

    public SubCategoryAdapter(Context context, ArrayList<SubCategoryModel> datalist) {
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
        final SubCategoryModel cat = Datalist.get(i);
        if(view==null)
        {
            LayoutInflater inflater =  (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sub_category_list_item,null);
        }
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView address = (TextView)view.findViewById(R.id.address);
        TextView address1 = (TextView)view.findViewById(R.id.address1);
        name.setText(cat.getFirst_name()+" "+cat.getLast_name());
        address.setText(cat.getAdrs()+" ");
        address1.setText(cat.getCountry()+" "+cat.getState()+" "+cat.getCity()+" "+cat.getZipcode());

        ImageView cat_img = (ImageView) view.findViewById(R.id.cat_img);

      //Log.i("IMAGE",API.IMG_PATH+cat.getImg());
        Picasso.with(context).load(R.drawable.man).placeholder(R.drawable.place).resize(150, 150)
                .centerInside().into(cat_img);

        return view;
    }
}
