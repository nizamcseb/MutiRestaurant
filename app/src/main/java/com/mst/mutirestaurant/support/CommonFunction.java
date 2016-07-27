package com.mst.mutirestaurant.support;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ha on 10/21/15 AD.
 */
public class CommonFunction {
    static Context mcon;
    public static String url = "http://52.27.125.60/apps/api/Multi_restaurant/test/";
    //public static String url = "http://nsamsoft.com/apps/api/Multi_restaurant/test/";
    //public static String url = "http://10.0.0.9/dashboard/mst/apps/Multi_restaurant/test/";
    public static String PHP_FILE_Login = "login.php";
    public static String PHP_FILE_SignUp = "registration.php";
    public static String PHP_FILE_Bulk_Image = "bulkimage.php";
    public static String PHP_FILE_TAB_PRODUCT = "category.php";
    public static String PHP_FILE_NEAREST_PLACE = "distance.php";
    public static String PHP_FILE_FULL_STORE_PLACE = "store_address.php";
    public static String PHP_FILE_SEARCH_PRODUCT = "searchproduct.php";
    public static String PHP_FILE_ORDER_ID = "orderid.php";
    public static String PHP_FILE_BOOKINGS = "booking.php";
    public static String PHP_FILE_MYORDERS ="myorder_list.php";
    public static String PHP_FILE_MYORDERSCONTENT ="indival_order_list.php";
    public static String PHP_FILE_VIEWPROFILE ="profile.php";
    public static String PHP_FILE_EDIT_PROFILE ="edit_profile.php";
    public static String PHP_FILE_VERIFY_CODE ="verify_code.php";
    public static String PHP_FILE_CREATE_SMS_CODE ="create_sms_code.php";
    public static String PHP_FILE_ENCODE_SMS_CODE ="encode_sms.php";
    public static String PHP_FILE_FORGOT_PASSWORD ="forgot_password.php";
   // public static String php_SIGNUP_CREATE_SMS_VERFICATION_CODE = "create_sms_code.php";

    //public static String php_SMS = "http://www.mobily.ws/api/msgSend.php";
    public static String php_SMS = "http://www.mobily.ws/api";
    public String STR_STOREID = "", STR_Deliaddress = "";
    public static final String TAG_address = "delivery_address";
    public static final String TAG_storeid = "store_id";
    public static String TAG_prodname = "prod_name";
    public static String TAG_caid = "category_id";
    public static String TAG_proddesc = "prod_desc";
    public static String TAG_price = "prod_price";
    public static String TAG_imageurl = "prod_image";
    public static String TAG_prodid = "prod_id";
    public static String curloc = "";
    public static String curcity = "";
    public static String setaddress="";
    public static String sublocality="";
    public static String CityName="";
    public static String StateName="";
    public static ProgressDialog pd;
    public static String etlocation="";
    public static String CountryName="";
    public static String lattitude="";
    public static String longitude="";
    public static final long EARTH_RADIUS = 6371000;
    public static TransparentProgressDialog tp;
    public static DataBase db;
    public Dialog add_dialog;
    public static String adress = "address";
    public static String storerid="";
    public static  String radioselected="";
    public static final String TAG_category = "food_category";
    public static int chk_login = 0;
    public static String TAG_TXT_NAME = "name";
    public static String TAG_TXT_EMAIL = "email";
    public static String TAG_TXT_MOBILE = "mobile";
    public static String TAG_TXT_PASSWORD = "pass";
    public static String ccode="";
    GPSTracker gps;
    public static double currentlat;
    double currentlong;
    public static String addressStr="";
    public static Double latitude;
    public static Double longitudee;
    public static PopupWindow popupWindow;
    public CommonFunction(Context con) {
        this.mcon = con;
        db = new DataBase(this.mcon);
        gps = new GPSTracker(this.mcon);
    }

    public static boolean isInternetOn() {
        boolean chk = false;
        ConnectivityManager conMgr = (ConnectivityManager) mcon.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            chk = true;
        } else {
            chk = false;
        }
        return chk;
    }
    public static void showCustom_Toast(Context c, LayoutInflater inflater, String msg, int img, int duration, int x, int y) {


        //Toast.makeText(c, msg, duration).show();

        //inflater = inf;
        View toastRoot = inflater.inflate(R.layout.toast, null);

        TextView tv_msg = (TextView) toastRoot.findViewById(R.id.toasttv);
        ImageView img_iv = (ImageView) toastRoot.findViewById(R.id.toast_img);

        img_iv.setImageResource(img);
        tv_msg.setText(msg);
        Toast toast = new Toast(c);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, x, y);
        toast.setDuration(duration);
        toast.show();
    }
    public boolean eMailValidation(CharSequence stringemailaddress1) {
        // TODO Auto-generated method stub

        //final String EMAIL_PATTERN = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";

        final String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(stringemailaddress1);

        return matcher.matches();

    }
    public static boolean isGpsOn() {
        boolean gps = false;
        LocationManager manager = (LocationManager) mcon.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            gps = false;
        } else {
            gps = true;
        }
        return gps;
    }

    public static boolean isLogin() {
        boolean log_condition = false;
        db.CreateTable(2);
        Cursor c = db.restaurant.rawQuery("select * from " + db.Login + " where status ='1' ", null);
        if (c.getCount() > 0) {
            log_condition = true;
        } else {
            log_condition = false;
        }
        return log_condition;
    }
    public static void setLocation(Double currentlat, Double currentlong, Context con) {

        // TODO Auto-generated method stub
        try
        { System.out.println("tyrgp="+currentlat);
            //Getting address from found locations.
            if(isInternetOn() == true){
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
                curcity = CityName;
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
    public void DisplayToastMesage(Context mcon2, String string) {
        // TODO Auto-generated method stub
        Toast.makeText(mcon2, string, Toast.LENGTH_LONG).show();
    }
   /* try {
        db.restaurant.execSQL("delete from " + db.Cart);
    } catch(Exception e) { System.out.println("deleted table"+e.getMessage());}*/
   /* public void getaddressDb() {

        try {

            db.CreateTable(3);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Deliveryaddress + " where status = '1' ORDER BY lid DESC", null);
            System.out.println("countdb==" + c.getCount());
            if (c.getCount() > 0) {
                c.moveToFirst();
                city = c.getString(c.getColumnIndex("city"));
                address = c.getString(c.getColumnIndex("address"));

                System.out.println("Getting local db" + city + " " + " " + address);
            }
        } catch (Exception e) {
            System.out.println("Getting details" + e.getMessage());
        }
    }
*/


    public static double findDistance(LatLng latLng1, LatLng latLng2) {

        // http://www.ig.utexas.edu/outreach/googleearth/latlong.html

  /*
   * Should convert all the values to radians before find any
   * sine,cosine,tangent values
   */



        double distance = 0;
        double diff_lat = latLng2.latitude - latLng1.latitude;
        double diff_lng = latLng2.longitude - latLng1.longitude;


        // Haversine formula:
        // a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
        // c = 2.atan2(√a, √(1−a))
        // d = R.c

        double sineValue1 = Math.sin(Math.toRadians(diff_lat / 2)) * Math.sin(Math.toRadians(diff_lat / 2));
        double sineValue2 = Math.sin(Math.toRadians(diff_lng / 2)) * Math.sin(Math.toRadians(diff_lng / 2));

        double a = sineValue1 + Math.cos(Math.toRadians(latLng1.latitude)) * Math.cos(Math.toRadians(latLng2.latitude)) * sineValue2;

        double c = 2 * Math.atan2(Math.toRadians(Math.sqrt(a)), Math.toRadians(Math.sqrt(1 - a)));

        distance = Math.round(EARTH_RADIUS * c);

        return distance;
    }
    public static void show_ProgressDialog(String string) {
        // TODO Auto-generated method stub

        String source = "<b><font color=#ffffff>" + string + " Please wait...</font></b>";

        pd = ProgressDialog.show(mcon, "", Html.fromHtml(source), true);


    }
    public static void showTProgress(Context context, View view) {

        // LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        tp = new TransparentProgressDialog(context, R.drawable.loading_green_pb, 200, 200);
        tp.show();

       /* if (context != null && view != null) {
            LayoutInflater layoutInflater_pay = LayoutInflater.from(context);
            View popupView = layoutInflater_pay.inflate(R.layout.custom_progres_gif, null);
            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setTouchable(true);


    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            //popupWindow.showAsDropDown();

        }*/
    }

    public static void dismissTProgress() {

        // LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (tp != null) {
            tp.dismiss();
        }

        /*if (popupWindow != null) {
            popupWindow.dismiss();
        }
*/
    }
    public void getcurrentlocation() {
        if(gps.getLocation()!=null)
        {
            currentlat = gps.getLatitude(); System.out.println("H&U share loc meetingpoint lat"+currentlat);
            currentlong = gps.getLongitude();
            System.out.println("H&U share loc meetingpoint long"+currentlong);
            setLocation(currentlat, currentlong, this.mcon);
            System.out.println("CurrentLoc=="+ curloc);
        }
        else
        {
            System.out.println("CurrentLoc1");
        }

    }
    public void getlatitudelongitude(Context con, String address) {

        // TODO Auto-generated method stub
       // String addressStr = addre.getText().toString();

        Geocoder geoCoder = new Geocoder(mcon);

       // if(addressStr.contains("Tiruchirappalli") || addressStr.contains("Trichy"))
      //  {
            // cf.checkCity=0;
           // addre.setBackgroundColor(Color.WHITE);

            try {
                List<Address> addresses =geoCoder.getFromLocationName(address, 1);
                if (addresses.size() >  0) { System.out.println("Addresses"+addresses);
                    double lati = addresses.get(0).getLatitude(); System.out.println("zoom"+latitude);
                    double longi =addresses.get(0).getLongitude();
                    LatLng latlng = new LatLng(lati, longi);
                    latitude=latlng.latitude;
                    longitudee=latlng.longitude;System.out.println("zoom=="+latitude+"  "+longitudee);

                }
                else {
                    latitude =0.0;
                    longitudee=0.0;
                }
            } catch (IOException e) { // TODO Auto-generated catch blocks
                System.out.println("addresses"+e.getMessage());
                e.printStackTrace();
            }

       // }
        //else {
            // cf.checkCity=1;
            // Toast.makeText(SelectLoactionActivity.this, " We are processing only in Trichy City ",Toast.LENGTH_SHORT).show();
            // addre.setBackgroundColor(Color.RED);
       // }

    }
     public void customizealert(Context con ,String title,String msg) {
       add_dialog = new Dialog(con, android.R.style.Theme_Translucent_NoTitleBar);

        add_dialog.setCancelable(false);
        add_dialog.getWindow().setContentView(R.layout.custom_alert);

        add_dialog.show();

        final TextView text1 = (TextView) add_dialog.findViewById(R.id.textView1);
        final TextView text2 = (TextView) add_dialog.findViewById(R.id.textView2);
        final TextView text3 = (TextView) add_dialog.findViewById(R.id.textView3);

        text1.setText(title);

        text3.setText(msg);

        Button cancel = (Button) add_dialog.findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                add_dialog.dismiss();
            }
        });


    /*    Button ok = (Button) add_dialog.findViewById(R.id.okbtn);
        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                adress = SelectLoactionActivity.addre.getText().toString();
                storeaddr = SelectLoactionActivity.nearstore.getText().toString();
                System.out.println("Input-->" + adress + " " + citysa);
                try {
                    db.CreateTable(3);
                    db.restaurant.execSQL("INSERT INTO "
                            + db.Deliveryaddress
                            + "(city,address,status)"
                            + "VALUES ('" + citysa + "', '" + address + "','" + 1 + "')");
                    Toast.makeText(mcon, "Successfully Address Saved" + address + " " + citysa, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SelectLoactionActivity.this, MainActivity.class));
                    Intent i = new Intent(SelectLoactionActivity.this, MainActivity.class);
                    i.putExtra(TAG_address, address);
                    startActivity(i);
                } catch (Exception e) {
                    System.out.println("insert value into records db" + e.getMessage());
                }
            }
        });*/
    }
}
