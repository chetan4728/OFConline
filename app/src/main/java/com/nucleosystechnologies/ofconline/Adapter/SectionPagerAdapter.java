package com.nucleosystechnologies.ofconline.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nucleosystechnologies.ofconline.Fragments.FirstTabFragment;
import com.nucleosystechnologies.ofconline.Fragments.SecondTabFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstTabFragment();
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
                return "Top Rated Category";
            case 1:
            default:
                return "Advertise Section";
        }
    }
}
