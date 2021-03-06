package com.mst.mutirestaurant.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.StoreAdapter;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import com.blyncsolutions.restaurant.adapter.LazyAdapter;

public class ListActivity extends AppCompatActivity {
    static ListView list;
    CommonFunction cf;
    String response=null;
    static String str_result="", str_message="";
    String str_vehname="", str_vehid="", str_vehmodel="", str_chargeday="", str_chargedeposit="", str_likes="", str_imageurl="";
    public static String str_storeid = "", str_storename = "", str_address = "", str_storelat = "", str_storelng = "",str_add="",stoid="",stoadd="",stoname="";
    public   static final String TAG_storeid = "storeid";
    public   static final String TAG_storename = "storename";
    public   static final String TAG_storeaddress = "storeaddress";
    public   static final String TAG_storelat = "storelat";
    public   static final String TAG_storelng = "storelng";
   // public   static final String TAG_likes = "likes";
    public   static final String TAG_distance = "distance";
    double currentlat;
    double currentlong;
    ArrayList<HashMap<String, String>> vehiclelist;
    public static Double distance;
    StoreAdapter adapter;
    GPSTracker gps;
    ProgressBar pb;
    DataBase db;
    TextView tv_loading;
    public static Toolbar toolbarTop;
    HashMap<String, String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choos_store);
        toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        toolbarTop.setTitle("Store Location");

        pb= (ProgressBar)findViewById(R.id.progressBar2);
        list = (ListView) findViewById(R.id.storelist);
        tv_loading= (TextView)findViewById(R.id.textView);
        gps = new GPSTracker(this);
        vehiclelist = new ArrayList<HashMap<String, String >>();
        db = new DataBase(this);
        cf = new CommonFunction(this);
        if(gps.getLocation()!=null)
        {
            currentlat = gps.getLatitude(); System.out.println("H&U share loc meetingpoint lat"+currentlat);
            currentlong = gps.getLongitude();
            System.out.println("H&U share loc meetingpoint long" + currentlong);
            //setLocation(currentlat, currentlong, this);
            //System.out.println("CurrentLoc=="+ curloc);
        }
        else
        {
            System.out.println("CurrentLoc1");
        }

        if(cf.isInternetOn() == true) {

                new ListViewTask().execute();
        }
        else {
            Toast.makeText(ListActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    class ListViewTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            if(cf.isInternetOn() == true) {

                try {

                    String API = (cf.url+cf.PHP_FILE_FULL_STORE_PLACE);

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                   /* List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("category", "breakfast"));


                    System.out.println("ListViewTask input value" + "Bike");

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("ListViewTask response"+response.toString());

                    JSONObject json=new JSONObject(response);
                    str_result = json.getString("success");

                    if(Integer.parseInt(str_result)==0)
                    {
                        System.out.println("ListViewTask failure");
                        str_message = json.getString("message");

                    }
                    else if(Integer.parseInt(str_result)==1)
                    {
                        System.out.println("ListViewTask success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++)
                        {
                            JSONObject json1=data.getJSONObject(i);

                            str_storeid = json1.getString("storeid");
                            str_storename = json1.getString("storename");
                            str_address = json1.getString("storeaddr");
                            str_storelat = json1.getString("storelat");
                            str_storelng = json1.getString("storelng");

                            System.out.println("ListViewTask output value"+ str_storeid + " " + str_storename + " " + str_address + " " + str_storelat + " " + str_storelng);

                            Double Olat = Double.valueOf(str_storelat);
                            Double Olng = Double.valueOf(str_storelng);System.out.println("OL=="+currentlat+"  "+currentlong);
                            distance=cf.findDistance(new LatLng(Olat, Olng), new LatLng(currentlat, currentlong));
                            distance=distance/1000;
                            distance=Math.round(distance*100.0)/100.0; System.out.println("DIS==" + distance);
                            map = new HashMap<String, String>();

                            map.put(TAG_storeid, str_storeid);
                            map.put(TAG_storename, str_storename);
                            map.put(TAG_storeaddress, str_address);
                            map.put(TAG_storelat, str_storelat);
                            map.put(TAG_storelng, str_storelng);
                            map.put(TAG_distance, String.valueOf(distance));

                            vehiclelist.add(map);
                        }
                    }

                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch ListViewTask error"+e.getMessage());
                    return null;
                }
            }

            else {
                Toast.makeText(ListActivity.this,"Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            return str_result;
        }

        protected void onPreExecute(){
            cf.showTProgress(ListActivity.this, tv_loading);

        }

        protected void onPostExecute(String file)
        {


            if(file.equals("1")) {
                cf.dismissTProgress();
                adapter = new StoreAdapter(ListActivity.this, vehiclelist);

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        HashMap<String, String> hmap = new HashMap<String, String>();
                        hmap = vehiclelist.get(position);
                    try {
                    db.CreateTable(5);
                        db.restaurant.execSQL("update " + db.Store + " set storeid ='" + hmap.get(TAG_storeid).toString() + "' , storename ='" + hmap.get(TAG_storename).toString() + "' , stolocation ='" + hmap.get(TAG_storeaddress).toString()  + "',storelat='" + hmap.get(TAG_storelat).toString()  + "',storelng='" + hmap.get(TAG_storelng).toString()  + "' where status='1' ");
                   // db.restaurant.execSQL("update " + db.Deliveryaddress + " set storeid ='" + hmap.get(TAG_storeid).toString() + "' , storename ='" + hmap.get(TAG_storename).toString() + "' , stolocation ='" + hmap.get(TAG_storeaddress).toString() + "' ");
                    System.out.println("UP==" + str_storeid + " " + str_storename + " " + str_address);
                   // Toast.makeText(ListActivity.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println("Urror==" + e.getMessage());
                }


                        Intent intent = new Intent(ListActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ListActivity.this.startActivity(intent);
                        finish();



                    }
                });
            } else {

            }   cf.dismissTProgress();
        }
    }
}
