package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.squareup.picasso.Picasso;

//import com.blyncsolutions.vroom.SupportClass.ConfirmBooking;


public class SelecteditemActivity extends AppCompatActivity {

    String str_prodname = "", str_proid = "", str_prodesc = "", str_prodrate = "", str_imageurl = "";
    public String [] arr_pname=null,arr_pdesc=null,arr_rate=null,arr_pid=null,arr_img=null;
    ImageView imageView;
    TextView tv_charge, tv_depcharge, tv_likes,tv_desc;
    CommonFunction cf;
    String response = "", str_result = "", str_message = "";
    GPSTracker gps;

    Context con;
    //ConfirmBooking cb;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            str_prodname =getIntent().getExtras().getString(cf.TAG_prodname);
            str_prodesc =getIntent().getExtras().getString(cf.TAG_proddesc);
            str_prodrate =getIntent().getExtras().getString(cf.TAG_price);
            str_proid =getIntent().getExtras().getString(cf.TAG_prodid);
            str_imageurl =getIntent().getExtras().getString(cf.TAG_imageurl);
           } System.out.println("pN=="+str_prodname+" "+str_proid);

        setContentView(R.layout.activity_selecteditem);

        cf = new CommonFunction(this);
        gps = new GPSTracker(this);
        db = new DataBase(this);
        //cb = new ConfirmBooking(this);


        tv_charge = (TextView) findViewById(R.id.tv_price);
        tv_depcharge = (TextView) findViewById(R.id.tv_depcharge);
        //tv_likes = (TextView) findViewById(R.id.tv_likes);
        imageView = (ImageView) findViewById(R.id.imageView);
        tv_desc = (TextView)findViewById(R.id.txt_desc);
        // ViewCompat.setTransitionName(findViewById(R.id.app_bar),str_imageurl);

        tv_charge.setText( str_prodrate +" "+ "$");
        tv_depcharge.setText(str_prodname);
       // tv_likes.setText(str_proid);
        tv_desc.setText(str_prodesc);


        Picasso.with(SelecteditemActivity.this).load(str_imageurl).into(imageView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(str_prodname);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));
       // collapsingToolbarLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        // System.out.println("name===" + str_Vname);
        //toolbar.setTitle(str_Vname);

       /* cf.SharedPreference();
        cf.STR_FROMDATE = cf.sh_pref.getString(cf.TAG_STR_FROMDATE, "");
        cf.STR_FROMTIME = cf.sh_pref.getString(cf.TAG_STR_FROMTIME, "");
        cf.STR_TODATE = cf.sh_pref.getString(cf.TAG_STR_TODATE, "");
        cf.STR_TOTIME = cf.sh_pref.getString(cf.TAG_STR_TOTIME, "");*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                db.CreateTable(4);
                Cursor c = db.restaurant.rawQuery("select * from " + db.Cart + " where prodid = '" + str_proid + "'", null);
                System.out.println("car==>" + c.getCount());
                if (c.getCount() == 0) {

                    db.CreateTable(4);
                    db.restaurant.execSQL("INSERT INTO "
                            + db.Cart
                            + "(prodid,prodname,prodrate,produrl,prodqty)"
                            + "VALUES ('" + str_proid + "', '" + str_prodname + "','" + str_prodrate + "','" + str_imageurl + "','1')");

                  //  ((Tab_Activity)getApplicationContext()).doIncrease();

                    Toast.makeText(SelecteditemActivity.this, "Product is addred to cart :" + str_prodname, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SelecteditemActivity.this, "This Product Is Already To Your Cart", Toast.LENGTH_LONG).show();
                }





            }
        });
        //fab.setRippleColor(getColor(android.R.color.white));
        //fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));

    }

    /*@Override
    public void onBackPressed() {
        startActivity(new Intent(SelecteditemActivity.this, MainActivity.class));
    }*/
}