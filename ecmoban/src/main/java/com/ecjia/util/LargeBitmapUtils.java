package com.ecjia.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ecmoban.android.sijiqing.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

/**
 * Created by Administrator on 2015/9/14.
 */
public class LargeBitmapUtils {
    Drawable d=null;
    private BitmapDisplayConfig config;
    private static LargeBitmapUtils instance=null;
    BitmapUtils bitmapUtils;
    public static LargeBitmapUtils getInstance(Context context){
        if(null==instance){
            instance=new LargeBitmapUtils();
            instance.d=context.getResources().getDrawable(R.drawable.default_image);
            instance.bitmapUtils=new BitmapUtils(context);
            instance.bitmapUtils.configMemoryCacheEnabled(true);
            instance.bitmapUtils.configDiskCacheEnabled(false);
            instance.bitmapUtils.configDefaultLoadingImage(instance.d);
            instance.bitmapUtils.configDefaultLoadFailedImage(instance.d);
            instance.config=new BitmapDisplayConfig();
        }
        return instance;
    }
    public  <T extends android.view.View>  void displayImage(T container,String url,boolean option){
        if(option) {
            config.setBitmapConfig(Bitmap.Config.RGB_565);
            if (!(container == null || url == null)) {
                if (!url.equals((String)container.getTag())) {
                    instance.bitmapUtils.display(container,url);
                    container.setTag(url);
                }
            }
        }else{
            if (!(container == null || url == null)) {
                if (!url.equals((String)container.getTag())) {
                    instance.bitmapUtils.display(container,url);
                    container.setTag(url);
                }
            }
        }
    }

    public  <T extends android.view.View>  void displayImage(T container,String url){
        if (!(container == null || url == null)) {
            if (!url.equals((String)container.getTag())) {
                LG.e("=====url======"+url+"----"+(String)container.getTag());
                instance.bitmapUtils.display(container,url);
                container.setTag(url);
            }
        }
    }

}
