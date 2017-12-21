package com.ecjia.view.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.entity.responsemodel.REGISTERCODE;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.common.DialogUtils;
import com.ecjia.util.common.VUtils;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：注册输入手机验证码
 * Created by sun
 * Created time 2017-04-26.
 */

public class RegisterInputCodeActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.tv_phone_hinit)
    TextView tv_phone_hinit;
    @BindView(R.id.edit_code)
    EditText edit_code;
    @BindView(R.id.messagecodecheck_time)
    TextView messagecodecheck_time;
    @BindView(R.id.et_invitation)
    EditText et_invitation;
    @BindView(R.id.tv_invitation)
    TextView tv_invitation;
    @BindView(R.id.get_password_next)
    Button get_password_next;

    private TimeCount time;
    private MyDialog myDialog;

    @Extra
    String phoneNumber;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_inputcode;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);//add this line code
        textView_title.setText("填写验证码");
        tv_phone_hinit.setText(res.getString(R.string.register_code_send) + phoneNumber);
        time = new TimeCount(119900, 1000);
        time.start();
        edit_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (edit_code.getText().toString().length() > 0) {
                    get_password_next.setEnabled(true);
                    get_password_next.setTextColor(Color.parseColor("#ffffffff"));
                    get_password_next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    get_password_next.setEnabled(false);
                    get_password_next.setTextColor(Color.parseColor("#ff999999"));
                    get_password_next.setBackgroundResource(R.drawable.shape_unable);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edit_code.getText().toString().length() > 0) {
                    get_password_next.setEnabled(true);
                    get_password_next.setTextColor(Color.parseColor("#ffffffff"));
                    get_password_next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    get_password_next.setEnabled(false);
                    get_password_next.setTextColor(Color.parseColor("#ff999999"));
                    get_password_next.setBackgroundResource(R.drawable.shape_unable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edit_code.getText().toString().length() > 0) {
                    get_password_next.setEnabled(true);
                    get_password_next.setTextColor(Color.parseColor("#ffffffff"));
                    get_password_next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    get_password_next.setEnabled(false);
                    get_password_next.setTextColor(Color.parseColor("#ff999999"));
                    get_password_next.setBackgroundResource(R.drawable.shape_unable);
                }
            }
        });
    }

    @OnClick({R.id.imageView_back, R.id.messagecodecheck_time, R.id.get_password_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.messagecodecheck_time:
                if (!VUtils.isMobileNO(phoneNumber)) {
                    if (phoneNumber == null || phoneNumber == "") {
                        ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_num_null));
                        finish();
                    } else {
                        ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_num_format));
                        finish();
                    }
                } else {
                    getHttpPhoneCode(phoneNumber);
                }
                break;
            case R.id.get_password_next:
                String codeStr = edit_code.getText().toString();
                String invite_codeStr = et_invitation.getText().toString();
                if (codeStr.length() != 6) {
                    ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_wrong_code));
                    return;
                }
                if (invite_codeStr.length() > 0 && invite_codeStr.length() != 6) {
                    ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_wrong_invite_code));
                    return;
                }
                getCheckPhoneCode(phoneNumber, codeStr);
                break;
        }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            //获取验证码
            messagecodecheck_time.setBackgroundResource(R.drawable.shape_unable);
            messagecodecheck_time.setTextColor(Color.parseColor("#ff999999"));
            messagecodecheck_time.setClickable(false);
            messagecodecheck_time.setText(res.getString(R.string.register_resend) + "(" + (millisUntilFinished / 1000) + ")");
        }

        @Override
        public void onFinish() {
            messagecodecheck_time.setText(res.getString(R.string.register_resend));
            messagecodecheck_time.setClickable(true);
            messagecodecheck_time.setTextColor(Color.parseColor("#ffffff"));
            messagecodecheck_time.setBackgroundResource(R.drawable.selector_get_code);
        }
    }

    private void getHttpPhoneCode(String mobileNo) {
        RetrofitAPIManager.getAPIUser()
                .getRegisterPhoneCode(UserQuery.getInstance().getRegisterPhoneCode(mobileNo))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<REGISTERCODE>() {
                    @Override
                    public void onNext(REGISTERCODE usercoupon) {
                        if ("1".equals(usercoupon.getRegistered())) {//手机号已经注册
                            DialogUtils.showDialog(mActivity, res.getString(R.string.register_tips), res.getString(R.string.register_num_extinct), new DialogUtils.ButtonClickListener() {
                                @Override
                                public void negativeButton() {
                                    //返回登录页面，填入手机号
                                    Intent intent = new Intent(mActivity, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                }

                                @Override
                                public void positiveButton() {
                                    finish();
                                }
                            });
                        } else {
                            time.start();
                            //倒计时开始
                            myDialog = new MyDialog(mActivity, res.getString(R.string.register_tips), res.getString(R.string.register_code_send) + "\n" + mobileNo);
                            myDialog.show();
                            myDialog.setBackKeyNo();
                            myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                            myDialog.setSureOnClickListener(view -> myDialog.dismiss());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeLongToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getCheckPhoneCode(String mobileNo, String code) {
        RetrofitAPIManager.getAPIUser()
                .getRegisterCheckCode(UserQuery.getInstance().getRegisterCheckCode(mobileNo, code))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<REGISTERCODE>() {
                    @Override
                    public void onNext(REGISTERCODE usercoupon) {
                        if ("0".equals(usercoupon.getRegistered())) {
                            RegisterSetPwdActivity_Builder.intent(mActivity).mobile(mobileNo).invite_code(et_invitation.getText().toString()).start();
                            finish();
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        } else {
                            ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_num_extinct2));
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeLongToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myDialog = new MyDialog(this, res.getString(R.string.register_tips), res.getString(R.string.register_back));
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
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        CloseKeyBoard();
                }
            });
            myDialog.show();
        }
        return true;
    }


    @Override
    public void dispose() {

    }
}
