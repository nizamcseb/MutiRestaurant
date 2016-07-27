package com.mst.mutirestaurant.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mst.mutirestaurant.Activities.MainActivity;
import com.mst.mutirestaurant.Activities.MyOrderContentActivity;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.OrderAdapter;
import com.mst.mutirestaurant.adapter.SimpleTapbsadapter;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.getDb;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 17-05-2016.
 */
@SuppressLint("ValidFragment")
public class Gift_fragment extends Fragment {


    public Gift_fragment(Context context) {
    }
    TabLayout mTabs;
    private ViewPager tabsviewPager;
    private SimpleTapbsadapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_giftcard, container, false);

         tabsviewPager= (ViewPager) rootView.findViewById(R.id.vpPager_gift);
        try {
            //tabsviewPager = (ViewPager) findViewById(R.id.tabspager);

            mTabsAdapter = new SimpleTapbsadapter(getActivity().getSupportFragmentManager());

            //creating the tabs and adding them to adapter class
            mTabsAdapter.addFragment(new frag_gift_buy(getActivity()), "Buy");

            mTabsAdapter.addFragment(new frag_gift_send(getActivity()), "Send");

           mTabsAdapter.addFragment(new frag_gift_bookings(getActivity()), "Bookings");

           // mTabsAdapter.addFragment(new canceled(MyordersActivity.this, Str_userId, "CANCELLED"), "Cancelled");


            //setup viewpager to give swipe effect
            tabsviewPager.setAdapter(mTabsAdapter);

            //mTabs = (TabLayout) findViewById(R.id.tabs);
            //  mTabs.setupWithViewPager(tabsviewPager);
        } catch (Exception e) {
            System.out.println("err==" + e.getMessage());

        }
        return rootView;
    }

}