package com.ecjia.base;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ECJiaTopView;

/**
 * Created by Adam on 2016-06-14.
 */
public class BaseFragmentActivity extends FragmentActivity {

    public Resources resources;
    public ECJiaApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (ECJiaApplication) getApplication();
        resources = getResources();
    }

    protected final int BACKIMAGE_ID = R.drawable.header_back_arrow;

    public ECJiaTopView topView;
    protected void initTopView(){

    }



    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
