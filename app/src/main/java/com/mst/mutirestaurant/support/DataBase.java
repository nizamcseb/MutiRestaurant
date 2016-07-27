package com.mst.mutirestaurant.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ha on 10/21/15 AD.
 */
public class DataBase {

    public String MY_DATABASE_NAME = "restaurantdatabase.db";
    public static final String Registration = "Registration_Credentials";
    public static final String Login = "Login_Credentials";
    public static final String Deliveryaddress = "Delivery_Address";
    public static final String Cart = "Cart_prod";
    public static final String Store = "Store_Credential";
    public static SQLiteDatabase restaurant;
    Context con;

    public DataBase(Context con)
    {
        restaurant = con.openOrCreateDatabase(MY_DATABASE_NAME, 1, null);
        this.con = con;

    }

    public void CreateTable(int i) {

        switch (i) {
            case 1:
                /* Registration Credentials */
                try {
                    restaurant.execSQL("create table if not exists "
                            + Registration
                            + " (rid integer primary key autoincrement,name varchar(50), mobilenum varcahr(50),email varchar(50),userid varchar(50),password varchar2(50))");
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("Reg table creation"+e.getMessage());
                    e.printStackTrace();
                }
                break;

            case 2:
                /* Login Credentials */
                try {
                    restaurant.execSQL("create table if not exists "
                            + Login
                            + " (lid integer primary key autoincrement,userid varchar(50),email varchar(50),password varchar2(50),status varchar(50),name varchar(50), mobilenum varcahr(50))");
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("log table creation"+e.getMessage());
                    e.printStackTrace();
                }
                break;

            case 3:
                /* deliveryaddress Credentials */
                try {
                    restaurant.execSQL("create table if not exists "
                            + Deliveryaddress
                            + " (lid integer primary key autoincrement,city varchar,address varchar,status varchar,latitude varchar, longitude varchar,storeid varchar)");
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("log table creation"+e.getMessage());
                    e.printStackTrace();
                }
                break;
            case 4:
                /* Cart Credentials */
                try {
                    restaurant.execSQL("create table if not exists "
                            + Cart
                            + " (lid integer primary key autoincrement,prodid varchar ,prodname varchar ,prodrate varchar ,produrl varchar, prodqty varchar, prodtotal varchar,total varchar )");
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("cart table=="+e.getMessage());
                    e.printStackTrace();
                }
                break;
            case 5:
                /* Store Credentials */
                try {
                    restaurant.execSQL("create table if not exists "
                            + Store
                            + " (lid integer primary key autoincrement,address varchar,storeid varchar,storename varchar,stolocation varchar, storelat varchar,storelng varchar,storedist varchar,status varchar)");
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("store table=="+e.getMessage());
                    e.printStackTrace();
                }
                break;

        }
    }
}
