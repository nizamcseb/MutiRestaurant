package com.mst.mutirestaurant.support;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.mst.mutirestaurant.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ha on 10/22/15 AD.
 */
public class ForgotPassword {

    Context con;

    CommonFunction cf;
    View v;
    public ForgotPassword(Context c,View view) {
        this.v=view;
        this.con = c;
    }

    public void sendsms(String mobnum) {
        if(mobnum!=null) {
            System.out.println("sendsms"+mobnum);
            smstask smstask = new smstask(mobnum);
            smstask.execute();
        }
        else {

        }
    }

    String response="";

    class smstask extends AsyncTask<Void, Void, String>
    {

        private final String mMobile;


        smstask(String mobile) {

            mMobile = mobile;
            cf = new CommonFunction(con);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.



                try {

                    String API = cf.url+cf.PHP_FILE_FORGOT_PASSWORD;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("mobilenum", mMobile));

                    System.out.println("forgotpwd input value" + mMobile);

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("forgotpwd response"+response.toString());



                    Thread.sleep(4000);
                } catch (Exception e) {
                    System.out.println("catch forgotpwd error"+e.getMessage());
                    return null;
                }



            // TODO: register the new account here.
            return response;
        }

        @Override
        protected void onPreExecute() {
            // super.onPreExecute();

            cf.showTProgress(con, v);
        }

        @Override
        protected void onPostExecute(final String result) {

            //showProgress(false);
            cf.dismissTProgress();
if(result!=null) {
    if (result.contains("1")) {
        Toast.makeText(con, "Message sent successfully", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(con, "Message not sent", Toast.LENGTH_SHORT).show();
        //mPasswordView.setError(getString(R.string.error_incorrect_password));
        //mPasswordView.requestFocus();
    }
}else{
    Toast.makeText(con, "Message not sent", Toast.LENGTH_SHORT).show();
}
        }
    }
}
