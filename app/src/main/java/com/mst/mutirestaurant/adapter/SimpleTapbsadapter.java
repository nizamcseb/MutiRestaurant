package com.mst.mutirestaurant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nizamuddeen on 12-10-2015.
 */
public class SimpleTapbsadapter extends FragmentStatePagerAdapter {

    private final List<Fragment> FragmentList = new ArrayList();
    private final List<String> FragmentTitles = new ArrayList();

    public SimpleTapbsadapter(FragmentManager fm) {
        super(fm);
    }



    public void addFragment(Fragment fragment, String title) {
        FragmentList.add(fragment);
        FragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentTitles.get(position);
    }

}