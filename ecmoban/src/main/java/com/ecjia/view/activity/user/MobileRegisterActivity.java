package com.ecjia.view.activity.user;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.LoginModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RegisterModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecmoban.android.sijiqing.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * 类名介绍：手机号注册
 * Created by sun
 * Created time 2017-02-13.
 */
public class MobileRegisterActivity extends BaseActivity implements View.OnClickListener,
        TextWatcher, HttpResponse {

    private Button next;
    private EditText mailEdit;
    private EditText passwordEdit;
    private EditText et_username;
    private TextView register;
    private CheckBox show_password;
    private String name, psd, mail, mobile;
    private LoginModel loginModel;
    private RegisterModel registerModel;
    private MyDialog myDialog;
    private boolean flag = true;
    private JSONArray jsonArray = new JSONArray();
    private ImageView root_view;
    private String invite_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword);


        invite_code = getIntent().getStringExtra("invite");

        initTopView();
        root_view = (ImageView) findViewById(R.id.root_view);
        ImageLoader.getInstance().displayImage("file:///" + AndroidManager.LOGIN_BG, root_view, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                root_view.setImageResource(R.drawable.login_defaultbg);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        //手机号
        mobile = getIntent().getStringExtra("userName");
        //下一步
        next = (Button) findViewById(R.id.setpassword_next);
        //密码
        passwordEdit = (EditText) findViewById(R.id.setpassword_password);
        //用户名
        et_username = (EditText) findViewById(R.id.et_username);
        //邮箱
        mailEdit = (EditText) findViewById(R.id.setpassword_mail);
        //显示密码
        show_password = (CheckBox) findViewById(R.id.setpassword_showpassword);

        next.setOnClickListener(this);
        passwordEdit.addTextChangedListener(this);
        mailEdit.addTextChangedListener(this);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    //否则隐藏密码
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance
                            ());
                }
            }
        });

        registerModel = new RegisterModel(this);
        registerModel.addResponseListener(this);
    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.mobile_register_topview);
        topView.setBackgroundColor(Color.parseColor("#ffffff"));
        topView.setTopTextColor(getResources().getColor(R.color._333333));
        topView.setTitleText(R.string.mobile_register);
        topView.setLeftBackImage(R.drawable.header_back_arrow_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new MyDialog(MobileRegisterActivity.this, res.getString(R.string.register_tips),
                        res.getString(R.string.register_back));
                myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                myDialog.show();
                myDialog.setNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                        Intent intent = new Intent(MobileRegisterActivity.this,
                                LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        Resources resource = (Resources) getBaseContext().getResources();
        String email = resource.getString(R.string.register_email_cannot_be_empty);
        String pass = resource.getString(R.string.register_password_cannot_be_empty);
        String fault = resource.getString(R.string.register_email_format_false);
        String hold = resource.getString(R.string.hold_on);
        Intent intent;
        switch (v.getId()) {
            case R.id.setpassword_next:
                mail = mailEdit.getText().toString();
                psd = passwordEdit.getText().toString();
                name = et_username.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastView toastView = new ToastView(this, resource.getString(R.string.input_username_tips3));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else if ("".equals(psd)) {
                    ToastView toast = new ToastView(this, pass);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (psd.length() < 6) {
                    ToastView toast = new ToastView(this, resource.getString(R.string
                            .register_pwd_tooshort));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (!MobileRegisterActivity.this.isPsd(psd)) {
                    ToastView toast = new ToastView(this, resource.getString(R.string
                            .register_pwd_format_false));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    signup();
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myDialog = new MyDialog(this, res.getString(R.string.register_tips),
                    res.getString(R.string.register_back));
            myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
            myDialog.show();
            myDialog.setNegativeListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();

                }
            });
            myDialog.setPositiveListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                    Intent intent = new Intent(MobileRegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });
        }
        return true;

    }

    public void signup() {
        if (flag) {
            registerModel.signup(name, psd, mail, jsonArray, mobile, invite_code);
        }
    }


    public static boolean isPsd(String pasd) {

        String reg = "^[A-Za-z0-9*#@.&_]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(pasd);
        return m.matches();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (passwordEdit.getText().toString().length() >= 6) {
//        if (passwordEdit.getText().toString().length() != 0 && mailEdit.getText().toString().length() != 0) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(flag);
            next.setTextColor(Color.parseColor("#ff999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (passwordEdit.getText().toString().length() >= 6) {
//        if (passwordEdit.getText().toString().length() != 0 && mailEdit.getText().toString().length() != 0) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(flag);
            next.setTextColor(Color.parseColor("#ff999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (passwordEdit.getText().toString().length() >= 6) {
//        if (passwordEdit.getText().toString().length() != 0 && mailEdit.getText().toString().length() != 0) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(flag);
            next.setTextColor(Color.parseColor("#ff999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Event event) {

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        Resources resource = MobileRegisterActivity.this.getResources();
        String invalid_password = resource.getString(R.string.login_invalid_password);
        String welcome = resource.getString(R.string.login_welcome);
        if (url.equals(ProtocolConst.SIGNUP)) {
            if (status.getSucceed() == 1) {
                String str = resource.getString(R.string.register_success);
                String content = resource.getString(R.string.logonId);
                EventBus.getDefault().post(new MyEvent("frommobile"));
                if (mApp.getUser().getBonus_list().size() > 0) {
                    content = content.replace(AppConst.REPLACE, mApp.getUser().getBonus_list().get(0).getBonus_amount());
                    myDialog = new MyDialog(this, str,content);
                    myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                    myDialog.setSureOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("login", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                    myDialog.show();
                } else {
                    ToastView toast = new ToastView(MobileRegisterActivity.this,
                            resource.getString(R.string.login_welcome));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Intent intent = new Intent();
                    intent.putExtra("login", true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else {
                ToastView toast = new ToastView(MobileRegisterActivity.this,
                        status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

}
