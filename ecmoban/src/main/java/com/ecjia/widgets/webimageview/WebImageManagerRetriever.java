package com.ecjia.widgets.webimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 异步任务类加载图片
 *
 * @author Administrator
 */
public class WebImageManagerRetriever extends AsyncTask<Void, Void, Bitmap> {
    private final static String TAG = WebImageManagerRetriever.class.getSimpleName();

    // cache
    private static WebImageCache mCache;

    // what we're looking for
    private Context mContext;
    private String mURLString;
    private int mDiskCacheTimeoutInSeconds = -1;
    private OnWebImageLoadListener mListener;

    static {
        mCache = new WebImageCache();
    }

    public WebImageManagerRetriever(Context context, String urlString, int diskCacheTimeoutInSeconds, OnWebImageLoadListener listener) {
        mContext = context;
        mURLString = urlString;
        mDiskCacheTimeoutInSeconds = diskCacheTimeoutInSeconds;
        mListener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        // check mem cache first
        Bitmap bitmap = mCache.getBitmapFromMemCache(mURLString);

        // check disk cache first
        if (bitmap == null) {
            bitmap = mCache.getBitmapFromDiskCache(mContext, mURLString, mDiskCacheTimeoutInSeconds);
            mCache.addBitmapToMemCache(mURLString, bitmap);
        }

        if (bitmap == null) {
            InputStream is = null;
            FlushedInputStream fis = null;

            try {
                URL url = new URL(mURLString);
                URLConnection conn = url.openConnection();

                is = conn.getInputStream();
                int length = is.available();


                fis = new FlushedInputStream(is);

                /**
                 * 优化图片
                 */

                bitmap = BitmapFactory.decodeStream(fis);

                // cache
                if (bitmap != null) {
                    mCache.addBitmapToCache(mContext, mURLString, bitmap);
                }
            } catch (Exception ex) {
                Log.e(TAG, "Error loading image from URL " + mURLString + ": " + ex.toString());
            } finally {
                try {
                    is.close();
                } catch (Exception ex) {
                }
            }
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        // complete!
        if (null != mListener) {
            if (null == bitmap) {
                mListener.onWebImageError();
            } else {
                mListener.onWebImageLoad(mURLString, bitmap);
            }
        }
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;

            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);

                if (bytesSkipped == 0L) {
                    int b = read();

                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }

                totalBytesSkipped += bytesSkipped;
            }

            return totalBytesSkipped;
        }
    }

    //图片优化
    public Bitmap alterimage(InputStream is) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        Bitmap btp = BitmapFactory.decodeStream(is, null, options);
        return btp;
    }

    public interface OnWebImageLoadListener {
        public void onWebImageLoad(String url, Bitmap bitmap);

        public void onWebImageError();
    }
}
