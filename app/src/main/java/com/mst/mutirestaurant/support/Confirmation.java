package com.mst.mutirestaurant.support;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.mst.mutirestaurant.R;

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
 * Created by pc on 19-Nov-15.
 */
public class Confirmation {
    Context con;
    CommonFunction cf;
    DataBase db;
    Booking bk;
    getDb gdb;
    View v;

    public static String[] arr_prodname = null, arr_prodrate = null, arr_ptotal = null, arr_prodid = null, arr_total = null, arr_qty = null;
    ArrayList<HashMap<String, String>> ProdArrayList=null;
    public Confirmation(Context c, View view) {
        this.v=view;
        this.con = c;
        cf = new CommonFunction(this.con);
        db = new DataBase(this.con);
       bk = new Booking(this.con);
        gdb = new getDb(this.con);
    }

    public void orderid(ArrayList<HashMap<String, String>> cartArrayList, String userid, String address, String mode, String total, String latt, String lngg, String stoid, String distance) {
        if (userid != null &&cartArrayList!=null ) {
            System.out.println("Confirm Booking userid" + userid + " " + address+" "+latt+"  "+lngg+" "+cartArrayList);
            this.ProdArrayList = cartArrayList;
            OrderId orderid = new OrderId();

            orderid.execute(userid, address,mode,total,latt,lngg,stoid,distance);
        } else {

        }
    }


    private class OrderId extends AsyncTask<String, Void, String> {
        String result = "", str_message = "", str_orderId = "", str_userid="";

        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(cf.url+cf.PHP_FILE_ORDER_ID);
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("userid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("deliaddress", params[1]));
                nameValuePairs.add(new BasicNameValuePair("mode", params[2]));
                nameValuePairs.add(new BasicNameValuePair("totamount", params[3]));
                nameValuePairs.add(new BasicNameValuePair("latt", params[4]));
                nameValuePairs.add(new BasicNameValuePair("lngg", params[5]));
                nameValuePairs.add(new BasicNameValuePair("stoid", params[6]));
                nameValuePairs.add(new BasicNameValuePair("distance", params[7]));
                System.out.println("orderid input value" + params[0] + " " + params[1] + "  " + params[2] + " " + params[3]);
                System.out.println("orderid 2nd value" + params[4] + " " + params[5]+"  "+ params[6]+" "+ params[7]);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httppost, responseHandler);
                System.out.println("orderid response" + response.toString());

                JSONObject json = new JSONObject(response);
                result = json.getString("success");
                System.out.println("orderid result=" + result);

                if (Integer.parseInt(result) == 0) {
                    System.out.println("orderid failure");
                    str_message = json.getString("message");

                } else if (Integer.parseInt(result) == 1) {
                    System.out.println("orderid success");

                    JSONArray data = json.getJSONArray("product");


                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);


                        str_orderId = json1.getString("orderid");
                        str_userid = json1.getString("userid");

                        System.out.println("orderid output value" + str_orderId + " " + str_userid);
                    }
                }
            } catch (Exception e) {
                System.out.println("orderid error" + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPreExecute() {

            cf.showTProgress(con,v);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.equals("1")) {

                   //OLd
                   bk.booking(ProdArrayList,str_userid, str_orderId);




                } else if (result.equals("null")) {
                    Toast.makeText(con, "Something went wrong", Toast.LENGTH_SHORT).show();
                    //  cf.dismissTProgress();
                } else {
                    Toast.makeText(con, str_message, Toast.LENGTH_SHORT).show();
                    //cf.dismissTProgress();
                }
                cf.dismissTProgress();
            }
            catch(Exception e){
             System.out.println("Confirmerr=="+e.getMessage());
            }
            }



    }
}
