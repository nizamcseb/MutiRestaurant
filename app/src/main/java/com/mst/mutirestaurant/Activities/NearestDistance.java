package com.mst.mutirestaurant.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mst.mutirestaurant.support.CommonFunction;

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

/**
 * Created by ha on 10/24/15 AD.
 */
public class NearestDistance {
    Context con;
    CommonFunction cf;
    String response=null;
   public static String str_result="", str_message="",Storeid="";
    static String str_storeid="", str_storename="", str_address="", str_storelat="", str_storelng="",str_add="";
public static  Dialog addlert;
    public static final String TAG_storeid = "store_id";
    public static final String TAG_storename = "store_name";
    public static  String TAG_address = "delivery_address";
    public static  String TAG_storeaddress = "store_address";
    public NearestDistance(Context c) {
        this.con = c;
    }

    public void inputs( String myLat, String myLng,String address) {
        cf = new CommonFunction(con);

//this.addlert = add_dialog;
        if (myLat != null && myLng != null && address !=null) {
           this.str_add = address;
            new RetrieveTrackInfo().execute(myLat, myLng);
        }
    }

    class RetrieveTrackInfo extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {

   try {

                    String API = cf.url+cf.PHP_FILE_NEAREST_PLACE;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("latitude", params[0]));
                    nameValuePairs.add(new BasicNameValuePair("longitude", params[1]));


                    System.out.println("distance input value" + params[0] + " " + params[1]);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("distance response"+response.toString());

                    JSONObject json=new JSONObject(response);
                    str_result = json.getString("success");

                    if(Integer.parseInt(str_result)==0)
                    {
                        System.out.println("distance failure");
                        str_message = json.getString("message");

                    }
                    else if(Integer.parseInt(str_result)==1)
                    {
                        System.out.println("distance success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++)
                        {
                            JSONObject json1=data.getJSONObject(i);

                            str_storeid = json1.getString("storeid");
                            str_storename = json1.getString("storename");
                            str_address = json1.getString("storeaddr");
                            str_storelat = json1.getString("storelat");
                            str_storelng = json1.getString("storelng");

                            System.out.println("distance output value-->"+ str_storeid + " " + str_storename + " " + str_address + " " + str_storelat + " " + str_storelng);
                        }
                    }

                    Thread.sleep(4000);

                } catch (Exception e) {
                    System.out.println("AsyncTask distance error" + e.getMessage());
                }


            return str_storename;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result == null) {

                } else {
                   // MainActivity.storelocation.setText(result);
                  Storeid = str_storeid;
                    cf.STR_STOREID = str_storeid;
                   // cf.TAG_storename = str_storename ;
                    System.out.println("Nearest storeID==" + Storeid);
                    // if(addlert!=null)
                    if(Storeid.equals("")) {
                        Toast.makeText(con, "Please Enable Gps on your mobile", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent i = new Intent(con, MainActivity.class);
                        i.putExtra(TAG_storeid, str_storeid);
                        i.putExtra(TAG_address, str_add);
                        i.putExtra(TAG_storename, str_storename);
                        i.putExtra(TAG_storeaddress, str_address);
                        con.startActivity(i);
                    }
                    //addlert.dismiss();
                }
            }catch (Exception e){
                System.out.println("Intenerroe-->"+e.getMessage());
            }
        }



    }
}
