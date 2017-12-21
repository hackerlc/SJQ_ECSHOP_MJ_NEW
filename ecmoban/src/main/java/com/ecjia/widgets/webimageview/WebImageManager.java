package com.ecjia.widgets.webimageview;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;

import com.ecjia.util.LG;

public class WebImageManager implements WebImageManagerRetriever.OnWebImageLoadListener {
	private static WebImageManager mInstance = null;
	
	// TODO: pool retrievers
	
	// views waiting for an image to load in
	private Map<String, WebImageManagerRetriever> mRetrievers;
	private Map<WebImageManagerRetriever, Set<WebImageView>> mRetrieverWaiters;
	private Set<WebImageView> mWaiters;
	
	public static WebImageManager getInstance()
    {
		if (mInstance == null)
        {
			mInstance = new WebImageManager();
		}
		
		return mInstance;
	}
	
	public WebImageManager()
    {
		mRetrievers = new HashMap<String, WebImageManagerRetriever>();
		mRetrieverWaiters = new HashMap<WebImageManagerRetriever, Set<WebImageView>>();
		mWaiters = new HashSet<WebImageView>();
	}

	public void downloadURL(Context context, String urlString, final WebImageView view, int diskCacheTimeoutInSeconds) {
		WebImageManagerRetriever retriever = mRetrievers.get(urlString);

		if (mRetrievers.get(urlString) == null)
        {
			retriever = new WebImageManagerRetriever(context, urlString, diskCacheTimeoutInSeconds, this);
			mRetrievers.put(urlString, retriever);
			mWaiters.add(view);

			Set<WebImageView> views = new HashSet<WebImageView>();
			views.add(view);
			mRetrieverWaiters.put(retriever, views);
			
			// start!
			retriever.execute();
		}
        else
        {
			mRetrieverWaiters.get(retriever).add(view);
			mWaiters.add(view);
		}
	}

    public void reportImageLoad(String urlString, Bitmap bitmap)
    {
        if (!mRetrievers.containsKey(urlString))
        {
            return;
        }
        WebImageManagerRetriever retriever = mRetrievers.get(urlString);

        for (WebImageView iWebImageView : mRetrieverWaiters.get(retriever))
        {
            if (mWaiters.contains(iWebImageView))
            {
                iWebImageView.setImageBitmap(bitmap);
                mWaiters.remove(iWebImageView);
            }
            else
            {
                LG.i("************error********");
            }
        }

        mRetrievers.remove(urlString);
        mRetrieverWaiters.remove(retriever);
    }

	public void cancelForWebImageView(WebImageView view)
    {
        String urlString = view.urlString;
        WebImageManagerRetriever retriever = mRetrievers.get(urlString);
        Set<WebImageView> waitSet = mRetrieverWaiters.get(retriever);

        if (null != waitSet)
        {
            if ( waitSet.size() > 1)
            {
                waitSet.remove(view);
                mRetrieverWaiters.put(retriever,waitSet);
                mWaiters.remove(view);
            }
            else
            {
                if (mWaiters.contains(view))
                {
                    mRetrievers.remove(urlString);
                    mRetrieverWaiters.remove(retriever);
                    mWaiters.remove(view);
                    retriever.cancel(true);
                }

            }

        }
        else
        {
            mRetrievers.remove(urlString);
            mRetrieverWaiters.remove(retriever);
            mWaiters.remove(view);
        }
		// TODO: cancel connection in progress, too

	}

    @Override
    public void onWebImageLoad(String url, Bitmap bitmap)
    {
        reportImageLoad(url, bitmap);
    }

    @Override
    public void onWebImageError()
    {

    }
}
