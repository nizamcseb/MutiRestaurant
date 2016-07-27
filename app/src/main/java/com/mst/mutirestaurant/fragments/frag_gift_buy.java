package com.mst.mutirestaurant.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.HorizontalListView;
import com.mst.mutirestaurant.support.CustomOnItemSelectedListener;

/**
 * Created by Lenovo on 19-05-2016.
 */
@SuppressLint("ValidFragment")
public class frag_gift_buy extends Fragment {


    public frag_gift_buy(Context context) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_gift_buy, container, false);


        HorizontalListView listview = (HorizontalListView) rootView.findViewById(R.id.listview);
        listview.setAdapter(mAdapter);
        listview.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        return rootView;
    }

    private static String[] tiltes = new String[]{"P.F. Chang's Complimentary Carrier",
            "Congratulations Greeting Card $3.99",
            "Birthday Greeting Card $3.99", "All Occasion Greeting Card $3.99"};
    private static Integer[] images = new Integer[]{R.drawable.gc_classic,
            R.drawable.gc_congrz,
            R.drawable.gc_birthday, R.drawable.gc_foryou};

    private BaseAdapter mAdapter = new BaseAdapter() {

        private View.OnClickListener mOnButtonClicked = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("hello from " + v);
                builder.setPositiveButton("Cool", null);
                builder.show();*/

            }
        };

        @Override
        public int getCount() {
            return tiltes.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item, null);
            TextView title = (TextView) retval.findViewById(R.id.title);
            ImageView imageView = (ImageView) retval.findViewById(R.id.iv_gc_item);
            Button button = (Button) retval.findViewById(R.id.clickbutton);
            button.setOnClickListener(mOnButtonClicked);
            imageView.setImageResource(images[position]);
            title.setText(tiltes[position]);

            return retval;
        }

    };

}
