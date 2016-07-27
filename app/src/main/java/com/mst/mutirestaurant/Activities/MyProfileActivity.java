package com.mst.mutirestaurant.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.TransparentProgressDialog;
import com.mst.mutirestaurant.support.getDb;

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

public class MyProfileActivity extends AppCompatActivity {
    String name = "", mob = "", email = "", str_mob = "";
    public static String str_result = "";
    CommonFunction cf;
    Button btnsubmit, edit,logout,cancel;
    ActionBar actionBar;
    private TransparentProgressDialog tpd;
    public static Context con;
    private String Api;
    public Dialog add_dialog;
    LinearLayout L1, L2, L3,L5;
    EditText etname, etphone, etemail;
    getDb gdb;
    DataBase db;
    TextView txtname, txtmum, txtemail;
    int show_handler = 0;
    String response = null;
    String str_message = "", str_name = "", str_phone = "", str_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
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
        gdb = new getDb(this);
        db = new DataBase(this);
        cf = new CommonFunction(this);
        str_mob = gdb.getMobile();
        txtname = ((TextView) findViewById(R.id.tvname));
        txtmum = ((TextView) findViewById(R.id.tvmob));
        txtemail = ((TextView) findViewById(R.id.tvemail));

        etname = ((EditText) findViewById(R.id.name));
        etphone = ((EditText) findViewById(R.id.mobilenum));
        etemail = ((EditText) findViewById(R.id.email));

        L2 = (LinearLayout) findViewById(R.id.len2);
        L2.setVisibility(View.GONE);
        L1 = (LinearLayout) findViewById(R.id.len1);
        L1.setVisibility(View.VISIBLE);
        L3 = (LinearLayout) findViewById(R.id.len3);
        L3.setVisibility(View.VISIBLE);
        L5 = (LinearLayout) findViewById(R.id.li5);
        L5.setVisibility(View.VISIBLE);

        if (cf.isInternetOn() == true) {

            new AsyncHttpTask().execute(Api);
        }

        logout = (Button) findViewById(R.id.btnlogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    cf.customizealert(MyProfileActivity.this, "Logout!!", "Do you want to logout ?");
                    Button ok = (Button) cf.add_dialog.findViewById(R.id.okbtn);
                    ok.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {

                            try {
                                db.CreateTable(1);

                                db.restaurant.execSQL("delete from " + db.Login);
                                Toast.makeText(MyProfileActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                MyProfileActivity.this.startActivity(intent);
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
            }
        });
        edit = (Button) findViewById(R.id.btn_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this,editBasicDetail.class));
                /*L1.setVisibility(View.GONE);
                L5.setVisibility(View.GONE);
                L2.setVisibility(View.VISIBLE);
                L3.setVisibility(View.GONE);
                L5.setVisibility(View.GONE);*/

            }
        });
        cancel = (Button) findViewById(R.id. cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyProfileActivity.this, MyProfileActivity.class));
            }
        });

        btnsubmit = (Button) findViewById(R.id.submit_btn);
        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add_dialog = new Dialog(MyProfileActivity.this, android.R.style.Theme_Translucent_NoTitleBar);

                add_dialog.setCancelable(false);
                add_dialog.getWindow().setContentView(R.layout.custom_alert);

                add_dialog.show();

                final TextView text1 = (TextView) add_dialog.findViewById(R.id.textView1);
                final TextView text2 = (TextView) add_dialog.findViewById(R.id.textView2);
                final TextView text3 = (TextView) add_dialog.findViewById(R.id.textView3);

                text1.setText("Edit Profile");

                text3.setText("Dou You Want To Save This Profile ?");

                Button cancel = (Button) add_dialog.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        add_dialog.dismiss();
                    }
                });

                Button ok = (Button) add_dialog.findViewById(R.id.okbtn);
                ok.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        try {

                            name = etname.getText().toString();
                            mob = etphone.getText().toString();
                            email = etemail.getText().toString();
                            System.out.println("GV=" + mob + "  " + name + " " + email);
                            if (cf.isInternetOn() == true) {
                                add_dialog.dismiss();
                                new UpdateHttpTask().execute(Api);
                            }

                        } catch (Exception e) {
                            System.out.println("insert value into records db" + e.getMessage());
                        }
                    }
                });


            }
        });


    }

    private void back() {
        Intent i = new Intent(MyProfileActivity.this, MainActivity.class);
        startActivity(i);
    }
    public class UpdateHttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Integer result = 0;
            // try {

            try {

                String API = (cf.url + cf.PHP_FILE_EDIT_PROFILE);

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobnum", mob));
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                 httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //Execute HTTP Post Request
                System.out.println("ListViewTask input value" + mob + "  " + name + " " + email);
                // Execute HTTP Post Request
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("Image response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success");
                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("failure");
                    str_message = json.getString("message");

                } else {
                    System.out.println("login success");



                    JSONArray data = json.getJSONArray("product");

                    /*for (int i = 0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);
                        str_name = json1.getString("proname");
                        str_email = json1.getString("emailid");
                        str_phone = json1.getString("mobnum");System.out.println("ed=="+str_name+" "+str_email+" "+str_phone);


                    }*/
               /* ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("Image response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success");
                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("failure");
                    str_message = json.getString("message");
                                        *//*show_handler = 2;
                                        handler.sendEmptyMessage(0);*//*
                } else {
                    System.out.println("login success");

                    JSONArray data = json.getJSONArray("product");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);
                        str_name = json1.getString("proname");
                        str_email = json1.getString("emailid");
                        str_phone = json1.getString("mobnum");System.out.println("ed=="+str_name+" "+str_email+" "+str_phone);


                    }*/


                }
               // Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch login error" + e.getMessage());
                return null;
            }
            return str_result;
        }

        protected void onPreExecute() {
            cf.showTProgress(MyProfileActivity.this,logout);
            // pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String str_result) {
            // Download complete. Lets update UI
            System.out.println("OnpostExecutive");
            if (str_result != null) {
               // if (Integer.parseInt(str_result) == 1) {

              //  } else {
                try {
                    db.CreateTable(2);
                    db.restaurant.execSQL("update " + db.Login + " set mobilenum ='" + mob + "'");
                    System.out.println("ToVALUES==" + mob);

                } catch (Exception e) {
                    System.out.println("Urror==" + e.getMessage());
                }
                    Toast.makeText(MyProfileActivity.this, "Sucessfuly Update Your Records", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MyProfileActivity.this, MyProfileActivity.class));

                //}
            }
            //Hide progressbar3
            cf.dismissTProgress();
            //  mProgressBar.setVisibility(View.GONE);
        }
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // Integer result = 0;
            // try {

            try {

                String API = (cf.url + cf.PHP_FILE_VIEWPROFILE);

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobnum", str_mob));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("Image response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success");
                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("failure");
                    str_message = json.getString("message");
                                        /*show_handler = 2;
                                        handler.sendEmptyMessage(0);*/
                } else {
                    System.out.println("login success");

                    JSONArray data = json.getJSONArray("product");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);
                        str_name = json1.getString("proname");
                        str_email = json1.getString("emailid");
                        str_phone = json1.getString("mobnum");


                    }


                }
                Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch login error" + e.getMessage());
                return null;
            }
            return str_result;
        }

        protected void onPreExecute() {
            cf.showTProgress(MyProfileActivity.this, logout);
            // pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String str_result) {
            // Download complete. Lets update UI
            System.out.println("OnpostExecutive");
            if (str_result != null) {
                if (Integer.parseInt(str_result) == 1) {

                    txtname.setText(str_name);
                    txtmum.setText(str_phone);
                    txtemail.setText(str_email);
                } else {
                    Toast.makeText(MyProfileActivity.this, "No Product Found!", Toast.LENGTH_LONG).show();
                }
            }
            //Hide progressbar3
            cf.dismissTProgress();
            //  mProgressBar.setVisibility(View.GONE);
        }
    }

   }

