package com.mst.mutirestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.DataBase;
import com.mst.mutirestaurant.support.GPSTracker;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ChoosStoreActivity extends AppCompatActivity {
    Button manual,automatic;
    public static PopupWindow popupWindow = null;
    public static View popupView = null;
    public static ListView fulladdresslist;

    HashMap<String, String> map;
    public static  String str_getaddress="";
    CommonFunction cf;
    String response=null;
    private ProgressBar mProgressBar;
    public static View mProgressView;
    NearestDistance near_distance;
    public String [] arr_storeid=null,arr_storename=null,arr_staddress=null;
    ProgressBar pb;
    static ListView list;
    double currentlat;
    double currentlong;
    TextView tv_loading;
     public static Double distance;
    public static final String TAG_storeid = "store_id";
    public static final String TAG_storename = "store_name";
    public static  String TAG_address = "delivery_address";
    public static  String TAG_storeaddress = "store_address";
    DataBase db;
    GPSTracker gps;
    public static String str_result="", str_message="",Storeid="";
    static String str_storeid="", str_storename="", str_address="", str_storelat="", str_storelng="",str_add="";
    ArrayList<HashMap<String, String>> vehiclelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
              str_getaddress = getIntent().getExtras().getString(TAG_address).toString();
           }System.out.println("Deladd==" + str_getaddress);

        setContentView(R.layout.activity_choos_store);
        db = new DataBase(this);
        pb= (ProgressBar)findViewById(R.id.progressBar2);
        list = (ListView) findViewById(R.id.storelist);
        tv_loading= (TextView)findViewById(R.id.textView);

        vehiclelist = new ArrayList<HashMap<String, String >>();
        //text1.setText(title);
        cf = new CommonFunction(this);
        //text3.setText(msg);
        gps = new GPSTracker(this);
        if(cf.isInternetOn() == true) {

            new ListViewTask().execute();
        }
        else {
            Toast.makeText(ChoosStoreActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
        }

            if(gps.getLocation()!=null)
            {
                currentlat = gps.getLatitude(); System.out.println("H&U share loc meetingpoint lat"+currentlat);
                currentlong = gps.getLongitude();
                System.out.println("H&U share loc meetingpoint long" + currentlong);
                //setLocation(currentlat, currentlong, this);
                //System.out.println("CurrentLoc=="+ curloc);
            }
            else
            {
                System.out.println("CurrentLoc1");
            }




       /* manual = (Button)findViewById(R.id.manualbtn);
        manual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                popupView = layoutInflater.inflate(R.layout.fullstoreaddress_list, null);
                fulladdresslist = (ListView) popupView.findViewById(R.id.address_list);
                mProgressView = popupView.findViewById(R.id.progressBar3);
                popupWindow = new PopupWindow(popupView, 300, 300);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(manual, 200, -30);

                if(cf.isInternetOn() == true) {
                    FullStoreAddress FullStoreAddress = new FullStoreAddress(ChoosStoreActivity.this,str_getaddress);
                    FullStoreAddress.execute();

                } else {
                    Toast.makeText(ChoosStoreActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        automatic = (Button)findViewById(R.id.automabtn);
        automatic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                near_distance = new NearestDistance(ChoosStoreActivity.this);
                if(cf.isInternetOn() == true) {

                    near_distance.inputs(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()),str_getaddress);
                    //add_dialog.dismiss();
                } else {
                    Toast.makeText(ChoosStoreActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    class ListViewTask extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            if (cf.isInternetOn() == true) {

                try {

                    String API = cf.url + cf.PHP_FILE_FULL_STORE_PLACE;

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(API);

                    // Add your data
                    /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(gps.getLatitude())));
                    nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(gps.getLongitude())));


                    System.out.println("distance input value" + String.valueOf(gps.getLatitude()) + " " + String.valueOf(gps.getLongitude()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
*/
                    // Execute HTTP Post Request

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    response = httpclient.execute(httppost, responseHandler);
                    System.out.println("distance response" + response.toString());

                    JSONObject json = new JSONObject(response);
                    str_result = json.getString("success");

                    if (Integer.parseInt(str_result) == 0) {
                        System.out.println("distance failure");
                        str_message = json.getString("message");

                    } else if (Integer.parseInt(str_result) == 1) {
                        System.out.println("distance success");

                        JSONArray data = json.getJSONArray("product");


                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json1 = data.getJSONObject(i);
                            str_storeid += json1.getString("storeid")+"~";
                            str_storename += json1.getString("storename")+"~";
                            str_address += json1.getString("storeaddr")+"~";
                            str_storelat += json1.getString("storelat")+"~";
                            str_storelng += json1.getString("storelng")+"~";

                            System.out.println("storeaddress output value"+ str_storeid + " " + str_storename + " " + str_address + " " + str_storelat + " " + str_storelng);
                           /* str_storeid = json1.getString("storeid");
                            str_storename = json1.getString("storename");
                            str_address = json1.getString("storeaddr");
                            str_storelat = json1.getString("storelat");
                            str_storelng = json1.getString("storelng");

                            System.out.println("distance output value-->" + str_storeid + " " + str_storename + " " + str_address + " " + str_storelat + " " + str_storelng);*/
                            if(str_storeid.contains("Null"))
                            {
                                str_storeid = str_storeid.replace("Null", "");
                            }
                            if(str_storename.contains("Null"))
                            {
                                str_storename = str_storename.replace("Null", "");
                            }
                            if(str_address.contains("Null"))
                            {
                                str_address = str_address.replace("Null", "");
                            }



                        }
                        Double Olat = Double.valueOf(str_storelat);
                        Double Olng = Double.valueOf(str_storelng);System.out.println("OL=="+currentlat+"  "+currentlong);
                         distance=cf.findDistance(new LatLng(Olat, Olng), new LatLng(currentlat, currentlong));
                        distance=distance/1000;
                        distance=Math.round(distance*100.0)/100.0; System.out.println("DIS==" + distance);


                        arr_storeid = str_storeid.split("~");System.out.println("arr_storeid="+arr_storeid);
                        arr_storename = str_storename.split("~");System.out.println("arr_storename="+arr_storename);
                        arr_staddress = str_address.split("~");System.out.println("arr_staddress="+arr_staddress);
                    }

                    Thread.sleep(4000);

                } catch (Exception e) {
                    System.out.println("catch ListViewTask error" + e.getMessage());
                    return null;
                }
            } else {
                Toast.makeText(ChoosStoreActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            }

            return str_result;
        }

        protected void onPreExecute() {
            cf.showTProgress(ChoosStoreActivity.this,tv_loading);
           // pb.setVisibility(View.VISIBLE);
            tv_loading.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(String file) {


            if(file.equals("1")) {


                tv_loading.setVisibility(View.GONE);
                // adapter = new LazyAdapter(getActivity(), foodlist);

                // list.setAdapter(adapter);
                try {
                    list.setAdapter(new EfficientAdapter(ChoosStoreActivity.this, arr_storeid.length));
                }catch (Exception e) {
                    System.out.println("ListError"+e.getMessage());
                }
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        try {
                            db.CreateTable(5);
                            db.restaurant.execSQL("update " + db.Store + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "',address='" + str_address + "',storelat='"+str_storelat +"',storelng='"+str_storelng +"' where status='1' ");
                           // db.restaurant.execSQL("update " + db.Store + " set storeid ='" + str_storeid + "' , storename ='" + str_storename + "' , stolocation ='" + str_address + "' ,storelat='" + str_storelat +"','"+ str_storelng+"'");
                            System.out.println("ToVALU==" + str_storeid + " " + str_storename + " " +str_address);
                            // Toast.makeText(CartView.this, "Product updated on Cart", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            System.out.println("Urror==" + e.getMessage());
                        }
                        //System.out.println("name==="+name);
                        Intent i = new Intent(ChoosStoreActivity.this, MainActivity.class);


                        startActivity(i);

                    }
                });
            }
            else {

            }
            cf.dismissTProgress();
           // pb.setVisibility(View.GONE);
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

                convertView = mInflater.inflate(R.layout.activity_storelist, null);
                holder.txtstorename = (TextView) convertView.findViewById(R.id.txtstore_view);
                holder.txtstoreloc = (TextView) convertView.findViewById(R.id.txtstore_loca);
                holder.tv_distance = (TextView)convertView.findViewById(R.id.Distance);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            try {
                if (arr_storeid[position].contains("") || arr_storename[position].contains("")
                        ) {

                    arr_storeid[position] = arr_storeid[position].replace("null", "");
                    arr_storename[position] = arr_storename[position].replace("null", "");
                    arr_staddress[position] = arr_staddress[position].replace("null", "");

                    holder.txtstorename.setText(Html.fromHtml(arr_storename[position]));
                    System.out.println("hellohello" + arr_storename[position]);
                    holder.txtstoreloc.setText(Html.fromHtml(arr_staddress[position]));
                    holder.tv_distance.setText(Html.fromHtml(String.valueOf(distance))+"  Km");

                   // holder.txtprice.setText(Html.fromHtml(arr_price[position])+" SAR");



                } else {
                    //llayout.setVisibility(View.VISIBLE);

                    holder.txtstorename.setText(Html.fromHtml(arr_storename[position]));
                    System.out.println("hellohello" + arr_storename[position]);
                    holder.txtstoreloc.setText(Html.fromHtml(arr_staddress[position]));
                    holder.tv_distance.setText(Html.fromHtml(String.valueOf(distance))+"  Km");
                   /* holder.txtfooddesc.setText(Html.fromHtml(arr_fooddesc[position]));
                    holder.txtprice.setText(Html.fromHtml(arr_price[position])+" SAR");
                    String iman = arr_url[position].toString();
                    System.out.println("LoadImg-->" + iman);
                    Uri uri = Uri.parse(arr_url[position]);
                    Picasso.with(con)
                            .load(uri)

                            .into(holder.img);
                    return  holder.img;*/
                }
               /* holder.plusbutton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        System.out.println("INSERTBUTTON");

                        try {
                            str_plus = holder.quanty.getText().toString();


                            System.out.println("Quantity+++-->" + str_plus);
                            try {

                                counter ++;
                                System.out.println("Final==>" + counter);

                                holder.quanty.setText(String.valueOf(counter));

                                // holder.quanty.setText(Integer.toString(counter));
                            } catch (Exception e) {
                                System.out.println("increment--" + e.getMessage());
                            }


                        } catch (Exception e) {
                            System.out.println("ListBtn==" + e.getMessage());
                        }
                    }

                    private Context getActivity() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                });*/


            }

            catch(Exception e)
            {

            }

            return convertView;
        }

        class ViewHolder {
            public   TextView txtstorename,txtstoreloc,tv_distance;
            Button plusbutton,minusbutton;
            ImageView img;

        }

    }
    }

