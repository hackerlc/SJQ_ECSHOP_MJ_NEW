package com.ecjia.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.ActivityManagerModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class BaseActivity extends Activity implements Handler.Callback,HttpResponse {
    public Handler mHandler;

    public Resources res;
    public ECJiaApplication mApp;
    protected ECJiaTopView topView;
    public Activity mActivity;
    public LayoutInflater mInflater;

    public BaseActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (ECJiaApplication) getApplicationContext();
        mActivity = BaseActivity.this;
        mInflater = LayoutInflater.from(BaseActivity.this);
        PushAgent.getInstance(this).onAppStart();
        res = this.getResources();
        mHandler = new Handler(this);
        ActivityManagerModel.addLiveActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityManagerModel.addVisibleActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ActivityManagerModel.removeVisibleActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerModel.removeLiveActivity(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 添加顶部布局
     */
    public void initTopView() {

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕宽度
     */
    public int getDisplayMetricsWidth() {
        int i = getWindowManager().getDefaultDisplay().getWidth();
        int j = getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    // 关闭键盘
    protected void closeKeyBoard(EditText mEditText) {
        mEditText.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    protected final int BACKIMAGE_ID = R.drawable.header_back_arrow;

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

    }
}
