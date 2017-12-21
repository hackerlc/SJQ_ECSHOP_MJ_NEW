package com.ecjia.view.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.network.netmodle.GetPasswordModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * 类名介绍：忘记密码-输入找回密码的验证码
 * Created by sun
 * Created time 2017-02-13.
 */
public class PostSMSActivity extends BaseActivity implements TextWatcher,HttpResponse {
    private ImageView back;
    private TextView attention,btnGetcode;
    private EditText codeEdit;
    private Button next;
    private TimeCount time;
    private String mobileNo;
    private GetPasswordModel getPasswordModel;
    private String code;
    MyProgressDialog mpd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sms);
        initView();
    }

    private void initView() {
        mpd = MyProgressDialog.createDialog(this);
        mobileNo = getIntent().getStringExtra("mobile");
        getPasswordModel=GetPasswordModel.getIntacne(this);
        getPasswordModel.addResponseListener(this);
        back= (ImageView) findViewById(R.id.imageView_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        attention= (TextView) findViewById(R.id.tv_phone_hinit);
        String receive=res.getString(R.string.register_code_send);
        attention.setText(receive + mobileNo );

        codeEdit= (EditText) findViewById(R.id.edit_code);
        codeEdit.addTextChangedListener(this);
        time = new TimeCount(119900, 1000);
        time.start();

        btnGetcode= (TextView) findViewById(R.id.messagecodecheck_time);
        btnGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.start();
                getPasswordModel.getUser_forget_password("mobile", mobileNo);
                mpd.show();
            }
        });


        next= (Button) findViewById(R.id.get_password_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = codeEdit.getText().toString();
                if (code.length() == 6) {
                    getPasswordModel.getValidate_forget_password("mobile",mobileNo,code);

                } else {
                    ToastView toastView = new ToastView(PostSMSActivity.this, res.getString(R.string.register_wrong_code));
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                }
            }
        });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url== ProtocolConst.VALIDATE_FORGET_PASSWORD){
            mpd.dismiss();
            if(status.getSucceed()==1){
                Intent intent=new Intent(this,ResetPasswordActivity.class);
                intent.putExtra("mobile", mobileNo);
                startActivity(intent);
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
            btnGetcode.setText(res.getString(R.string.register_resend)+"(" + (millisUntilFinished / 1000) + ")");
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
    // 关闭键盘
    public void CloseKeyBoard() {
        codeEdit.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(codeEdit.getWindowToken(), 0);
    }
}
