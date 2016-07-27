package com.mst.mutirestaurant.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.MainActivity;
import com.mst.mutirestaurant.Activities.PlaceOrderActivity;

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
 * Created by pc on 13-Nov-15.
 */
public class Booking {
    Context con;
    CommonFunction cf;
    DataBase db;
    BookingSms bs;
    getDb gdb;
    public static int val = 0;
    public static  String str_orderId="",Str_mob="",st_orde="";
    ArrayList<HashMap<String, String>> prodlist=null;

    public static String[] arr_prodname = null, arr_prodrate = null, arr_ptotal = null, arr_prodid = null, arr_total = null, arr_qty = null;
    public Booking(Context c) {
        this.con = c;
        cf = new CommonFunction(this.con);
        db = new DataBase(this.con);
        bs = new BookingSms(this.con);
        gdb = new getDb(this.con);
    }

    public void booking(ArrayList<HashMap<String, String>> prodArrayList, String userid, String str_orderId) {
        if (userid != null && prodArrayList !=null) {
            this.st_orde = str_orderId;
            this.prodlist = prodArrayList;System.out.println("Confirm Booking userid" +prodArrayList+" "+ userid + " " + str_orderId);
            BookingConfirm BookingConfirm = new BookingConfirm(prodArrayList,str_orderId);
            BookingConfirm.execute(userid, str_orderId);
        } else {

        }
    }



    private class BookingConfirm extends AsyncTask<String, Void, String> {
        String response = "", result = "", str_message = "";
        HashMap<String, String> hmap;

        public BookingConfirm(ArrayList<HashMap<String, String>> prodArrayList, String str_orderId) {
            prodlist =prodArrayList;
            st_orde = str_orderId;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                for (int i = 0; i < prodlist.size(); i++) {
                    System.out.println("Array value==" + i+" "+prodlist.size());

                    hmap = new HashMap<String, String>();
                    hmap = prodlist.get(i);

                   // System.out.println("Array" + hmap.get(PlaceOrderActivity.TAG_CART_ID).toString() + "" + hmap.get(PlaceOrderActivity.TAG_CART_RATE).toString());
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(cf.url+cf.PHP_FILE_BOOKINGS);
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("userid", params[0]));
                    nameValuePairs.add(new BasicNameValuePair("orderid", st_orde));System.out.print("oo=="+st_orde);
                    nameValuePairs.add(new BasicNameValuePair("qty", hmap.get(PlaceOrderActivity.TAG_CART_QUANTITY).toString()));
                    nameValuePairs.add(new BasicNameValuePair("prodid", hmap.get(PlaceOrderActivity.TAG_CART_ID).toString()));
                    nameValuePairs.add(new BasicNameValuePair("price", hmap.get(PlaceOrderActivity.TAG_CART_RATE).toString()));
                    nameValuePairs.add(new BasicNameValuePair("prodtotal", hmap.get(PlaceOrderActivity.TAG_CART_PROTOTAL).toString()));
                    nameValuePairs.add(new BasicNameValuePair("status", "0"));
                    nameValuePairs.add(new BasicNameValuePair("mobile", gdb.getMobile()));System.out.println("Mob=="+gdb.getMobile());
                    System.out.println("booking input value" + hmap.get(PlaceOrderActivity.TAG_CART_ID).toString() + " " + hmap.get(PlaceOrderActivity.TAG_CART_RATE).toString() + " " + hmap.get(PlaceOrderActivity.TAG_CART_QUANTITY).toString()+" "+ hmap.get(PlaceOrderActivity.TAG_CART_PROTOTAL).toString());

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("booking response" + response.toString());

                    JSONObject json = new JSONObject(response);
                    result = json.getString("success");
                    System.out.println("booking result=" + result);

                    if (Integer.parseInt(result) == 0) {
                        System.out.println("booking failure");
                        str_message = json.getString("message");

                    } else {
                        System.out.println("booking success");

                        JSONArray data = json.getJSONArray("product");


                        /*for (int k = 0; k < data.length(); k++) {
                            JSONObject json1 = data.getJSONObject(i);


                            str_orderId = json1.getString("orderid");
                            Str_mob = json1.getString("mobile");

                            System.out.println("Bookin Response" + str_orderId + " " + Str_mob);
                        }*/
                    }
                   // Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.out.println("booking error" + e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPreExecute() {

            //cf.showTProgress(con, R.drawable.gear, 200, 200);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("1")) {

                try {

                    String str = "Your order id is"+st_orde+" and"+" amount is "+hmap.get(PlaceOrderActivity.TAG_CART_PROTOTAL).toString()+" $."+"We will update your order status shortly.";
                    String St_mob = gdb.getMobile();System.out.print("Mo=="+St_mob);

                   // bs.Sendsms(St_mob,str);
                    Toast.makeText(con, "Success", Toast.LENGTH_SHORT).show();
                    try {

                        db.restaurant.execSQL("delete from " + db.Cart);
                    } catch(Exception e) { System.out.println("deleted table"+e.getMessage());}
                    Intent intent = new Intent(con, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    con.startActivity(intent);

                } catch (Exception e) {
                    System.out.println("booking_confirm post execute" + e.getMessage());
                }
            } else if (result.equals("null")) {
                Toast.makeText(con, "Something went wrong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(con, str_message, Toast.LENGTH_SHORT).show();

            }

            cf.dismissTProgress();
        }
    }
}
