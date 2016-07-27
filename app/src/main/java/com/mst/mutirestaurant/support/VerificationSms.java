package com.mst.mutirestaurant.support;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

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
 * Created by Blyncsolutions on 10/29/15 AD.
 */
public class VerificationSms {
    Context con;
    PopupWindow pw;
    EditText et;
    public static CommonFunction cf;

    public VerificationSms(Context c) {
        this.con = c;
        cf = new CommonFunction(this.con);
    }

    public void sendVerificationSms(String mobilenum, PopupWindow popupWindow, EditText editText, String code) {
        if (mobilenum != null && code !=null) {
            System.out.println("verify sendsms" + mobilenum);
            this.pw = popupWindow;
            this.et = editText;
            smstask smstask = new smstask();
            smstask.execute(mobilenum, code);
        } else {

        }
    }

    String response = "", encodedMsg="";
    public static String responsef = "";

    class smstask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.


            try {

                String API = cf.url + cf.PHP_FILE_ENCODE_SMS_CODE;
                //String API = "http://blyncsolutions.com/BLYNC_APPS/android/rentataxi/test/encode_sms.php";

                HttpClient httpclient = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(API);

                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                //nameValuePairs.add(new BasicNameValuePair("mobilenum", params[0]));
                nameValuePairs.add(new BasicNameValuePair("input", params[1]));

                System.out.println("verifysms input value" + params[0] + " " + params[1]);

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);
                System.out.println("verifysms response" + response.toString());

                JSONObject json1 = new JSONObject(response);
                String result = json1.getString("success");

                if(result.equals("1")) {
                    JSONArray data1 = json1.getJSONArray("product");
                    for(int i=0; i < data1.length(); i++) {
                        JSONObject json2 = data1.getJSONObject(i);
                        encodedMsg = json2.getString("result");
                    }

                    HttpClient httpclientf = new DefaultHttpClient();
                    if(params[0].startsWith("0")) {
                        params[0] = params[0].substring(1);
                    }

                    HttpPost httppodtf = new HttpPost(cf.php_SMS+"?mobile=Blync&password=1520&numbers="+params[0]+"&sender=CABXY%20&msg="+encodedMsg+"&dateSend=mm:dd:yyyy&timeSend=0&msgId=0&applicationType=24");
                    System.out.println(cf.php_SMS+"?mobile=Blync&password=1520&numbers="+params[0]+"&sender=CABXY%20&msg="+encodedMsg+"&dateSend=mm:dd:yyyy&timeSend=0&msgId=0&applicationType=24");
                    ResponseHandler<String> responseHandlerf = new BasicResponseHandler();
                    responsef = httpclientf.execute(httppodtf, responseHandlerf);
                    System.out.println("verifysms==newoutputt="+responsef);
                }


                Thread.sleep(4000);
            } catch (Exception e) {
                System.out.println("catch verifysms error" + e.getMessage());
                return null;
            }


            // TODO: register the new account here.
            return responsef;
        }

        @Override
        protected void onPostExecute(final String result) {

            //showProgress(false);

            /*if (result.contains("Message Submitted")) {
                pw.showAtLocation(et, Gravity.CENTER, 0, 0);
                Toast.makeText(con, "Message sent successfully", Toast.LENGTH_SHORT).show();

            } else if (result.equals("null")) {
                Toast.makeText(con, "Something went wrong", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(con, "Message not sent", Toast.LENGTH_SHORT).show();

            }*/

            /*if(result != null) {
                if (result.equals("-2") || result.equals("-1") || result.equals("2") ||
                        result.equals("3") || result.equals("4") || result.equals("5") ||
                        result.equals("6") || result.equals("13") || result.equals("14") ||
                        result.equals("15") || result.equals("16") || result.equals("17")) {
                    Toast.makeText(con, "Message not sent", Toast.LENGTH_SHORT).show();
                } else if (response.equals("1")) {
                    pw.showAtLocation(et, Gravity.CENTER, 0, 0);
                    Toast.makeText(con, "Message sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(con, "Try again Later", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(con, "Something went wrong", Toast.LENGTH_SHORT).show();
            }*/

            if(result.equals("-2")||result.equals("-1")||result.equals("2")||
                    result.equals("3")||result.equals("4")||result.equals("5")||
                    result.equals("6")||result.equals("13")||result.equals("14")||
                    result.equals("15")||result.equals("16")||result.equals("17"))

            {
                Toast.makeText(con, "Message not sent", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("1"))
            {
                pw.showAtLocation(et, Gravity.CENTER, 0, 0);
                Toast.makeText(con, "Message sent successfully", Toast.LENGTH_SHORT).show();
            }

            cf.dismissTProgress();
        }
    }
}
