package com.ecjia.view.activity.user;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RegisterModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 类名介绍：邮箱注册
 * Created by sun
 * Created time 2017-02-13.
 */
public class RegisterActivity extends BaseActivity implements OnClickListener, HttpResponse {

	private Button register;

	private EditText userName;
	private EditText email;
	private EditText password1;

	private String name;
	private String mail;
	private String psd1;

	private RegisterModel registerModel;

	public static Map<Integer, EditText> edit;
	private JSONArray jsonArray = new JSONArray();

	private boolean flag = true;

	Resources resource;
	private String invite_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		PushAgent.getInstance(this).onAppStart();

		initTopView();

		invite_code=getIntent().getStringExtra("invite_code");

		resource = (Resources) getBaseContext().getResources();

		register = (Button) findViewById(R.id.register_register);
		userName = (EditText) findViewById(R.id.register_name);
		email = (EditText) findViewById(R.id.register_email);
		password1 = (EditText) findViewById(R.id.register_password1);
		register.setOnClickListener(this);

		registerModel = new RegisterModel(this);
		registerModel.addResponseListener(this);


	}

	@Override
	public void initTopView() {
		super.initTopView();
		topView = (ECJiaTopView) findViewById(R.id.register_topview);
		topView.setTitleText(R.string.login_register);
		topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.register_register:
				name = userName.getText().toString();
				mail = email.getText().toString();
				psd1 = password1.getText().toString();
				String user = resource.getString(R.string.register_user_name_cannot_be_empty);
				String email = resource.getString(R.string.register_email_cannot_be_empty);
				String pass = resource.getString(R.string.register_password_cannot_be_empty);
				String fault = resource.getString(R.string.register_email_format_false);

				if ("".equals(name)) {
					ToastView toast = new ToastView(this, user);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if ("".equals(mail)) {
					ToastView toast = new ToastView(this, email);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if ("".equals(psd1)) {
					ToastView toast = new ToastView(this, pass);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (!RegisterActivity.this.isEmail(mail)) {
					ToastView toast = new ToastView(this, fault);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (name.length() > 15) {
					ToastView toast = new ToastView(this, "用户名过长");
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (psd1.length() < 6) {
					ToastView toast = new ToastView(this, "密码不能少于6位");
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (!RegisterActivity.this.isPsd(psd1)) {
					ToastView toast = new ToastView(this, "密码格式不正确");
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					signup();

				}
				break;
		}

	}

	public void signup() {
		if (flag) {
			CloseKeyBoard(); // 关闭键盘
			registerModel.signup(name, psd1, mail, jsonArray, "",invite_code);
		}

	}


	// 关闭键盘
	public void CloseKeyBoard() {
		userName.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
	}

	public static boolean isEmail(String email) {
		String str = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isPsd(String pasd) {
		String reg = "^[A-Za-z0-9*#@.&_]+$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(pasd);
		return m.matches();
	}

	@Override
	public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
		if (url == ProtocolConst.SIGNUPFIELDS) {
		} else if (url == ProtocolConst.SIGNUP) {
			if (status.getSucceed() == 1) {
				Intent intent = new Intent();
				intent.putExtra("login", true);
				setResult(Activity.RESULT_OK, intent);
				finish();
				String wel = resource.getString(R.string.login_welcome);
				ToastView toast = new ToastView(RegisterActivity.this, wel);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
	}
}
