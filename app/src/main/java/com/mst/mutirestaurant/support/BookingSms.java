package com.mst.mutirestaurant.support;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by pc on 20-Nov-15.
 */
public class BookingSms {
    Context con;
    CommonFunction cf;
    DataBase db;

    public BookingSms(Context c) {
        this.con = c;
        cf = new CommonFunction(this.con);
        db = new DataBase(this.con);
    }

    public void Sendsms(String mobile, String str) {
        if (mobile != null) {
            System.out.println("paysms" + mobile + " " + str);
            PaySms smstask = new PaySms();
            smstask.execute(mobile, str);
        } else {

        }
    }

    String response = "", encodedMsg = "";
    public static String responsef = "";

    private class PaySms extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            {
                try {
                    String API = cf.url + cf.PHP_FILE_ENCODE_SMS_CODE;
                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("input", params[1]));

                    System.out.println("paysms input value" + params[1]);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("paysms response" + response.toString());

                    JSONObject json1 = new JSONObject(response);
                    String result = json1.getString("success");
                    System.out.println("paysms result=" + result);

                    if (result.equals("1")) {
                        JSONArray data1 = json1.getJSONArray("product");
                        for (int i = 0; i < data1.length(); i++) {
                            JSONObject json2 = data1.getJSONObject(i);
                            encodedMsg = json2.getString("result");
                        }

                        HttpClient httpclientf = new DefaultHttpClient();
                        if (params[0].startsWith("0")) {
                            params[0] = params[0].substring(1);
                        }

                        HttpPost httppodtf = new HttpPost(cf.php_SMS + "?mobile=Blync&password=1520&numbers=" + params[0] + "&sender=CABXY%20&msg=" + encodedMsg + "&dateSend=mm:dd:yyyy&timeSend=0&msgId=0&applicationType=24");
                        System.out.println(cf.php_SMS + "?mobile=Blync&password=1520&numbers=" + params[0] + "&sender=CABXY%20&msg=" + encodedMsg + "&dateSend=mm:dd:yyyy&timeSend=0&msgId=0&applicationType=24");
                        ResponseHandler<String> responseHandlerf = new BasicResponseHandler();
                        responsef = httpclientf.execute(httppodtf, responseHandlerf);
                        System.out.println("verifysms==newoutputt=" + responsef);
                    }
                } catch (Exception e) {
                    System.out.println("orderid error" + e.getMessage());
                }
                return responsef;
            }
        }

        @Override
        protected void onPostExecute(final String result) {


            if (result.equals("-2") || result.equals("-1") || result.equals("2") ||
                    result.equals("3") || result.equals("4") || result.equals("5") ||
                    result.equals("6") || result.equals("13") || result.equals("14") ||
                    result.equals("15") || result.equals("16") || result.equals("17"))

            {
                Toast.makeText(con, "Message not sent", Toast.LENGTH_SHORT).show();
            } else if (result.equals("1")) {
                //Toast.makeText(con, "Message sent successfully", Toast.LENGTH_SHORT).show();
                try {

                    Toast.makeText(con, "Message sent successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    System.out.println("deleted table" + e.getMessage());
                }
            }

           // cf.dismissTProgress();
        }
    }
}
