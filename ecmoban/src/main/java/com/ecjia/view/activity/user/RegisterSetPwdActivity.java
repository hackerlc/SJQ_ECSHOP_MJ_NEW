package com.ecjia.view.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.LOGINDATA;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.common.SPUtils;
import com.ecjia.util.common.Verification;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.ybk.intent.inject.annotation.Extra;
import com.ybk.intent.inject.api.IntentInject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：
 * Created by sun
 * Created time 2017-04-27.
 */

public class RegisterSetPwdActivity extends LibActivity implements HttpResponse {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;
    @BindView(R.id.setpassword_password)
    EditText setpassword_password;
    @BindView(R.id.setpassword_next)
    Button setpassword_next;
    @BindView(R.id.setpassword_showpassword)
    CheckBox setpassword_showpassword;

    private MyDialog myDialog;
    @Extra
    String invite_code;
    @Extra
    String mobile;
    @Extra
    String type;//1第三方登录设置密码

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_setpwd;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        IntentInject.inject(this);//add this line code
        textView_title.setText("密码设置");
        if ("1".equals(type)) {
            setpassword_next.setText("确定");
        }
        setpassword_showpassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    setpassword_showpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    setpassword_showpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        setpassword_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (setpassword_password.getText().toString().length() > 0) {
                    setpassword_next.setEnabled(true);
                    setpassword_next.setTextColor(Color.parseColor("#ffffffff"));
                    setpassword_next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    setpassword_next.setEnabled(false);
                    setpassword_next.setTextColor(Color.parseColor("#ff999999"));
                    setpassword_next.setBackgroundResource(R.drawable.shape_unable);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (setpassword_password.getText().toString().length() > 0) {
                    setpassword_next.setEnabled(true);
                    setpassword_next.setTextColor(Color.parseColor("#ffffffff"));
                    setpassword_next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    setpassword_next.setEnabled(false);
                    setpassword_next.setTextColor(Color.parseColor("#ff999999"));
                    setpassword_next.setBackgroundResource(R.drawable.shape_unable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (setpassword_password.getText().toString().length() > 0) {
                    setpassword_next.setEnabled(true);
                    setpassword_next.setTextColor(Color.parseColor("#ffffffff"));
                    setpassword_next.setBackgroundResource(R.drawable.selector_login_button);
                } else {
                    setpassword_next.setEnabled(false);
                    setpassword_next.setTextColor(Color.parseColor("#ff999999"));
                    setpassword_next.setBackgroundResource(R.drawable.shape_unable);
                }
            }
        });
    }

    @OnClick({R.id.imageView_back, R.id.setpassword_showpassword, R.id.setpassword_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                myDialog = new MyDialog(mActivity, res.getString(R.string.register_tips), res.getString(R.string.register_back));
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
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                break;
            case R.id.setpassword_showpassword:
                break;
            case R.id.setpassword_next:
                String pwd = setpassword_password.getText().toString();
                if ("".equals(pwd)) {
                    ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_password_cannot_be_empty));
                    return;
                }
                if (pwd.length() < 6) {
                    ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_pwd_tooshort));
                    return;
                } else if (!Verification.isPsd(pwd)) {
                    ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.register_pwd_format_false));
                    return;
                }
                if ("1".equals(type)) {
                    userInfoModel = new UserInfoModel(this);
                    userInfoModel.addResponseListener(this);
                    ChangePwd(pwd);
                } else {
                    RegisterSetPwd(mobile, pwd, invite_code);
                }
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String tips = res.getString(R.string.register_back);
            if ("1".equals(type)) {
                tips = res.getString(R.string.register_password_back);
            }
            myDialog = new MyDialog(this, res.getString(R.string.register_tips), tips);
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
                    if ("1".equals(type)) {
                        finish();
                    } else {
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                }
            });
        }
        return true;

    }

    private UserInfoModel userInfoModel;

    private void ChangePwd(String password) {
        userInfoModel.changePassword(password);
    }

    private void RegisterSetPwd(String mobile, String password, String invite_code) {
        RetrofitAPIManager.getAPIUser()
                .getRegisterSetPwd(UserQuery.getInstance().getRegisterSetPwd(mobile, password, invite_code))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<LOGINDATA>() {
                    @Override
                    public void onNext(LOGINDATA logindata) {
                        mApp.setUser(logindata.getUser());
                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_LOGIN_SUCCESS));
                        EventBus.getDefault().post(new MyEvent("frommobile"));
//                        SharedPreferences shared = mActivity.getSharedPreferences("userInfo", 0);
//                        SharedPreferences.Editor editor = shared.edit();
//                        editor.putString("uid", logindata.getSession().getUid());
//                        editor.putString("sid", logindata.getSession().getSid());
//                        editor.putString("local_uid", logindata.getSession().getUid());
//                        editor.putString("uname", logindata.getUser().getName());
//                        editor.putString("level", logindata.getUser().getRank_name());
//                        editor.putString("email", logindata.getUser().getEmail());
//                        editor.commit();
//                        SPUtils.init(mActivity);
                        String uidStr=logindata.getSession().getUid();
                        SESSION session=SESSION.getInstance();
                        session.setUid(uidStr);
                        session.setSid(logindata.getSession().getSid());

                        SPUtils.setUid(uidStr);
                        SPUtils.setSid(logindata.getSession().getSid());
                        SPUtils.setLocalUid(uidStr);
                        SPUtils.setUname(logindata.getUser().getName());
                        SPUtils.setLevel(logindata.getUser().getRank_name());
                        SPUtils.setLevel(logindata.getUser().getEmail());
                        ToastUtil.getInstance().makeLongToast(mActivity, res.getString(R.string.login_welcome));
//                        Intent intent = new Intent();
//                        intent.putExtra("login", true);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        setResult(Activity.RESULT_OK, intent);
                        finish();
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

    @Override
    public void dispose() {

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.CHANGE_PASSWORD)) {
            //修改密码成功后
            if (status.getSucceed() == 1) {
                finish();
            } else if (status.getSucceed() == 0) {

            }
        }
    }
}
