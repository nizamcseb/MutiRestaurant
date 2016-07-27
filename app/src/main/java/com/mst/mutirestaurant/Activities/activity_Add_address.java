package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class activity_Add_address extends AppCompatActivity {


    public Button addnew,gpsbtn;
    GPSTracker gps;
    public TextView tvaddress;
     Context con;
    DataBase db;
    static CommonFunction cf;
    Double latitude;
    public static String curloc = "";
    public static String curcity = "";
    public static String setaddress="";
    public static String sublocality="";
    public static String CityName="";
    public static String StateName="";
    public static String etlocation="";
    public static String CountryName="";
    public static String lattitude="";
    public static String longitude="";
    double currentlat;
    double currentlong;
    Double longitudee;
    public String [] arr_fulladd=null,arr_tid=null;
    public String str_preaddress="",str_tid="";
    static ListView list;
    public static View popupView = null;
    public static PopupWindow popupWindow = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__add_address);

        cf= new CommonFunction(this);
        db = new DataBase(this);
        list = (ListView)findViewById(R.id.list_recentloc);
        gps = new GPSTracker(this);


      //  cf.getcurrentlocation();
        gpsbtn = (Button)findViewById(R.id.btngps);
        getcurrentlocation();
        gpsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   // if (gps.getLocation() != null) {

                       // String address = curloc;
                        System.out.println("===>" + curloc);

                        Geocoder geoCoder = new Geocoder(activity_Add_address.this);


                        List<Address> addresses = geoCoder.getFromLocationName(curloc, 1);
                        if (addresses.size() > 0) {
                            System.out.println("Addresses" + addresses);
                            double lati = addresses.get(0).getLatitude();
                            System.out.println("zoom" + latitude);
                            double longi = addresses.get(0).getLongitude();
                            LatLng latlng = new LatLng(lati, longi);
                            latitude = latlng.latitude;
                            longitudee = latlng.longitude;
                            System.out.println("zoom==" + latitude + "  " + longitudee);

                        }

                        try {
                            db.CreateTable(3);
                            db.restaurant.execSQL("INSERT INTO "
                                    + db.Deliveryaddress
                                    + "(address,status,latitude,longitude)"
                                    + "VALUES ('" + curloc + "','1','" + latitude + "','" + longitudee + "')");

                            Intent i = new Intent(activity_Add_address.this, PlaceOrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("ad", curloc);System.out.println("cu==" + curloc);
                            i.putExtras(bundle);
                            startActivity(i);



                        } catch (Exception e) {
                            System.out.println("Urror==" + e.getMessage());
                        }
                        Intent i = new Intent(activity_Add_address.this, PlaceOrderActivity.class);

                        startActivity(i);

                    //}
                    }catch(Exception e){
                        System.out.println("insert value into records db" + e.getMessage());
                    }
                }

        });

        try {


            db.CreateTable(3);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Deliveryaddress + " where status = '1' ORDER BY lid DESC", null);
            System.out.println("countdb==" + c.getCount());
            if (c.getCount() > 0) {
                //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                c.moveToFirst();
                do{
                str_preaddress += c.getString(c.getColumnIndex("address"))+"~";
                 str_tid       += c.getInt(c.getColumnIndex("lid"))+"~";
                }while(c.moveToNext());
                System.out.println("OLD_ADD=" + str_preaddress);
            }
            else
            {
                Toast.makeText(activity_Add_address.this, "No address found", Toast.LENGTH_SHORT).show();
                list.setVisibility(View.GONE);
            }
            arr_fulladd = str_preaddress.split("~");
            arr_tid = str_tid.split("~"); System.out.println("str_tid=" + str_tid);
            list.setAdapter(new EfficientAdapter(activity_Add_address.this, arr_fulladd.length));


            /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("LTI=");
                        *//*db.CreateTable(3);
                        db.restaurant.execSQL("update " + db.Deliveryaddress + " set address ='" + arr_fulladd[position] + "' where lid ='" + arr_tid[position] + "' ");
                        System.out.println("LTID==" + arr_tid[position]);
                        Intent i = new Intent(activity_Add_address.this, PlaceOrderActivity.class);
                        startActivity(i);*//*

                }
            });*/
            } catch (Exception e) {
                System.out.println("ListUpdate==" + e.getMessage());
            }
            list.refreshDrawableState();


        addnew = (Button)findViewById((R.id.btnadd));
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_Add_address.this, SelectLoactionActivity.class));
            }
        });
    }

    public static void setLocation(Double currentlat, Double currentlong, Context con) {

        // TODO Auto-generated method stub
        try
        { System.out.println("tyrgp="+currentlat);
            //Getting address from found locations.
            if(cf.isInternetOn() == true){
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(con, Locale.getDefault());
                addresses = geocoder.getFromLocation(currentlat, currentlong, 1); System.out.println("Address"+addresses);

		     /*  StreetName= addresses.get(0).getLocality();System.out.println("StreetName="+StreetName);
		       StateName= addresses.get(0).getAdminArea();*/
                setaddress = addresses.get(0).getAddressLine(0);
                sublocality= addresses.get(0).getSubLocality();
                CityName = addresses.get(0).getLocality();
                CountryName = addresses.get(0).getCountryName();

                //StreetName= addresses.get(0).get;
                // you can get more details other than this . like country code, state code, etc.
                System.out.println(" sublocality " + sublocality);
                System.out.println(" setaddress " + setaddress);
                System.out.println(" CityName " + CityName);
                System.out.println(" CountryName " + CountryName);
                curloc = sublocality +"," + setaddress + "," + CityName + "," + CountryName;
                curcity = CityName;
                System.out.println("CULOC=="+curloc);
                if(curloc.contains("null,"))
                {
                    curloc = curloc.replace("null,", "");
                }


            }
            else
            {

            }
        }
        catch (Exception e)
        {
            System.out.println("setlocationerror"+e.getMessage());
            e.printStackTrace();
        }




    }
    public void getcurrentlocation() {
        if(gps.getLocation()!=null)
        {
            currentlat = gps.getLatitude(); System.out.println("H&U share loc meetingpoint lat"+currentlat);
            currentlong = gps.getLongitude();
            System.out.println("H&U share loc meetingpoint long"+currentlong);
            setLocation(currentlat, currentlong, this);
            System.out.println("CurrentLoc=="+ curloc);
        }
        else
        {
            System.out.println("CurrentLoc1");
        }

    }
    public class EfficientAdapter extends BaseAdapter {
        private static final int IMAGE_MAX_SIZE = 0;
        private LayoutInflater mInflater;
        ViewHolder holder;
        public  int counter=0;
        View v2;
        int len=0, arrlen=0;


        public EfficientAdapter(Context context, int length) {
            mInflater = LayoutInflater.from(context);
            len = length;
        }

        public int getCount() {
			   /*int arrlen = 0;
			   arrlen = arr_fname.length;


			   return arrlen;*/
            arrlen=len;

            return arrlen;
        }
		/*public EfficientAdapter(Activity activity, int length) {
			// TODO Auto-generated constructor stub
		}


		public int getCount() {
			int arrlen = 0;
			arrlen = arr_userid.length;
			System.out.println("jashdf;lha;ldsf"+arrlen);

			return arrlen;
		}*/

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            holder = new ViewHolder();
            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.activity_oldaddress, null);
                holder.txtstorename = (TextView) convertView.findViewById(R.id.tv_oldadd);
                holder.delete = (Button)convertView.findViewById(R.id.btn_delete);
                holder.edit = (Button)convertView.findViewById(R.id.btn_edit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                if (arr_fulladd[position].contains("") ) {
                    arr_fulladd[position] = arr_fulladd[position].replace("null", "");
                    holder.txtstorename.setText(Html.fromHtml(arr_fulladd[position]));
                    System.out.println("hellohello" + arr_fulladd[position]);
                   // holder.txtstoreloc.setText("Location :" + Html.fromHtml(arr_staddress[position]));
                    // holder.txtprice.setText(Html.fromHtml(arr_price[position])+" SAR");
                } else {
                    //llayout.setVisibility(View.VISIBLE);

                    holder.txtstorename.setText(Html.fromHtml(arr_fulladd[position]));
                    System.out.println("hellohello" + arr_fulladd[position]);
                    //holder.txtstoreloc.setText("Location :" + Html.fromHtml(arr_staddress[position]));

                }
                convertView.setClickable(true);
                convertView.setFocusable(true);
                convertView.setBackgroundResource(android.R.drawable.menuitem_background);
                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        System.out.println("LTI=");
                        /*db.CreateTable(3);
                        db.restaurant.execSQL("update " + db.Deliveryaddress + " set address ='" + arr_fulladd[position] + "' where lid ='" + arr_tid[position] + "' ");
                        System.out.println("LTID==" + arr_tid[position]);*/
                        Intent i = new Intent(activity_Add_address.this, PlaceOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ad", arr_fulladd[position]);System.out.println("aaa=="+arr_fulladd[position]);
                        i.putExtras(bundle);
                       // i.putExtra("adress",  arr_fulladd[position]);
                        startActivity(i);
                    }

                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cf.getcurrentlocation();
                            String address = cf.curloc;
                            try {

                                db.CreateTable(3);
                                db.restaurant.execSQL("delete from " + db.Deliveryaddress + " where lid ='" + arr_tid[position] + "'");
                                System.out.println("delete==");
                            } catch (Exception e) {
                                System.out.println("Urror==" + e.getMessage());
                            }
                            Intent i = new Intent(activity_Add_address.this, activity_Add_address.class);

                            startActivity(i);


                        } catch (Exception e) {
                            System.out.println("insert value into records db" + e.getMessage());
                        }
                    }
                });
                holder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            System.out.println("poss=" + arr_tid.length);
                            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            popupView = layoutInflater.inflate(R.layout.alert_editaddress, null);
                            final EditText fpEdAddress = (EditText) popupView.findViewById(R.id.editaddress);
                            fpEdAddress.setText(arr_fulladd[position]);
                            final Button fpok = (Button) popupView.findViewById(R.id.ok_btn);
                            final Button btn_cancel = (Button) popupView.findViewById(R.id.cancel_btn);

                            popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            popupWindow.setOutsideTouchable(true);
                            popupWindow.setFocusable(true);

                            popupWindow.showAtLocation(holder.edit, Gravity.CENTER, 0, 0);
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });
                            fpok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String str_fpEditAdd = fpEdAddress.getText().toString();
                                    System.out.println("getting input" + str_fpEditAdd);
                                    try {

                                        db.CreateTable(3);
                                        //db.restaurant.execSQL("update " + db.Cart + " set prodqty ='" + s + "' where prodid = '" + friends.getProdid() + "'  ");
                                        db.restaurant.execSQL("update " + db.Deliveryaddress + " set address ='" + str_fpEditAdd + "' where lid ='" + arr_tid[position] + "' ");
                                        System.out.println("TID==" + arr_tid[position]);
                                        Intent i = new Intent(activity_Add_address.this, activity_Add_address.class);

                                        startActivity(i);
                                    } catch (Exception e) {
                                        System.out.println("ListUpdate==" + e.getMessage());
                                    }
                                }
                            });

                        } catch (Exception e) {
                            System.out.println("insert value into records db" + e.getMessage());
                        }
                    }
                });

            }

            catch(Exception e)
            {

            }

            return convertView;
        }

        class ViewHolder {
            public   TextView txtstorename,txtstoreloc;
            Button delete,edit;
            ImageView img;

        }

    }
}
