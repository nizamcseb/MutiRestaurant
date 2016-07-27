package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.OrderlistAdapter;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.getDb;

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

public class MyOrderContentActivity extends AppCompatActivity {

    static ListView list;
    CommonFunction cf;
    Context con;
    String response = null;
    static String str_result = "", str_message = "";
    public static String str_orderid = "", str_rate = "", str_qty = "", str_total = "", str_prodname = "", str_mode = "", str_imageurl = "";
    public static String[] arr_prodname = null, arr_prodrate = null, arr_total = null, arr_qty = null, arr_mode = null;
    getDb gdb;
    MyOrderContentActivity my;
    public static final String TAG_ESTIM_TIME = "estideli_time";
    public static final String TAG_QUANTITY = "quantity";
    public static final String TAG_RATE = "price";
    public static final String TAG_PRODNAME = "prodname";
    public static final String TAG_PAYMENT_MODE = "payment_mode";
    public static final String TAG_TOTAL = "total";
    HashMap<String, String> hmap = new HashMap<String, String>();
    TextView totalam, mode, estitime, driname, drimob;
    public static final String TAG_ORDER_ID = "order_id";
    ArrayList<HashMap<String, String>> orderlist;
    int position;
    OrderlistAdapter adapter;
    ProgressBar pb;
    HashMap<String, String> map;

    /*public MyOrderContentActivity(Context c) {
        this.con = c;
    }
*/
   /* public MyOrderContentActivity(Context c) {
        this.con = c;
      //  cf = new CommonFunction(this.con);
        my = new MyOrderContentActivity(this.con);
    }*/

    /*public void MyOrderContentActivity(String orderid) {
        if (orderid != null) {
            System.out.println("Myorderid==" + orderid);
            Myorder Myorder = new Myorder();
            Myorder.execute(orderid);
        } else {

        }
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            str_orderid = b.getString("orderID");

        } else {
        }

        System.out.println("oo==" + str_orderid);
        setContentView(R.layout.activity_my_order_list);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        list = (ListView) findViewById(R.id.myorderlist);
        totalam = (TextView) findViewById(R.id.tv_total);
        mode = (TextView) findViewById(R.id.tv_mode);
        estitime = (TextView) findViewById(R.id.tv_estime);
        driname = (TextView) findViewById(R.id.tv_drivername);
        drimob = (TextView) findViewById(R.id.tv_deriverph);



        orderlist = new ArrayList<HashMap<String, String>>();
        gdb = new getDb(this);
        cf = new CommonFunction(this);

        if (cf.isInternetOn() == true) {

            new Myorder().execute();
        } else {
            Toast.makeText(MyOrderContentActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
        }

       /* totalam.setText("Total Amount :" +str_total +" "+"SAR");
        mode.setText("Payment Mode :"+ str_mode);*/
    }


    private class Myorder extends AsyncTask<String, String, String> {
       // String str_orderid = "", str_estime = "", str_order_date = "", str_order_time = "", str_mode = "";

        protected String doInBackground(String... params) {

            if (cf.isInternetOn() == true) {

                try {

                    String API = (cf.url + cf.PHP_FILE_MYORDERSCONTENT);

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    // nameValuePairs.add(new BasicNameValuePair("orderid", str_orderid));
                    nameValuePairs.add(new BasicNameValuePair("orderid", str_orderid));

                    System.out.println("ListViewTask input value==" + str_orderid);

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
                        str_qty="";str_rate="";str_mode="";str_total="";str_prodname="";

                        for (int i = 0; i < data.length(); i++) {
                            System.out.println("LEE==" + data.length());
                            JSONObject json1 = data.getJSONObject(i);

                            str_qty += json1.getString("quantity") + "~";System.out.println("qq=="+str_qty);
                            str_rate += json1.getString("rate") + "~";
                            str_mode = json1.getString("mode");
                            str_total = json1.getString("total");
                            str_prodname += json1.getString("prodname") + "~";

                            System.out.println("ListViewTask output value==" + str_qty + " " + str_rate + " " + str_mode + " " + str_total + " " + str_prodname);



                        }
                        arr_prodname = str_prodname.split("~");
                        System.out.println("arr_prodname=" + arr_prodname);
                        arr_prodrate = str_rate.split("~");
                        System.out.println("arr_prodid=" + arr_prodrate);
                        arr_qty = str_qty.split("~");
                        System.out.println("arr_qty=" + arr_qty);
                        /*arr_mode = str_mode.split("~");
                        System.out.println("arr_mode=" + arr_mode);*/
                        /*arr_total = str_total.split("~");
                        System.out.println("arr_total=" + arr_total);
*/

                    }

                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch ListViewTask error" + e.getMessage());
                    return null;
                }
            } else {
                Toast.makeText(MyOrderContentActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            return str_result;
        }

        protected void onPreExecute() {

               cf.showTProgress(MyOrderContentActivity.this, list);
            totalam.setText("Total Amount :" +str_total +" "+"$");
            mode.setText("Payment Mode :"+ str_mode);
        }

        protected void onPostExecute(String str_result) {
            {
                // Download complete. Lets update UI
                System.out.println("OnpostExecutive=="+str_result);
                try {
                    if (str_result != null) {

                        if (Integer.parseInt(str_result) == 1) {
                            System.out.println("DATALENGTH==" + arr_prodname.length);
                            list.setAdapter(new EfficientAdapter(MyOrderContentActivity.this, arr_prodname.length));

                        } else {
                            Toast.makeText(MyOrderContentActivity.this, "No Product Found!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e){
                    System.out.println("EEE=="+e.getMessage());
                }
                //Hide progressbar3
                cf.dismissTProgress();
                //  mProgressBar.setVisibility(View.GONE);
            }
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
                convertView = mInflater.inflate(R.layout.orderlist_item, null);
                holder.tv_ProdName = (TextView) convertView.findViewById(R.id.tv_prodname); // order id

                holder.tv_Quty = (TextView) convertView.findViewById(R.id.tv_qty); // order date

                holder.tv_Rate = (TextView) convertView.findViewById(R.id.tv_rate);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (arr_prodname[position].contains("null") || arr_qty[position].contains("null") || arr_prodrate[position].contains("null")) {

                arr_prodname[position] = arr_prodname[position].replace("null", "");
                arr_qty[position] = arr_qty[position].replace("null", "");
                arr_prodrate[position] = arr_prodrate[position].replace("null", "");

                holder.tv_ProdName.setText(Html.fromHtml(arr_prodname[position]));
                holder.tv_Quty.setText(Html.fromHtml(arr_qty[position]));
                holder.tv_Rate.setText(Html.fromHtml(arr_prodrate[position])+" $");


            } else {
                holder.tv_ProdName.setText(Html.fromHtml(arr_prodname[position]));
                holder.tv_Quty.setText(Html.fromHtml(arr_qty[position]));
                holder.tv_Rate.setText(Html.fromHtml(arr_prodrate[position])+" $");


            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_ProdName, tv_Quty, tv_Rate;

        }
    }
 }



    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/






