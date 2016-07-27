package com.mst.mutirestaurant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mst.mutirestaurant.Activities.CartView;
import com.mst.mutirestaurant.R;
import com.mst.mutirestaurant.support.CommonFunction;
import com.mst.mutirestaurant.support.ListTask;

//import com.blyncsolutions.restaurant.adapter.LazyAdapter;

/**
 * Created by blync on 10/28/2015.
 */
public class fragment_product extends Fragment {

    public static ListView list;
 public static String cus_foodname="",cus_foodmodel="";
    String response=null;
    static String str_result="", str_message="",str_plus="";
    String str_foodname="", str_foodid="", str_fooddesc="", str_price="", str_cateid="", str_likes="", str_imageurl="";
    public static String[] arr_foodid=null,arr_foodname=null,arr_url=null,arr_fooddesc=null,arr_price=null,arr_catid=null;
    public   static final String TAG_prodname = "name";
    public   static final String TAG_prodid = "proid";
    public   static final String TAG_catid = "caid";
    public   static final String TAG_price = "rate";
    public   static final String TAG_proddesc = "desc";

    public   static final String MAP_PID = "pid";
    public   static final String MAP_CID = "cid";
    public   static final String MAP_PRODNAME = "pname";
    public   static final String MAP_PRICE = "price";
    public   static final String MAP_DESC = "descp";
    public   static final String MAP_IMAGE = "imageurl";
    // public   static final String TAG_likes = "likes";
    public   static final String TAG_imageurl = "image";
    public LinearLayout L1;
    //ArrayList<HashMap<String, String>> foodlist;

    CommonFunction cf;
    //Context con;
   // LazyAdapter adapter;
    Button plus,minus,check;
    public static ProgressBar pb;
    public String str_cid=null;
    Integer count=0;
    public static  TextView tv_loading,tv_rateview,tv_count;
    //HashMap<String, String> map;
    //HashMap<String,Integer> positiveNumbers = new HashMap<String,Integer>();



    // newInstance constructor for creating fragment with arguments
    public static fragment_product newInstance(int page, String title) {
        fragment_product fragment = new fragment_product();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("catId", title);
        fragment.setArguments(args);
        return fragment;
    }

    String argString;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       int page = getArguments().getInt("someInt", 0);
        argString = getArguments().getString("catId");


        System.out.println("fragment title "+argString+" "+String.valueOf(page) );

        cf = new CommonFunction(getActivity());

        //getActivity().getResources().getInteger(R.id.listvalue);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /* argString = getArguments().getString(
                "catId");*/

        System.out.println("fragment cat Id "+argString);
        //cf = new CommonFunction(getActivity());
        View rootView = inflater.inflate(R.layout.activity_list,container, false);

        pb= (ProgressBar)rootView.findViewById(R.id.progressBar2);
        list = (ListView) rootView.findViewById(R.id.listvalue);
        tv_loading = (TextView) rootView.findViewById(R.id.textView);
        tv_count = (TextView)rootView.findViewById(R.id.txtrareview);
        tv_rateview= (TextView)rootView.findViewById(R.id.txtsar);
        L1 = (LinearLayout)rootView.findViewById(R.id.v1);
        check =(Button)rootView.findViewById(R.id.btn_checkout);
        //plus = (Button)rootView.findViewById(R.id.btnplus);
        //minus = (Button)rootView.findViewById(R.id.btnminus);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getActivity(), CartView.class);
                startActivity(in);System.out.println("INT=="+in);
            }
        });
        ListTask listTask= new ListTask(getActivity());


            listTask.inPuts(getActivity(), argString, list, pb, tv_loading,check);



        return rootView;
    }






}
