package com.ecjia.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ecjia.view.ECJiaMainActivity;

/**
 * Created by Adam on 2016/1/12.
 */
public class ECJiaBaseFragment extends Fragment {

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
}
