package com.ecjia.entity.model;

import com.ecjia.entity.responsemodel.PHOTO;
import com.ecjia.entity.responsemodel.PLAYER;
import com.ecjia.entity.responsemodel.QUICK;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/3/16 14:12.
 */

public class WholesaleTop {

    @SerializedName("banner")
    protected List<QUICK> banners;
    protected List<Category> category;

    public List<QUICK> getBanners() {
        return banners;
    }

    public void setBanners(List<QUICK> banners) {
        this.banners = banners;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    /**
     * banner需要类型转换
     * @return
     */
    public List<PLAYER> getPlayer(){
        List<PLAYER> players =new ArrayList<>();
        if(banners!=null && banners.size()>0){
            for(QUICK banner:banners){
                PLAYER player =new PLAYER();
                PHOTO photo =new PHOTO();
                photo.setSmall(banner.getImg());
                photo.setThumb(banner.getImg());
                photo.setUrl(banner.getImg());
                player.setPhoto(photo);

                player.setUrl(banner.getUrl());
                players.add(player);
            }
            return players;
        }else{
            return new ArrayList<>();
        }
    }
}
