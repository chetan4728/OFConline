package com.nucleosystechnologies.ofconline.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nucleosystechnologies.ofconline.Model.Addvertise_model;
import com.nucleosystechnologies.ofconline.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePagerSlider extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context Context;
    private ArrayList<Addvertise_model> SliderModelList;
    private int pos = 0;
    private int maxlength = 100;


    public HomePagerSlider(Context Context, ArrayList<Addvertise_model> SliderModelList) {
        this.Context = Context;
        this.SliderModelList = SliderModelList;


    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        final Addvertise_model SliderModelPos = SliderModelList.get(pos);
        View view = layoutInflater.inflate(R.layout.home_slider_layout, container, false);


        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);


        Picasso.with(Context.getApplicationContext())
                .load(R.drawable.place)
                .placeholder(R.drawable.place)
                .into(im_slider);


        container.addView(view);



        if(position!=pos) {
            if (pos >= SliderModelList.size() - 1) {
                pos = 0;
            } else {
                pos++;
            }
        }


        return view;

    }


    @Override
    public int getCount() {
        return maxlength;
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

