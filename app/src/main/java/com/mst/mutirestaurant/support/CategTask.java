package com.mst.mutirestaurant.support;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mst.mutirestaurant.Activities.MainActivity;
import com.mst.mutirestaurant.Activities.Tab_Activity;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.adapter.GridItem;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLYNC SOLUTIONS on 30-01-2016.
 */
public class CategTask extends AsyncTask<String, Void, String> {

    CommonFunction cf;
    Context con;
    GridView mGridView;
    ListView list;
    public static String str_result = "", str_qty="",str_name = "", str_storeid = "",str_cc ="", str_image = "", str_getaddress = "", str_stoadd = "", str_catyid = "", str_rate = "", str_prodid = "", str_storeidd = "", str_storename = "";
    public static String[] arr_Catid = null, arr_caname = null, arr_Caimg = null,arr_stoid=null,arr_poid=null;

    public CategTask(Context context, ListView listView, GridView list) {
        this.list=listView;
        this.mGridView=list;
        this.con=context;
        cf=new CommonFunction(context);
    }

    @Override
    protected String doInBackground(String... params) {
        // Integer result = 0;
        try {

           String Api = cf.url + cf.PHP_FILE_Bulk_Image;
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(Api);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("storeid", params[0]));
            // Add your data

            System.out.println("StoreiD--" + params[0]);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
           String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Image response" + response.toString());

            JSONObject json = new JSONObject(response);
            str_result = json.getString("success");
            GridItem item;
            if (Integer.parseInt(str_result) == 0) {
                System.out.println("login failure");
                String str_message = json.getString("message");

            } else if (Integer.parseInt(str_result) == 1) {
                System.out.println("login success");

                JSONArray data = json.getJSONArray("product");

                str_name="";
                str_image="";
                str_catyid="";
                str_storeid="";
                str_prodid="";
                str_rate="";
                for (int i = 0; i < data.length(); i++) {
                    JSONObject json1 = data.getJSONObject(i);
                    str_name += json1.getString("prodname") + "~";
                    str_image += json1.getString("url") + "~";
                    str_catyid += json1.getString("categoryid") + "~";
                    System.out.println("CA==" + str_catyid);
                    str_storeid = json1.getString("stoid");
                    str_prodid = json1.getString("poid");
                    str_rate = json1.getString("rate");
                    str_qty = json1.getString("qty");
                    System.out.println("GridResponse==" + str_name + " " + str_image + " " + str_catyid);
                    System.out.println("Res--" + str_prodid + " " + str_rate + " " + str_qty);

                        /*if (str_name.contains("Null")) {



                                 /*item = new GridItem();
                                 item.setTitle(cur_name);
                                 item.setImage(str_image);
                                 item.setstid(str_catyid);
                                 System.out.println("Itemimage==" + item.getImage());
                                 mGridData.add(item);*/

                }
                arr_caname = str_name.split("~");
                System.out.println("CATT==" + arr_caname.length);
                arr_Catid = str_catyid.split("~");
                System.out.println("CATT1==" + arr_Catid.length);
                arr_Caimg = str_image.split("~");
                System.out.println("CATT2==" + arr_Caimg.length);
                   /* arr_stoid = str_catyid.split("~");
                    System.out.println("CATT1==" + arr_Catid.length);
                    arr_Caimg = str_image.split("~");
                    System.out.println("CATT2==" + arr_Caimg.length);*/
                            /* item = new GridItem();
                             int position =0;
                             item.setTitle(arr_caname[position]);
                             item.setImage(str_image);
                             item.setstid(str_catyid);
                             System.out.println("Itemimage==" + item.getImage());
                             mGridData.add(item);*/
            }

            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println("catch login error" + e.getMessage());
            return null;
        }
        return str_result;
    }
    protected void onPreExecute() {
        cf.showTProgress(con, mGridView);
        list.setVisibility(View.GONE);
        // pb.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onPostExecute(String str_result) {
        // Download complete. Lets update UI
        System.out.println("OnpostExecutive");
        if (str_result != null) {
            if (Integer.parseInt(str_result) == 1) {
                System.out.println("DATALENGTH==" + arr_caname.length);
                mGridView.setAdapter(new EfficientAdapter(con, arr_caname.length));
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int pos = position;
                        System.out.println("position=" + position + " " + pos);
                        str_cc = arr_Catid[position].toString();
                        System.out.println("CATT4==" + str_cc);
                        Intent intent = new Intent(con, Tab_Activity.class);
                        intent.putExtra(cf.TAG_category, arr_caname[position]);
                        intent.putExtra(cf.TAG_storeid, str_storeidd);
                        intent.putExtra("cid", arr_Catid);
                        intent.putExtra(cf.TAG_caid, arr_Catid[position]);
                        intent.putExtra("Caname", arr_caname);
                        con.startActivity(intent);
                    }
                });
                //  mGridAdapter.setGridData(mGridData);
                MainActivity.toolbarTop.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(con, "No Product Found!", Toast.LENGTH_LONG).show();
            }
        }
        //Hide progressbar3
        cf.dismissTProgress();
        //  mProgressBar.setVisibility(View.GONE);
    }

     class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        ViewHolder holder;
        int len = 0, arrlen = 0;

        public EfficientAdapter(Context context, int length) {
            mInflater = LayoutInflater.from(context);
            len = length;
        }
        public int getCount() {
            arrlen = len;
            return  arrlen;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.grid_item_layout, null);
                holder.titleTextView = (TextView)convertView. findViewById(R.id.grid_item_title);
                holder.imageView = (ImageView)convertView.findViewById(R.id.grid_item_image);
                holder.Textviewid = (TextView)convertView.findViewById(R.id.grid_item_id);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (arr_caname[position].contains("null") || arr_Catid[position].contains("null") || arr_Caimg[position].contains("null") ) {

                arr_caname[position] = arr_caname[position].replace("null", "");
                arr_Catid[position] = arr_Catid[position].replace("null", "");
                arr_Caimg[position] = arr_Caimg[position].replace("null", "");

                holder.titleTextView.setText(Html.fromHtml(arr_caname[position]));
                holder.Textviewid.setText(Html.fromHtml(arr_Catid[position]));
                Picasso.with(con)
                        .load(arr_Caimg[position])
                        .into(holder.imageView);

            } else {
                holder.titleTextView.setText(Html.fromHtml(arr_caname[position]));
                holder.Textviewid.setText(Html.fromHtml(arr_Catid[position]));
                Picasso.with(con)
                        .load(arr_Caimg[position])
                        .into(holder.imageView);

            }

            return convertView;
        }

        class ViewHolder {
            TextView titleTextView, Textviewid;
            ImageView imageView;
        }
    }
}


