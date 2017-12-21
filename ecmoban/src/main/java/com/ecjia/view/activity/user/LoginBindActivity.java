package com.ecjia.view.activity.user;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.LoginModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.CONFIG;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class LoginBindActivity extends BaseActivity implements OnClickListener, TextWatcher,
        HttpResponse {

    private Button login;
    private EditText userName;
    private EditText password;
    private CheckBox show_password;

    private String name;
    private String psd;
    private String openid;
    private String code;

    private LoginModel loginModel;
    SharedPreferences  shared;
    SharedPreferences.Editor  editor;

    private LinearLayout root_view;
    private View buttom_view;
    private ImageView login_view;
    private CONFIG config;
    private int unpress_color, pressed_color;
    private int old=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_login_bind);
        initTopView();

        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        config=mApp.getConfig();

        if (config != null && !TextUtils.isEmpty(config.getMobile_phone_login_bgcolor())) {
            unpress_color = Color.parseColor(config.getMobile_phone_login_bgcolor());
        } else {
            unpress_color = Color.parseColor("#ff999999");
        }

        login_view = (ImageView) findViewById(R.id.login_view);
        login_view.setVisibility(View.INVISIBLE);

        ImageLoader.getInstance().displayImage("file:///" + AndroidManager.LOGIN_BG, login_view, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                login_view.setVisibility(View.VISIBLE);
                login_view.setImageResource(R.drawable.login_defaultbg);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                login_view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        EventBus.getDefault().register(this);

        loginModel = new LoginModel(LoginBindActivity.this);
        loginModel.addResponseListener(this);
        root_view = (LinearLayout) findViewById(R.id.root_view);
        buttom_view = findViewById(R.id.buttom_view);

        login = (Button) findViewById(R.id.login_login);
        userName = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        userName.addTextChangedListener(this);
        password.addTextChangedListener(this);
        login.setOnClickListener(this);

        show_password = (CheckBox) findViewById(R.id.login_show_pwd);
        show_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                if(password.length()>0){
                    password.setSelection(password.length());
                }
            }
        });

        controlKeyboardLayout(root_view, buttom_view);

        if (AppConst.THIRD_QQ.equals(shared.getString("thirdWay", ""))) {
            openid=shared.getString("qq_id", "");
            code= AppConst.THIRD_QQ;
        } else if (AppConst.THIRD_WX.equals(shared.getString("thirdWay", ""))) {
            openid=shared.getString("wx_id", "");
            code= AppConst.THIRD_WX;
        }

    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.login_topview);
        topView.setBackgroundColor(Color.parseColor("#ffffff"));
        topView.setTopTextColor(getResources().getColor(R.color._333333));
        topView.setTitleText(R.string.login_bind);
        topView.setLeftBackImage(R.drawable.login_back_black, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CloseKeyBoard();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

    }

    @Override
    public void onClick(View v) {
        String usern = res.getString(R.string.register_user_name_cannot_be_empty);
        String pass = res.getString(R.string.register_password_cannot_be_empty);
        String check_the_network = res.getString(R.string.check_the_network);
        Intent intent;
        switch (v.getId()) {
            case R.id.login_login:
                name = userName.getText().toString();
                psd = password.getText().toString();

                if ("".equals(name)) {
                    ToastView toast = new ToastView(this, usern);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if ("".equals(psd)) {
                    ToastView toast = new ToastView(this, pass);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    loginModel.ThirdLoginBind(name, psd, openid, code);
                    CloseKeyBoard();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
        return true;
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        userName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
    }

    public void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
                    int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    if(scrollHeight>old){
                        old=scrollHeight;
                        root.scrollTo(0, scrollHeight);
                    }

                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                    old=0;
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        if (userName.getText().toString().length() > 0 && password.getText().toString().length()
                >= 1) {
            login.setEnabled(true);
            login.setTextColor(Color.parseColor("#ffffffff"));
            login.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            login.setEnabled(false);
            login.setTextColor(unpress_color);
            login.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (userName.getText().toString().length() > 0 && password.getText().toString().length()
                >= 1) {
            login.setEnabled(true);
            login.setTextColor(Color.parseColor("#ffffffff"));
            login.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            login.setEnabled(false);
            login.setTextColor(unpress_color);
            login.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (userName.getText().toString().length() > 0 && password.getText().toString().length()
                >= 1) {
            login.setEnabled(true);
            login.setTextColor(Color.parseColor("#ffffffff"));
            login.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            login.setEnabled(false);
            login.setTextColor(unpress_color);
            login.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String invalid_password = res.getString(R.string.login_invalid_password);
        String welcome = res.getString(R.string.login_welcome);
        if (url.equals(ProtocolConst.CONNECT_BIND)) {
            if (status.getSucceed() == 1) {
                setResult(RESULT_OK);
                finish();
            }else{
                ToastView toast = new ToastView(LoginBindActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }


}
