package com.mst.mutirestaurant.support;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.mst.mutirestaurant.R;


/**
 * Created by ha on 10/29/15 AD.
 */
public class Custom_Textwatcher implements TextWatcher {
    private EditText mEditText;
    String TAG;
    Context con;
    /* String TAG_TXT_NAME = "name";
     String TAG_TXT_EMAIL = "email";
     String TAG_TXT_MOBILE = "mobile";
     String TAG_TXT_PASSWORD = "pass";*/
    CommonFunction cf;
    Drawable d_verified, d_notVerfied;

    public Custom_Textwatcher(Context context, String tag, EditText e) {
        this.con = context;
        this.TAG = tag;
        this.mEditText = e;
        if (context != null) {
            cf = new CommonFunction(context);

            d_verified = con.getResources().getDrawable(R.mipmap.ic_tick);
            d_verified.setBounds(0, 0, 50, 50);
            d_notVerfied = con.getResources().getDrawable(R.mipmap.ic_tick);
            d_notVerfied.setBounds(0, 0, 50, 50);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /*if (mEditText.getText().toString().length() == 10) {
                SignupActivity.Verify.setVisibility(View.VISIBLE);
                //GuestActivity.verify.setVisibility(View.VISIBLE);
        } else {
                SignupActivity.Verify.setVisibility(View.GONE);
                //GuestActivity.verify.setVisibility(View.GONE);
        }*/
        if (TAG.equals(cf.TAG_TXT_NAME)) {
            System.out.println("inside the Name tag");
            if (count >= 4) {
                System.out.println("inside the Name tag if condition");


                mEditText.setCompoundDrawables(null, null, d_verified, null);

            } else {
                mEditText.setCompoundDrawables(null, null, d_notVerfied, null);
            }


        } else if (TAG.equals(cf.TAG_TXT_EMAIL)) {

            if (cf.eMailValidation(s) == true) {

                mEditText.setCompoundDrawables(null, null, d_verified, null);
            } else {

                mEditText.setCompoundDrawables(null, null, d_notVerfied, null);
            }

        } else if (TAG.equals(cf.TAG_TXT_MOBILE)) {
            if (s.length() == 10) {

                mEditText.setCompoundDrawables(null, null, d_verified, null);

            } else {
                mEditText.setCompoundDrawables(null, null, d_notVerfied, null);
            }

        } else if (TAG.equals(cf.TAG_TXT_PASSWORD)) {
            if (s.length() >= 6) {

                mEditText.setCompoundDrawables(null, null, d_verified, null);

            } else {
                mEditText.setCompoundDrawables(null, null, d_notVerfied, null);
            }


        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
