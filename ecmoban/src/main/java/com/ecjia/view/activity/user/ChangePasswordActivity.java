package com.ecjia.view.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 类名介绍：修改密码
 * Created by sun
 * Created time 2017-02-13.
 */
public class ChangePasswordActivity extends BaseActivity {
    private TextView tv_title;
    private ImageView back;
    private FrameLayout fl_old_password;
    private EditText old_pwd, new_pwd, new_pwd2;
    private Button sure;
    public Handler Intenthandler;
    private UserInfoModel userInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        PushAgent.getInstance(this).onAppStart();

        userInfoModel = new UserInfoModel(this);
        userInfoModel.addResponseListener(this);

        back = (ImageView) findViewById(R.id.change_password_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        fl_old_password = (FrameLayout) findViewById(R.id.fl_old_password);

        if("1".equals(mApp.getUser().getPassword_null())){
            tv_title.setText(getResources().getString(R.string.customer_setting_password));
            fl_old_password.setVisibility(View.GONE);
        } else {
            tv_title.setText(getResources().getString(R.string.customer_change_password));
            fl_old_password.setVisibility(View.VISIBLE);
        }

        old_pwd = (EditText) findViewById(R.id.change_password_old);
        new_pwd = (EditText) findViewById(R.id.change_password_new);
        new_pwd2 = (EditText) findViewById(R.id.change_password_new2);
        sure = (Button) findViewById(R.id.change_password_sure);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyBoard();
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if ("".equals(old_pwd.getText().toString())) {
//                    ToastView toast = new ToastView(ChangePasswordActivity.this, "原始密码不能为空");
//                    toast.show();
//                } else
                if (new_pwd.getText().toString().length() < 6) {
                    ToastView toast = new ToastView(ChangePasswordActivity.this, "密码长度不能少于6位");
                    toast.show();
                } else if (old_pwd.getText().toString().equals(new_pwd.getText().toString())) {
                    ToastView toast = new ToastView(ChangePasswordActivity.this, "新老密码不能一样");
                    toast.show();
                } else if (!new_pwd2.getText().toString().equals(new_pwd.getText().toString())) {
                    ToastView toast = new ToastView(ChangePasswordActivity.this, "两次新密码不一致");
                    toast.show();
                } else {
                    CloseKeyBoard();//old_pwd.getText().toString(),
                    userInfoModel.changePassword(old_pwd.getText().toString(), new_pwd.getText().toString());
                }
            }
        });


    }

    // 关闭键盘
    public void CloseKeyBoard() {
        old_pwd.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(old_pwd.getWindowToken(), 0);
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url.equals(ProtocolConst.CHANGE_PASSWORD)) {
            //修改密码成功后
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(ChangePasswordActivity.this, R.string.change_password_succeed);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                ChangePasswordActivity.this.finish();
                EventBus.getDefault().post(new MyEvent("exsit"));

                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_buttom_in,
                        R.anim.push_buttom_out);
            } else if (status.getSucceed() == 0) {

            }
        }
    }
}
