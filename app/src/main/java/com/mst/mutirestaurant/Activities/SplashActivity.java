package com.mst.mutirestaurant.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private static int code = 1;
    static CommonFunction cf;
    DataBase resta;
    double currentlat;
    String response=null;
    double currentlong;
    public static String curloc = "";
    public static String setaddress="";
    public static String sublocality="";
    public static String CityName="";
    public static String StateName="";
    public static String etlocation="";
    public static String CountryName="";
    public static String lattitude="";
    public static String longitude="";
    GPSTracker gps;
    public static Double distance;
    public String preaddress="";
    public static final String TAG_address = "delivery_address";
    public static String str_result="", str_message="",Storeid="";
    static String str_storeid="", str_storename="", str_address="", str_storelat="", str_storelng="",str_add="";
    public String [] arr_storeid=null,arr_storename=null,arr_staddress=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        cf = new CommonFunction(this);
        resta = new DataBase(this);
        gps = new GPSTracker(this);


            if(gps.getLocation()!=null)
            {
                currentlat = gps.getLatitude(); System.out.println("H&U share loc meetingpoint lat"+currentlat);
                currentlong = gps.getLongitude();
                System.out.println("curr==" + currentlong);
                setLocation(currentlat, currentlong, this);
                System.out.println("CurreLo=="+ cf.curloc);
            }
            else
            {
                System.out.println("CurrentLoc1");
            }


        if(cf.isInternetOn() == true) {

            //new ListViewTask().execute();
        }
        else {
            Toast.makeText(SplashActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
        }
        try {

            resta.CreateTable(2);
            Cursor c = resta.restaurant.rawQuery("select * from " + resta.Cart , null);
            if (c.getCount() > 0) {

                resta.restaurant.execSQL("delete from " + resta.Cart);
            } else {

            }
        }
        catch(Exception e){
            System.out.println("dbclear=="+e.getMessage());
        }

        if(cf.isInternetOn()==true) {

            if(cf.isGpsOn()==true) {

                new Handler().postDelayed(new Runnable() {

                        public void run () {
                        try {
                            resta.CreateTable(5);
                            Cursor c = resta.restaurant.rawQuery("select * from " + resta.Store + " where status = '1'", null);
                            System.out.println("countofc==" + c.getCount());

                            if (c.getCount() > 0) {
                                //startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                                c.moveToFirst();
                               // preaddress = c.getString(c.getColumnIndex("address")); System.out.println("mainad="+preaddress);
                                resta.restaurant.execSQL("update " + resta.Store + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "',address='" + curloc + "' where status='1' ");
                                Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                               // i.putExtra(TAG_address, str_address);
                                startActivity(i);
                            } else {
                                try {
                                    resta.CreateTable(5);
                                    resta.restaurant.execSQL("INSERT INTO "
                                            + resta.Store
                                            + "(storeid,storename,status,stolocation,address)"
                                            + "VALUES ('" + str_storeid + "', '" + str_storename + "','1','" + str_address + "','" + curloc + "')");
                                   // db.restaurant.execSQL("update " + db.Deliveryaddress + " set address ='" + str_fpEditAdd + "' where lid ='" + arr_tid[position] + "' ");
                                     //resta.restaurant.execSQL("update " + resta.Store + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "',address='"+ str_address+"' where status='1' ");
                                    System.out.println("ToVALUES==" + str_storeid + " " + str_storename + " " + str_address);
                                //    Toast.makeText(SplashActivity.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    System.out.println("Urror==" + e.getMessage());
                                }
                                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                            }
                        }
                         catch(Exception e) {
                             //else {
                                 startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                            // }
                             System.out.println("Intent error-->"+e.getMessage());
                         }
                        finish();
                    }

                   // }
                }, SPLASH_TIME_OUT);
            }
            else {
                //Toast.makeText(SplashActivity.this, "Please enable gps location", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("Location Service")
                        .setMessage("Your GPS seems to be disabled, Do you want to enable it?")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int id) {
                                Intent gps_intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(gps_intent, 0);
                            }
                        });
                Dialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        }
        else {

            //Toast.makeText(SplashActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setTitle("Network Service")
                    .setMessage("Your Network connection seems to be disabled, Do you want to enable it?")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int id) {
                            Intent nw_intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivityForResult(nw_intent, 1);
                        }
                    });
            Dialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }


    }
    public static void setLocation(Double currentlat, Double currentlong, Context con) {


        // TODO Auto-generated method stub
        try
        { System.out.println("tyrgp=" + currentlat);
            //Getting address from found locations.
            if(cf.isInternetOn() == true){
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(con, Locale.getDefault());
                addresses = geocoder.getFromLocation(currentlat, currentlong, 1); System.out.println("Address"+addresses);

		     /*  StreetName= addresses.get(0).getLocality();System.out.println("StreetName="+StreetName);
		       StateName= addresses.get(0).getAdminArea();*/
                setaddress = addresses.get(0).getAddressLine(0);
                sublocality= addresses.get(0).getSubLocality();
                CityName = addresses.get(0).getLocality();
                CountryName = addresses.get(0).getCountryName();

                //StreetName= addresses.get(0).get;
                // you can get more details other than this . like country code, state code, etc.
                System.out.println(" sublocality " + sublocality);
                System.out.println(" setaddress " + setaddress);
                System.out.println(" CityName " + CityName);
                System.out.println(" CountryName " + CountryName);
                curloc = sublocality +"," + setaddress + "," + CityName + "," + CountryName;
                System.out.println("CULOC=="+curloc);
                if(curloc.contains("null,"))
                {
                    curloc = curloc.replace("null,", "");
                }


            }
            else
            {

            }
        }
        catch (Exception e)
        {
            System.out.println("setlocationerror"+e.getMessage());
            e.printStackTrace();
        }




    }
    @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(code == 0) {
            /*startActivity(new Intent(SplashActivity.this, SelectLoactionActivity.class));
            finish();*/
            resta.CreateTable(5);
            Cursor c = resta.restaurant.rawQuery("select * from " + resta.Store + " where status = '1'", null);
            System.out.println("countofc==" + c.getCount());

            if (c.getCount() > 0) {
                //startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                c.moveToFirst();
                //preaddress = c.getString(c.getColumnIndex("address")); System.out.println("mainad="+preaddress);

                resta.restaurant.execSQL("update " + resta.Store + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "',address='" + curloc + "',storelat='"+str_storelat +"',storelng='"+str_storelng +"' where status='1' ");
                Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
               // i.putExtra(TAG_address, str_address);
                startActivity(i);
            } else {
                try {
                    resta.CreateTable(5);

                    resta.restaurant.execSQL("INSERT INTO "
                            + resta.Store
                            + "(storeid,storename,status,stolocation,address,storelat,storelng)"
                            + "VALUES ('" + str_storeid + "', '" + str_storename + "','1','" + str_address + "','" +curloc +"','"+str_storelat +"','"+str_storelng +"')");
                    //resta.restaurant.execSQL("update " + resta.Deliveryaddress + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "',status='1' ");
                    System.out.println("ToVALUES==" + str_storeid + " " + str_storename + " " + str_address+" "+str_storelat);
                    //    Toast.makeText(SplashActivity.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println("Urror==" + e.getMessage());
                }
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
        }
        else {
            startActivity(new Intent(SplashActivity.this, SplashActivity.class));
        }
    }
    class ListViewTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            if (cf.isInternetOn() == true) {

                try {

                    String API = cf.url + cf.PHP_FILE_NEAREST_PLACE;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(gps.getLatitude())));
                    nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(gps.getLongitude())));


                    System.out.println("distance input value" + String.valueOf(gps.getLatitude()) + " " + String.valueOf(gps.getLongitude()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("distance response" + response.toString());

                    JSONObject json = new JSONObject(response);
                    str_result = json.getString("success");

                    if (Integer.parseInt(str_result) == 0) {
                        System.out.println("distance failure");
                        str_message = json.getString("message");

                    } else if (Integer.parseInt(str_result) == 1) {
                        System.out.println("distance success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json1 = data.getJSONObject(i);

                            str_storeid = json1.getString("storeid");
                            str_storename = json1.getString("storename");
                            str_address = json1.getString("storeaddr");
                            str_storelat = json1.getString("storelat");
                            str_storelng = json1.getString("storelng");

                            System.out.println("distance output value-->" + str_storeid + " " + str_storename + " " + str_address + " " + str_storelat + " " + str_storelng);
                            if(str_storeid.contains("Null"))
                            {
                                str_storeid = str_storeid.replace("Null", "");
                            }
                            if(str_storename.contains("Null"))
                            {
                                str_storename = str_storename.replace("Null", "");
                            }
                            if(str_address.contains("Null"))
                            {
                                str_address = str_address.replace("Null", "");
                            }



                        }
                        Double Olat = Double.valueOf(str_storelat);
                        Double Olng = Double.valueOf(str_storelng);System.out.println("OL=="+currentlat+"  "+currentlong);
                        distance=cf.findDistance(new LatLng(Olat, Olng), new LatLng(currentlat, currentlong));
                        distance=distance/1000;
                        distance=Math.round(distance*100.0)/100.0; System.out.println("DIS==" + distance);


                        arr_storeid = str_storeid.split("~");System.out.println("arr_storeid="+arr_storeid);
                        arr_storename = str_storename.split("~");System.out.println("arr_storename="+arr_storename);
                        arr_staddress = str_address.split("~");System.out.println("arr_staddress="+arr_staddress);
                    }

                    Thread.sleep(4000);

                } catch (Exception e) {
                    System.out.println("catch ListViewTask error" + e.getMessage());
                    return null;
                }
            } else {
                Toast.makeText(SplashActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            return str_result;
        }

        protected void onPreExecute() {
          //  cf.showTProgress(SplashActivity.this, R.drawable.progress, 200, 200);

        }

        protected void onPostExecute(String file) {


            if(file.equals("1")) {
               /* try {
                    resta.CreateTable(4);
                    resta.restaurant.execSQL("update " + resta.Deliveryaddress + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "' ");
                    System.out.println("ToVALUES==" + str_storeid + " " + str_storename + " " + str_address);
                    Toast.makeText(SplashActivity.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    System.out.println("Urror==" + e.getMessage());
                }
*/
            }
            else {

            }
            //  cf.dismissTProgress();
            // pb.setVisibility(View.GONE);
        }

    }

}
//}
