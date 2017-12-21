package com.ecjia.view.activity;


import android.content.Context;
import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.ShoppingCartModel;

import com.ecjia.widgets.ToastView;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import java.util.Timer;
import java.util.TimerTask;

public class IntegralActivity extends BaseActivity {

    private ImageView back;
    private TextView submit;
    private TextView num;
    private EditText input;
    private String integral1;
    private String max_integral;
    private ShoppingCartModel shoppingCartModel;
    private int min_inteagral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral);

        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        String s = intent.getStringExtra("integral");
        try {
            JSONObject jo = new JSONObject(s);

            integral1 = jo.get("your_integral").toString();
            max_integral = jo.get("order_max_integral").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        min_inteagral = Math.min(Integer.valueOf(integral1), Integer.valueOf(max_integral));

        shoppingCartModel = new ShoppingCartModel(this);
        shoppingCartModel.addResponseListener(this);

        back = (ImageView) findViewById(R.id.integral_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        input = (EditText) findViewById(R.id.integral_input);

        input.setFocusable(true);
        input.setFocusableInTouchMode(true);
        input.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(input, 0);
            }

        }, 300);

        num = (TextView) findViewById(R.id.integral_num);

        submit = (TextView) findViewById(R.id.integral_submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Resources resource = (Resources) getBaseContext().getResources();
                String enter_score = resource.getString(R.string.integral_enter_score);
                String score_not_zero = resource.getString(R.string.integral_score_not_zero);
                if (TextUtils.isEmpty(input.getText().toString())) {
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                } else if (Integer.parseInt(input.getText().toString()) > min_inteagral) {
                    ToastView toast = new ToastView(IntegralActivity.this, enter_score);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (Integer.valueOf(input.getText().toString()) > 0) {
                    shoppingCartModel.integral(input.getText().toString());
                }

            }
        });

        Resources resource = (Resources) getBaseContext().getResources();
        String all_of_you = resource.getString(R.string.integral_all_of_you);
        String can_use = resource.getString(R.string.integral_can_use);
        String integral = resource.getString(R.string.integral_integral);
        num.setText(all_of_you + integral1 + integral);
        input.setHint(can_use + min_inteagral + integral);

        input.setText(getIntent().getStringExtra("used_integral"));

    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url == ProtocolConst.VALIDATE_INTEGRAL) {
            if (status.getSucceed() == 1) {
                JSONObject data = null;
                try {
                    data = shoppingCartModel.Integraljson.getJSONObject("data");
                    String bonus = data.getString("bonus").toString();
                    String bonus_formated = data.getString("bonus_formated").toString();
                    Intent intent = new Intent();
                    intent.putExtra("input", input.getText().toString());
                    intent.putExtra("bonus", bonus);
                    intent.putExtra("bonus_formated", bonus_formated);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
