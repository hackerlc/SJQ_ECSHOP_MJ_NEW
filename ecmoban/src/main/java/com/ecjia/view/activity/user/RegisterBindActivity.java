package com.ecjia.view.activity.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.InviteModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RegisterModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.AppConst;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * 类名介绍：邮箱注册绑定
 * Created by sun
 * Created time 2017-02-13.
 */
public class RegisterBindActivity extends BaseActivity implements View.OnClickListener, TextWatcher, HttpResponse {
    private TextView attention, btnGetcode;
    private EditText codeEdit;
    private Button next;
    private TimeCount time;
    private String mobileNo;
    private ToastView toastView;
    private LinearLayout ll_invitation,ll_invitation2;
    private EditText et_invitation;
    private TextView tv_invitation;
    private EditText mobile;        //手机号输入框

    private String code,snscode,openid;
    private String invite;
    private String name;

    private RegisterModel registerModel;
    private InviteModel inviteModel;
    private ImageView root_view;
    private MyDialog myDialog;
    private EditText password;
    private EditText et_username;
    private CheckBox show_password;
    private String psd;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_register_bind);

        initTopView();

        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        if (AppConst.THIRD_QQ.equals(shared.getString("thirdWay", ""))) {
            openid=shared.getString("qq_id", "");
            name=shared.getString("myscreen_name", "");
            snscode= AppConst.THIRD_QQ;
        } else if (AppConst.THIRD_WX.equals(shared.getString("thirdWay", ""))) {
            openid=shared.getString("wx_id", "");
            name=shared.getString("nick_name", "");
            snscode= AppConst.THIRD_WX;
        }

        registerModel = new RegisterModel(this);
        registerModel.addResponseListener(this);
        inviteModel = new InviteModel(this);
        inviteModel.addResponseListener(this);

        initView();

    }

    private void initView() {
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

        attention = (TextView) findViewById(R.id.messagecodecheck_attention);
        ll_invitation = (LinearLayout) findViewById(R.id.ll_invitation);
        ll_invitation2 = (LinearLayout) findViewById(R.id.ll_invitation2);
        et_invitation = (EditText) findViewById(R.id.et_invitation);
        tv_invitation = (TextView) findViewById(R.id.tv_invitation);
        mobile = (EditText) findViewById(R.id.mobileregister_edit);

        mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mobile.getText().toString().length() == 11) {
                    next.setEnabled(true);
                    next.setTextColor(Color.parseColor("#ffffffff"));
                    next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    next.setEnabled(false);
                    next.setTextColor(Color.parseColor("#ff999999"));
                    next.setBackgroundResource(R.drawable.shape_unable);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mobile.getText().toString().length() == 11) {
                    next.setEnabled(true);
                    next.setTextColor(Color.parseColor("#ffffffff"));
                    next.setBackgroundResource(R.drawable.selector_login_button);
                    mobile.setEnabled(false);
                    inviteModel.getInviteValidate(mobile.getText().toString());
                } else {
                    next.setEnabled(false);
                    next.setTextColor(Color.parseColor("#ff999999"));
                    next.setBackgroundResource(R.drawable.shape_unable);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mobile.getText().toString().length() == 11) {
                    next.setEnabled(true);
                    next.setTextColor(Color.parseColor("#ffffffff"));
                    next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    next.setEnabled(false);
                    next.setTextColor(Color.parseColor("#ff999999"));
                    next.setBackgroundResource(R.drawable.shape_unable);
                }
            }
        });

        codeEdit = (EditText) findViewById(R.id.messagecodecheck_edit);
        codeEdit.addTextChangedListener(this);
        password = (EditText) findViewById(R.id.login_password);
        password.addTextChangedListener(this);
        et_username = (EditText) findViewById(R.id.et_username);
        et_username.setText(name);
        time = new TimeCount(119900, 1000);
        btnGetcode = (TextView) findViewById(R.id.messagecodecheck_time);
        next = (Button) findViewById(R.id.messagecodecheck_next);
        next.setOnClickListener(this);
        btnGetcode.setOnClickListener(this);

        show_password = (CheckBox) findViewById(R.id.login_show_pwd);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                if (password.length() > 0) {
                    password.setSelection(password.length());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.messagecodecheck_next:
                String pass = res.getString(R.string.register_password_cannot_be_empty);
                code = codeEdit.getText().toString();
                psd = password.getText().toString();
                mobileNo=mobile.getText().toString();
                name=et_username.getText().toString();
                invite=et_invitation.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    toastView = new ToastView(this, res.getString(R.string.input_username_tips3));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else
                if (!isName(name)) {
                    toastView = new ToastView(this, res.getString(R.string.input_username_tips1));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else
                if (!isMobileNO(mobileNo)) {
                    if (mobileNo == null || mobileNo == "") {
                        toastView = new ToastView(this, res.getString(R.string
                                .register_num_null));
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    } else {
                        toastView = new ToastView(this, res.getString(R.string
                                .register_num_format));
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }
                }else

                if (code.length() != 6) {
                    toastView = new ToastView(this, res.getString(R.string.register_wrong_code));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else  if (invite.length() >0&&invite.length() !=6) {
                    toastView = new ToastView(this, res.getString(R.string.register_wrong_invite_code));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else
                if ("".equals(psd)) {
                    ToastView toast = new ToastView(this, pass);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (psd.length() < 6) {
                    ToastView toast = new ToastView(this, res.getString(R.string
                            .register_pwd_tooshort));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (!RegisterBindActivity.this.isPsd(psd)) {
                    ToastView toast = new ToastView(this, res.getString(R.string
                            .register_pwd_format_false));
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
//                    registerModel.ThirdLoginRegister(name,psd,mobileNo,invite,openid,snscode,code);
                }

                break;

            case R.id.messagecodecheck_time:

                mobileNo=mobile.getText().toString();

                if (!isMobileNO(mobileNo)) {
                    if (mobileNo == null || mobileNo == "") {
                        toastView = new ToastView(this, res.getString(R.string
                                .register_num_null));
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    } else {
                        toastView = new ToastView(this, res.getString(R.string
                                .register_num_format));
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }
                } else {

                    registerModel.getShopToken();
                }
                break;

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (codeEdit.getText().toString().length() == 6&&password.getText().toString().length()>=6) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(false);
            next.setTextColor(Color.parseColor("#ff999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (codeEdit.getText().toString().length() == 6&&password.getText().toString().length()>=6) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(false);
            next.setTextColor(Color.parseColor("#ff999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (codeEdit.getText().toString().length() == 6&&password.getText().toString().length()>=6) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(false);
            next.setTextColor(Color.parseColor("#ff999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.GETCODE) {
            if (status.getArgI() == 0) {
                myDialog = new MyDialog(RegisterBindActivity.this, res.getString(R.string.register_tips), res
                        .getString(R.string.register_code_send) + "\n" + mobileNo);
                myDialog.show();
                myDialog.setBackKeyNo();
                myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                myDialog.setSureOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
            }else if (status.getArgI() == 1) {
                toastView = new ToastView(this, res.getString(R.string
                        .register_num_extinct2));
                toastView.setGravity(Gravity.CENTER, 0, 0);
                toastView.show();
            } else if (status.getArgI() == 2) {
                toastView = new ToastView(this, res.getString(R.string
                        .getcode_attention_sendfail));
                toastView.setGravity(Gravity.CENTER, 0, 0);
                toastView.show();
            }
        } else if (url == ProtocolConst.SHOP_TOKEN) {
            if (status.getSucceed() == 1) {
                time.start();
                registerModel.getCode(mobileNo);
            }
        } else if (url == ProtocolConst.CONNECT_SIGNUP) {
            if (status.getSucceed() == 1) {
                String str = res.getString(R.string.register_success);
                String content=res.getString(R.string.logonId);
                EventBus.getDefault().post(new MyEvent("frommobile"));
                if (mApp.getUser().getBonus_list().size() > 0) {
                    content=content.replace(AppConst.REPLACE,mApp.getUser().getBonus_list().get(0).getBonus_amount());
                    myDialog = new MyDialog(this, str,
                            content);
                    myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                    myDialog.setSureOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                    myDialog.show();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                ToastView toast = new ToastView(RegisterBindActivity.this, status.getError_desc());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else if(url.equals(ProtocolConst.INVITE_VALIDATE)){
        mobile.setEnabled(true);
        if(status.getSucceed()==1){
            if(!TextUtils.isEmpty(inviteModel.invite_code)){
                invite=inviteModel.invite_code;
                et_invitation.setText(inviteModel.invite_code);
                tv_invitation.setText(inviteModel.invite_code);
                ll_invitation.setVisibility(View.GONE);
                ll_invitation2.setVisibility(View.VISIBLE);
            }else{
                ll_invitation.setVisibility(View.VISIBLE);
                ll_invitation2.setVisibility(View.GONE);
            }
        }else{
            ll_invitation.setVisibility(View.VISIBLE);
            ll_invitation2.setVisibility(View.GONE);
        }
    }
    }

    @Override
    public void initTopView() {

        topView = (ECJiaTopView) findViewById(R.id.bind_topview);
        topView.setTitleText(R.string.register_bind);
        topView.setBackgroundColor(Color.parseColor("#ffffff"));
        topView.setTopTextColor(getResources().getColor(R.color._333333));
        topView.setLeftBackImage(R.drawable.header_back_arrow_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new MyDialog(RegisterBindActivity.this, res.getString(R.string.register_tips), res
                        .getString(R.string.register_back));
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
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        CloseKeyBoard();
                    }
                });
            }
        });
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            //获取验证码
            btnGetcode.setBackgroundResource(R.drawable.shape_unable);
            btnGetcode.setTextColor(Color.parseColor("#ff999999"));
            btnGetcode.setClickable(false);
            btnGetcode.setText(res.getString(R.string.register_resend) + "(" + (millisUntilFinished / 1000) + ")");
        }

        @Override
        public void onFinish() {
            btnGetcode.setText(res.getString(R.string.register_resend));
            btnGetcode.setClickable(true);
            btnGetcode.setTextColor(Color.parseColor("#ffffff"));
            btnGetcode.setBackgroundResource(R.drawable.selector_get_code);

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            myDialog = new MyDialog(this, res.getString(R.string.register_tips), res.getString(R.string
                    .register_back));
            myDialog.show();
            myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
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
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    CloseKeyBoard();
                }
            });
        }
        return true;

    }


    // 关闭键盘
    public void CloseKeyBoard() {
        codeEdit.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(codeEdit.getWindowToken(), 0);
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    public static boolean isPsd(String pasd) {
        String reg = "^[A-Za-z0-9*#@.&_]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(pasd);
        return m.matches();
    }

    public static boolean isName(String name) {
        String reg = "^[A-Za-z0-9_\\P{Cn}]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(name);
        return m.matches();
    }

}
