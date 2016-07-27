package com.mst.mutirestaurant.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;

import java.util.concurrent.TimeUnit;

public class OrderStatucActivity extends AppCompatActivity {
    CommonFunction cf;
    Context con;
    Button btntrack;
    ActionBar actionBar;
    ImageView img, img1, img2;
    LinearLayout ltrack;
    TextView orderid, deladdress, textViewTime,msg;
    public static String str_orderid = "", str_address = "", str_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            str_orderid = b.getString("orderID");
            str_address = b.getString("deliveryaddress");
            str_status = b.getString("statuscode");
        } else {
        }

        System.out.println("oo==" + str_orderid + " " + str_status);
        setContentView(R.layout.view_top);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        img = (ImageView) findViewById(R.id.tv_charge);
        img1 = (ImageView) findViewById(R.id.tv_depcharge);
        img2 = (ImageView) findViewById(R.id.tv_off);
        ltrack = (LinearLayout)findViewById(R.id.LTRACK);

        orderid = (TextView) findViewById(R.id.orderid);
        deladdress = (TextView) findViewById(R.id.address_list);
        orderid.setText(str_orderid);
        orderid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(OrderStatucActivity.this, MyOrderContentActivity.class);
                in.putExtra("orderID", str_orderid);

                startActivity(in);
            }
        });
        msg = (TextView)findViewById(R.id.tv_msg);
        btntrack = (Button)findViewById(R.id.btn_confirm);
        deladdress.setText(str_address);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTime.setText("03:00" + " " + "Minutes");
        /*final CounterClass timer = new CounterClass(180000,1000);
        timer.start();*/
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                // textViewTime.setText(String.valueOf(millisUntilFinished / 1000));
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                System.out.println("HMS==" + hms);
                textViewTime.setText(hms);
            }

            public void onFinish() {
                textViewTime.setText("Driver Came!");
            }
        }.start();

        if (str_status.equals("0")) {
            img.setBackgroundResource(R.mipmap.ic_orange);
            img1.setBackgroundResource(R.mipmap.ic_green_border);
            img2.setBackgroundResource(R.mipmap.ic_red_border);
        }else if(str_status.equals("1")){
            img.setBackgroundResource(R.mipmap.ic_orange_border);
            img1.setBackgroundResource(R.mipmap.ic_green);
            img2.setBackgroundResource(R.mipmap.ic_red_border);
        }else if(str_status.equals("2") || str_status.equals("3") ){
            img1.setBackgroundResource(R.mipmap.ic_orange_border);
            img.setBackgroundResource(R.mipmap.ic_green_border);
            img2.setBackgroundResource(R.mipmap.ic_red);
            btntrack.setVisibility(View.VISIBLE);
        }

        else{
            img.setBackgroundResource(R.mipmap.ic_orange_border);
            img1.setBackgroundResource(R.mipmap.ic_green_border);
            img2.setBackgroundResource(R.mipmap.ic_red_border);
            msg.setVisibility(View.VISIBLE);
            ltrack.setVisibility(View.GONE);
            btntrack.setVisibility(View.GONE);
        }
    }





    private void back() {
        Intent i = new Intent(OrderStatucActivity.this, MyorderActivity.class);
        startActivity(i);
        }

   /* @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); }
        @Override public void onFinish() {
            textViewTime.setText("Completed."); }
        @SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            System.out.println("HMS=="+hms);
            textViewTime.setText(hms);
        }
    }
*/

}
