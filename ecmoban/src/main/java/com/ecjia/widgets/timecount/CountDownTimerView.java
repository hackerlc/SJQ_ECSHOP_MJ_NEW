/*
 * Copyright (c) 2014, �ൺ˾ͨ�Ƽ����޹�˾ All rights reserved.
 * File Name��RushBuyCountDownTimerView.java
 * Version��V1.0
 * Author��zhaokaiqiang
 * Date��2014-9-26
 */
package com.ecjia.widgets.timecount;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ToastView;

import java.util.Timer;
import java.util.TimerTask;


public class CountDownTimerView extends LinearLayout {

    TextView hourDecade;
    TextView hourUnit;
    TextView minDecade;
    TextView minUnit;
    TextView secDecade;
    TextView secUnit;


    private Context context;

    private long hDecade;
    private long hUnit;
    private long mDecade;
    private long mUnit;
    private long sDecade;
    private long sUnit;

    private Timer timer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        }
    };

    public CountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_countdown_timer, this);
        hourDecade = (TextView) view.findViewById(R.id.hour_decade);
        hourUnit = (TextView) view.findViewById(R.id.hour_unit);
        minDecade = (TextView) view.findViewById(R.id.min_decade);
        minUnit = (TextView) view.findViewById(R.id.min_unit);
        secDecade = (TextView) view.findViewById(R.id.sec_decade);
        secUnit = (TextView) view.findViewById(R.id.sec_unit);
    }

    private long testTime = (10 * 3600 + 18 * 60 + 53) * 1000;

    public void setTime() {
        long time = testTime / 1000;
        long hours = time / (60 * 60);
        long minutes = (time - hours * 3600) / 60;
        long seconds = time - hours * 3600 - minutes * 60;

        setTime(hours, minutes, seconds);
    }

    public void setTime(long hour, long min, long sec) {
        if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
            throw new RuntimeException("Time format is error");
        }

        hDecade = hour / 10;
        hUnit = hour - hDecade * 10;

        mDecade = min / 10;
        mUnit = min - mDecade * 10;

        sDecade = sec / 10;
        sUnit = sec - sDecade * 10;

        setTextTime();
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void setTextTime() {
        hourDecade.setText(hDecade + "");
        hourUnit.setText(hUnit + "");
        minDecade.setText(mDecade + "");
        minUnit.setText(mUnit + "");
        secDecade.setText(sDecade + "");
        secUnit.setText(sUnit + "");
    }

    private void countDown() {
        if (isCarry4Unit(secUnit)) {
            if (isCarry4Decade(secDecade)) {
                if (isCarry4Unit(minUnit)) {
                    if (isCarry4Decade(minDecade)) {
                        if (isCarry4Unit(hourUnit)) {
                            if (isCarry4Decade(hourDecade)) {
                                ToastView toastView = new ToastView(context, "倒计时结束了");
                                toastView.setGravity(Gravity.CENTER);
                                toastView.show();
                                stop();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isCarry4Decade(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 5;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }

    private boolean isCarry4Unit(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 9;
            tv.setText(time + "");
            return true;
        } else {
            tv.setText(time + "");
            return false;
        }

    }
}
