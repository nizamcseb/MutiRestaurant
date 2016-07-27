package com.mst.mutirestaurant.Activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.mst.mutirestaurant.support.JSONParser;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectLoactionActivity extends AppCompatActivity {
    Spinner spinner ;
    public Context con;
    CommonFunction cf;
    Button btnlocation,gpsbtn;
    DataBase db;
    String[] search_text;
    String url;
    ArrayAdapter<String> adp;
    JSONObject json;
    JSONArray contacts = null;
    ArrayList<String> names;
    String browserKey="AIzaSyA1ygXmQ12RIopgji0IrNfMNWQslXuPpiA";
   // public static  EditText addre;
    public static AutoCompleteTextView addre;
    GPSTracker gps;
    public static TextView flatno,landmark,cityaddress,villaname;
    Double latitude;
    Double longitudee;
    double currentlat;
    double currentlong;
    EditText streetaddre;
    ActionBar actionBar;
    public String address="",citysa="",sendaddress="";
    public static String curloc = "";
    public static String setaddress="";
    public static String sublocality="";
    public static String CityName="";
    public static String StateName="";
    public static String etlocation="";
    public static String CountryName="";
    public static String flat="",villa="",land="",fulladdress="";
    public static String longitude="";
    private static final String TAG_RESULT = "predictions";
    public static final String TAG_address = "delivery_address";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_loaction);
        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        cf= new CommonFunction(this);
        db = new DataBase(this);
       // spinner = (Spinner) findViewById(R.id.spinnercity);
       // addre = (EditText)findViewById((R.id.edtxtaddress));
        addre = (AutoCompleteTextView)findViewById((R.id.edtxtaddres));
        streetaddre = (EditText)findViewById((R.id.edtxtaddress));
        villaname = (EditText)findViewById(R.id.villaname);
        flatno = (EditText)findViewById(R.id.flatname);
        landmark = (EditText)findViewById(R.id.landmark);
        cityaddress  =(TextView)findViewById(R.id.txtriyd);
        cityaddress.setText(cf.curcity);
        gps = new GPSTracker(this);
        btnlocation = (Button) findViewById(R.id.btnsave);
        gpsbtn = (Button)findViewById(R.id.btngps);

       // spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        addre.setThreshold(0);
        names=new ArrayList<String>();
        addre.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                search_text = addre.getText().toString().split(",");
                System.out.println("SS==" + search_text);
                String stru = "";
                try {
                    stru = URLEncoder.encode(search_text[0], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + stru + "&components=country:in&radius=500&sensor=true&key=" + browserKey;
                System.out.println("urll==" + url);
                if (search_text.length <= 1) {
                    names = new ArrayList<String>();
                    //  Log.d("URL", url);
                    paserdata parse = new paserdata();
                    parse.execute();
                }

            }
        });

       // citysa = String.valueOf(spinner.getSelectedItem());
        getcurrentlocation();

        gpsbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                try {
                    getlatitudelongitude();
                    addre.setText(curloc);

                } catch (Exception e) {
                    System.out.println("insert value into records db" + e.getMessage());
                }
            }
        });


        getlatitudelongitude();



        btnlocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getlatitudelongitude();
                String ad = streetaddre.getText().toString();System.out.println("--"+ad);
                if (ad.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Deliver Address", Toast.LENGTH_SHORT).show();
                } else {
                    cf.customizealert(SelectLoactionActivity.this, "Alert", "Do You Want To Save This Address as Default ?");
                    Button ok = (Button) cf.add_dialog.findViewById(R.id.okbtn);
                    ok.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            address = streetaddre.getText().toString();
                            flat = flatno.getText().toString();
                            villa = villaname.getText().toString();
                            land = landmark.getText().toString();
                          String city =   cityaddress.getText().toString();
                            fulladdress = city+","+" "+"Flat No:"+flat+","+" "+"Villa Name:"+villa+","+" "+"Landmark:"+land;
                            System.out.println("Input-->" + fulladdress);
                            /*try {

                                db.CreateTable(3);
                                db.restaurant.execSQL("update into" + db.Deliveryaddress + " set address ='" + fulladdress + "',city= '" + city + "' ");
                                System.out.println("UPADDRESS==" + fulladdress);
                                Intent i = new Intent(SelectLoactionActivity.this, PlaceOrderActivity.class);
                                i.putExtra(TAG_address, address);
                                startActivity(i);
                            } catch (Exception e) {
                                System.out.println("ListUpdate==" + e.getMessage());
                            }*/
                            try {


                                db.CreateTable(3);
                                db.restaurant.execSQL("INSERT INTO "
                                        + db.Deliveryaddress
                                        + "(city,address,status)"
                                        + "VALUES ('" +city+"' ,'" + fulladdress + "','1')");

                                Intent i = new Intent(SelectLoactionActivity.this, PlaceOrderActivity.class);
                                i.putExtra(TAG_address, address);
                                startActivity(i);
                            } catch (Exception e) {
                                System.out.println("insert value into records db" + e.getMessage());
                            }
                        }
                    });


                }
            }

        });


        //addre.setText(curloc);
       // sendaddress = addre.getText().toString();
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void back() {
        Intent i = new Intent(SelectLoactionActivity.this, activity_Add_address.class);
        startActivity(i);
    }

    private void getlatitudelongitude() {

        // TODO Auto-generated method stub
        String addressStr = addre.getText().toString();

        Geocoder geoCoder = new Geocoder(this);

        if(addressStr.contains("Tiruchirappalli") || addressStr.contains("Trichy"))
        {
            // cf.checkCity=0;
            addre.setBackgroundColor(Color.WHITE);

            try {
                List<Address> addresses =geoCoder.getFromLocationName(addressStr, 1);
                if (addresses.size() >  0) { System.out.println("Addresses"+addresses);
                    double lati = addresses.get(0).getLatitude(); System.out.println("zoom"+latitude);
                    double longi =addresses.get(0).getLongitude();
                    LatLng latlng = new LatLng(lati, longi);
                    latitude=latlng.latitude;
                    longitudee=latlng.longitude;System.out.println("zoom=="+latitude+"  "+longitudee);

                }
            } catch (IOException e) { // TODO Auto-generated catch blocks
                System.out.println("addresses"+e.getMessage());
                e.printStackTrace();
            }

        }
        else {
            // cf.checkCity=1;
           // Toast.makeText(SelectLoactionActivity.this, " We are processing only in Trichy City ",Toast.LENGTH_SHORT).show();
            // addre.setBackgroundColor(Color.RED);
        }

    }

    public class paserdata extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

          System.out.println("ININI");

                JSONParser jParser = new JSONParser();

            // getting JSON string from URL
            json = jParser.getJSONFromUrl(url.toString());
            if(json !=null)
            {
                try {
                    // Getting Array of Contacts
                    contacts = json.getJSONArray(TAG_RESULT);System.out.println("INPP=="+contacts);

                    for(int i = 0; i < contacts.length(); i++){
                        JSONObject c = contacts.getJSONObject(i);System.out.println("LEE=="+contacts.length());
                        String description = c.getString("description");
                        //Log.d("description", description);
                        names.add(description);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            adp = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, names) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);
                    return view;
                }
            };
            addre.setAdapter(adp);
        }


    }




    private void getcurrentlocation() {
        if(gps.getLocation()!=null)
        {
            currentlat = gps.getLatitude(); System.out.println("H&U share loc meetingpoint lat"+currentlat);
            currentlong = gps.getLongitude();
            System.out.println("H&U share loc meetingpoint long"+currentlong);
            setLocation(currentlat, currentlong, this);
            System.out.println("CurrentLoc=="+ curloc);
        }
        else
        {
            System.out.println("CurrentLoc1");
        }

    }

    private void setLocation(Double currentlat, Double currentlong, Context con) {


        // TODO Auto-generated method stub
        try
        { System.out.println("tyrgp="+currentlat);
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



}
