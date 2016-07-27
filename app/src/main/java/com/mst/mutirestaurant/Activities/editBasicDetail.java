package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.Custom_Textwatcher;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.ProfileUpdateTask;
import com.mst.mutirestaurant.support.VerificationSms;
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

/**
 * Created by pc on 10-Dec-15.
 */
public class editBasicDetail extends AppCompatActivity {
    DataBase db;
    public static CommonFunction cf;
    Context con;
    getDb gdb;
    public static ActionBar actionBar;
    String strfirst = "", strlast = "", strphone = "", stremail = "", strpassword = "", struserid = "";
    EditText et_cfname, et_clname, et_cphone;
    Button btn_change, btn_cancel;
    public static View popupView = null;
    public static PopupWindow popupWindow = null;
    public static String vmobile = "";
    VerificationSms vsms;
    public static String fname = "", lname = "", phone = "",email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbasicdetail);
        db = new DataBase(this);
        cf = new CommonFunction(this);
        vsms = new VerificationSms(this);
        con = editBasicDetail.this;
        gdb = new getDb(this);
        strfirst = gdb.getName();
        stremail = gdb.getEmail();
        strphone = gdb.getMobile();
        strpassword = gdb.getPassword();
        struserid = gdb.getUserid();System.out.println("Get=="+strfirst+" "+stremail+" "+strphone+" "+struserid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();


        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


        et_cfname = (EditText) findViewById(R.id.et_cfname);
        et_clname = (EditText) findViewById(R.id.et_clname);
        et_cphone = (EditText) findViewById(R.id.et_cphone);

        et_cfname.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_NAME,et_cfname));
        et_clname.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_EMAIL,et_clname));
        et_cphone.addTextChangedListener(new Custom_Textwatcher(this, cf.TAG_TXT_MOBILE,et_cphone));

        et_cfname.setText(strfirst);
        et_clname.setText(stremail);
        et_cphone.setText("+" + strphone);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cf.getDb(con);
                finish();
                startActivity(new Intent(con, MyProfileActivity.class));

            }
        });

        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = et_cfname.getText().toString();
                email = et_clname.getText().toString();
                phone = et_cphone.getText().toString();

                if (fname.equals("")) {
                    cf.showCustom_Toast(getApplicationContext(), getLayoutInflater(), "please enter First Name", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);

                } else if (email.equals("")) {

                    cf.showCustom_Toast(getApplicationContext(), getLayoutInflater(), "please enter Last Name", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);

                } else if (phone.equals("")) {

                    cf.showCustom_Toast(getApplicationContext(), getLayoutInflater(), "please enter phone number", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                } else {
                    //cf.showCustom_Toast(getApplicationContext(), getLayoutInflater(), "Success", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                   // new UpdateProfileTask().inputs(intent, add_dialog, context, php_UPDATE_PROFILE, "1", Str_userId, fname, lname, phone);
                    if (phone.startsWith("+")) {
                        phone = phone.substring(1);
                        System.out.println("EBD phone=" + phone);
                    }

                    if (phone.startsWith("0")) {
                        phone = phone.substring(1);
                        System.out.println("EBD phone=" + phone);
                    }

                    if (phone.equals(strphone)) {
                       // cf.showCustom_Toast(getApplicationContext(), getLayoutInflater(), "entered same phone number", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);

                        new ProfileUpdateTask(con).inputs(cf.PHP_FILE_EDIT_PROFILE, struserid, fname, email, phone);
                    } else {
                       // cf.showCustom_Toast(getApplicationContext(), getLayoutInflater(), "please enter new phone number", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                        verify_mobile vm = new verify_mobile(phone);
                        vm.execute();
                    }

                }
            }
        });

    }

    public void back() {
       // cf.getDb(con);
        finish();
        startActivity(new Intent(con, MyProfileActivity.class));
    }

   /* private void dbshow() {
        try {
            Cursor c = DataBase.restaurant.rawQuery("select * from " + db.Login + " where status = '1'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                //System.out.println(c.getString(c.getColumnIndex("firstname")));

                strfirst = c.getString(c.getColumnIndex("name"));
                System.out.println("firstname==" + strfirst);
               // strlast = c.getString(c.getColumnIndex("lastname"));
                strphone = c.getString(c.getColumnIndex("mobilenum"));
                stremail = c.getString(c.getColumnIndex("email"));
                strpassword = c.getString(c.getColumnIndex("password"));
                struserid = c.getString(c.getColumnIndex("userid"));


            }
        } catch (Exception e) {
            System.out.println("editBasicDetail DB" + e.getMessage());
        }
    }*/


    private class verify_mobile extends AsyncTask<Void, Void, String> {
        String mobile_veri;
        String response = null, str_result = "", str_failmessage = "", mobres = "";

        public verify_mobile(String phone) {
            mobile_veri = phone;
        }

        @Override
        protected void onPreExecute() {
            cf.show_ProgressDialog("");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                String API = cf.url + cf.PHP_FILE_CREATE_SMS_CODE;

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobilenum", mobile_veri));

                System.out.println("verifynumber input value" + mobile_veri);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("verifynumber response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success");
                System.out.println("verifynumber strresult" + str_result);

                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("verifynumber failure");
                    str_failmessage = json.getString("message");

                } else if (Integer.parseInt(str_result) == 1) {
                    System.out.println("verifynumber success");

                    JSONArray data = json.getJSONArray("product");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject json1 = data.getJSONObject(i);
                        mobres = json1.getString("result");
                        System.out.println("unicode" + mobres);
                    }
                }

                Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch verifynumber error" + e.getMessage());
                return null;
            }


            // TODO: register the new account here.
            return str_result;
        }

        @Override
        protected void onPostExecute(final String result) {


            if (result.equals("1")) {

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.alert_number_verify, null);
                final EditText et_verifycode = (EditText) popupView.findViewById(R.id.tv_verifycode);
                final Button fpok = (Button) popupView.findViewById(R.id.ok_btn);
                final Button btn_cancel = (Button) popupView.findViewById(R.id.cancel_btn);

                popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);


                if (cf.isInternetOn() == true) {
                    vsms.sendVerificationSms(mobile_veri, popupWindow, et_cphone, mobres);

                } else {
                    cf.showCustom_Toast(con, LayoutInflater.from(con), "Please enable Internet connection", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                }

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                fpok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str_vcode = et_verifycode.getText().toString();
                        System.out.println("getting input" + str_vcode);

                        if (cf.isInternetOn() == true) {

                            if (str_vcode.equals("")) {
                                cf.showCustom_Toast(con, LayoutInflater.from(con), "Please enter your verification code", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                            } else {

                                verify_mob_code vmc = new verify_mob_code(mobile_veri, str_vcode);
                                vmc.execute();
                            }
                        } else {
                            cf.showCustom_Toast(con, LayoutInflater.from(con), "Please enable Internet connection", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                        }
                    }
                });

            } else if (result.equals("null")) {
                cf.showCustom_Toast(con, LayoutInflater.from(con), "Oops Something went wrong..Please try after some time.", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                cf.pd.dismiss();
            } else {
                cf.showCustom_Toast(con, LayoutInflater.from(con), str_failmessage, R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                cf.pd.dismiss();
            }
        }
    }

    private class verify_mob_code extends AsyncTask<Void, Void, String> {
        String ver_mobile;
        String ver_code;
        String response = null, str_result = "", str_message = "";

        public verify_mob_code(String mobile_veri, String str_vcode) {
            ver_mobile = mobile_veri;
            ver_code = str_vcode;
        }

        @Override
        protected void onPreExecute() {

            cf.show_ProgressDialog("");
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                String API = cf.url + cf.PHP_FILE_VERIFY_CODE;

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobileno", ver_mobile));
                nameValuePairs.add(new BasicNameValuePair("code", ver_code));

                System.out.println("verifycode input value" + ver_mobile + " " + ver_code);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("verifycode response" + response.toString());

                JSONObject json = new JSONObject(response);
                str_result = json.getString("success");

                if (Integer.parseInt(str_result) == 0) {
                    System.out.println("verifycode failure");
                    str_message = json.getString("message");

                } else if (Integer.parseInt(str_result) == 1) {
                    System.out.println("verifycode success");
                }

                Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch verifycode error" + e.getMessage());
                return null;
            }

            // TODO: register the new account here.
            return str_result;
        }

        @Override
        protected void onPostExecute(final String result) {


            if (result.equals("1")) {
                cf.DisplayToastMesage(editBasicDetail.this, "Successfully verified");
                popupWindow.dismiss();
                if (cf.isInternetOn() == true) {
                    cf.DisplayToastMesage(editBasicDetail.this, "ready to update");
                    new ProfileUpdateTask(con).inputs(cf.PHP_FILE_EDIT_PROFILE,struserid, fname, lname, phone);
                } else {
                    cf.DisplayToastMesage(editBasicDetail.this, "Please enable Internet connection");
                }

            } else if (result.equals("null")) {

                cf.DisplayToastMesage(editBasicDetail.this, "Something went wrong");
                cf.pd.dismiss();
            } else {

                cf.DisplayToastMesage(editBasicDetail.this, str_message);
                cf.pd.dismiss();
            }
        }
    }
}
