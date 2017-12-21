package com.ecjia.widgets.webimageview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.*;


public class WebImageView extends ImageView {
    public String urlString;
    AlphaAnimation animation;
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;

	public WebImageView(Context context) {
		super(context);
	}

	public WebImageView(Context context, AttributeSet attSet) {
		super(context, attSet);
        animation  = new AlphaAnimation(0, 1);
        animation.setDuration(500);//设置动画持续时间
	}
	
	public WebImageView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle);
    } 

	public static void setMemoryCachingEnabled(boolean enabled) {
		WebImageCache.setMemoryCachingEnabled(enabled);
	}

	public static void setDiskCachingEnabled(boolean enabled) {
		WebImageCache.setDiskCachingEnabled(enabled);
	}

	public static void setDiskCachingDefaultCacheTimeout(int seconds) {
		WebImageCache.setDiskCachingDefaultCacheTimeout(seconds);
	}

	@Override
	public void onDetachedFromWindow() {
		// cancel loading if view is removed
		cancelCurrentLoad();
	}

	public void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable, int diskCacheTimeoutInSeconds) {
        if (urlString != null )
        {
            if (null != this.urlString && urlString.compareTo(this.urlString) == 0)
            {
                return;
            }

            final WebImageManager mgr = WebImageManager.getInstance();

            if(null != this.urlString)
            {
            	cancelCurrentLoad();
            }

            setImageDrawable(placeholderDrawable);

            this.urlString = urlString;
            mgr.downloadURL(context, urlString, WebImageView.this, diskCacheTimeoutInSeconds);
        }

	}

	public void setImageWithURL(Context context, String urlString, Drawable placeholderDrawable) {

		setImageWithURL(context, urlString, placeholderDrawable, -1);
	}

//	public void setImageWithURL(final Context context, final String urlString, int diskCacheTimeoutInSeconds) {
//	    setImageWithURL(context, urlString, null, diskCacheTimeoutInSeconds);
//	}

    public void setImageWithURL(final Context context, final String urlString, int resId)
    {
        Resources rsrc = this.getResources();
        Drawable placeholderDrawable =  rsrc.getDrawable(resId);
        setImageWithURL(context, urlString, placeholderDrawable, -1);
    }

    public void setImageWithURL(final Context context, final String urlString, int resId,int diskCacheTimeoutInSeconds)
    {
        Resources rsrc = this.getResources();
        Drawable placeholderDrawable =  rsrc.getDrawable(resId);
        setImageWithURL(context, urlString, placeholderDrawable, diskCacheTimeoutInSeconds);
    }

	public void setImageWithURL(final Context context, final String urlString) {
            setImageWithURL(context, urlString, null, -1);
	}

	public void cancelCurrentLoad() {
	    WebImageManager mgr = WebImageManager.getInstance();

	    // cancel any existing request
	    mgr.cancelForWebImageView(this);
	}

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.startAnimation(animation);
    }
}
