package com.mst.mutirestaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mst.mutirestaurant.Activities.CartView;
import com.mst.mutirestaurant.R;

import java.util.ArrayList;
import java.util.HashMap;

//import com.blyncsolutions.restaurant.Activities.SelectedItem;

/**
 * Created by pc on 12-Nov-15.
 */
public class CartAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public CartAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.cart_item, null);


        ImageView img_url = (ImageView) vi.findViewById(R.id.img_prod); // veh image
        TextView tv_ProdName = (TextView) vi.findViewById(R.id.tv_ProdName); // veh id
        TextView tv_Prodrate = (TextView) vi.findViewById(R.id.tv_ProdPrice); // veh name

        HashMap<String, String> hmap = new HashMap<String, String>();
        hmap = data.get(position);

        /*Picasso.with(activity.getApplicationContext())
                .load(hmap.get(CartView.TAG_CART_IMAGEURL))
                .into(img_url);*/

        tv_ProdName.setText(hmap.get(CartView.TAG_CART_NAME));
        tv_Prodrate.setText(hmap.get(CartView.TAG_CART_RATE));
        return vi;
    }

}
