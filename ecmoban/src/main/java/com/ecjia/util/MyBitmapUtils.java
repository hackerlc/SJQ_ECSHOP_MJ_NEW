package com.ecjia.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.ecmoban.android.sijiqing.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

/**
 * Created by Administrator on 2015/4/29.
 */
public class MyBitmapUtils {
    Drawable d = null;
    private BitmapDisplayConfig config;
    private static MyBitmapUtils instance = null;
    private BitmapDisplayConfig circleconfig;
    private BitmapDisplayConfig tanconfig1;
    private BitmapDisplayConfig tanconfig2;
    BitmapUtils bitmapUtils;

    public static MyBitmapUtils getInstance(Context context) {
        if (null == instance) {
            instance = new MyBitmapUtils();
            instance.d = context.getResources().getDrawable(R.drawable.default_image);
            instance.bitmapUtils = new BitmapUtils(context);
            instance.bitmapUtils.configMemoryCacheEnabled(true);
            instance.bitmapUtils.configDiskCacheEnabled(false);
            instance.bitmapUtils.configDefaultLoadingImage(instance.d);
            instance.bitmapUtils.configDefaultLoadFailedImage(instance.d);
            instance.config = new BitmapDisplayConfig();
            instance.circleconfig = new BitmapDisplayConfig();
            instance.circleconfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.default_circle_image));
            instance.circleconfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.default_circle_image));

            instance.tanconfig1 = new BitmapDisplayConfig();
            instance.tanconfig1.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.default_image));
            instance.tanconfig1.setLoadingDrawable(context.getResources().getDrawable(R.drawable.default_image));

            instance.tanconfig2 = new BitmapDisplayConfig();
            instance.tanconfig2.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.default_image));
            instance.tanconfig2.setLoadingDrawable(context.getResources().getDrawable(R.drawable.default_image));
        }
        return instance;
    }

    public <T extends android.view.View> void displayImage(T container, String url, boolean option) {
        if (option) {
            config.setBitmapConfig(Bitmap.Config.RGB_565);
            if (!(container == null || url == null)) {
                if (!url.equals((String) container.getTag())) {
                    instance.bitmapUtils.display(container, url);
                    container.setTag(url);
                }
            }
        } else {
            if (!(container == null || url == null)) {
                if (!url.equals((String) container.getTag())) {
                    instance.bitmapUtils.display(container, url);
                    container.setTag(url);
                }
            }
        }
    }

    public <T extends android.view.View> void displayImage(T container, String url) {
        if (!(container == null || url == null)) {
            if (!url.equals((String) container.getTag())) {
                instance.bitmapUtils.display(container, url);
                container.setTag(url);
            }
        }
    }

    public <T extends android.view.View> void displayCircleImage(T container, String url) {
        if (!(container == null || url == null)) {
            if (!url.equals((String) container.getTag())) {
                bitmapUtils.display(container, url, instance.circleconfig);
                container.setTag(url);
            }
        }
    }

    public <T extends android.view.View> void displayTanImage1(T container, String url) {
        if (!(container == null || url == null)) {
            if (!url.equals((String) container.getTag())) {
                bitmapUtils.display(container, url, instance.tanconfig1);
                container.setTag(url);
            }
        }
    }

    public <T extends android.view.View> void displayTanImage2(T container, String url) {
        if (!(container == null || url == null)) {
            if (!url.equals((String) container.getTag())) {
                bitmapUtils.display(container, url, instance.tanconfig2);
                container.setTag(url);
            }
        }
    }

}
