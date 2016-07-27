package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.paymentModeAdapter;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.Confirmation;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.mst.mutirestaurant.support.getDb;
import com.mst.mutirestaurant.support.yourorder;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceOrderActivity extends AppCompatActivity {
   // ListView list;
    public static String prod_name = "", prod_rate = "",getaddress="", deli_address = "", prod_id = "",str_ad="", str_message = "", prod_qt = "", prod_qty = "", prod_total = "", str_deladress = "", str_userid = "", str_storeid = "";
    public static String[] arr_prodname = null, arr_prodrate = null, arr_ptotal = null, arr_prodid = null, arr_total = null, arr_qty = null;
    DataBase db;
    HashMap<String, String> map;
    public static ArrayList<HashMap<String, String>> cartArrayList = null;
    MyCustomAdaptermeetpoint adapter;
    public static TextView totalamt, deliveryadd, address;


    Button btn_check;
    public static int val = 0, show_handler = 0;
    CommonFunction cf;
    Double lat, lng;
    public Double distance;
    getDb gdb;
    Context con;
    int pos = 0;
    Confirmation ct;
    double currentlat;
    public static String TAG_CART_ID = "id";
    public static String TAG_CART_NAME = "name";
    public static String TAG_CART_PROTOTAL = "total";
    public static String TAG_CART_RATE = "rate";
    public static String TAG_CART_QUANTITY = "quantity";
    double currentlong;
    GPSTracker gps;
    TableLayout prodlist;
    Button placeorder;
    Bundle bundle;
    ViewPager mViewPager;
    LinearLayout ll_list;


    private FragmentStatePagerAdapter mSectionsRideAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if (savedInstanceState != null) {
            str_ad =getIntent().getExtras().getString("adress");

        }*/

        setContentView(R.layout.order);
         bundle = getIntent().getExtras();
         if(bundle!=null) {
             str_ad = bundle.getString("ad");
           }
        System.out.println("pNN==" + str_ad);
        //list = (ListView) findViewById(R.id.listorder);
        placeorder = (Button)findViewById(R.id.btn_confirm);
        ll_list =(LinearLayout)findViewById(R.id.ll_list);
       // tv_itemName= (TextView)findViewById(R.id.tv_itemName);
       // tv_itemRate= (TextView)findViewById(R.id.tv_itemRate);

        //  btn_check = (Button) findViewById(R.id.btn_pay);cc vv
        totalamt = (TextView) findViewById(R.id.tv_ProdTotal);
        deliveryadd = (TextView) findViewById(R.id.tv_delivery);
        address = (TextView) findViewById(R.id.tv_Address);
        prodlist=(TableLayout)findViewById(R.id.prodlists_table);


        db = new DataBase(this);
        cf = new CommonFunction(this);
        //  bk = new Booking(this);
        gdb = new getDb(this);
        ct = new Confirmation(this,placeorder);
        gps = new GPSTracker(this);

        mViewPager = (ViewPager) findViewById(R.id.vpager);
        mSectionsRideAdapter = new paymentModeAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsRideAdapter);

        //address.setText(cf.curloc);
        setadapterlistview();
        getcurrentlocation();
        str_userid = gdb.getUserid();
        str_deladress = gdb.getAddress();
        str_storeid = gdb.getStoreid();
        lat = gdb.getLattitude();
        lng = gdb.getLongitude();
        distance = SplashActivity.distance;

        deli_address = gdb.getAddress();
        getaddress = gdb.getDeliaddress();
        System.out.println("addd===" + deli_address+"  "+getaddress);

        deliveryadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PlaceOrderActivity.this, activity_Add_address.class));
            }
        });


        if(str_ad=="") {
            address.setText(deli_address);

        }
       else {
            address.setText(str_ad);
        }


        // Addarrayvalues();
        try {
            cartArrayList = new ArrayList<HashMap<String, String>>();
            db.CreateTable(4);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);

            val = c.getCount();
            System.out.println("FTcount==" + c.getCount());
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {

                    //  prod_total = "";
                    prod_name = "";
                    prod_rate = "";
                    prod_total = "";
                    prod_id = "";
                    prod_qty = "";
                    do {
                        prod_id = c.getString(c.getColumnIndex("prodid"));
                        prod_name = c.getString(c.getColumnIndex("prodname"));
                        prod_rate = c.getString(c.getColumnIndex("prodrate"));
                        prod_qty = c.getString(c.getColumnIndex("prodqty"));
                        prod_total = c.getString(c.getColumnIndex("prodtotal"));
                        System.out.println("Finaldb==" + prod_total + " " + prod_name + " " + prod_rate + " " + prod_id + " " + prod_qty);
                        map = new HashMap<String, String>();
                        map.put(TAG_CART_ID, prod_id);
                        map.put(TAG_CART_NAME, prod_name);
                        map.put(TAG_CART_PROTOTAL, prod_total);
                        map.put(TAG_CART_RATE, prod_rate);
                        map.put(TAG_CART_QUANTITY, prod_qty);
                        cartArrayList.add(map);
                        System.out.println("ToMAP=" + cartArrayList);
                    } while (c.moveToNext());

                }
                totalamt.setText("$: "+prod_total);

            }
        } catch (Exception e) {
            System.out.println("Fierroe==" + e.getMessage());
        }
        placeorder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String sd = address.getText().toString();

                cf.getlatitudelongitude(PlaceOrderActivity.this,sd);

                String radio = cf.radioselected;System.out.println("radio==" + radio);

                //Old
                ct.orderid(cartArrayList, str_userid, sd, radio, prod_total, String.valueOf(cf.latitude), String.valueOf(cf.longitudee), str_storeid, String.valueOf(distance));


                System.out.print("LL==" + sd + " " + cf.latitude + " " + cf.longitudee+" "+str_storeid);
            }

        });
        }



    private void getcurrentlocation() {
        if (gps.getLocation() != null) {
            currentlat = gps.getLatitude();
            System.out.println("H&U share loc meetingpoint lat" + currentlat);
            currentlong = gps.getLongitude();
            System.out.println("curr==" + currentlong);
            cf.setLocation(currentlat, currentlong, this);
            System.out.println("CurreLo==" + cf.curloc);
        } else {
            System.out.println("CurrentLoc1");
        }

    }


    private void setadapterlistview() {

        try {
            cartArrayList = new ArrayList<HashMap<String, String>>();
            db.CreateTable(4);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);
            // Cursor c = db.restaurant.rawQuery("select sum(prodrate) AS "TotalSalary" from " + db.Cart, null);

            // select id,dt,pool_name,pool_id,buy_price,(select sum(buy_price) from yourtable) total from yourtable

            System.out.println("cart count==" + c.getCount());
            val = c.getCount();
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    prod_name = "";
                    prod_rate = "";
                    prod_total = "";
                    prod_id = "";
                    // prod_total="";
                    prod_qt = "";
                    do {
                        prod_id += c.getString(c.getColumnIndex("prodid")) + "~";
                        prod_name += c.getString(c.getColumnIndex("prodname")) + "~";
                        prod_rate += c.getString(c.getColumnIndex("prodrate")) + "~";
                        // prod_ptotal += c.getString(c.getColumnIndex("total"))+ "~";
                        // prod_total += c.getString(c.getColumnIndex("prodtotal"))+ "~";
                        prod_qt += c.getString(c.getColumnIndex("prodqty")) + "~";
                        System.out.println("PlaceDb==" + prod_name + " " + prod_rate + " " + prod_total + " " + prod_qt + "  " + prod_id);

                    } while (c.moveToNext());


                }
                arr_prodname = prod_name.split("~");
                System.out.println("arr_prodname=" + arr_prodname);
                arr_prodid = prod_id.split("~");
                System.out.println("arr_prodid=" + arr_prodid);
                arr_prodrate = prod_rate.split("~");
                System.out.println("arr_prodrate=" + arr_prodrate);
                arr_qty = prod_qt.split("~");
                System.out.println("arr_qty=" + arr_qty);
               /* arr_total = prod_total.split("~");
                System.out.println("arr_total=" + arr_total);*/


                int sum = 0;


                pos = arr_prodid.length;
                /*System.out.println("p-" + pos);
                ArrayList<yourorder> friendsList = new ArrayList<yourorder>();
                for (int i = 0; i < pos; i++) {
                    yourorder friendneetpoint = new yourorder(arr_prodid[i], arr_prodname[i], arr_prodrate[i], arr_qty[i]);
                    friendsList.add(friendneetpoint);
                    System.out.println("PlaceLength==" + arr_prodid[i] + "  " + arr_prodname[i] + "  " + arr_prodrate[i] + "  " + arr_qty[i]);
                    adapter = new MyCustomAdaptermeetpoint(PlaceOrderActivity.this, R.layout.list_item_food, friendsList);
                    //cartArrayList.add(map);
                    //list.setAdapter(adapter);


                    tv_itemName.setText(arr_prodname[i]);
                    tv_itemRate.setText(arr_prodrate[i]);


                }*/
                try {
                    TableRow row;
                    TextView t1, t2,t3;
                    //Converting to dip unit
                    int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            (float) 1, getResources().getDisplayMetrics());
                    for (int i = 0; i < pos; i++) {
                        row = new TableRow(this);

                        t1 = new TextView(this);
                        Color.parseColor("#000000");
                        t1.setTextColor(Color.parseColor("#000000"));

                        t3 = new TextView(this);
                        Color.parseColor("#740d20");
                        t3.setTextColor(Color.parseColor("#740d20"));

                        t2 = new TextView(this);
                        Color.parseColor("#000000");
                        t2.setTextColor(Color.parseColor("#000000"));

                        t1.setText(arr_prodname[i]);
                        t3.setText(arr_prodrate[i]);
                        t2.setText(arr_qty[i]);

                        t1.setTypeface(null, 1);
                        t2.setTypeface(null, 1);
                        t3.setTypeface(null, 1);

                        t1.setTextSize(15);
                        t2.setTextSize(15);
                        t3.setTextSize(15);
                        t1.setSingleLine(true);
                        t3.setGravity(Gravity.RIGHT);
                        t1.setGravity(Gravity.START);
                        t2.setGravity(Gravity.CENTER);

                        t1.setWidth(220 * dip);
                        //t2.setWidth(50 * dip);
                        //t3.setWidth(20 * dip);

                        /*t1.setPadding(20 * dip, 10, 0, 0);
                        t2.setPadding(60 * dip, 10, 0, 0);
                        t3.setPadding(40 * dip, 10, 0, 0);*/


                        row.addView(t1);
                        row.addView(t2);
                        row.addView(t3);
                        prodlist.addView(row, new TableLayout.LayoutParams(
                                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

                    }
                }catch(Exception e){
                    System.out.println("Table=="+e.getMessage());
                }
                /*List<TextView> textList = new ArrayList<TextView>(pos);
                //textList.set(Gravity.LEFT,tv_itemName);

                for(int i = 0; i < pos; i++)
                {

                    TextView newTV = new TextView(getApplicationContext());
                    TextView newTV1 = new TextView(getApplicationContext());
                    newTV.setPadding(2, 2, 2, 2);
                    newTV.setSingleLine(true);

                    newTV.setEms(5);
                    newTV.setGravity(Gravity.LEFT);
                    newTV.setTextSize(16);
                    //tv_itemRate.setText(arr_prodrate[i]);
                    newTV.setText(arr_prodname[i] + "   "+arr_prodrate[i]);
                    newTV.setTextColor(Color.BLACK);
                    *//**** Any other text view setup code ****//*
                    ll_list.addView(newTV);
                    textList.add(newTV);
                    //textList.add(tv_itemRate);
                }*/
                //adapter = new CartAdapter(CartView.this, cartArrayList);
                // list.setAdapter(adapter);
            } else {

                Toast.makeText(PlaceOrderActivity.this, "Your Cart Is Empty", Toast.LENGTH_SHORT).show();
                /*totalamt.setVisibility(View.INVISIBLE);
                txtsar.setVisibility(View.INVISIBLE);
                btn_check.setVisibility(View.INVISIBLE);*/
                // startActivity(new Intent(CartView.this, CartView.class));
            }


        } catch (Exception e) {
            System.out.println("Error on db3" + e.getMessage());
        }
    }

    private class MyCustomAdaptermeetpoint extends ArrayAdapter<yourorder> {
        private ArrayList<yourorder> friendsList;
        private Context activity;

        public MyCustomAdaptermeetpoint(Context listtask, int list_item_food, ArrayList<yourorder> friendsList) {
            super(listtask, list_item_food, friendsList);
            activity = listtask;
            this.friendsList = new ArrayList<yourorder>();
            this.friendsList.addAll(friendsList);
        }

        private class ViewHolder {
            TextView tv_ProdName, tv_TotalPrice, tv_ProdRate, tv_Quanty;
            Button btn_plus, btn_minus;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.your_list_item_order, null);
                holder = new ViewHolder();

                holder.tv_ProdName = (TextView) convertView.findViewById(R.id.tv_ProdName); // Prod name
                holder.tv_ProdRate = (TextView) convertView.findViewById(R.id.tv_ProdRate); // veh ProdRate
                holder.tv_Quanty = (TextView) convertView.findViewById(R.id.tv_qty);

                // holder.img_prod = (ImageView) convertView.findViewById(R.id.img_prod); // veh image


                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            yourorder friends = friendsList.get(position);
            holder.tv_ProdName.setText(friends.getProdname());
            holder.tv_ProdRate.setText(friends.getProdrate());
            holder.tv_Quanty.setText(friends.getQty());
            holder.tv_ProdName.setTag(friends);
            holder.tv_ProdRate.setTag(friends);
            return convertView;

        }


    }

    @Override
    public void onBackPressed() {
        try
        {
            cf.customizealert(PlaceOrderActivity.this, "DELETE !!", "Do you want to delete your order details ?");
            Button ok = (Button) cf.add_dialog.findViewById(R.id.okbtn);
            ok.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    try {
                        db.CreateTable(1);

                        db.restaurant.execSQL("delete from " + db.Cart);
                        Toast.makeText(PlaceOrderActivity.this, "Deleted your order details", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlaceOrderActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PlaceOrderActivity.this.startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        System.out.println("insert value into records db" + e.getMessage());
                    }
                }
            });

        }

        catch(Exception e)
        {
            System.out.println("logouterror="+e.getMessage());
        }
       // startActivity(new Intent(PlaceOrderActivity.this, PlaceOrderActivity.class));
    }
}

