package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.SearchAdapter;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;

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
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static AutoCompleteTextView et_search;
    public static String autotxt;
    String response = null;
    static String str_result = "", str_message = "", str_plus = "";
    String str_foodname = "", str_foodid = "", str_fooddesc = "", str_price = "", str_cateid = "", str_quanty = "", str_imageurl = "";
    public static String[] arr_foodid = null, arr_foodname = null, arr_url = null, arr_fooddesc = null, arr_price = null, arr_catid = null;
    CommonFunction cf;
    Context con;
    ProgressBar pb;
    ActionBar actionBar;
    public static final String TAG_prodname = "prodname";
    public static final String TAG_prodid = "prodid";
    public static final String TAG_catid = "cid";
    public static final String TAG_price = "rate";
    public static final String TAG_proddesc = "proddesc";
    public static final String TAG_QUANTY = "quty";

    public static final String MAP_PID = "pid";
    public static final String MAP_CID = "cid";
    public static final String MAP_PRODNAME = "pname";
    public static final String MAP_PRICE = "price";
    public static final String MAP_DESC = "descp";
    public static final String MAP_IMAGE = "imageurl";
    // public   static final String TAG_likes = "likes";
    public static final String TAG_imageurl = "image";
    HashMap<String, String> map = null;
    ArrayList<HashMap<String, String>> foodlist;
    SearchAdapter adapter;
    static ListView list;
    public static int count = 0;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        pb = (ProgressBar) findViewById(R.id.progressBar2);
        et_search = (AutoCompleteTextView) findViewById(R.id.edt_trackID);
        list = (ListView) findViewById(R.id.listvalue);
        et_search.addTextChangedListener(new TextWatcher() {

                                             // @Override
                                             public void afterTextChanged(Editable s) {
                                                 // TODO Auto-generated method stub
                                             }

                                             @Override
                                             public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                 // TODO Auto-generated method stub

                                             }

                                             // @Override
                                             public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                 System.out.println("count12==" + start + " " + count);
                                                 if (count == 0) {
                                                     list.setAdapter(null);

                                                     new ListViewTask().execute();
                                                     System.out.println("count==");
                                                 } else {
                                                     autotxt = et_search.getText().toString();
                                                     System.out.println("Ontestchanges" + autotxt);
                                                     new ListViewTask().execute(autotxt);
                                                 }
                                             }

                                             class ListViewTask extends AsyncTask<String, String, SearchAdapter> {
                                                 protected SearchAdapter doInBackground(String... args) {


                                                     // private String searchlistload() {


                                                     if (cf.isInternetOn() == true) {
                                                         // new Thread() {
                                                         // @SuppressLint("NewApi")
                                                         //public void run() {
                                                         // Looper.prepare();
                                                         try {

                                                             String API = cf.url + cf.PHP_FILE_SEARCH_PRODUCT;

                                                             HttpClient httpclient = new DefaultHttpClient();

                                                             HttpPost httppost = new HttpPost(API);

                                                             // Add your data
                                                             List<BasicNameValuePair> nameValuePairs = new ArrayList<>();
                                                             nameValuePairs.add(new BasicNameValuePair("productname", args[0]));


                                                             System.out.println("ListViewTask value" + autotxt);

                                                             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                                                             // Execute HTTP Post Request

                                                             ResponseHandler<String> responseHandler = new BasicResponseHandler();
                                                             response = httpclient.execute(httppost, responseHandler);
                                                             System.out.println("ListViewTask response" + response.toString());

                                                             JSONObject json = new JSONObject(response);
                                                             str_result = json.getString("success");

                                                             if (Integer.parseInt(str_result) == 0) {
                                                                 System.out.println("ListViewTask failure");
                                                                 str_message = json.getString("message");

                                                             } else if (Integer.parseInt(str_result) == 1) {
                                                                 System.out.println("ListViewTask success");

                                                                 JSONArray data = json.getJSONArray("product");
                                                                 str_foodname = "";
                                                                 str_foodid = "";
                                                                 str_cateid = "";
                                                                 str_price = "";
                                                                 str_fooddesc = "";
                                                                 str_imageurl = "";
                                                                 foodlist = new ArrayList<HashMap<String, String>>();
                                                                 for (int i = 0; i < data.length(); i++) {
                                                                     JSONObject json1 = data.getJSONObject(i);
                                                                     System.out.println("Length==" + data.length());

                                                                     str_foodname = json1.getString("prodname");
                                                                     str_foodid = json1.getString("prodid");
                                                                     str_cateid = json1.getString("cid");
                                                                     str_price = json1.getString("rate");
                                                                     str_fooddesc = json1.getString("proddesc");
                                                                     str_quanty = json1.getString("quty");
                                                                     str_imageurl = json1.getString("prodimg");

                                                                     System.out.println("RESPONSE output value" + str_foodname + " " + str_foodid + " " + str_cateid + " " + str_price + " " + str_imageurl);

                                                                     map = new HashMap<String, String>();

                                                                     map.put(MAP_PRODNAME, str_foodname);
                                                                     map.put(MAP_CID, str_cateid);
                                                                     map.put(MAP_PID, str_foodid);
                                                                     map.put(MAP_PRICE, str_price);
                                                                     map.put(MAP_DESC, str_fooddesc);
                                                                     // map.put(TAG_likes, str_likes);
                                                                     map.put(MAP_IMAGE, str_imageurl);

                                                                     foodlist.add(map);
                                                                     System.out.println("FF==" + foodlist);

                                                                 }

                                                             }

                                                             //Thread.sleep(4000);
                                                         } catch (Exception e) {
                                                             System.out.println("catch ListViewTask error" + e.getMessage());
                                                             return null;
                                                         }
                                                     }
                                                     // }.start();
                                                     // }

                                                     else {
                                                         Toast.makeText(con, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                                                     }
                                                     return new SearchAdapter(SearchActivity.this, foodlist);
                                                     //  }
                                                 }

                                                 protected void onPreExecute() {

                                                     pb.setVisibility(View.VISIBLE);
                                                     //tv_loading.setVisibility(View.VISIBLE);
                                                 }

                                                 protected void onPostExecute(SearchAdapter adapterr) {
                                                     try {

                                                         if (adapterr != null) {

                                                             pb.setVisibility(View.GONE);

                                                           //  tv_loading.setVisibility(View.GONE);

                                                             list.setAdapter(adapterr);
                                                             System.out.println("Addp==" + list);
                                                             //list.setAdapter(new EfficientAdapter(getActivity(), arr_foodid.length));

                                                             list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                 @Override
                                                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                                     HashMap<String, String> hmap = new HashMap<String, String>();
                                                                     hmap = foodlist.get(position);
                                                                     //System.out.println("name==="+name);
                                                                     Intent i = new Intent(SearchActivity.this, SelecteditemActivity.class);
                                                                     i.putExtra(cf.TAG_prodname, hmap.get(MAP_PRODNAME).toString());
                                                                     i.putExtra(cf.TAG_prodid, hmap.get(MAP_PID).toString());
                                                                     //i.putExtra(MAP_CID, hmap.get(MAP_CID).toString());
                                                                     i.putExtra(cf.TAG_proddesc, hmap.get(MAP_DESC).toString());
                                                                     i.putExtra(cf.TAG_price, hmap.get(MAP_PRICE).toString());
                                                                     // i.putExtra(TAG_likes, hmap.get(TAG_likes).toString());
                                                                     i.putExtra(cf.TAG_imageurl, hmap.get(MAP_IMAGE).toString());
                                                                     con.startActivity(i);

                                                                 }
                                                             });

                                                         } else {

                                                         }
                                                         pb.setVisibility(View.GONE);
                                                     }
                                                     catch(Exception e){
                                                            System.out.println("Er=="+e.getMessage());
                                                     }
                                                     }

                                             }

                                             private void refreshList(String enteredText) {
                                                 // TODO Auto-generated method stub
                                                 Toast.makeText(SearchActivity.this, enteredText, Toast.LENGTH_SHORT).show();
                                                 if (cf.isInternetOn() == true) {


                                                 } else {
                                                     Toast.makeText(getApplicationContext(), "Please enable internet connection", Toast.LENGTH_SHORT).show();
                                                 }

                                             }
                                         }

        );
        et_search.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                autotxt = et_search.getText().toString();
                autocomplate(autotxt);
                System.out.println("REAUTO==" + autotxt);

            }

            private void autocomplate(String autotxt) {

            }
        });

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
            Intent productIntent = new Intent(SearchActivity.this, SearchActivity.class);
            startActivity(productIntent);
            return true;
        }
        if (id == R.id.increment) {
            Intent productIntent = new Intent(SearchActivity.this, CartView.class);
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
    private void back() {
        Intent i = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(i);
    }
}