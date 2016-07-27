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
 * Created by blync on 12/3/2015.
 */
public class Track_fragment extends Fragment {

    int fragVal;
    private RadioGroup radiocardsgroup;
    public static String radio="";
    private RadioButton radiocash;
    CommonFunction cf;

    public static Track_fragment init(int val) {
        Track_fragment truitonFrag = new Track_fragment();
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
        View layoutView = inflater.inflate(R.layout.frag_cash, container, false);


        cf = new CommonFunction(getContext());
        radiocardsgroup = (RadioGroup) layoutView.findViewById(R.id.radiocash);
        radiocash = (RadioButton)layoutView.findViewById(R.id.rtbtncash);


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
