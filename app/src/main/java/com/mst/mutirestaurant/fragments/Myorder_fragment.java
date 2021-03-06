package com.mst.mutirestaurant.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.MyOrderContentActivity;
import com.mst.mutirestaurant.Activities.OrderStatucActivity;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.OrderAdapter;
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

/**
 * Created by blync on 12/3/2015.
 */
public class Myorder_fragment extends Fragment {

    int fragVal;
    static ListView list;

    Context con;
    String response=null;
    static String str_result="", str_message="";
    String str_vehname="", str_vehid="", str_vehmodel="", str_chargeday="", str_chargedeposit="", str_likes="", str_imageurl="",str_deli_address="";
    public static String[] arr_orderid = null, arr_prodestime = null, arr_proddate = null, arr_qty = null, arr_mode = null,arr_deli=null;
    getDb gdb;
    public   static final String TAG_ESTIM_TIME = "estideli_time";
    public   static final String TAG_ORDER_ID = "order_id";
    public   static final String TAG_ORDER_DATE = "order_date";
    public   static final String TAG_ORDER_TIME = "order_time";
    public   static final String TAG_PAYMENT_MODE = "payment_mode";


    ArrayList<HashMap<String, String>> orderlist;
    MyOrderContentActivity my;
    OrderAdapter adapter;

    ProgressBar pb;

    TextView tv_loading;
    HashMap<String, String> map;
    CommonFunction cf;

    public static Myorder_fragment init(int val) {
        Myorder_fragment truitonFrag = new Myorder_fragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.activity_myorder, container, false);


        list = (ListView)layoutView. findViewById(R.id.myorderlist);

        gdb = new getDb(getContext());
        orderlist = new ArrayList<HashMap<String, String >>();
        my = new MyOrderContentActivity();
        cf = new CommonFunction(getContext());
      try {
          if (cf.isInternetOn() == true) {

              new ListViewTask().execute();
          } else {
              Toast.makeText(getContext(), "Please enable internet connection", Toast.LENGTH_SHORT).show();
          }
      }catch(Exception e){
          System.out.println("M=="+e.getMessage());
      }

        return layoutView;
    }

    class ListViewTask extends AsyncTask<String, String, String> {
        String  str_orderid="",str_estime="",str_order_date="",str_order_time="",str_mode="";
        protected String doInBackground(String... args) {

            if(cf.isInternetOn() == true) {

                try {

                    String API = (cf.url+cf.PHP_FILE_MYORDERS);

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid", gdb.getUserid()));


                    System.out.println("ListViewTask input value" + gdb.getUserid());

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
                        //  str_orderid="";str_estime="";str_order_date="";str_order_time="";str_mode="";

                        for (int i = 0; i < data.length(); i++)
                        {System.out.println("LEE=="+data.length());
                            JSONObject json1=data.getJSONObject(i);

                            str_orderid += json1.getString("order_id")+ "~";
                            str_estime += json1.getString("estideli_time")+ "~";
                            str_order_date += json1.getString("order_date")+ "~";
                            str_order_time += json1.getString("order_time")+ "~";
                            str_mode += json1.getString("payment_mode")+ "~";
                            str_deli_address +=json1.getString("delivery_address")+"~";
                            //str_likes = json1.getString(TAG_likes);
                            //str_imageurl = json1.getString(TAG_imageurl);

                            System.out.println("ListViewTask output value" + str_orderid + " " + str_estime + " " + str_order_date + " " + str_order_time + " " + str_mode+" "+str_deli_address);

                           /* map = new HashMap<String, String>();

                            map.put(TAG_ORDER_ID, str_orderid);
                            map.put(TAG_ESTIM_TIME, str_estime);
                            map.put(TAG_ORDER_DATE, str_order_date);
                            map.put(TAG_ORDER_TIME, str_order_time);
                            map.put(TAG_PAYMENT_MODE, str_mode);*/
                            // map.put(TAG_likes, str_likes);
                            //  map.put(TAG_imageurl, str_imageurl);

                            // orderlist.add(map);

                        }
                        arr_orderid = str_orderid.split("~");
                        System.out.println("arr_orderid=" + arr_orderid);
                        arr_prodestime = str_estime.split("~");
                        System.out.println("arr_proddate=" + arr_proddate);
                        arr_proddate = str_order_date.split("~");
                        System.out.println("arr_qty=" + arr_qty);
                        arr_mode = str_order_time.split("~");
                        System.out.println("arr_mode=" + arr_mode);
                        arr_deli = str_deli_address.split("~");
                        System.out.println("arr_deli=" + arr_deli);

                        // arr_ = str_mode.split("~");
                        //System.out.println("arr_total=" + arr_total);
                    }

                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch ListViewTask error"+e.getMessage());
                    return null;
                }
            }

            else {
                Toast.makeText(con,"Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            return str_result;
        }

        protected void onPreExecute(){

//            cf.showTProgress(con, R.drawable.progress, 200, 200);
        }

        protected void onPostExecute(String file)
        {
            /*ListAdapter adapter = new SimpleAdapter(ListActivity.this, vehiclelist, R.layout.list_item_vehicle, new String[]
                    {TAG_vehid, TAG_vehname, TAG_vehmodel, TAG_chargeday, TAG_rating, TAG_imageurl},
                    new int[] {R.id.vehicleid, R.id.vehicledetail, R.id.vehicledet, R.id.vehiclecharge, R.id.vehiclerating, R.id.vehicleimage});*/

            if(file.equals("1")) {
                // pb.setVisibility(View.GONE);
                // tv_loading.setVisibility(View.GONE);
                // adapter = new OrderAdapter(MyorderActivity.this, orderlist);

                //list.setAdapter(adapter);
                try {
                    if (str_result != null) {

                        if (Integer.parseInt(str_result) == 1) {
                            System.out.println("DATALENGTH==" + arr_orderid.length);
                            list.setAdapter(new EfficientAdapter(getContext(), arr_orderid.length));

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    int pos = position;
                                    System.out.println("position=" + position + " " + pos);
                                    String pos_orderid = arr_orderid[position].toString();
                                    String pos_address = arr_deli[position].toString();

                                    Intent in = new Intent(con, OrderStatucActivity.class);
                                    in.putExtra("orderID", pos_orderid);
                                    in.putExtra("deliveryaddress", pos_address);

                                    con.startActivity(in);
                                }
                            });


                        } else {
                            Toast.makeText(getContext(), "No Product Found!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e){
                    System.out.println("EEE=="+e.getMessage());
                }
                /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        HashMap<String, String> hmap = new HashMap<String, String>();
                        hmap = orderlist.get(position);
                        my.MyOrderContentActivity(hmap.get(TAG_ORDER_ID).toString());

                        Intent i = new Intent(MyorderActivity.this, MyOrderContentActivity.class);
                        i.putExtra(TAG_ORDER_ID, hmap.get(TAG_ORDER_ID).toString());System.out.println("name===" + hmap.get(TAG_ORDER_ID).toString());
                        startActivity(i);

                    }
                });*/
            } else {

            }
            cf.dismissTProgress();
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
                convertView = mInflater.inflate(R.layout.list_item_myorder, null);
                holder.tv_Orderid = (TextView) convertView.findViewById(R.id.tv_orderid); // order id

                holder.tv_Oderdate = (TextView) convertView.findViewById(R.id.tv_orderdate); // order date

                holder.tv_Odertime = (TextView) convertView.findViewById(R.id.tv_estime);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (arr_orderid[position].contains("null")) {

                arr_orderid[position] = arr_orderid[position].replace("null", "");
                arr_proddate[position] = arr_proddate[position].replace("null", "");
                //arr_prodrate[position] = arr_prodrate[position].replace("null", "");

                holder.tv_Orderid.setText(Html.fromHtml(arr_orderid[position]));
                holder.tv_Oderdate.setText(Html.fromHtml(arr_proddate[position]));
                // holder.tv_Rate.setText(Html.fromHtml(arr_prodrate[position]));


            } else {
                holder.tv_Orderid.setText(Html.fromHtml(arr_orderid[position]));
                holder.tv_Oderdate.setText(Html.fromHtml(arr_proddate[position]));


            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_Orderid, tv_Oderdate,tv_Odertime;

        }
    }
}










