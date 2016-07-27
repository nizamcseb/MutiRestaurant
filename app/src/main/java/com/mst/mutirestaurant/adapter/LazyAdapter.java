package com.mst.mutirestaurant.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.Tab_Activity;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.fragments.fragment_product;
import com.mst.mutirestaurant.support.DataBase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ha on 10/23/15 AD.
 */
public class LazyAdapter extends BaseAdapter {
    DataBase db;
    private Context activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    ViewHolder Vholder;
    public String val="",str_proname="",str_rrate="";
    Button btn_plus, btn_minus;
    HashMap<String,Integer> positiveNumbers = new HashMap<String,Integer>();
    public static final String MAP_PID = "pid";
    public static final String MAP_CID = "cid";
    public static final String MAP_PRODNAME = "pname";
    public static final String MAP_PRICE = "price";
    public static final String MAP_DESC = "descp";
    public static final String MAP_IMAGE = "imageurl";
    Integer count=0 , count_pos=0;
    float number;
    //public ImageLoader imageLoader;
    HashMap<String, String> hmap = new HashMap<String, String>();
    public LazyAdapter(Context c, ArrayList<HashMap<String, String>> d) {
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
            convertView = inflater.inflate(R.layout.list_item_food, null);
            db = new DataBase(activity);
            holder.tv_ProdName = (TextView) convertView.findViewById(R.id.tv_prodname); // veh id
           // holder.tv_Quantity = (TextView) convertView.findViewById(R.id.txtquantiry); // veh name
            holder.tv_ProdRate = (TextView) convertView.findViewById(R.id.tv_price); // veh model
            // holder.tv_count = (TextView) convertView.findViewById(R.id.txtrareview);
            holder.img_prod = (ImageView) convertView.findViewById(R.id.img_Prod); // veh image
         //   holder. img_prod.setVisibility(View.GONE);
           // final ProgressBar progressBar=(ProgressBar)convertView.findViewById(R.id.progressBar);
          //  holder.uniqueKey = String.valueOf(position);System.out.println("pos=="+String.valueOf(position));

            holder.btn_plus = (Button) convertView.findViewById(R.id.btnplus);
            holder.btn_plus.setTag(position);
          //  holder.btn_minus = (Button) convertView.findViewById(R.id.btnminus);
           // holder.btn_minus.setTag(position);
            /*fragment_product.tv_count.setVisibility(View.INVISIBLE);
            fragment_product.tv_sar.setVisibility(View.INVISIBLE);
            fragment_product.check.setVisibility(View.INVISIBLE);*/
            convertView.setTag(holder);


        }


        Vholder = (ViewHolder) convertView.getTag();


        hmap = data.get(position);


       // Vholder.tv_ProdRate.setText(hmap.get(SelectedItem.TAG_PROD_RATE));
        //Vholder.tv_ProdName.setText(hmap.get(SelectedItem.TAG_PROD_NAME));
        Vholder. tv_ProdName.setText(hmap.get(fragment_product.MAP_PRODNAME));
        //Vholder.tv_Vmodel.setText(hmap.get(fragment_product.MAP_DESC));
        Vholder.tv_ProdRate.setText(hmap.get(fragment_product.MAP_PRICE));

        //System.out.println("Adapter output " + hmap.get(SelectedItem.TAG_PROD_IMAGEURL));

        Picasso.with(activity.getApplicationContext())
                .load(hmap.get(fragment_product.MAP_IMAGE))
                        .into(Vholder.img_prod);


        Vholder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hmap = data.get(position);
                Vholder.uniqueKey = String.valueOf(position);
                System.out.println("pos==" + String.valueOf(position));
               // Vholder.tv_Quantity.setTag(position);
               // Vholder.tv_Quantity.getTag(Integer.parseInt(Vholder.uniqueKey));
                /*count = count + 1;
                //Vholder.tv_Quantity.setText(String.valueOf(count));
                Vholder.tv_Quantity.setText(count.toString());System.out.println("SE==" + count.toString());*/
                //positiveNumbers.put(Vholder.uniqueKey, count);
             /*   int  addd =   Integer.parseInt(Vholder.tv_Quantity.getText().toString());
                int add =  Integer.parseInt(Vholder.tv_ProdRate.getText().toString());
                int ad = (add * addd );
                 val = Integer.toString(ad);System.out.println("AD-->" + ad + "  " + add + "  " + val);
                fragment_product.tv_count.setText(val);*/
                db.CreateTable(4);
                Cursor c = db.restaurant.rawQuery("select * from " + db.Cart + " where prodid = '"+ hmap.get(MAP_PID).toString()+"'", null);
                System.out.println("cart table count" + c.getCount());
                if(c.getCount() == 0) {

                    db.CreateTable(4);
                    db.restaurant.execSQL("INSERT INTO "
                            + db.Cart
                            + "(prodid,prodname,prodrate,produrl,prodqty)"
                            + "VALUES ('" + hmap.get(MAP_PID).toString() + "', '" + hmap.get(MAP_PRODNAME).toString() + "','" + hmap.get(MAP_PRICE).toString() + "','" + hmap.get(MAP_IMAGE).toString() + "','1')");

                    ((Tab_Activity)activity).doIncrease();

                   // Toast.makeText(activity, "Product is addred to cart :" + hmap.get(MAP_PRODNAME).toString(), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(activity,"This Product Is Already To Your Cart",Toast.LENGTH_LONG).show();
                }
               /* try {
                    db.CreateTable(4);
                    c = db.restaurant.rawQuery("select sum(prodrate) from "+db.Cart, null);
                    if(c.moveToFirst()) {
                        int amount = c.getInt(0);System.out.println("pp=="+amount);
                    }

                        else{
                        //amount = -1;
                    }
                    c.close();


                }
                catch (Exception e){
                    System.out.println("Uerror=="+e.getMessage());
                }*/

             /*   try {
                    db.CreateTable(3);
                    Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);
                    System.out.println("cart rate count" + c.getCount());
                    if (c.getCount() > 0) {

                        if(c.getCount()==1) {
                            c.moveToFirst();
                            str_rrate = c.getString(c.getColumnIndex("prodrate"));
                            System.out.println("check 1st price" + str_proname);
                        } else {
                            c.moveToLast();
                            str_rrate = c.getString(c.getColumnIndex("prodrate"));
                            System.out.println("check last price" + str_rrate);
                        }

                        number = number + Float.parseFloat(str_rrate); System.out.println("final price"+ number);
                        fragment_product.tv_count.setText(String.valueOf(number));

                    } else {
                        fragment_product.tv_count.setText("0");
                    }
                } catch (Exception e) {
                    System.out.println("geting rate" + e.getMessage());
                }*/
            }
        });
       /* Vholder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // v.getTag();

                count = count - 1;
                Vholder.tv_Quantity.setText(count.toString());System.out.println("SE1==" + count.toString());
                //Vholder.tv_Quantity.setText(String.valueOf(count));
                positiveNumbers.put(Vholder.uniqueKey, count);
            }
        });*/

        return convertView;
    }

    public Map<String,Integer> getPositiveNumbers()
    {
        return positiveNumbers;
    }
    static class ViewHolder {
        TextView tv_ProdName, tv_Quantity, tv_ProdRate;//text4,text5,text6,datetext,drivertext,vehicletext,tracktext;
        int count = 0;

        Button btn_plus, btn_minus;
        ImageView img_prod;
        public String uniqueKey;
        //LinearLayout lindriver, cancellay, picklay, droplay;
        // RelativeLayout rellay;


    }


}