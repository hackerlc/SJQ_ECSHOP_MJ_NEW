package com.ecjia.view.activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.CodeModel;
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

/**
 * 类名介绍：检测手机验证码
 * Created by sun
 * Created time 2017-02-13.
 */
public class CheckCodeActivity extends BaseActivity implements View.OnClickListener, TextWatcher, HttpResponse {
    private TextView attention, btnGetcode;
    private EditText codeEdit;
    private Button next;
    private TimeCount time;
    private String mobileNo;
    private MyDialog myDialog;
    private ToastView toastView;

    private String code;
    private String invite;

    private CodeModel codeModel;
    private MyProgressDialog mpd;
    private boolean isOneButtonDialogShow = false;
    private ImageView root_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_message_code_check);

        initTopView();

        invite=getIntent().getStringExtra("invite");

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
        mpd = MyProgressDialog.createDialog(this);
        codeModel = CodeModel.getInstance();
        codeModel.addResponseListener(this);
        mobileNo = getIntent().getStringExtra("mobile");
        attention = (TextView) findViewById(R.id.messagecodecheck_attention);
        String receive = res.getString(R.string.register_enter_recode);
        String a = receive.substring(0, 3);
        String b = receive.substring(3, receive.length());
        attention.setText(a + mobileNo + b);
        codeEdit = (EditText) findViewById(R.id.messagecodecheck_edit);
        codeEdit.addTextChangedListener(this);
        time = new TimeCount(119900, 1000);
        btnGetcode = (TextView) findViewById(R.id.messagecodecheck_time);
        time.start();
        next = (Button) findViewById(R.id.messagecodecheck_next);
        next.setOnClickListener(this);
        btnGetcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.messagecodecheck_next:
                code = codeEdit.getText().toString();
                if (code.length() == 6) {
                    codeModel.checkCode(mobileNo, code);
                    mpd.show();
                } else {
                    toastView = new ToastView(this, res.getString(R.string.register_wrong_code));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }
                break;

            case R.id.messagecodecheck_time:
                time.start();
                codeModel.getCode(mobileNo);
                mpd.show();
                break;

        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (codeEdit.getText().toString().length() == 6) {
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

        if (codeEdit.getText().toString().length() == 6) {
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
        if (codeEdit.getText().toString().length() == 6) {
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
        if (url == ProtocolConst.GETCODE) {
            if (status.getArgI() == 0) {
                myDialog = new MyDialog(CheckCodeActivity.this, res.getString(R.string.register_tips), res
                        .getString(R.string.register_code_send) + "\n" + mobileNo);
                myDialog.show();
                myDialog.setBackKeyNo();
                isOneButtonDialogShow = true;
                myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                myDialog.setSureOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isOneButtonDialogShow = false;
                        myDialog.dismiss();
                    }
                });
            }
        } else if (url == ProtocolConst.CHECKCODE) {
            if (status.getSucceed() == 1) {
                Intent intent = new Intent(CheckCodeActivity.this, MobileRegisterActivity.class);
                intent.putExtra("userName", mobileNo);
                intent.putExtra("invite",invite);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            } else {
                String error_desc = status.getError_desc();
                myDialog = new MyDialog(CheckCodeActivity.this, res.getString(R.string.register_tips), error_desc);
                myDialog.show();
                myDialog.setBackKeyNo();
                myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                myDialog.setSureOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void initTopView() {

        topView = (ECJiaTopView) findViewById(R.id.checkcode_topview);
        topView.setTitleText(R.string.mobile_register);
        topView.setBackgroundColor(Color.parseColor("#ffffff"));
        topView.setTopTextColor(getResources().getColor(R.color._333333));
        topView.setLeftBackImage(R.drawable.header_back_arrow_black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new MyDialog(CheckCodeActivity.this, res.getString(R.string.register_tips), res
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
                        Intent intent = new Intent(CheckCodeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
                    Intent intent = new Intent(CheckCodeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
