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
public class Cash_Fragment extends Fragment {

    int fragVal;
    boolean fragSeleted=true;
    private RadioGroup radiocardsgroup;
    public static String radio="";
    private RadioButton radiocash;
    CommonFunction cf;

    public static Cash_Fragment init(int val, boolean selected) {
        Cash_Fragment truitonFrag = new Cash_Fragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        args.putBoolean("selected",selected);
        truitonFrag.setArguments(args);

        return truitonFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        fragSeleted = getArguments().getBoolean("selected");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.frag_cash, container, false);


        cf = new CommonFunction(getContext());
        radiocardsgroup = (RadioGroup) layoutView.findViewById(R.id.radiocash);
        radiocash = (RadioButton)layoutView.findViewById(R.id.rtbtncash);


         if(fragSeleted=true){
             System.out.println("True");
         }else{
             System.out.println("false");
         }
       // if (radiocardsgroup.getCheckedRadioButtonId() == -1)
       // {
            cf.radioselected = radiocash.getText().toString();
       /* }
        else
        {
            cf.radioselected = radiodeb.getText().toString();
        }*/
        return layoutView;
    }
}
