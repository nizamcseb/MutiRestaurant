package com.mst.mutirestaurant.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.ChoosStoreActivity;
import com.mst.mutirestaurant.Activities.MainActivity;
import com.mst.mutirestaurant.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by ha on 10/24/15 AD.
 */
public class FullStoreAddress extends AsyncTask<String, Void, String> {

    Context con;

    ListView list = null;
    CommonFunction cf;
    String response = null;
    static String str_result = "", str_message = "";
    public static String str_storeid = "", str_storename = "", str_address = "", str_storelat = "", str_storelng = "",str_add="",stoid="",stoadd="",stoname="";
    public static String[] arr_str_storeid = null, arr_str_storename = null, arr_str_storeaddress = null, arr_str_storelat = null, arr_str_storelng = null;

    public static final String TAG_storeid = "store_id";
    public static final String TAG_storename = "store_name";
    public static  String TAG_address = "delivery_address";
    public static  String TAG_storeaddress = "store_address";
    public FullStoreAddress(Context context, String address) {
        this.con = context;
        this.str_add = address;
        cf = new CommonFunction(this.con);
    }


    @Override
    protected String doInBackground(String... params) {

        if (cf.isInternetOn() == true) {
            try {

                String API = cf.url+cf.PHP_FILE_FULL_STORE_PLACE;

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Execute HTTP Post Request  
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("storeaddress response"+response.toString());

                JSONObject json=new JSONObject(response);
                str_result = json.getString("success");

                if(Integer.parseInt(str_result)==0)
                {
                    System.out.println("storeaddress failure");
                    str_message = json.getString("message");

                }
                else if(Integer.parseInt(str_result)==1)
                {
                    System.out.println("storeaddress success");

                    str_storeid=""; str_storename=""; str_address=""; str_storelat=""; str_storelng="";

                    JSONArray data = json.getJSONArray("product");


                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject json1=data.getJSONObject(i);

                        str_storeid += json1.getString("storeid")+"~";
                        str_storename += json1.getString("storename")+"~";
                        str_address += json1.getString("storeaddr")+"~";
                        str_storelat += json1.getString("storelat")+"~";
                        str_storelng += json1.getString("storelng")+"~";

                        System.out.println("storeaddress output value"+ str_storeid + " " + str_storename + " " + str_address + " " + str_storelat + " " + str_storelng);
                    }

                    arr_str_storeid = str_storeid.split("~");
                    arr_str_storename = str_storename.split("~");
                    arr_str_storeaddress = str_address.split("~");
                    arr_str_storelat = str_storelat.split("~");
                    arr_str_storelng = str_storelng.split("~");

                }


            } catch (Exception e) {
                System.out.println("AsyncTask storeaddress error" + e.getMessage());
            }

        } else {
            Toast.makeText(con, "Please enable internet connection", Toast.LENGTH_SHORT).show();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        try {

            ChoosStoreActivity.mProgressView.setVisibility(View.GONE);
            ChoosStoreActivity.fulladdresslist.setAdapter(new EfficientAdapter(con, arr_str_storeid.length));

            ChoosStoreActivity.fulladdresslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //ChoosStoreActivity.storelocation.setText(arr_str_storename[position]);
                    ChoosStoreActivity.fulladdresslist.setVisibility(View.GONE);
                    ChoosStoreActivity.popupWindow.dismiss();
                    stoid = arr_str_storeid[position].toString();
                    stoadd = arr_str_storeaddress[position].toString();
                    stoname = arr_str_storename[position].toString();
                    System.out.println("Intenvalue--"+stoid+" "+stoadd+" "+stoname);
                    if(stoid !=null && stoadd !=null && stoname !=null ) {
                        Intent i = new Intent(con, MainActivity.class);
                        i.putExtra(TAG_storeid, stoid);
                        i.putExtra(TAG_address, str_add);
                        i.putExtra(TAG_storename, stoname);
                        i.putExtra(TAG_storeaddress, stoadd);
                        con.startActivity(i);
                    }else {
                        Toast.makeText(con, "Please Choose Store Location", Toast.LENGTH_SHORT).show();
                    }
                   //ChoosStoreActivity.add_dialog.dismiss();

                }
            });


        } catch (Exception e) {
            System.out.println("storeaddress post execute"+e.getMessage());
        }

    }


    public static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        ViewHolder holder;
        View v;
        int len=0, arrlen=0;

        public EfficientAdapter(Context con, int length) {
            mInflater = LayoutInflater.from(con);
            len =length;
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

            if(convertView == null) {
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.address_list_item, null);

                holder.text_address = (TextView) convertView.findViewById(R.id.tv_storeaddress);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(arr_str_storeid[position].contains("null") || arr_str_storename[position].contains("null") ||
                    arr_str_storeaddress[position].contains("null") || arr_str_storelng[position].contains("null") ||
                    arr_str_storelat[position].contains("null")) {

                arr_str_storename[position] = arr_str_storename[position].replace("null", "");

                holder.text_address.setText(Html.fromHtml(arr_str_storename[position]));

            } else {
                holder.text_address.setText(Html.fromHtml(arr_str_storename[position]));

            }

            return convertView;
        }

        static class  ViewHolder {
           public  TextView text_address;
        }
    }
}