package com.ecjia.util.gallery;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.ecmoban.android.sijiqing.R;

import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by Hank on 2016/5/1.
 */
public class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(R.drawable.default_circle_image)
                .error(R.drawable.default_circle_image)
//                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                .centerCrop()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public void setRequest(Request request) {
                        imageView.setTag(R.id.adapter_item_tag_key, request);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                    }
                });
    }

    @Override
    public void clearMemoryCache() {

    }
}