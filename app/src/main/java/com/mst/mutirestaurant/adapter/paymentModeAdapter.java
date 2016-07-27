package com.mst.mutirestaurant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.mst.mutirestaurant.fragments.Card_Fragment;
import com.mst.mutirestaurant.fragments.Cash_Fragment;
import com.mst.mutirestaurant.fragments.Wallet_Fragment;

/**
 * Created by Nizamuddeen on 27-11-2015.
 */
 public class paymentModeAdapter extends FragmentStatePagerAdapter {
    public paymentModeAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    int Items = 3;
   public boolean selected=false;
    ViewPager mViewPager;
    private FragmentStatePagerAdapter mSectionsRideAdapter;

    @Override
    public int getCount() {
        return Items;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show image
                return Cash_Fragment.init(position,selected);
            case 1: // Fragment # 1 - This will show image
                return Card_Fragment.init(position,selected);
            default:// Fragment # 2-9 - Will show list
                return Wallet_Fragment.init(position,selected);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return "Page " + position;
        switch (position) {
            case 0: // Fragment # 0 - This will show image
                return "By Cash";
            case 1: // Fragment # 1 - This will show image
                return "By Card";
            default:// Fragment # 2-9 - Will show list
                return "By Wallet";
        }
    }
}