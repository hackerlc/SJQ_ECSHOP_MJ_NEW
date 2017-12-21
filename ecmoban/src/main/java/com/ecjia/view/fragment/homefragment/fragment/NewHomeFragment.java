package com.ecjia.view.fragment.homefragment.fragment;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.base.NewBaseFragment;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.activity.MyCaptureActivity;
import com.ecjia.view.activity.SearchNewActivity;
import com.ecjia.view.activity.chooseCity.CityActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecjia.view.adapter.MsgSql;
import com.ecjia.view.fragment.TabsFragment;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.NewHomeFragmentBinding;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;

public class NewHomeFragment extends NewBaseFragment implements AppConst.RegisterApp,View.OnClickListener {
    NewHomeFragmentBinding mBinding;
    NewGoodShopFragment goodShopFragment;
    public String openTypeCategoryId;
    private SharedPreferences shared;
    private String localName;

    private View mainView;

    ECJiaDealUtil ecJiaDealUtil;
    TabsFragment.FragmentListener fragmentListener;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        EventBus.getDefault().register(this);
        RxBus.getInstance().register(this);
        mActivity = activity;
        fragmentListener = (TabsFragment.FragmentListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ecJiaDealUtil = new ECJiaDealUtil(mActivity);
    }

    private TextView cityName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding= DataBindingUtil.inflate(inflater, R.layout.new_home_fragment,container,false);
        mBinding.setOnClick(this);
        if (null != mainView) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (null != parent) {
                parent.removeView(mainView);
            }
        } else {
            mainView = mBinding.getRoot();
            initTopView();
        }
        cityName = (TextView) mainView.findViewById(R.id.city_name);
        changeHandler("city");

        return mainView;
    }

    private void initView() {

        if(goodShopFragment==null) {
            goodShopFragment = new NewGoodShopFragment();
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(mBinding.homeGoodShop.getId(), goodShopFragment).commit();
    }


    private void initTopView() {
        shared = mActivity.getSharedPreferences(SharedPKeywords.SPUSER, 0);
//        localName=shared.getString(SharedPKeywords.LOCAL_NAME, "");
//        if("".equals(localName)){
//            //弹出选择城市
//            startActivity(new Intent(mActivity,CityActivity.class));
//        }else{
//            ECJiaApplication.cityName=localName;
//            DEVICE.getInstance().setCity(shared.getString(SharedPKeywords.LOCAL_ALIAS,""));
//        }
    }


    public boolean isActive = false;
    public Bitmap bitmap;

    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            AppConst.registerApp(this);
        }
        MobclickAgent.onPageStart("Home");

        getMessageNumber();
        initView();
    }

    private void getMessageNumber() {
        int messageNum = MsgSql.getIntence(getActivity()).getUnReadMsgCount();
        if (messageNum == 0) {
            mBinding.messageNum.setVisibility(View.GONE);
        } else {
            mBinding.messageNum.setVisibility(View.VISIBLE);
            mBinding.messageNum.setText(messageNum + "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Home");
    }


    public void onEvent(MyEvent event) {

        if (event.getMsg().equals(EventKeywords.MESSAGE)) {
            mBinding.messageNum.setText(event.getmTag() + "");
        }

        if (event.getMsg().equals(EventKeywords.UPDATE_MESSAGE)) {
            getMessageNumber();
        }

    }

    @Override
    public void onRegisterResponse(boolean success) {

    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        int j = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            // app 进入后台
            isActive = false;
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) mActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mActivity.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        RxBus.getInstance().unRegister(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_item:
                Intent intent = new Intent(NewHomeFragment.this.mActivity, CityActivity.class);
                intent.putExtra("chooseagain", "chooseagain");
                startActivityForResult(intent, 100);
                mActivity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                break;
            case R.id.search_input:
                Intent intent2 = new Intent();
                intent2.setClass(mActivity, SearchNewActivity.class);
                intent2.putExtra("filter", new FILTER().toString());
                startActivityForResult(intent2, 100);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.ll_div:
                startActivity(new Intent(getActivity(), PushActivity.class));
                    mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.ll_div_scan:
                startActivity(new Intent(getActivity(), MyCaptureActivity.class));
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
    }

    @Subscribe(tag = RxBus.TAG_CHANGE)
    public void changeHandler(String str){
        if("city".equals(str)) {
            String city = ECJiaApplication.cityName;
            if("".equals(city)){
                city = "浙江";
            }
            if(city.length() >=2){
                city = city.substring(0, 2);
            }
            cityName.setText(city);
        }
    }
}
