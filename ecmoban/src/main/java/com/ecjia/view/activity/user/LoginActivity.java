package com.ecjia.view.activity.user;


import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.CONFIG;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.netmodle.LoginModel;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.MD5;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecjia.view.activity.AccountActivity;
import com.ecjia.view.activity.AddressManageActivity;
import com.ecjia.view.activity.MyPurseActivity;
import com.ecjia.view.activity.ShareQRCodeActivity;
import com.ecjia.view.activity.ShoppingCartActivity;
import com.ecjia.view.activity.goodsorder.OrderListActivity;
import com.ecjia.view.activity.goodsorder.OrderdetailActivity;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecmoban.android.sijiqing.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;

public class LoginActivity extends BaseActivity implements OnClickListener, TextWatcher,
        HttpResponse {

    private Button login;
    private EditText userName;
    private EditText password;
    private CheckBox show_password;

    private String name;
    private String psd;

    private LoginModel loginModel;
    private TextView getpassword,login_reg;
    SharedPreferences remdname, shared;
    SharedPreferences.Editor edit, editor;
    private String intentFlag = "";

    // 第三方登录
    // 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
    private ImageView qqlogin, wxlogin;
    //手机短信注册登录
    private MyProgressDialog pb;
    private LinearLayout root_view;
    private View buttom_view;
    private ImageView login_view;
    private CONFIG config;
    private RelativeLayout user_img_item;
    private int unpress_color, pressed_color;
    private ImageView user_head_img;
    private String uname, local_uid;//本地存储的用户名
    private boolean show_local_img = false;


    UMShareAPI mShareAPI;
    private int old=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.login);
        initTopView();

        EventBus.getDefault().register(this);
        config=mApp.getConfig();
        mShareAPI = UMShareAPI.get(this);
        login_view = (ImageView) findViewById(R.id.user_head_img);
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
        user_img_item = (RelativeLayout) findViewById(R.id.user_img_item);


        if (null == pb) {
            pb = MyProgressDialog.createDialog(this);
            pb.setMessage(res.getString(R.string.loading));
        }

        user_head_img = (ImageView) findViewById(R.id.user_head_img);

        loginModel = new LoginModel(LoginActivity.this);
        loginModel.addResponseListener(this);
        root_view = (LinearLayout) findViewById(R.id.root_view);
        buttom_view = findViewById(R.id.buttom_view);
        qqlogin = (ImageView) findViewById(R.id.qq_login);
        wxlogin = (ImageView) findViewById(R.id.wx_login);
        qqlogin.setOnClickListener(this);
        wxlogin.setOnClickListener(this);

        intentFlag = getIntent().getStringExtra("from");
        login = (Button) findViewById(R.id.login_login);
        userName = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        userName.addTextChangedListener(this);
        password.addTextChangedListener(this);
        getpassword = (TextView) findViewById(R.id.login_getpassword);
        login.setOnClickListener(this);
        getpassword.setOnClickListener(this);
        if (config != null && !TextUtils.isEmpty(config.getMobile_phone_login_bgcolor())) {
            unpress_color = Color.parseColor("#ff999999");
        } else {
            unpress_color = Color.parseColor("#ff999999");
        }

        if (AndroidManager.SUPPORT_MOBILE_SININ) {
            userName.setHint(res.getString(R.string.login_username2));
        } else {
            userName.setHint(res.getString(R.string.login_username_normal));
        }

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

        shared = getSharedPreferences("userInfo", 0);
        uname = shared.getString("uname", "");
        local_uid = shared.getString("local_uid", "");
        editor = shared.edit();

        remdname = getPreferences(Activity.MODE_APPEND);
        edit = remdname.edit();
        String name_str = remdname.getString("name", "");
        userName.setText(name_str);

        controlKeyboardLayout(root_view, buttom_view);
        show_user_Img(show_local_img);

    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.login_topview);
        topView.setTitleText(R.string.login_login);
        topView.setTopTextColor(getResources().getColor(R.color.login_bg_color));
        topView.setLeftBackImage(R.drawable.login_back, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CloseKeyBoard();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        login_reg = (TextView) findViewById(R.id.login_reg);
        login_reg.setOnClickListener(v -> {
            Intent intent;
            if (AndroidManager.SUPPORT_MOBILE_SININ) {
                intent = new Intent(LoginActivity.this, RegisterInputPhoneActivity.class);
                //                    startActivityForResult(intent, 1);
                startActivity(intent);
            } else {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_GONE);
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
                    edit.putString("name", userName.getText().toString());
                    edit.commit();
                    loginModel.login(name, psd);
                    CloseKeyBoard();
                }
                break;

            case R.id.login_getpassword:
                intent = new Intent(this, GetPasswordActivity.class);
                startActivity(intent);

                break;
            case R.id.qq_login:
                UnusualLogin(SHARE_MEDIA.QQ);


                break;
            case R.id.wx_login:

                UnusualLogin(SHARE_MEDIA.WEIXIN);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pb.isShowing()) {
            pb.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Intent intent = new Intent();
                intent.putExtra("login", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        }else  if (requestCode == 1001&&resultCode==RESULT_OK) {
            String welcome = res.getString(R.string.login_welcome);
            editor.putBoolean("thirdLog", true);
            editor.commit();
            ToastView toast = new ToastView(LoginActivity.this, welcome);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            EventBus.getDefault().post(new MyEvent("userinfo_refresh"));
            EventBus.getDefault().post(new MyEvent("refresh_price"));
            Intent intent = new Intent();
            intent.putExtra("login", true);
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
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

    /**
     * 授权
     */
    private void UnusualLogin(final SHARE_MEDIA platform) {

        mShareAPI.doOauthVerify(this, platform, new UMAuthListener() {

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                if (data != null ) {
                    //debug模式下可以打印返回参数
                    if(AndroidManager.isDebug){
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Object key = entry.getKey();
                            Object value = entry.getValue();
                            LG.e("key=" + key + " value=" + value);
                        }
                    }
                    //判断平台
                    if (platform == SHARE_MEDIA.QQ) {
                        //如果之前qq登录过，并且此次授权信息和上次一致，直接登录（存值的shared是userInfo）
                        if (!TextUtils.isEmpty(shared.getString("qq_id", "")) && shared.getString("qq_id", "").equals(data.get("openid"))) {
                            ThirdLogin(shared.getString("myscreen_name", ""), shared.getString("qq_id", ""), AppConst.THIRD_QQ);
                        } else {
                            //第一次授权成功，通过openid获取用户信息
                            getUserInfo(platform, data.get("openid"));
                        }
                    } else if (platform == SHARE_MEDIA.WEIXIN) {
                        //如果之前weixin登录过，并且此次授权信息和上次一致，直接登录
                        if (!TextUtils.isEmpty(shared.getString("wx_id", "")) && (shared.getString("wx_id", "").equals(data.get("openid"))
                                ||shared.getString("wx_id", "").equals(data.get("unionid")))) {
                            ThirdLogin(shared.getString("nick_name", ""), shared.getString("wx_id", ""), AppConst.THIRD_WX);
                        } else {
                            if (!TextUtils.isEmpty(data.get("unionid"))) {
                                //第一次授权成功，通过unionid获取用户信息
                                getUserInfo(platform, data.get("unionid"));
                            } else {
                                //第一次授权成功，通过openid获取用户信息
                                getUserInfo(platform, data.get("openid"));
                            }
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
            }


        });
    }

    /**
     * 第三方登录
     *
     * @param name
     * @param id
     */
    private void ThirdLogin(String name, String id, String type) {
        loginModel.ThirdLogin(name, id, type);
    }


    /**
     * 获取授权平台的用户信息</br>
     */
    private void getUserInfo(final SHARE_MEDIA platform, final String openid) {
        mShareAPI.getPlatformInfo(this, platform, new UMAuthListener() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int status, Map<String, String> map) {
                if (status == 2 && map != null) {
                    //debug模式下可以打印返回参数
                    if (AndroidManager.isDebug) {
                        Iterator it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Object key = entry.getKey();
                            Object value = entry.getValue();
                            LG.e("key2=" + key + " value2=" + value);
                        }
                    }
                    //判断平台
                    if (platform == SHARE_MEDIA.QQ) {
                        //第三方登录并存值
                        editor.putString("myscreen_name", map.get("screen_name").toString());  //qq上的昵称
                        editor.putString("qq_log_img", map.get("profile_image_url").toString());  //qq上的头像
                        editor.putString("qq_id", openid);  //qq的openid
                        editor.commit();
                        ThirdLogin(map.get("screen_name").toString(), openid, AppConst.THIRD_QQ);
                    } else if (platform == SHARE_MEDIA.WEIXIN) {
                        //第三方登录并存值
                        editor.putString("nick_name", map.get("nickname").toString());  //weixin上的昵称
                        editor.putString("wx_log_img", map.get("headimgurl").toString());  //weixin上的头像
                        editor.putString("wx_id", openid);  //weixin的openid或者也可能是unionid
                        editor.commit();
                        ThirdLogin(map.get("nickname").toString(), openid, AppConst.THIRD_WX);
                    }

                } else {
                    LG.i("获取用户信息失败");
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }

        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("frommobile".equals(event.getMsg())) {
            Intent intent = new Intent();
            intent.putExtra("login", true);
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
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
                        user_img_item.setVisibility(View.INVISIBLE);
                    }

                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                    old=0;
                    user_img_item.setVisibility(View.VISIBLE);
                }
                show_user_Img(show_local_img);
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
        int size = userName.getText().toString().length();
        if (size > 0) {
            if (uname.equals(userName.getText().toString())) {
                if (local_uid != "") {
                    show_local_img = true;
                } else {
                    show_local_img = false;
                }
            } else {
                show_local_img = false;
            }
        } else {
            show_local_img = false;
        }
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

    private void show_user_Img(boolean isshow) {
        if (isshow) {
            if (local_uid != "") {
                user_head_img.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(local_uid));
                LG.e("===runuser_head_img===");
            } else {
                user_head_img.setImageBitmap(null);
            }
        } else {
            user_head_img.setImageBitmap(null);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        String invalid_password = res.getString(R.string.login_invalid_password);
        String welcome = res.getString(R.string.login_welcome);
        //因为要登录环信，所以要解析数据ORZ
        if (url.equals(ProtocolConst.SIGNIN)) {
            if (status.getSucceed() == 1) {
                String id = jo.getJSONObject("data").optJSONObject("user").optString("id");
                loginHX(id);
                editor.putBoolean("thirdLog", false);
                editor.commit();
                EventBus.getDefault().post(new MyEvent("userinfo_refresh"));
                EventBus.getDefault().post(new MyEvent("refresh_price"));
                ToastView toast = new ToastView(LoginActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if ("cart".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, ShoppingCartActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("orders_list".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, OrderListActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("orders_detail".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, OrderdetailActivity.class);
                    intent.putExtra("orderid", getIntent().getStringExtra("orderid"));
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("user_wallet".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, MyPurseActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("user_address".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, AddressManageActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);


                } else if ("user_account".equals(intentFlag)) {//账户余额
                    Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("user_password".equals(intentFlag)) {//登录了还要修改密码干什么?
                    Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                } else if ("user_center".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, CustomercenterActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else if ("qrshare".equals(intentFlag)) {
                    Intent intent = new Intent(LoginActivity.this, ShareQRCodeActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("login", true);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }
            } else {
                ToastView toast = new ToastView(LoginActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if ((url == ProtocolConst.CONNECT_SIGNIN)) {
            if (status.getSucceed() == 1) {
                String id = jo.getJSONObject("data").optJSONObject("user").optString("id");
                loginHX(id);
                editor.putBoolean("thirdLog", true);
                editor.commit();
                EventBus.getDefault().post(new MyEvent("userinfo_refresh"));
                EventBus.getDefault().post(new MyEvent("refresh_price"));
                ToastView toast = new ToastView(LoginActivity.this, welcome);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent intent = new Intent();
                intent.putExtra("login", true);
                setResult(Activity.RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            } else if (status.getNew_error_code().equals("connect_no_userbind")) {
                Intent intent = new Intent(LoginActivity.this, OuterLoginBindActivity.class);
                startActivityForResult(intent, 1001);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            }else {
                ToastView toast = new ToastView(LoginActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

        }
    }

    //环信登录
    public void loginHX(String id){
        if(null == id || "".equals(id)){
            ToastUtil.getInstance().makeShortToast(this,"未能获取到用户id，无法登录环信");
            return;
        }
        String name = "sjq" + id;
        String pwd = "pass_" + id;
        pwd = MD5.getMD532(pwd);
        EMClient.getInstance().login(name,pwd,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
//                ToastUtil.getInstance().makeShortToast(LoginActivity.this,"环信登录失败"+message);
            }
        });
    }

}
