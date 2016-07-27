package com.mst.mutirestaurant.adapter;

public class GridItem {
    private String image;
    private String title;
    private  String stid;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return stid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setstid(String stid) {
        this.stid = stid;
    }
}