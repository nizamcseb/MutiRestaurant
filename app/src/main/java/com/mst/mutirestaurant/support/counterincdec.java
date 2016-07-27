package com.mst.mutirestaurant.support;

/**
 * Created by pc on 17-Nov-15.
 */
public class counterincdec {
    String prodid = null;
    String prodname = null;
    String prodimg = null;
    String prodrate = null;
    String[] pid=null;

    public counterincdec(String prodid, String prodname, String prodrate) {
        super();
        this.prodid = prodid;
        this.prodname = prodname;
        this.prodimg = prodimg;
        this.prodrate = prodrate;
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
}
