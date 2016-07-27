package com.mst.mutirestaurant.support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.SelecteditemActivity;
import com.mst.mutirestaurant.adapter.LazyAdapter;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nizamuddeen on 14-11-2015.
 */
public class ListTask {


    Context con;
    CommonFunction cf;
    String response = null;
    static String str_result = "", str_message = "", str_plus = "";
    String str_foodname = "", str_foodid = "", str_fooddesc = "", str_price = "", str_cateid = "", str_likes = "", str_imageurl = "";
    public static String[] arr_foodid = null, arr_foodname = null, arr_url = null, arr_fooddesc = null, arr_price = null, arr_catid = null;
    public static final String TAG_prodname = "name";
    public static final String TAG_prodid = "proid";
    public static final String TAG_catid = "caid";
    public static final String TAG_price = "rate";
    public static final String TAG_proddesc = "desc";

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
    LazyAdapter adapter;

    ListView list;
    ProgressBar pb;
    TextView tv_loading;
    Activity activity;
    Button check;

    public ListTask(Context c) {

        this.con = c;
    }

    public void inPuts(Activity activityy,String CatId, ListView listt, ProgressBar pbb, TextView tv_loadingg, Button checkk) {
    this.activity=activityy;
        this.list = listt;
        this.pb = pbb;
        this.tv_loading = tv_loadingg;
        this.check = checkk;
        cf = new CommonFunction(con);

        if (cf.isInternetOn() == true) {

            new ListViewTask().execute(CatId);
        }


    }


    class ListViewTask extends AsyncTask<String, String, LazyAdapter> {

        protected LazyAdapter doInBackground(String... args) {

            if (cf.isInternetOn() == true) {

                try {


                    String API = cf.url + cf.PHP_FILE_TAB_PRODUCT;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("category", args[0]));

                    System.out.println("ListViewTask input value-->" + args[0]);

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

                            str_foodname = json1.getString(TAG_prodname);
                            str_foodid = json1.getString(TAG_prodid);
                            str_cateid = json1.getString(TAG_catid);
                            str_price = json1.getString(TAG_price);
                            str_fooddesc = json1.getString(TAG_proddesc);
                            //str_likes = json1.getString(TAG_likes);
                            str_imageurl = json1.getString(TAG_imageurl);

                            System.out.println("ListViewTask output value" + str_foodname + " " + str_foodid + " " + str_cateid + " " + str_price + " " + str_fooddesc + " " + str_imageurl);

                               /* if (str_foodname.contains("Null")) {
                                    str_foodname = str_foodname.replace("Null", "");
                                }
                                if (str_foodid.contains("Null")) {
                                    str_foodid = str_foodid.replace("Null", "");
                                }
                                if (str_cateid.contains("Null")) {
                                    str_cateid = str_cateid.replace("Null", "");
                                }
                                if (str_price.contains("Null")) {
                                    str_price = str_price.replace("Null", "");
                                }
                                if (str_fooddesc.contains("Null")) {
                                    str_fooddesc = str_fooddesc.replace("Null", "");
                                }
                                if (str_imageurl.contains("Null")) {
                                    str_imageurl = str_imageurl.replace("Null", "");
                                }*/

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
                            /*arr_foodname = str_foodname.split("~");
                            System.out.println("arr_foodname=" + arr_foodname);
                            arr_foodid = str_foodid.split("~");
                            System.out.println("arr_foodid=" + arr_foodid);
                            arr_fooddesc = str_fooddesc.split("~");
                            System.out.println("arr_fooddesc=" + arr_fooddesc);
                            arr_price = str_price.split("~");
                            System.out.println("arr_price=" + arr_price);
                            arr_catid = str_cateid.split("~");
                            System.out.println("arr_catid=" + arr_catid);
                            arr_url = str_imageurl.split("~");
                            System.out.println("arr_url=" + arr_url);*/
                    }

                    //  Thread.sleep(4000);

                } catch (Exception e) {
                    System.out.println("catch ListViewTask error" + e.getMessage());
                    return null;
                }

            } else {
                Toast.makeText(con, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            return new LazyAdapter(activity, foodlist);
        }

        protected void onPreExecute() {


            pb.setVisibility(View.VISIBLE);
            tv_loading.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(LazyAdapter adapterr) {

            try {
               /* if (file.equals("1")) {
                    pb.setVisibility(View.GONE);

                    tv_loading.setVisibility(View.GONE);
                    try {

                        adapter = new LazyAdapter(getActivity(), foodlist);System.out.println("LCOUNT=="+foodlist);
                    } catch (Exception e) {
                        System.out.println("LERROR==" + e.getMessage());
                    }
*/
                if (adapterr != null) {
                    pb.setVisibility(View.GONE);

                    tv_loading.setVisibility(View.GONE);

                    list.setAdapter(adapterr);
                    //list.setAdapter(new EfficientAdapter(getActivity(), arr_foodid.length));

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            HashMap<String, String> hmap = new HashMap<String, String>();
                            hmap = foodlist.get(position);
                            //System.out.println("name==="+name);
                            Intent i = new Intent(con, SelecteditemActivity.class);
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
            } catch (Exception e) {
                System.out.println("trt==" + e.getMessage());
            }
            // pb.setVisibility(View.GONE);
        }
    }
}
