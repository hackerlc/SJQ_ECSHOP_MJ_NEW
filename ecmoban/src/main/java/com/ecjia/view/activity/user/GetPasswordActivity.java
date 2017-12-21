package com.ecjia.view.activity.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名介绍：忘记密码-输入手机号
 * Created by sun
 * Created time 2017-02-13.
 * 改（和第一次注册公用）
 */
public class GetPasswordActivity extends BaseActivity implements TextWatcher,HttpResponse {

    private TextView textView_title;
    private ImageView back;
    private EditText mobile;
    private Button next;
    private MyDialog myDialog;
    private MyProgressDialog mpd;
    private GetPasswordModel getPasswordModel;
    public ToastView toastView;
    private LinearLayout root_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);
        initView();
    }

    private void initView() {
        root_view= (LinearLayout) findViewById(R.id.root_view);
        if(null!= ConfigModel.getInstance().login_bitmap_bg){
            root_view.setBackgroundDrawable(ConfigModel.getInstance().login_bitmap_bg);
        }
        mpd = MyProgressDialog.createDialog(this);
        getPasswordModel=new GetPasswordModel(this);
        getPasswordModel.addResponseListener(this);
        getPasswordModel.getShopToken();
        back= (ImageView) findViewById(R.id.imageView_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mobile= (EditText) findViewById(R.id.get_password_edit);
        textView_title=(TextView) findViewById(R.id.textView_title);
        mobile.addTextChangedListener(this);
        next= (Button) findViewById(R.id.get_password_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNo = mobile.getText().toString();
                if (!isMobileNO(mobileNo)) {
                    if (mobileNo == null || mobileNo == "") {
                        toastView = new ToastView(GetPasswordActivity.this, res.getString(R.string.register_num_null));
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    } else {
                        toastView = new ToastView(GetPasswordActivity.this, res.getString(R.string.register_num_format));
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }
                } else {
                    if(getPasswordModel.access_token!=null){
                        getPasswordModel.getUser_forget_password("mobile",mobileNo);
                        mpd.show();
                    }
                }
            }
        });
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        mobile.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
            next.setTextColor(Color.parseColor("#999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mobile.getText().toString().length() == 11) {
            next.setEnabled(true);
            next.setTextColor(Color.parseColor("#ffffffff"));
            next.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            next.setEnabled(false);
            next.setTextColor(Color.parseColor("#999999"));
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
            next.setTextColor(Color.parseColor("#999999"));
            next.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url== ProtocolConst.USER_FORGET_PASSWORD){
            mpd.dismiss();
            if(status.getSucceed()==1){
                Intent intent=new Intent(this,PostSMSActivity.class);
                intent.putExtra("mobile", mobile.getText().toString());
                startActivity(intent);
            }else{

            }
        }else if(url==ProtocolConst.SHOP_TOKEN){
            mpd.dismiss();
        }
    }
}
