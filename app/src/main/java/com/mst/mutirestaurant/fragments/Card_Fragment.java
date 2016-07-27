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
public class Card_Fragment extends Fragment {


    int fragVal;

    private RadioGroup radiocardsgroup;
    public static String radio="";
    private RadioButton radiocre ,radiodeb;
    CommonFunction cf;
    public static Card_Fragment init(int val, boolean selected) {
        Card_Fragment truitonFrag = new Card_Fragment();
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
        View layoutView = inflater.inflate(R.layout.frag_card, container, false);
        cf = new CommonFunction(getContext());
        radiocardsgroup = (RadioGroup) layoutView.findViewById(R.id.radiocards);
        radiocre = (RadioButton)layoutView.findViewById(R.id.rtbtncre);
        radiodeb = (RadioButton)layoutView.findViewById(R.id.rtbtdeb);

        if (radiocardsgroup.getCheckedRadioButtonId() == -1)
        {
            cf.radioselected = radiocre.getText().toString();
    }
        else
        {
            cf.radioselected = radiodeb.getText().toString();
        }
        /*int selectedId = radiocardsgroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) layoutView.findViewById(selectedId);
        radio = radioButton.getText().toString();*/
        return layoutView;
    }
}
