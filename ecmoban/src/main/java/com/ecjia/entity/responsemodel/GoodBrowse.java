package com.ecjia.entity.responsemodel;

public class GoodBrowse {
    private Integer goodid;
    private String goodname;
    private String goodprice;
    private float goodrating;
    private byte[] goodimage;

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    private String market_price;

    private String activity_type;
    private String formatted_saving_price; //已省xx元
    private int saving_price;

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getFormatted_saving_price() {
        return formatted_saving_price;
    }

    public void setFormatted_saving_price(String formatted_saving_price) {
        this.formatted_saving_price = formatted_saving_price;
    }

    public int getSaving_price() {
        return saving_price;
    }

    public void setSaving_price(int saving_price) {
        this.saving_price = saving_price;
    }

    public Integer getGoodid() {
        return goodid;
    }

    public void setGoodid(Integer goodid) {
        this.goodid = goodid;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getGoodprice() {
        return goodprice;
    }

    public void setGoodprice(String goodprice) {
        this.goodprice = goodprice;
    }

    public float getGoodrating() {
        return goodrating;
    }

    public void setGoodrating(float goodrating) {
        this.goodrating = goodrating;
    }

    public byte[] getGoodimage() {
        return goodimage;
    }

    public void setGoodimage(byte[] goodimage) {
        this.goodimage = goodimage;
    }


}
