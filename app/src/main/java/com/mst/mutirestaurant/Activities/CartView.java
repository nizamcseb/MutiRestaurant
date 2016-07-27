package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.counterincdec;

import java.util.ArrayList;
import java.util.HashMap;

public class CartView extends AppCompatActivity {
    ListView list;
    public static String prod_name = "", prod_rate = "", prod_url = "", prod_id = "", str_message = "",qt="",str_rrate="";
    public static String[] arr_prodname = null, arr_prodrate = null, arr_produrl = null, arr_prodid = null;
    DataBase db;
    HashMap<String, String> map;
    public static ArrayList<HashMap<String, String>> cartArrayList = null;
    MyCustomAdaptermeetpoint adapter;
   public static  TextView totalamt,txtsar;
    public static String TAG_CART_ID = "id";
    public static String TAG_CART_NAME = "name";
    public static String TAG_CART_IMAGEURL = "url";
    public static String TAG_CART_RATE = "rate";
    Button btn_check;
    public static int val = 0, show_handler = 0;
    CommonFunction cf;
    //Booking bk;
    //getDb gdb;
    int pos = 0;
   public static int  total;
    public int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        list = (ListView) findViewById(R.id.list_cart);
        btn_check = (Button) findViewById(R.id.btn_pay);
        totalamt = (TextView)findViewById(R.id.tv_totalamt);
        txtsar= (TextView)findViewById(R.id.tv_curr);
        db = new DataBase(this);
        cf = new CommonFunction(this);
      //  bk = new Booking(this);
       // gdb = new getDb(this);
        MyCustomAdaptermeetpoint adapter = null;

        setadapterlistview();

       // Addarrayvalues();
           try {
                    db.CreateTable(4);

                     Cursor c = db.restaurant.rawQuery("SELECT Sum(prodrate)  AS myTotal FROM " + db.Cart, null);
                    System.out.println("cart rate count" + c.getCount());
                    if (c.getCount() > 0) {

                        if(c.getCount()==1) {
                            c.moveToFirst();
                             total = c.getInt(c.getColumnIndex("myTotal"));
                             System.out.println("check 1st price" + total);


                        } else {

                        }
                        totalamt.setText(String.valueOf(total));


                    } else {
                      //  fragment_product.tv_count.setText("0");
                    }
                } catch (Exception e) {
                    System.out.println("geting rate" + e.getMessage());
                }
             btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setadapterlistview();
                String fintot = totalamt.getText().toString();
                try {
                    db.CreateTable(4);
                    db.restaurant.execSQL("update " + db.Cart + " set prodtotal ='" + fintot + "'");
                    System.out.println("ToVALUES==" + fintot);

                } catch (Exception e) {
                    System.out.println("Urror==" + e.getMessage());
                }
                if (cf.isLogin() == true) {

                    if (cf.isInternetOn() == true) {

                        db.CreateTable(4);
                        db.restaurant.execSQL("update " + db.Cart + " set prodtotal ='" + fintot + "'  ");

                        startActivity(new Intent(CartView.this, PlaceOrderActivity.class));
                        // bk.booking(gdb.getUserid());
                    } else {
                        Toast.makeText(CartView.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cf.customizealert(CartView.this, "Login", "You Have Not Logged in.Do You Want to Log in ?");
                    Button ok = (Button) cf.add_dialog.findViewById(R.id.okbtn);
                    ok.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            try {

                                cf.chk_login = 2;
                                startActivity(new Intent(CartView.this, LoginActivity.class));
                                finish();
                            } catch (Exception e) {
                                System.out.println("insert value into records db" + e.getMessage());
                            }
                        }
                    });
                    //Toast.makeText(CartView.this, "please login", Toast.LENGTH_SHORT).show();
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(CartView.this);
                    builder.setTitle("Login")
                            .setMessage("You have not logged in. Do you want to log in?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cf.chk_login = 2;
                                    startActivity(new Intent(CartView.this, LoginActivity.class));
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
*/
                }
            }
        });
    }

   /* private void Addarrayvalues() {

        if (arr_prodrate != null) {

            for (int i = 0; i < arr_prodrate.length; i++)
                sum = sum + Integer.valueOf(arr_prodrate[i]);
            System.out.println("sum==" + sum);System.out.println("ii"+arr_prodrate.length);
            // String str_add_price = totalamt.getText().toString();
            // System.out.println("added price "+str_add_price +" "+sum);
            // number = Integer.parseInt(str_add_price) + (sum); System.out.println("TOADD==" + number);
           // totalamt.setText(String.valueOf(sum));


        }
        else
        {
            totalamt.setText("");
        }
    }*/


    private void setadapterlistview() {

        try {
            cartArrayList = new ArrayList<HashMap<String, String>>();
            db.CreateTable(4);
           Cursor c = db.restaurant.rawQuery("select * from " + db.Cart, null);
           // Cursor c = db.restaurant.rawQuery("select sum(prodrate) AS "TotalSalary" from " + db.Cart, null);

           // select id,dt,pool_name,pool_id,buy_price,(select sum(buy_price) from yourtable) total from yourtable

            System.out.println("cart count==" + c.getCount());
            val = c.getCount();
            if (c.getCount() > 0) {
                if (c.moveToFirst()) {
                    prod_name = "";
                    prod_rate = "";
                    prod_url = "";
                    prod_id = "";
                    do {
                        prod_id += c.getString(c.getColumnIndex("prodid"))+ "~";
                        prod_name += c.getString(c.getColumnIndex("prodname"))+ "~";
                        prod_rate += c.getString(c.getColumnIndex("prodrate"))+ "~";
                        prod_url += c.getString(c.getColumnIndex("produrl"))+ "~";
                        System.out.println("CartDb==" + prod_name + " " + prod_rate + " " + prod_url + " " + prod_id);

                    } while (c.moveToNext());



                }
                arr_prodname = prod_name.split("~");
                System.out.println("arr_prodname=" + arr_prodname);
                arr_prodid = prod_id.split("~");
                System.out.println("arr_prodid=" + arr_prodid);
                arr_prodrate = prod_rate.split("~");
                System.out.println("arr_prodrate=" + arr_prodrate);


                int sum = 0;



                pos = arr_prodid.length; System.out.println("p-"+pos);
                ArrayList<counterincdec> friendsList = new ArrayList<counterincdec>();
                for (int i = 0; i < pos; i++) {
                    counterincdec friendneetpoint = new counterincdec(arr_prodid[i], arr_prodname[i], arr_prodrate[i]);
                    friendsList.add(friendneetpoint);
                    System.out.println("Length==" + arr_prodid[i] + "  " + arr_prodname[i] + "  " + arr_prodrate[i]);
                    adapter = new MyCustomAdaptermeetpoint(CartView.this, R.layout.list_item_food, friendsList);
                    //cartArrayList.add(map);
                    list.setAdapter(adapter);

                }
                //adapter = new CartAdapter(CartView.this, cartArrayList);
                // list.setAdapter(adapter);
            } else {

                Toast.makeText(CartView.this, "Your Cart Is Empty", Toast.LENGTH_SHORT).show();
                totalamt.setVisibility(View.INVISIBLE);
                txtsar.setVisibility(View.INVISIBLE);
                btn_check.setVisibility(View.INVISIBLE);
               // startActivity(new Intent(CartView.this, CartView.class));
            }


        } catch (Exception e) {
            System.out.println("Error on db3" + e.getMessage());
        }
    }

    private class MyCustomAdaptermeetpoint extends ArrayAdapter<counterincdec> {
        private ArrayList<counterincdec> friendsList;
        private Context activity;

        public MyCustomAdaptermeetpoint(Context listtask, int list_item_food, ArrayList<counterincdec> friendsList) {
            super(listtask, list_item_food, friendsList);
            activity = listtask;
            this.friendsList = new ArrayList<counterincdec>();
            this.friendsList.addAll(friendsList);
        }
        private class ViewHolder {
            TextView tv_ProdName, tv_ProdDesc, tv_ProdRate, tv_count;
            Button btn_plus, btn_minus;
            ImageView img_prod;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.cart_item, null);
                holder = new ViewHolder();

                holder.tv_ProdName = (TextView) convertView.findViewById(R.id.tv_ProdName); // veh id
                // holder.tv_ProdDesc = (TextView) convertView.findViewById(R.id.tv_ProdDesc); // veh name
                holder.tv_ProdRate = (TextView) convertView.findViewById(R.id.tv_ProdPrice); // veh model
                holder.tv_count = (TextView) convertView.findViewById(R.id.tv_quantity);

               // holder.img_prod = (ImageView) convertView.findViewById(R.id.img_prod); // veh image

                holder.btn_plus = (Button) convertView.findViewById(R.id.btnplus);

                holder.btn_minus = (Button) convertView.findViewById(R.id.btnminus);

                convertView.setTag(holder);
                final ViewHolder finalHolder2 = holder;
                /*holder.img_prod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterincdec friends = friendsList.get(position);

                        try {

                            db.restaurant.delete(db.Cart, "prodid = '" + friends.getProdid() + "'", null);
                            Toast.makeText(activity, "Deleted your Cart=" + friends.getProdname(), Toast.LENGTH_SHORT).show();
                            setadapterlistview();

                            int a = Integer.parseInt(finalHolder2.tv_count.getText().toString());
                            String str_add_price = totalamt.getText().toString();
                            int c = Integer.parseInt(friends.getProdrate());
                            int b = ( a * c );
                            System.out.println("Decremprice--> "+str_add_price +" "+b+" "+c);
                            number = Integer.parseInt(str_add_price) - Integer.parseInt(String.valueOf(b)); System.out.println("Decrement amt" + number);
                            totalamt.setText(String.valueOf(number));
                        }
                        catch (Exception e){
                            System.out.println("Ldb=="+e.getMessage());
                        }


                    }
                });*/

                final ViewHolder finalHolder = holder;
                holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        counterincdec friends = friendsList.get(position);
                        int a = Integer.parseInt(finalHolder.tv_count.getText().toString());
                        a++;
                        String s = String.valueOf(a);
                        finalHolder.tv_count.setText(s);

                       // int t3 = Integer.parseInt(s) * Integer.parseInt(friends.getProdrate());System.out.println("t3=="+t3);
                        try {
                            db.CreateTable(4);
                            db.restaurant.execSQL("update " + db.Cart + " set prodqty ='" + s + "' where prodid = '" + friends.getProdid() + "'  ");
                            // db.restaurant.execSQL("update " + db.Cart + " set prodqty ='" + s + "',total = '" + String.valueOf(t3) + "' where prodid = '" + friends.getProdid() + "'  ");
                            System.out.println("UPDATED VALUES" + s + " " + friends.getProdname());
                           // Toast.makeText(CartView.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){
                            System.out.println("Uerror=="+e.getMessage());
                        }

                        String str_add_price = totalamt.getText().toString();
                        System.out.println("added price "+str_add_price +" "+friends.getProdrate());
                        number = Integer.parseInt(str_add_price) + Integer.parseInt(friends.getProdrate()); System.out.println("added amt" + number);
                        totalamt.setText(String.valueOf(number));


                    }
                });

                final ViewHolder finalHolder1 = holder;
                holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        counterincdec friends = friendsList.get(position);
                        int a = Integer.parseInt(finalHolder1.tv_count.getText().toString());
                        if(a == 0) {
                            finalHolder1.tv_count.setText("0");
                        } else {
                            a--;
                            String s = String.valueOf(a);
                            finalHolder1.tv_count.setText(s);
                            if(s.equals("0")) {
                                db.CreateTable(4);
                                db.restaurant.execSQL("delete from " + db.Cart + " where prodid = '" + friends.getProdid() + "'");
                             //   Toast.makeText(CartView.this, "Product deleted from your Cart", Toast.LENGTH_SHORT).show();
                                 System.out.println("dell==");
                                totalamt.setText("");
                                 finish();
                                startActivity(getIntent());
                                 // setadapterlistview();
                            }
                            //setadapterlistview();
                            try {
                               // int t3 = Integer.parseInt(s) * Integer.parseInt(friends.getProdrate());System.out.println("t3==" + t3);

                                db.CreateTable(4);
                                db.restaurant.execSQL("update " + db.Cart + " set prodqty ='" + s + "' where prodid = '" + friends.getProdid() + "'  ");
                                // db.restaurant.execSQL("update " + db.Cart + " set prodqty ='" + s + "',total = '" + String.valueOf(t3) + "' where prodid = '" + friends.getProdid() + "' ");
                                System.out.println("UPDATED VALUES" + s + " " + friends.getProdname());
                                //Toast.makeText(CartView.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();

                                String str_add_pri = totalamt.getText().toString();System.out.println("STT=="+str_add_pri);
                                System.out.println("Decremeprice==> " + friends.getProdrate());

                                number = Integer.parseInt(str_add_pri) - Integer.parseInt(friends.getProdrate());
                                System.out.println("Decremamt==" + number + "  " + Integer.parseInt(str_add_pri));
                                totalamt.setText(String.valueOf(number));
                            }
                            catch (Exception e){
                                System.out.println("Uerr=="+e.getMessage());
                            }
                            /*String str_add_pri = totalamt.getText().toString();System.out.println("STT=="+str_add_pri);
                            System.out.println("Decremeprice==> "+friends.getProdrate());

                            number = Integer.parseInt(str_add_pri) - Integer.parseInt(friends.getProdrate());
                            System.out.println("Decremamt==" + number + "  " + Integer.parseInt(str_add_pri));
                            totalamt.setText(String.valueOf(number));*/
                           // setadapterlistview();
                           // Addarrayvalues();

                        }
                       // setadapterlistview();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            counterincdec friends = friendsList.get(position);
            holder.tv_ProdName.setText(friends.getProdname());
            holder.tv_ProdRate.setText(friends.getProdrate() + " $");


            /*Picasso.with(activity.getApplicationContext())
                    .load(friends.getProdimg())
                    .into(holder.img_prod);*/
            holder.tv_ProdName.setTag(friends);
            holder.tv_ProdRate.setTag(friends);
            return convertView;

        }


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CartView.this, MainActivity.class));

    }
}
