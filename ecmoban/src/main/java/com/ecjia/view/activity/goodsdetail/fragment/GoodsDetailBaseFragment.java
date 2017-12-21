package com.ecjia.view.activity.goodsdetail.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ecjia.base.NewBaseFragment;
import com.ecjia.base.ECJiaApplication;
import com.ecjia.view.activity.GoodsDetailActivity;

/**
 * Created by Adam on 2016/1/12.
 */
public class GoodsDetailBaseFragment extends NewBaseFragment {

    public ECJiaApplication mApp;
    public GoodsDetailActivity parentActivity;

    public Resources resources;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (GoodsDetailActivity) getActivity();
        mApp = (ECJiaApplication) parentActivity.getApplication();
        resources = getResources();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void cancelLoadingDialog() {
        if (!getActivity().isFinishing()) {
        }
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
