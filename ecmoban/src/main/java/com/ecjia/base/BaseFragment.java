package com.ecjia.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.util.EventBus.MyEvent;

/**
 * Created by Administrator on 2015/7/22.
 */
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    public ECJiaApplication mApp;
    public ECJiaMainActivity parentActivity;

    public Resources resources;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (ECJiaMainActivity) getActivity();
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    public abstract void onEvent(MyEvent event);
}