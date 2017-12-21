package com.ecjia.view.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
 * 类名介绍：设置新密码
 * Created by sun
 * Created time 2017-02-13.
 */
public class ResetPasswordActivity extends BaseActivity implements HttpResponse,TextWatcher{
    private String mobileNo;
    private ImageView back;
    private EditText new_pwd,new_pwd2;
    private Button sure;
    private GetPasswordModel getPasswordModel;
    private MyProgressDialog mpd;
    private CheckBox show_password,show_password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView() {
        show_password= (CheckBox) findViewById(R.id.reset_show_pwd);
        show_password2= (CheckBox) findViewById(R.id.reset_show_pwd2);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        show_password2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    new_pwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    new_pwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mpd=MyProgressDialog.createDialog(this);
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
        new_pwd= (EditText) findViewById(R.id.edit_password);
        new_pwd2= (EditText) findViewById(R.id.edit_password2);
        new_pwd.addTextChangedListener(this);
        new_pwd2.addTextChangedListener(this);
        sure= (Button) findViewById(R.id.reset_password_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new_pwd.getText().toString().length()<6){
                    ToastView toast =new ToastView(ResetPasswordActivity.this,"密码长度不能少于6位");
                    toast.show();
                }else if(!new_pwd2.getText().toString().equals(new_pwd.getText().toString())){
                    ToastView toast =new ToastView(ResetPasswordActivity.this,"两次新密码不一致");
                    toast.show();
                }else{
                    getPasswordModel.getUser_reset_password("mobile",mobileNo,new_pwd.getText().toString());
                    mpd.show();
                }
            }
        });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url == ProtocolConst.USER_RESET_PASSWORD){
            mpd.dismiss();
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.push_buttom_in,
                    R.anim.push_buttom_out);
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (new_pwd.getText().toString().length() >=6&&new_pwd2.getText().toString().length()>=6) {
            sure.setEnabled(true);
            sure.setTextColor(Color.parseColor("#ffffffff"));
            sure.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            sure.setEnabled(false);
            sure.setTextColor(Color.parseColor("#ff999999"));
            sure.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (new_pwd.getText().toString().length() >=6&&new_pwd2.getText().toString().length()>=6) {
            sure.setEnabled(true);
            sure.setTextColor(Color.parseColor("#ffffffff"));
            sure.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            sure.setEnabled(false);
            sure.setTextColor(Color.parseColor("#ff999999"));
            sure.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (new_pwd.getText().toString().length() >=6&&new_pwd2.getText().toString().length()>=6) {
            sure.setEnabled(true);
            sure.setTextColor(Color.parseColor("#ffffffff"));
            sure.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            sure.setEnabled(false);
            sure.setTextColor(Color.parseColor("#ff999999"));
            sure.setBackgroundResource(R.drawable.shape_unable);
        }
    }
}
