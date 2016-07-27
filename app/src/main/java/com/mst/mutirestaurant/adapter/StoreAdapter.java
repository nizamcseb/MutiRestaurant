package com.mst.mutirestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mst.mutirestaurant.Activities.ListActivity;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by blync on 11/24/2015.
 */
public class StoreAdapter extends BaseAdapter {

    DataBase db;
    private Context activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    ViewHolder Vholder;
    public String val="",str_proname="",str_rrate="";
    Button btn_plus, btn_minus;
    HashMap<String,Integer> positiveNumbers = new HashMap<String,Integer>();
    public static final String TAG_ORDER_ID = "orderid";
    public static final String MAP_CID = "cid";
    public static final String MAP_PRODNAME = "pname";
    public static final String MAP_PRICE = "price";
    public static final String MAP_DESC = "descp";
    public static final String MAP_IMAGE = "imageurl";
    Integer count=0 , count_pos=0;
    float number;
    //public ImageLoader imageLoader;
    HashMap<String, String> hmap = new HashMap<String, String>();
    public StoreAdapter(Context c, ArrayList<HashMap<String, String>> d) {

        try {
            activity = c;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(c.LAYOUT_INFLATER_SERVICE);
            //imageLoader=new ImageLoader(activity.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_storelist, null);
            db = new DataBase(activity);
            holder.txtstorename = (TextView) convertView.findViewById(R.id.txtstore_view);
            holder.txtstoreloc = (TextView) convertView.findViewById(R.id.txtstore_loca);
            holder.tv_distance = (TextView)convertView.findViewById(R.id.Distance);



            convertView.setTag(holder);
        }
        Vholder = (ViewHolder) convertView.getTag();
        hmap = data.get(position);
        Vholder. txtstorename.setText(hmap.get(ListActivity.TAG_storename));
        Vholder.txtstoreloc.setText(hmap.get(ListActivity.TAG_storeaddress));
        Vholder.tv_distance.setText(hmap.get(ListActivity.TAG_distance));
        return convertView;
    }

    public Map<String,Integer> getPositiveNumbers()
    {
        return positiveNumbers;
    }
    static class ViewHolder {
        public   TextView txtstorename,txtstoreloc,tv_distance;

    }


}
