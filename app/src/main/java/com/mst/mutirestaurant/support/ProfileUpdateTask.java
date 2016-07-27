package com.mst.mutirestaurant.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.MyProfileActivity;
import com.mst.mutirestaurant.R;

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
 * Created by pc on 11-Dec-15.
 */
public class ProfileUpdateTask {
    Context con;
    CommonFunction cf;
    DataBase db;
    String struserid = "", str_result = "65", str_message = "";
    String res_str_fname = "", res_str_lname = "", res_str_phone = "", res_str_email = "", res_str_pass = "";

    public ProfileUpdateTask(Context c) {
        this.con = c;
        cf = new CommonFunction(this.con);
        db = new DataBase(this.con);

    }

    public void inputs(String url, String struserid, String input1, String input2, String input3) {
        this.struserid = struserid;
        try {
            System.out.println("profile update task inputs" + url + " " +  struserid + " " + input1 + " " + input2 + " " + input3);
            if (struserid != null) {
                new update_profile().execute(url,struserid, input1, input2, input3);
            } else {
                CommonFunction.showCustom_Toast(con, LayoutInflater.from(con), "Oops Something went wrong..Please try after some time.", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
            }
        } catch (Exception e) {
            System.out.println("profile update task input error" + e.getMessage());
            e.printStackTrace();
        }
    }

    class update_profile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            if (CommonFunction.isInternetOn() == true) {

                try {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(CommonFunction.url + params[0]);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();



                        nameValuePairs.add(new BasicNameValuePair("userid", params[2]));
                        nameValuePairs.add(new BasicNameValuePair("name", params[3]));
                        nameValuePairs.add(new BasicNameValuePair("email", params[4]));
                        nameValuePairs.add(new BasicNameValuePair("phone", params[5]));

                        System.out.println("update_task input" + params[0] + "api=" + params[1] + "userid=" + params[2] + "fname=" + params[3] + "lname=" + params[4] + "phone=" + params[5]);




                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response = httpclient.execute(httppost, responseHandler);
                    System.out.println("update_task reponse=" + response);
                    JSONObject json = new JSONObject(response);
                    str_result = json.getString("success");


                    if (Integer.parseInt(str_result) == 0) {
                        System.out.println("failure");
                        str_message = json.getString("message");

                    } else if (Integer.parseInt(str_result) == 1) {
                        System.out.println("success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json1 = data.getJSONObject(i);

                            //res_str_fname="",res_str_lname="",res_str_phone="",res_str_email="",res_str_pass="";
                            res_str_fname = json1.getString("proname");
                            //res_str_lname = json1.getString("lname");
                            res_str_phone = json1.getString("mobnum");
                            res_str_email = json1.getString("email");
                            res_str_pass = json1.getString("pass");
                            str_message = json.getString("message");

                            //deviceId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                        }
                    }


                } catch (Exception e) {


                }


            } else {

                str_result = "999";
            }
            return str_result;
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here

            CommonFunction.show_ProgressDialog("Processing..");
        }

        @Override
        protected void onPostExecute(String result) {

            CommonFunction.pd.dismiss();

            if (Integer.parseInt(result) == 1) {
                try {

                    db.CreateTable(2);
                    DataBase.restaurant.execSQL("UPDATE " + DataBase.Login
                            + " set firstname='" + res_str_fname + "',lastname='" + res_str_lname + "',phonenum='" + res_str_phone + "',email='" + res_str_email + "',password='" + res_str_pass + "' where  userid='" + struserid + "'");
                    System.out.println("update_task UPDATE " + DataBase.Login
                            + " set firstname='" + res_str_fname + "',lastname='" + res_str_lname + "',phonenum='" + res_str_phone + "',email='" + res_str_email + "',password='" + res_str_pass + "' where  userid='" + struserid + "'");
                    //Toast.makeText(c, "Successfully updated", Toast.LENGTH_SHORT).show();
                    //clear();

                   // CommonFunction.showCustom_Toast(con, LayoutInflater.from(con), "Changed successfully..", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);


                    con.startActivity(new Intent(con, MyProfileActivity.class));
                    //dbshow();


                    //((Activity)con).finish();
                    //con.startActivity(intent);


                } catch (Exception e) {

                    CommonFunction.showCustom_Toast(con, LayoutInflater.from(con), "Error.." + e.getMessage(), R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);

                    //finish();
                }
            } else if (Integer.parseInt(result) == 2) {

                CommonFunction.showCustom_Toast(con, LayoutInflater.from(con), "Email already exist, Please provide another email", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);

            } else if (Integer.parseInt(result) == 999) {

                CommonFunction.showCustom_Toast(con, LayoutInflater.from(con), "Please enable Internet connection", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);

            } else {

                CommonFunction.showCustom_Toast(con, LayoutInflater.from(con), "Oops Something went wrong..Please try after some time.", R.drawable.alert, Toast.LENGTH_SHORT, 0, 350);
                //finish();
            }

            // finish();
        }

    }

}
