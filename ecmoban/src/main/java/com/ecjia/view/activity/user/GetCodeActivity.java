package com.ecjia.view.activity.user;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.CodeModel;
import com.ecjia.network.netmodle.InviteModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AndroidManager;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 类名介绍：注册输入信息
 * Created by sun
 * Created time 2017-02-13.
 */
public class GetCodeActivity extends BaseActivity implements OnClickListener, TextWatcher,
        HttpResponse {

    private LinearLayout ll_invitation,ll_invitation2;
    private EditText et_invitation;
    private TextView tv_invitation;
    private EditText mobile;        //手机号输入框
    private Button next;           //下一步
    private MyDialog myDialog;
    public ToastView toastView;
    public CodeModel codeModel;
    private InviteModel inviteModel;
    private MyProgressDialog mpd;
    private ImageView root_view;
    private TextView attention;
    private EditText codeEdit;
    private TimeCount time;
    private TextView btnGetcode;
    private String mobileNo;
    private String code;
    private boolean hasInviteCode;
    private String invite_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileregister);

        initTopView();
        root_view = (ImageView) findViewById(R.id.root_view);
        mpd = MyProgressDialog.createDialog(this);
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
        codeModel = new CodeModel(this);
        codeModel.addResponseListener(this);
        inviteModel = new InviteModel(this);
        inviteModel.addResponseListener(this);
        attention = (TextView) findViewById(R.id.messagecodecheck_attention);
        ll_invitation = (LinearLayout) findViewById(R.id.ll_invitation);
        ll_invitation2 = (LinearLayout) findViewById(R.id.ll_invitation2);
        et_invitation = (EditText) findViewById(R.id.et_invitation);
        tv_invitation = (TextView) findViewById(R.id.tv_invitation);
        mobile = (EditText) findViewById(R.id.mobileregister_edit);

        next = (Button) findViewById(R.id.mobileregister_next);
        next.setOnClickListener(this);

        mobile.addTextChangedListener(this);

        codeEdit = (EditText) findViewById(R.id.messagecodecheck_edit);
        codeEdit.addTextChangedListener(this);
        time = new TimeCount(119900, 1000);
        btnGetcode = (TextView) findViewById(R.id.messagecodecheck_time);
        btnGetcode.setOnClickListener(this);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mobile.getText().toString().length() > 0) {
                myDialog = new MyDialog(this, res.getString(R.string.register_tips),
                        res.getString(R.string.register_back));
                myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                myDialog.setNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.setPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                        Intent intent = new Intent(GetCodeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        CloseKeyBoard();
                    }
                });
                myDialog.show();
            } else {

                Intent intent = new Intent(GetCodeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                CloseKeyBoard();
                finish();
            }
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mobileregister_next:
                mobileNo=mobile.getText().toString();
                code = codeEdit.getText().toString();
                invite_code=et_invitation.getText().toString();
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

                } else  if (code.length() != 6) {
                    toastView = new ToastView(this, res.getString(R.string.register_wrong_code));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else  if (invite_code.length() >0&&invite_code.length() !=6) {
                    toastView = new ToastView(this, res.getString(R.string.register_wrong_invite_code));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                } else {
                    codeModel.checkCode(mobileNo,code);
                    mpd.show();
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
                    codeModel.getCode(mobileNo);
                    mpd.show();
                }
                break;

        }

    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.getcode_topview);
        topView.setBackgroundColor(Color.parseColor("#ffffff"));
        topView.setTopTextColor(getResources().getColor(R.color._333333));
        topView.setTitleText(R.string.mobile_register);
        topView.setLeftBackImage(R.drawable.header_back_arrow_black, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobile.getText().toString().length() > 0) {
                    myDialog = new MyDialog(GetCodeActivity.this, res.getString(R.string.register_tips),
                            res.getString(R.string.register_back));
                    myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                    myDialog.setNegativeListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();

                        }
                    });
                    myDialog.setPositiveListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                            Intent intent = new Intent(GetCodeActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            CloseKeyBoard();
                        }
                    });
                    myDialog.show();

                } else {
                    Intent intent = new Intent(GetCodeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    CloseKeyBoard();
                }
            }
        });
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        mobile.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mobile.getWindowToken(), 0);
    }

    public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();

    }

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
            if(!hasInviteCode){
                mobile.setEnabled(false);
                inviteModel.getInviteValidate(mobile.getText().toString());
            }
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

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        mpd.dismiss();
        if (url.equals(ProtocolConst.GETCODE)) {
            if (status.getArgI() == 1) {
                //已注册，弹出对话框，是否继续
                myDialog = new MyDialog(GetCodeActivity.this, res.getString(R.string
                        .register_tips), res.getString(R.string.register_num_extinct));
                myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                myDialog.setNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        //返回登录页面，填入手机号
                        Intent intent = new Intent(GetCodeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                myDialog.setPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新输入手机号
                        myDialog.dismiss();
                    }
                });
                myDialog.show();


            } else if (status.getArgI() == 0) {
                time.start();
                myDialog = new MyDialog(GetCodeActivity.this, res.getString(R.string.register_tips), res
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

            } else if (status.getArgI() == 2) {
                toastView = new ToastView(this, res.getString(R.string.getcode_attention_sendfail));
                toastView.setGravity(Gravity.CENTER, 0, 0);
                toastView.show();
            }
        }else if(url.equals(ProtocolConst.INVITE_VALIDATE)){
            hasInviteCode=true;
            mobile.setEnabled(true);
            if(status.getSucceed()==1){
                if(!TextUtils.isEmpty(inviteModel.invite_code)){
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
        } else if (url == ProtocolConst.CHECKCODE) {
            if (status.getSucceed() ==1) {
                Intent intent = new Intent(GetCodeActivity.this, MobileRegisterActivity.class);
                intent.putExtra("userName", mobileNo);
                intent.putExtra("invite",et_invitation.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            } else {
                toastView = new ToastView(this, status.getError_desc());
                toastView.setGravity(Gravity.CENTER, 0, 0);
                toastView.show();
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
