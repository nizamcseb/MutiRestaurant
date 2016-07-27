package com.mst.mutirestaurant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;

/**
 * Created by Nizamuddeen on 27-11-2015.
 */
public class Wallet_Fragment extends Fragment {


    int fragVal;
    private RadioGroup radiocardsgroup;
    public static String radio="";
    private RadioButton radiopaytm ,radiomob;
    CommonFunction cf;
    public static Wallet_Fragment init(int val, boolean selected) {
        Wallet_Fragment truitonFrag = new Wallet_Fragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.frag_wallet, container,   false);
        radiocardsgroup = (RadioGroup) layoutView.findViewById(R.id.radiowallets);
        radiopaytm = (RadioButton)layoutView.findViewById(R.id.rtbtpaytm);
        radiomob = (RadioButton)layoutView.findViewById(R.id.rtbmobili);

        if (radiocardsgroup.getCheckedRadioButtonId() == -1)
        {
            cf.radioselected = radiopaytm.getText().toString();
        }
        else
        {
            cf.radioselected = radiomob.getText().toString();
        }

        return layoutView;
    }
}
