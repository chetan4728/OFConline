package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nucleosystechnologies.ofconline.Model.CategoryModel;
import com.nucleosystechnologies.ofconline.R;

import java.util.ArrayList;

public class menu_content_adapter extends BaseAdapter{
    Context context;
    ArrayList<String> MenuName;
    ArrayList<Integer> ImageList;

    public menu_content_adapter(Context context, ArrayList<String> menuName, ArrayList<Integer> imageList) {
        this.context = context;
        this.MenuName = menuName;
        this.ImageList = imageList;
    }

    @Override
    public int getCount() {
        return MenuName.size();
    }

    @Override
    public Object getItem(int i) {
        return MenuName.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if(view==null)
        {
            LayoutInflater inflater =  (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.navigation_item_list,null);
        }

        ImageView imgIcon = (ImageView) view.findViewById(R.id.img);

        imgIcon.setImageResource(ImageList.get(i));

        TextView txtTitle = (TextView) view.findViewById(R.id.textme);
        txtTitle.setText(MenuName.get(i));

        return view;
    }
}
