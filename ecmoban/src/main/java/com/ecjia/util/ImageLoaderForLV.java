package com.ecjia.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Adam on 2015/2/4.
 */
public class ImageLoaderForLV extends LoaderImage {

    private DisplayImageOptions options;
    private static Context mContext;
    private static ImageLoaderForLV imageLoaderForLV;

    public static ImageLoaderForLV getInstance(Context context) {
        if (imageLoaderForLV == null) {
            imageLoaderForLV = new ImageLoaderForLV();
        }
        mContext = context;
        return imageLoaderForLV;
    }

    public static ImageLoaderForLV getInstance() {
        if (imageLoaderForLV == null) {
            imageLoaderForLV = new ImageLoaderForLV();
        }
        return imageLoaderForLV;
    }

    @Override
    public void setImageRes(ImageView imageView, String url) {
        if (!(imageView == null || url == null)) {
            if (!url.equals((String)imageView.getTag())) {
                ImageLoader.getInstance().displayImage(url, imageView);
                imageView.setTag(url);
            }
        }
    }

    @Override
    public void setImageRes(ImageView imageView, String url,int type) {
        options=buildOptions(type);
        if (!(imageView == null || url == null)) {
            if (!url.equals((String)imageView.getTag())) {
                ImageLoader.getInstance().displayImage(url, imageView,options);
                imageView.setTag(url);
            }
        }
    }

    @Override
    public void setImageRes(ImageView imageView, int resource) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setImageRes(ImageView imageView, String url, Object anything) {
        // TODO Auto-generated method stub

    }

    @Override
    public Drawable loadImageSync(String url) {
        if(url!=null){
            return new BitmapDrawable(ImageLoader.getInstance().loadImageSync(url));
        }
        return null;
    }

    private DisplayImageOptions buildOptions(int type) {
        switch (type){
            case AppConst.NORMALIMAGE:
                options=new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.default_image)
                        .showImageOnFail(R.drawable.default_image)
                        .showImageForEmptyUri(R.drawable.default_image)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                break;
            case AppConst.SHOPINFOIMAGE:
                options=new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.icon_minefragment_help)
                        .showImageOnFail(R.drawable.icon_minefragment_help)
                        .showImageForEmptyUri(R.drawable.icon_minefragment_help)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                break;
            case AppConst.USERAVATER:
                options=new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.profile_no_avarta_icon_light)
                        .showImageOnFail(R.drawable.profile_no_avarta_icon_light)
                        .showImageForEmptyUri(R.drawable.profile_no_avarta_icon_light)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                break;
            default:
                options=new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.default_image)
                        .showImageOnFail(R.drawable.default_image)
                        .showImageForEmptyUri(R.drawable.default_image)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                break;
        }

        return options;
    }

    public void setImageResnoTag(ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }
}