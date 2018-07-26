package com.nucleosystechnologies.ofconline.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.nucleosystechnologies.ofconline.Fragments.FirstTabFragment;
import com.nucleosystechnologies.ofconline.Fragments.SecondTabFragment;
import com.nucleosystechnologies.ofconline.Fragments.SubCatFragment;

public class SubCatPagerAdapter extends FragmentPagerAdapter {

    int category_id;
    Context context;

    public SubCatPagerAdapter(FragmentManager fm, int category_id, Context context) {
        super(fm);
        this.category_id = category_id;
        this.context = context;


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt("category_id", category_id );
                SubCatFragment subCatFragment = new SubCatFragment();
                subCatFragment.setArguments(bundle);

                return  subCatFragment;
            case 1:
            default:
                return new SecondTabFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Listing Section";
            case 1:
            default:
                return "Advertise Section";
        }
    }
}
