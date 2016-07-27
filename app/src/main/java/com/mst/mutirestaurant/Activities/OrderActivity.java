package com.mst.mutirestaurant.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.TrackorderAdapter;

public class OrderActivity extends AppCompatActivity {
    ViewPager mViewPager;
    DrawerLayout drawerLayout;
    Toolbar actionBar;
    private FragmentStatePagerAdapter mSectionsRideAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mViewPager = (ViewPager) findViewById(R.id.vpager);
        mSectionsRideAdapter = new TrackorderAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsRideAdapter);
        /*drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, actionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();*/
    }
}
