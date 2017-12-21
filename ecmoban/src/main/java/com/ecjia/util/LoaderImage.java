package com.ecjia.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by Adam on 2015/2/4.
 */
public abstract class LoaderImage {

    public abstract  void setImageRes(ImageView imageView,String url);
    public abstract  void setImageRes(ImageView imageView,String url,int type);
    public abstract void setImageRes(ImageView imageView,int resource);
    public abstract void setImageRes(ImageView imageView,String url,Object anything);
    public abstract Drawable loadImageSync(String url);
}
