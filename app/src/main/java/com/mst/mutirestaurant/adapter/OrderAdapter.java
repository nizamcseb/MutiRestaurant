package com.mst.mutirestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mst.mutirestaurant.Activities.MyorderActivity;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vignesh Arunagiri on 11/20/2015.
 */
public class OrderAdapter extends BaseAdapter {

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
    public OrderAdapter(Context c, ArrayList<HashMap<String, String>> d) {
        activity = c;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
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
            convertView = inflater.inflate(R.layout.list_item_myorder, null);
            db = new DataBase(activity);
            holder.tv_Orderid = (TextView) convertView.findViewById(R.id.tv_orderid); // order id

            holder.tv_Oderdate = (TextView) convertView.findViewById(R.id.tv_orderdate); // order date

            holder.tv_Odertime = (TextView) convertView.findViewById(R.id.tv_estime);

            convertView.setTag(holder);
        }
        Vholder = (ViewHolder) convertView.getTag();
        hmap = data.get(position);
        Vholder. tv_Orderid.setText(hmap.get(MyorderActivity.TAG_ORDER_ID));
        Vholder.tv_Oderdate.setText(hmap.get(MyorderActivity.TAG_ORDER_DATE));
        return convertView;
    }

    public Map<String,Integer> getPositiveNumbers()
    {
        return positiveNumbers;
    }
    static class ViewHolder {
        TextView tv_Orderid, tv_Oderdate,tv_Odertime;






    }


}
