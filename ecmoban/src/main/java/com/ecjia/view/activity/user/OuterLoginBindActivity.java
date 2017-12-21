package com.ecjia.view.activity.user;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.netmodle.RegisterModel;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityOuterLoginBindBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.utils.ToastUtil;

public class OuterLoginBindActivity extends BaseActivity implements OnClickListener {
    private ActivityOuterLoginBindBinding mBinding;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private RegisterModel registerModel;
    private TimeCount time;

    //提交信息
    private String snscode;
    private String mobileNo;
    private String code,openid;
    private String invite;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_outer_login_bind);
        mBinding.setOnClick(this);
        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        registerModel = new RegisterModel(this);
        registerModel.addResponseListener(this);
        initTopView();
        initUI();
    }

    private void initUI() {
        time = new TimeCount(60000, 1000);
        if (AppConst.THIRD_QQ.equals(shared.getString("thirdWay", ""))) {
            mBinding.tvDear.setText(res.getString(R.string.dear_third_login_user).replace(AppConst.REPLACE, "QQ"));
            mBinding.tvNickName.setText(shared.getString("myscreen_name", ""));
            name=shared.getString("myscreen_name", "");
            openid=shared.getString("qq_id", "");
            ImageLoaderForLV.getInstance(this).setImageRes(mBinding.userHeadImg, shared.getString("qq_log_img", ""));
            snscode = AppConst.THIRD_QQ;
        } else if (AppConst.THIRD_WX.equals(shared.getString("thirdWay", ""))) {
            mBinding.tvDear.setText(res.getString(R.string.dear_third_login_user).replace(AppConst.REPLACE, res.getString(R.string.wechat)));
            mBinding.tvNickName.setText(shared.getString("nick_name", ""));
            name=shared.getString("nick_name", "");
            openid=shared.getString("wx_id", "");
            ImageLoaderForLV.getInstance(this).setImageRes(mBinding.userHeadImg, shared.getString("wx_log_img", ""));
            snscode = AppConst.THIRD_WX;
        }
        mBinding.etPhone.addTextChangedListener(mTextWatcher);
        mBinding.etCode.addTextChangedListener(mTextWatcher);
    }

    private void changeOkBtn(){
        if (mBinding.etPhone.getText().toString().length() == 11 && !"".equals(mBinding.etCode.getText().toString())) {
            mBinding.btnOk.setEnabled(true);
            mBinding.btnOk.setTextColor(Color.parseColor("#ffffffff"));
            mBinding.btnOk.setBackgroundResource(R.drawable.selector_login_button);
        } else {
            mBinding.btnOk.setEnabled(false);
            mBinding.btnOk.setTextColor(Color.parseColor("#ff999999"));
            mBinding.btnOk.setBackgroundResource(R.drawable.shape_unable);
        }
    }

    @Override
    public void initTopView() {
        super.initTopView();
        mBinding.bindTopview.setTitleText(R.string.third_login);
        mBinding.bindTopview.setLeftBackImage(R.drawable.header_back_arrow, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        mBinding.tvNickName.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.tvNickName.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:
                mobileNo = mBinding.etPhone.getText().toString();

                if (!isMobileNO(mobileNo)) {
                    if (mobileNo == null || mobileNo == "") {
                        ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                                .register_num_null));
                    } else {
                        ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                                .register_num_format));
                    }
                } else {
                    registerModel.getShopToken();
                }
                break;
            case R.id.btn_ok:
                code = mBinding.etCode.getText().toString();
                mobileNo=mBinding.etPhone.getText().toString();
                invite=mBinding.etInvitation.getText().toString();
                if (!isMobileNO(mobileNo)) {
                    if (mobileNo == null || mobileNo == "") {
                        ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                                .register_num_null));
                    } else {
                        ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                                .register_num_format));
                    }
                } else if (code.length() != 6) {
                    ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                            .register_wrong_code));
                } else if(!code.equals(registerModel.bindCode)){
                    ToastUtil.getInstance().makeShortToast(this, "验证码错误,请重新输入");
                } else if (invite.length() >0&&invite.length() !=6) {
                    ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                            .register_wrong_invite_code));
                } else {
                    registerModel.ThirdLoginRegister(name,mobileNo,invite,openid,snscode,code);
                }
                break;
        }
    }

    private MyDialog myDialog;
    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.GETCODE) {
            if (status.getArgI() == 0) {
                ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                        .register_code_send)+"\n" + mobileNo);
            }else if (status.getArgI() == 1) {
                ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                        .register_num_extinct2));
            } else if (status.getArgI() == 2) {
                ToastUtil.getInstance().makeShortToast(this, res.getString(R.string
                        .getcode_attention_sendfail));
            }
        }
        if (url == ProtocolConst.SHOP_TOKEN) {
            if (status.getSucceed() == 1) {
                time.start();
                registerModel.getCode(mobileNo);
            }
        }
        if (url == ProtocolConst.CONNECT_SIGNUP) {
            if (status.getSucceed() == 1) {
                String str = res.getString(R.string.register_success);
                String content=res.getString(R.string.logonId);
                EventBus.getDefault().post(new MyEvent("frommobile"));
                if("0".equals(registerModel.isBind)){
                    RegisterSetPwdActivity_Builder.intent(this).type("1").start();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (mApp.getUser().getBonus_list().size() > 0) {
                        content=content.replace(AppConst.REPLACE,mApp.getUser().getBonus_list().get(0).getBonus_amount());
                        myDialog = new MyDialog(this, str,
                                content);
                        myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                        myDialog.setSureOnClickListener(v -> {
                            myDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        });
                        myDialog.show();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            } else {
                ToastUtil.getInstance().makeShortToast(this, status.getError_desc());
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
            mBinding.tvGetCode.setBackgroundResource(R.drawable.shape_unable);
            mBinding.tvGetCode.setTextColor(Color.parseColor("#ff999999"));
            mBinding.tvGetCode.setClickable(false);
            mBinding.tvGetCode.setText(res.getString(R.string.register_resend) + "(" + (millisUntilFinished / 1000) + ")");
        }

        @Override
        public void onFinish() {
            mBinding.tvGetCode.setText(res.getString(R.string.register_resend));
            mBinding.tvGetCode.setClickable(true);
            mBinding.tvGetCode.setTextColor(Color.parseColor("#ff999999"));
            mBinding.tvGetCode.setBackgroundResource(R.drawable.shape_able);

        }
    }


    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            changeOkBtn();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            changeOkBtn();
        }

        @Override
        public void afterTextChanged(Editable s) {
            changeOkBtn();
        }

    };
}
