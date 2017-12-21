package com.ecjia.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Adam on 2016/9/6.
 */
@SuppressLint("ParcelCreator")
public class ECJiaBaseIntent extends Intent {

    public static final String ACTIVITY_NAME = "activity_name";

    public ECJiaBaseIntent() {
    }

    public ECJiaBaseIntent(Intent o) {
        super(o);
    }

    public ECJiaBaseIntent(String action) {
        super(action);
    }

    public ECJiaBaseIntent(String action, Uri uri) {
        super(action, uri);
    }

    public ECJiaBaseIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
        putExtra(ACTIVITY_NAME, packageContext.getClass().getName());

    }

    public ECJiaBaseIntent(String action, Uri uri, Context packageContext, Class<?> cls) {
        super(action, uri, packageContext, cls);
        putExtra(ACTIVITY_NAME, packageContext.getClass().getName());
    }


    public String getAcitivityName() {
        return getStringExtra(ACTIVITY_NAME);
    }
}
