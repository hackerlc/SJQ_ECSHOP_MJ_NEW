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

import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 有天倒计时的View 红色
 */
public class CountDownNewRedView extends LinearLayout {

    TextView tvDay;
    TextView tvHour;
    TextView tvMinute;
    TextView tvSecond;
    TextView tv_activity_end;
    LinearLayout ly_activity_start;

    private Context context;
    private long mDay;
    private long mHour;
    private long mMinute;
    private long mSecond;

    private Timer timer = null;

    private Handler statusHandler;

    private boolean isEnd=false;//活动是否已经结束


    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            countDown();
        }
    };

    public CountDownNewRedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_countdown_new_red_layout, this);
        tvDay = (TextView) view.findViewById(R.id.tv_day);
        tvHour = (TextView) view.findViewById(R.id.tv_hour);
        tvMinute = (TextView) view.findViewById(R.id.tv_minute);
        tvSecond = (TextView) view.findViewById(R.id.tv_second);
        tv_activity_end=(TextView) view.findViewById(R.id.tv_activity_end);
        ly_activity_start=(LinearLayout) view.findViewById(R.id.ly_activity_start);
    }

    public void setActivityEnd(boolean isEnd){
        if(isEnd){//true end
            tv_activity_end.setVisibility(View.VISIBLE);
            ly_activity_start.setVisibility(View.GONE);
        }else{
            tv_activity_end.setVisibility(View.GONE);
            ly_activity_start.setVisibility(View.VISIBLE);
        }
    }


    public void setTime(long leftTime, Handler statusHandler) {
        long time = leftTime / 1000;
        long day = time / (3600 * 24);
        long hours = (time - day * 3600 * 24) / 3600;
        long minutes = (time - day * 3600 * 24 - hours * 3600) / 60;
        long seconds = time - day * 3600 * 24 - hours * 3600 - minutes * 60;
        this.statusHandler = statusHandler;
        setTime(day, hours, minutes, seconds);
    }

    public void setTime(long day, long hour, long min, long sec) {
        if (hour >= 60 || min >= 60 || sec >= 60 || hour < 0 || min < 0
                || sec < 0) {
            throw new RuntimeException("Time format is error");
        }
        mDay = day;
        mHour = hour;
        mMinute = min;
        mSecond = sec;
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

    //保证天，时，分，秒都两位显示，不足的补0
    private void setTextTime() {
        if (mDay < 10) {
            tvDay.setText("0" + mDay);
        } else {
            tvDay.setText(mDay + "");
        }

        if (mHour < 10) {
            tvHour.setText("0" + mHour);
        } else {
            tvHour.setText(mHour + "");
        }

        if (mMinute < 10) {
            tvMinute.setText("0" + mMinute);
        } else {
            tvMinute.setText(mMinute + "");
        }

        if (mSecond < 10) {
            tvSecond.setText("0" + mSecond);
        } else {
            tvSecond.setText(mSecond + "");
        }
    }

    private void countDown() {
        if (isCarry4Unit(tvSecond)) {
            if (isCarry4Unit(tvMinute)) {
                if (isCarry4Unit(tvHour)) {
                    if (isCarry4Unit(tvDay)) {
                        ToastView toastView = new ToastView(context, "倒计时结束了");
                        toastView.setGravity(Gravity.CENTER);
                        toastView.show();
//                        ly_activity_start.setVisibility(View.GONE);
//                        tv_activity_end.setVisibility(View.VISIBLE);
                        Message message = new Message();
                        message.what = 2000;
                        statusHandler.sendMessage(message);
                        stop();
                    }
                }
            }
        }
    }

    private boolean isCarry4Unit(TextView tv) {
        int time = Integer.valueOf(tv.getText().toString());
        time = time - 1;
        if (time < 0) {
            time = 59;
            tv.setText(time + "");
            return true;
        } else {
            if (time < 10) {
                tv.setText("0" + time + "");
            } else {
                tv.setText(time + "");
            }
            return false;
        }
    }
}
