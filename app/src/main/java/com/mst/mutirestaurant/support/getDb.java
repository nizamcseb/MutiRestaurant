package com.mst.mutirestaurant.support;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Vignesh Arunagiri on 09-11-2015.
 */
public class getDb {

    String Name;
    String Mobile;
    String Email;
    String Password;;
    String Userid;
    String Status;
    DataBase db;
    String address = "";
    String city = "";
    Double latitiude;
    Double longitude;
    String storeid="";
    String storename="";
    String deliaddress="";
    String storelocation ="";
    Double Storelat;
    Double Storelng;

    public getDb(Context context){

        try {
             db=new DataBase(context);
            db.CreateTable(2);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Login + " where status ='1' ", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                Email = c.getString(c.getColumnIndex("email"));
                Name = c.getString(c.getColumnIndex("name"));
                Password = c.getString(c.getColumnIndex("password"));
                Mobile = c.getString(c.getColumnIndex("mobilenum"));
                Userid = c.getString(c.getColumnIndex("userid"));
                System.out.println("Getting local db" + Email + " " + " " + Name + " " + Password + " " + Mobile + " " + Userid);
            }
        } catch (Exception e) {
            System.out.println("Getting details" + e.getMessage());
        }
        try {
            db=new DataBase(context);
            db.CreateTable(3);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Deliveryaddress + " where status = '1' ORDER BY lid DESC", null);
            System.out.println("countdb==" + c.getCount());
            if (c.getCount() > 0) {
                c.moveToFirst();
                city = c.getString(c.getColumnIndex("city"));
                deliaddress =c.getString(c.getColumnIndex("address"));
                latitiude = c.getDouble(c.getColumnIndex("latitude"));
                longitude = c.getDouble(c.getColumnIndex("longitude"));


                System.out.println("Getting local db" + city + " "+deliaddress+"  "+latitiude+" "+longitude);
            }
        } catch (Exception e) {
            System.out.println("Getting details" + e.getMessage());
        }
        try {
            db=new DataBase(context);
            db.CreateTable(5);
            Cursor c = db.restaurant.rawQuery("select * from " + db.Store + " where status = '1' ORDER BY lid DESC", null);
            System.out.println("countdb==" + c.getCount());
            if (c.getCount() > 0) {
                c.moveToFirst();
                address = c.getString(c.getColumnIndex("address"));
                Storelat = c.getDouble(c.getColumnIndex("storelat"));
                Storelat = c.getDouble(c.getColumnIndex("storelng"));
                storeid = c.getString(c.getColumnIndex("storeid"));
                storename = c.getString(c.getColumnIndex("storename"));
                storelocation = c.getString(c.getColumnIndex("stolocation"));
                System.out.println("Getting loca12l db" + Storelat + " " + " " + address+"  "+storeid+" "+storename+"  "+storelocation);
            }
        } catch (Exception e) {
            System.out.println("Getting details" + e.getMessage());
        }

    }



    public boolean isLogin() {
        boolean log_condition = false;
        db.CreateTable(2);
        Cursor c = db.restaurant.rawQuery("select * from " + db.Login + " where status ='1' ", null);
        if (c.getCount() > 0) {
            log_condition = true;
        } else {
            log_condition = false;
        }
        return log_condition;
    }

    public String getMobile(){

        return Mobile;
    }
    public String getEmail(){

        return Email;
    }
    public String getName(){

        return Name;
    }
    public String getPassword(){

        return Password;
    }
    public String getUserid(){

        return Userid;
    }
    public String getStatus(){

        return Status;
    }

    public String getCity(){

        return city;
    }
    public String getAddress(){

        return address;
    }
    public String getDeliaddress(){

        return deliaddress;
    }
    public Double getLattitude(){

        return latitiude;
    }
    public Double getLongitude(){

        return longitude;
    }
    public String getStoreid(){

        return storeid;
    }

    public String getStorename(){

        return storename;
    }
    public String getStoreLocation(){

        return storelocation;
    }
    public Double getStorelat(){

        return Storelat;
    }
    public Double getStoreLng(){

        return Storelng;
    }

}
