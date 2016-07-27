package com.mst.mutirestaurant.Activities;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.fragments.fragment_product;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.getDb;

public class Tab_Activity extends AppCompatActivity {
    public CommonFunction cf;
    public static int count = 0;
    DataBase db;
    public String str_categorynam = "", stoid = "", str_title = "";
    public static String str_catid = "", str_ccid = "", str_name = "";
    public static String[] arr_catd = null, arr_cname = null, arr_sp = null;
    //public int position = 1;
    getDb gdb;
    private ViewPager mViewPager;

    private FragmentStatePagerAdapter mSectionsRideAdapter;
    ActionBar actionBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            str_categorynam = getIntent().getExtras().getString(cf.TAG_category).toString();
            stoid = getIntent().getExtras().getString(cf.TAG_storeid).toString();
            arr_catd = getIntent().getExtras().getStringArray("cid");
            str_catid = getIntent().getExtras().getString(cf.TAG_caid).toString();
            System.out.println("SS==" + str_catid);
            arr_cname = getIntent().getExtras().getStringArray("Caname");
        }

        System.out.println("getCategory-->" + str_categorynam + "  " + stoid + "  " + arr_catd + " " + arr_cname);
        setContentView(R.layout.activity_tab);
        cf = new CommonFunction(this);
        gdb = new getDb(this);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        db = new DataBase(this);
        try {
            db.CreateTable(4);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);

            count = c.getCount();
            System.out.println("cartset1==" + c.getCount());
        }catch(Exception e){
            System.out.println("cc=="+e.getMessage());
        }

        doIncrease();
        listView =(ListView)findViewById(R.id.listvalue);
        mViewPager = (ViewPager) findViewById(R.id.container);
       // mViewPager.setBackgroundColor(Color.WHITE);
        mSectionsRideAdapter = new SectionsRideAdapter(getSupportFragmentManager());

        /*for (int i = 0; i < arr_cname.length; i++) {
            System.out.println("INPP----" + arr_cname.length);
            //  str_ccid =  arr_catd[position].toString();System.out.println("POSSS=="+arr_catd.length+"  "+str_ccid);
            mSectionsRideAdapter.addFragment(new fragment_product(), arr_catd[i], arr_cname[i]);
        }
*/
       /* mTabs = (TabLayout) findViewById(R.id.tabs);
        mTabs.setupWithViewPager(mViewPager);*/
        mViewPager.setAdapter(mSectionsRideAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                System.out.println("View Pager onPageScrolled"+String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {

                System.out.println("View Pager onPageSelected"+String.valueOf(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /* Fragment fragment = new fragment_product();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();*/
                //fragment_product.newInstance(mViewPager.getCurrentItem(),arr_catd[mViewPager.getCurrentItem()]);
                if(cf.isInternetOn()==true){



                }

                System.out.println("View Pager onPageScrollStateChanged" +String.valueOf( mViewPager.getCurrentItem()));
                //Toast.makeText(getApplicationContext(),"View Pager state changed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void back() {
        Intent i = new Intent(Tab_Activity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.increment);
        menuItem.setIcon(buildCounterDrawable(count, R.mipmap.ic_cart_white));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent productIntent = new Intent(Tab_Activity.this, SearchActivity.class);
            startActivity(productIntent);
            return true;
        }
        if (id == R.id.increment) {
            Intent productIntent = new Intent(Tab_Activity.this, CartView.class);
            startActivity(productIntent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);
        db.CreateTable(4);
        Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);

        count = c.getCount(); System.out.println("cartset==" + c.getCount());

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);

        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    public void doIncrease() {
        /*db.CreateTable(4);
        Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);
        System.out.println("cartset==" + c.getCount());
        count = c.getCount();*/
        count++;
        invalidateOptionsMenu();
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent productIntent = new Intent(getApplicationContext(), SearchActivity.class);
           *//* productIntent.putExtra(cf.TAG_category, item.getTitle());
            productIntent.putExtra(cf.TAG_storeid, str_storeidd);
            productIntent.putExtra(cf.TAG_caid, item.getId());
            productIntent.putExtra("Caname", arr_caname);*//*
            startActivity(productIntent);
            return true;
        }
        if (id == R.id.testAction) {
           // Intent productIntent = new Intent(getApplicationContext(), CartView.class);
            //startActivity(productIntent);
            Intent intent = new Intent(Tab_Activity.this, CartView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Tab_Activity.this.startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public static class SectionsRideAdapter extends FragmentStatePagerAdapter {
        private static int NUM_ITEMS = arr_cname.length;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        public SectionsRideAdapter(FragmentManager fragmentManager) {


            super(fragmentManager);


        }

        // Returns total number of pages
        @Override
        public int getCount() {
            System.out.println("getCount()=="+NUM_ITEMS);
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {

            System.out.println("getItem()=="+position);
                return fragment_product.newInstance(position, arr_catd[position]);



        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            //return "Page " + position;

            return arr_cname[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Tab_Activity.this, MainActivity.class));
    }
    /*public class SectionsRideAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> FragmentList = new ArrayList();
        private final List<String> FragmentTitles = new ArrayList();
        private final List<String> FragmentIds = new ArrayList();

        //Fragment fragmentt;

        public SectionsRideAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String s, String title) {
            // for(int i =0; i <arr_cname.length; i++) {

            //fragmentt=fragment;
            FragmentList.add(fragment);

            //}
            FragmentTitles.add(title);
            FragmentIds.add(s);
        }

        @Override
        public Fragment getItem(int position) {

            return fragment_product.newInstance(FragmentIds.get(position));

            //return FragmentList.get(position);
        }

        @Override
        public int getCount() {

            return FragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return FragmentTitles.get(position);
        }


    }*/
}
