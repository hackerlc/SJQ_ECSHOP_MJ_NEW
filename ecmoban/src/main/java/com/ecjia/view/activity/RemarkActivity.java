package com.ecjia.view.activity;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ToastView;
import com.umeng.message.PushAgent;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

import java.util.Timer;
import java.util.TimerTask;

public class RemarkActivity extends BaseActivity implements TextWatcher {
    private EditText message;
    private ImageView back;
    private Button save;
    private Resources resources;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);

        PushAgent.getInstance(this).onAppStart();
        back = (ImageView) findViewById(R.id.remark_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String remark = intent.getStringExtra("remark");
        message = (EditText) findViewById(R.id.remark_content);
        message.setText(remark);

        message.setFocusable(true);
        message.setFocusableInTouchMode(true);
        message.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) message.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(message, 0);
                           }

                       },
                300);

        message.addTextChangedListener(this);
        message.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText textView = (EditText) v;
                String hint;
                if (hasFocus) {
                    hint = textView.getHint().toString();
                    textView.setTag(hint);
                    textView.setHint("");
                } else {
                    hint = textView.getTag().toString();
                    textView.setHint(hint);
                }

            }
        });
        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        save = (Button) findViewById(R.id.remark_save);
        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("remark", message.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) {
        resources = getBaseContext().getResources();
        String content_over_limit = resources.getString(R.string.remark_content_over_limit);
        int left = 255 - message.getText().toString().trim().length();
        if (left == 0) {
            ToastView toast = new ToastView(RemarkActivity.this, content_over_limit);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


}
