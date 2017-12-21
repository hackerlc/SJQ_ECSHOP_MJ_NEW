package com.ecjia.view.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ecmoban.android.sijiqing.R;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：注册输入手机号码
 * Created by sun
 * Created time 2017-04-26.
 */

public class RegisterInputPhoneActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.get_password_next)
    Button get_password_next;
    @BindView(R.id.get_password_edit)
    EditText get_password_edit;


    @Override
    public int getLayoutId() {
        return R.layout.activity_get_password;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        textView_title.setText("验证手机号码");
        get_password_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (get_password_edit.getText().toString().length() == 11) {
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
                if (get_password_edit.getText().toString().length() == 11) {
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
                if (get_password_edit.getText().toString().length() == 11) {
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

    private void getHttp(String mobileNo) {
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
                                    get_password_edit.setText("");
                                }
                            });
                        } else {
                            RegisterInputCodeActivity_Builder.intent(mActivity).phoneNumber(mobileNo).start();
                            finish();
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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

    @OnClick({R.id.imageView_back, R.id.get_password_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.get_password_next:
                String mobileNo=get_password_edit.getText().toString();
                if (!VUtils.isMobileNO(mobileNo)) {
                    if (mobileNo == null || mobileNo == "") {
                        ToastUtil.getInstance().makeLongToast(mActivity,res.getString(R.string.register_num_null));
                    } else {
                        ToastUtil.getInstance().makeLongToast(mActivity,res.getString(R.string.register_num_format));
                    }
                } else {
                    getHttp(mobileNo);
//                    RegisterInputCodeActivity_Builder.intent(mActivity).phoneNumber(mobileNo).start();
//                    finish();
//                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
                break;
        }
    }


    @Override
    public void dispose() {

    }
}
