package com.mst.mutirestaurant.Activities;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.GridItem;
import com.mst.mutirestaurant.adapter.GridViewAdapter;
import com.mst.mutirestaurant.fragments.Gift_fragment;
import com.mst.mutirestaurant.fragments.stores_fragment;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.mst.mutirestaurant.support.PrefManager;
import com.mst.mutirestaurant.support.getDb;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar actionBar;
    DataBase db;
    ImageView img;
    // TextView header_name, header_email;
    Button check, manual, automatic;
    ViewPager viewPager;
    PagerAdapter adapter;
    //int[] flag;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String str_result = "", str_name = "", str_storeid = "", str_cc = "", str_image = "", str_getaddress = "", str_stoadd = "", str_catyid = "", str_rate = "", str_prodid = "", str_storeidd = "", str_storename = "",
            str_qty = "", cfstroreid = "";
    public String[] arr_Catid = null, arr_caname = null, arr_Caimg = null, arr_stoid = null, arr_poid = null;
    public static final String TAG_category = "food_category";
    Context con;
    private GridView mGridView;
    private ListView Lview;
    private ProgressBar mProgressBar;
    public static final String TAG_storeid = "store_id";
    public static final String TAG_storename = "store_name";
    public static String TAG_address = "delivery_address";
    public static String TAG_storeaddress = "store_address";
    CommonFunction cf;
    String response = null;
    public static int count = 0;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String Api;
    public static Toolbar toolbarTop;
    public static TextView storelocation;//mTitle,
    private Animation animMove;
    getDb gdb;
    public static PopupWindow popupWindow = null;
    public static View popupView = null;
    GPSTracker gps;
    public static ListView fulladdresslist;
    public static View mProgressView;
    public static int val = 0;
    android.support.v4.app.FragmentTransaction fragmentTransaction;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (savedInstanceState == null) {
           str_getaddress = getIntent().getExtras().getString(TAG_address).toString();
            str_storeidd = getIntent().getExtras().getString(TAG_storeid).toString();
            str_storename = getIntent().getExtras().getString(TAG_storename).toString();
            str_stoadd = getIntent().getExtras().getString(TAG_storeaddress).toString();
        }*/


        setContentView(R.layout.activity_main);

       /* PrefManager prefManager = new PrefManager(getApplicationContext());
        // make first time launch TRUE
        prefManager.setFirstTimeLaunch(true);*/
        db = new DataBase(this);
        cf = new CommonFunction(this);
        gdb = new getDb(this);

        try {
            System.out.println("car");
            db.CreateTable(4);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);

            count = c.getCount();
            System.out.println("cartmain1==" + c.getCount());
        } catch (Exception e) {

            System.out.println("cc1==" + e.getMessage());
        }
        doIncrease();
        toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        //mTitle = (TextView) findViewById(R.id.toolbar_title);
        // mTitle.setText("DELIVERY ADDRESS :" + " " + str_getaddress);
        storelocation = (TextView) findViewById(R.id.txtstoreloc);
        toolbarTop.setVisibility(View.VISIBLE);
        str_storeidd = gdb.getStoreid();
        str_getaddress = gdb.getAddress();
        str_storename = gdb.getStorename();
        str_stoadd = gdb.getStoreLocation();
        System.out.println("ToolBarText==" + str_getaddress + " " + str_storeidd + "  " + str_storename);
        storelocation.setText("StoreName :" + str_storename + " |   " + "Location :" + str_stoadd);
        toolbarTop.setTitle("Menu");

        //mTitle.setText("Tap to change store location");
        animMove = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move);
        //mTitle.startAnimation(animMove);


        mGridView = (GridView) findViewById(R.id.gridView);

        // Lview = (ListView)findViewById(R.id.listView);
        // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        /*Button increaseButton = (Button) findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doIncrease();
            }
        });
*/
        arr_Catid = str_catyid.split("~");

        gps = new GPSTracker(this);
        //new AsyncHttpTask().execute(Api);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new stores_fragment(this);

        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();

/*
https://github.com/leolin310148/ShortcutBadger
        try {
            int badgeCount = 1;
            ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4
            ShortcutBadger.applyCount(getApplicationContext(),badgeCount); //for 1.1.3
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //toolbarTop.setVisibility(View.VISIBLE);


        /*mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });*/



        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
        actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, actionBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action

                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        //startActivity(new Intent(MainActivity.this, MainActivity.class));


                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = new stores_fragment(MainActivity.this);

                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.nav_order:

                        if (cf.isLogin() == true) {
                            startActivity(new Intent(MainActivity.this, MyorderActivity.class));
                        } else {
                            try {
                                cf.customizealert(MainActivity.this, "Login !!", "Do you want to login ?");
                                Button ok = (Button) cf.add_dialog.findViewById(R.id.okbtn);
                                ok.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View v) {

                                        try {

                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            MainActivity.this.startActivity(intent);
                                            finish();
                                        } catch (Exception e) {
                                            System.out.println("insert value into records db" + e.getMessage());
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                System.out.println("logouterror=" + e.getMessage());
                            }

                        }


                        return true;
                    case R.id.nav_Reservation:

                       /* fragment = new RideHistory(Str_userId, MainActivity.this);

                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();*/
                        // startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        return true;
                    case R.id.nav_Rewards:

                       /* fragment = new RideHistory(Str_userId, MainActivity.this);

                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();*/
                        // startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        return true;
                    case R.id.nav_GiftCards:

                        //android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = new Gift_fragment(MainActivity.this);

                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.commit();

                        return true;

                    case R.id.nav_profile:
                        if (cf.isLogin() == true) {
                            startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
                        } else {
                            try {
                                cf.customizealert(MainActivity.this, "Login !!", "Do you want to login ?");
                                Button ok = (Button) cf.add_dialog.findViewById(R.id.okbtn);
                                ok.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View v) {

                                        try {

                                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            MainActivity.this.startActivity(intent);
                                            finish();
                                        } catch (Exception e) {
                                            System.out.println("insert value into records db" + e.getMessage());
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                System.out.println("logouterror=" + e.getMessage());
                            }

                        }
                        // startActivity(new Intent(MainActivity.this, MyProfileActivity.class));

                        return true;

                    case R.id.nav_call:
                        makeCall();
                        //startActivity(new Intent(MainActivity.this,share.class));
                        return true;

                    case R.id.nav_terms:

                        //startActivity(new Intent(MainActivity.this,share.class));
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                    //if()

                }
            }

            private void makeCall() {
                Log.i("Make call", "");
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:91-800-001-0101"));
                try {
                    startActivity(phoneIntent);
                    finish();
                    //  Log. i("Finished making a call...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this,
                            "Call faild, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

      /* TextView login= (TextView)findViewById(R.id.tv_name);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });*/
    }

            /* private void storealert() {
                 add_dialog = new Dialog(MainActivity.this, android.R.style.Theme_Translucent_NoTitleBar);

                 add_dialog.setCancelable(false);
                 add_dialog.getWindow().setContentView(R.layout.alert_storelocation);

                 add_dialog.show();

                 final TextView text1 = (TextView) add_dialog.findViewById(R.id.textView1);
                 final TextView text2 = (TextView) add_dialog.findViewById(R.id.textView2);
                 final TextView text3 = (TextView) add_dialog.findViewById(R.id.textView3);

                 //text1.setText(title);

                 //text3.setText(msg);

                 manual = (Button) add_dialog.findViewById(R.id.manualbtn);
                 manual.setOnClickListener(new View.OnClickListener() {

                     @Override
                     public void onClick(View v) {
                         // TODO Auto-generated method stub
                         LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.fullstoreaddress_list, null);

                mProgressView = popupView.findViewById(R.id.progressBar3);
                popupWindow = new PopupWindow(popupView, 300, 300);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(manual, 200, -30);

                 if(cf.isInternetOn() == true) {
                     FullStoreAddress FullStoreAddress = new FullStoreAddress(MainActivity.this, str_getaddress);
                     FullStoreAddress.execute();
                 } else {
                     Toast.makeText(MainActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                 }
                     }
                 });
                 automatic = (Button) add_dialog.findViewById(R.id.automabtn);
                 automatic.setOnClickListener(new View.OnClickListener() {

                     @Override
                     public void onClick(View v) {
                         // TODO Auto-generated method stub


                         near_distance = new NearestDistance(MainActivity.this);
                         if(cf.isInternetOn() == true) {

                             near_distance.inputs(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()));
                             //add_dialog.dismiss();
                         } else {
                             Toast.makeText(MainActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
*/

    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for zoom in animation
        if (animation == animMove) {
        }

    }


    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }


    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Integer result = 0;
            try {

                Api = cf.url + cf.PHP_FILE_Bulk_Image;
                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(Api);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("storeid", str_storeidd));
                // Add your data

                System.out.println("StoreiD--" + cf.STR_STOREID);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("Image response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success");
                GridItem item;
                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("login failure");
                    String str_message = json.getString("message");

                } else if (Integer.parseInt(str_result) == 1) {
                    System.out.println("login success");

                    JSONArray data = json.getJSONArray("product");

                    str_name = "";
                    str_image = "";
                    str_catyid = "";
                    str_storeid = "";
                    str_prodid = "";
                    str_rate = "";
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);
                        str_name += json1.getString("prodname") + "~";
                        str_image += json1.getString("url") + "~";
                        str_catyid += json1.getString("categoryid") + "~";
                        System.out.println("CA==" + str_catyid);
                        str_storeid = json1.getString("stoid");
                        str_prodid = json1.getString("poid");
                        str_rate = json1.getString("rate");
                        str_qty = json1.getString("qty");
                        System.out.println("GridResponse==" + str_name + " " + str_image + " " + str_catyid);
                        System.out.println("Res--" + str_prodid + " " + str_rate + " " + str_qty);

                        /*if (str_name.contains("Null")) {



                                 /*item = new GridItem();
                                 item.setTitle(cur_name);
                                 item.setImage(str_image);
                                 item.setstid(str_catyid);
                                 System.out.println("Itemimage==" + item.getImage());
                                 mGridData.add(item);*/

                    }
                    arr_caname = str_name.split("~");
                    System.out.println("CATT==" + arr_caname.length);
                    arr_Catid = str_catyid.split("~");
                    System.out.println("CATT1==" + arr_Catid.length);
                    arr_Caimg = str_image.split("~");
                    System.out.println("CATT2==" + arr_Caimg.length);
                   /* arr_stoid = str_catyid.split("~");
                    System.out.println("CATT1==" + arr_Catid.length);
                    arr_Caimg = str_image.split("~");
                    System.out.println("CATT2==" + arr_Caimg.length);*/
                            /* item = new GridItem();
                             int position =0;
                             item.setTitle(arr_caname[position]);
                             item.setImage(str_image);
                             item.setstid(str_catyid);
                             System.out.println("Itemimage==" + item.getImage());
                             mGridData.add(item);*/
                }

                Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch login error" + e.getMessage());
                return null;
            }
            return str_result;
        }

        protected void onPreExecute() {
            cf.showTProgress(MainActivity.this, mGridView);
            // pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String str_result) {
            // Download complete. Lets update UI
            System.out.println("OnpostExecutive");
            if (str_result != null) {
                if (Integer.parseInt(str_result) == 1) {
                    System.out.println("DATALENGTH==" + arr_caname.length);
                    mGridView.setAdapter(new EfficientAdapter(MainActivity.this, arr_caname.length));
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int pos = position;
                            System.out.println("position=" + position + " " + pos);
                            str_cc = arr_Catid[position].toString();
                            System.out.println("CATT4==" + str_cc);
                            Intent intent = new Intent(MainActivity.this, Tab_Activity.class);
                            intent.putExtra(cf.TAG_category, arr_caname[position]);
                            intent.putExtra(cf.TAG_storeid, str_storeidd);
                            intent.putExtra("cid", arr_Catid);
                            intent.putExtra(cf.TAG_caid, arr_Catid[position]);
                            intent.putExtra("Caname", arr_caname);
                            startActivity(intent);
                        }
                    });
                    //  mGridAdapter.setGridData(mGridData);
                    toolbarTop.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "No Product Found!", Toast.LENGTH_LONG).show();
                }
            }
            //Hide progressbar3
            cf.dismissTProgress();
            //  mProgressBar.setVisibility(View.GONE);
        }
    }

    private class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        ViewHolder holder;
        int len = 0, arrlen = 0;

        public EfficientAdapter(Context context, int length) {
            mInflater = LayoutInflater.from(context);
            len = length;
        }

        public int getCount() {
            arrlen = len;
            return arrlen;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.grid_item_layout, null);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.grid_item_title);
                holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
                holder.Textviewid = (TextView) convertView.findViewById(R.id.grid_item_id);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (arr_caname[position].contains("null") || arr_Catid[position].contains("null") || arr_Caimg[position].contains("null")) {

                arr_caname[position] = arr_caname[position].replace("null", "");
                arr_Catid[position] = arr_Catid[position].replace("null", "");
                arr_Caimg[position] = arr_Caimg[position].replace("null", "");

                holder.titleTextView.setText(Html.fromHtml(arr_caname[position]));
                holder.Textviewid.setText(Html.fromHtml(arr_Catid[position]));
                Picasso.with(MainActivity.this)
                        .load(arr_Caimg[position])
                        .into(holder.imageView);

            } else {
                holder.titleTextView.setText(Html.fromHtml(arr_caname[position]));
                holder.Textviewid.setText(Html.fromHtml(arr_Catid[position]));
                Picasso.with(MainActivity.this)
                        .load(arr_Caimg[position])
                        .into(holder.imageView);

            }

            return convertView;
        }

        class ViewHolder {
            TextView titleTextView, Textviewid;
            ImageView imageView;
        }
    }


   /* String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        System.out.println("OnpostExecutive");
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }*/

    /**
     * Parsing the feed results and get the list
     * <p>
     * //  * @param result
     */
          /*   private void parseResult(String result) {
                 try {
                     JSONObject response = new JSONObject(result);
                     JSONArray posts = response.optJSONArray("product");
                     GridItem item;
                     for (int i = 0; i < posts.length(); i++) {
                         JSONObject post = posts.optJSONObject(i);
                         System.out.println("Length=="+post);
                         String title = post.optString("slug");
                         item = new GridItem();
                         item.setTitle(title);
                         JSONArray attachments = post.getJSONArray("product");
                         System.out.println("Length1=="+attachments);
                         if (null != attachments && attachments.length() > 0) {
                             JSONObject attachment = attachments.getJSONObject(0);
                             if (attachment != null)
                                 item.setImage(attachment.getString("url"));
                         }
                         mGridData.add(item);
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent productIntent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(productIntent);
            return true;
        }
        if (id == R.id.increment) {
            Intent productIntent = new Intent(MainActivity.this, CartView.class);
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

        count = c.getCount();
        System.out.println("cartmain==" + c.getCount());
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

    private void doIncrease() {
        count++;

        invalidateOptionsMenu();
    }
   /* @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.main, menu);
       // menu.getItem(1).setIcon(R.mipmap.ic_cart_white);
        //menu.getItem(1).setTitle("1");
       *//* int positionOfMenuItem = 1;
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString("1");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);*//*
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

            Intent productIntent = new Intent(MainActivity.this, SearchActivity.class);
           *//* productIntent.putExtra(cf.TAG_category, item.getTitle());
            productIntent.putExtra(cf.TAG_storeid, str_storeidd);
            productIntent.putExtra(cf.TAG_caid, item.getId());
            productIntent.putExtra("Caname", arr_caname);*//*
            startActivity(productIntent);
            return true;
        }
        if (id == R.id.action_cart) {

           *//* MenuItem myMenuItem = mMenu.findItem(R.id.user_id_label);
            myMenuItem.setTitle("New title!");*//*
            Intent productIntent = new Intent(MainActivity.this, CartView.class);
           *//* productIntent.putExtra(cf.TAG_category, item.getTitle());
            productIntent.putExtra(cf.TAG_storeid, str_storeidd);
            productIntent.putExtra(cf.TAG_caid, item.getId());
            productIntent.putExtra("Caname", arr_caname);*//*
            startActivity(productIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

}
   /* @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

