package com.mst.mutirestaurant.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mst.mutirestaurant.R;

/**
 * Created by Lenovo on 19-05-2016.
 */
@SuppressLint("ValidFragment")
public class frag_gift_bookings extends Fragment {


    public frag_gift_bookings(Context context) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_gift_bookings, container, false);

        ViewPager viewPager= (ViewPager) rootView.findViewById(R.id.vpPager_gift);
        return rootView;
    }

}