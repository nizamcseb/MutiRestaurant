package com.mst.mutirestaurant.support;

/**
 * Created by blync on 11/25/2015.
 */
public class yourorder {

    String prodid = null;
    String prodname = null;
    String prodimg = null;
    String prodrate = null;
    String total = null;
    String[] pid=null;
    String qty =null;

    public yourorder(String prodid, String prodname, String prodrate,String qty) {
        super();
        this.prodid = prodid;
        this.prodname = prodname;
        this.prodimg = prodimg;
        this.prodrate = prodrate;
        this.total = total;
        this.qty = qty;
        this.pid=pid;
    }

    public String getProdid() {
        return prodid;
    }
    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getProdname() {
        return prodname;
    }
    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getProdimg() {
        return prodimg;
    }
    public void setProdimg(String prodimg) {
        this.prodimg = prodimg;
    }

    public String getProdrate() {
        return prodrate;
    }
    public void setProdrate(String prodrate) {
        this.prodrate = prodrate;
    }

    public String[] getPid() {
        return pid;
    }
    public void setPid(String[] pid) {
        this.pid = pid;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }

    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }

}


