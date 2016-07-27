package com.mst.mutirestaurant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mst.mutirestaurant.fragments.Myorder_fragment;
import com.mst.mutirestaurant.fragments.Trackorder_fragment;

/**
 * Created by blync on 12/3/2015.
 */
public class TrackorderAdapter extends FragmentStatePagerAdapter {
    public TrackorderAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    int Items = 2;

    @Override
    public int getCount() {
        return Items;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show image
                return Myorder_fragment.init(position);
            default : // Fragment # 1 - This will show image
                return Trackorder_fragment.init(position);
           /* default:// Fragment # 2-9 - Will show list
                return Track_fragment.init(position);*/
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return "Page " + position;
        switch (position) {
            case 0: // Fragment # 0 - This will show image
                return "All Orders";
            default : // Fragment # 1 - This will show image
                return "Track Orders";
            /*default:// Fragment # 2-9 - Will show list
                return "By Wallet";*/
        }
    }
}