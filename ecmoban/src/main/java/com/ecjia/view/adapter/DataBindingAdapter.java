package com.ecjia.view.adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.ecjia.util.ImageLoaderForLV;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/2/22 13:32.
 */

public class DataBindingAdapter {
    @BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url){
        ImageLoaderForLV.getInstance(view.getContext()).setImageRes(view, url);
    }
}
