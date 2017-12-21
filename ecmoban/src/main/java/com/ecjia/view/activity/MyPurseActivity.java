package com.ecjia.view.activity;

import java.util.ArrayList;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.ECJiaBaseIntent;
import com.ecjia.util.EventBus.MyEvent;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.entity.responsemodel.BONUS;
import com.umeng.message.PushAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class MyPurseActivity extends BaseActivity implements HttpResponse {
    private ImageView iv_redpager;
    private LinearLayout mypurseitem, redpaper_item, integral_item,mypurse_win_integral_item;
    private TextView my_purse, redpapper, my_integral;
    ArrayList<BONUS> data;
    //    private UserInfoModel model;
    private LinearLayout add_redpaper_item;
    private ECJiaTopView topView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_purse);
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        setInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setInfo() {
        topView = (ECJiaTopView) findViewById(R.id.mypurse_topview);

        topView.setLeftBackImage(R.drawable.header_back_arrow, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setTitleText(R.string.purse_mypurse);
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_GONE);

        mypurseitem = (LinearLayout) findViewById(R.id.mypurse_purse_item);
        redpaper_item = (LinearLayout) findViewById(R.id.mypurse_redpaper_item);
        integral_item = (LinearLayout) findViewById(R.id.mypurse_integral_item);
        mypurse_win_integral_item = (LinearLayout) findViewById(R.id.mypurse_win_integral_item);
        add_redpaper_item = (LinearLayout) findViewById(R.id.add_redpaper_item);
        my_purse = (TextView) findViewById(R.id.my_purse);//余额数
        redpapper = (TextView) findViewById(R.id.my_redpaper);//红包数
        iv_redpager = (ImageView) findViewById(R.id.iv_redpager);//红包箭头
        my_integral = (TextView) findViewById(R.id.my_integral);//积分数

        //余额账户
        mypurseitem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MyPurseActivity.this, AccountActivity.class);
                MyPurseActivity.this.startActivity(in);
            }
        });

        //红包账户
        redpaper_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(MyPurseActivity.this, RedpapperListActivity.class);
                MyPurseActivity.this.startActivity(in);
            }
        });
        add_redpaper_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPurseActivity.this, AddRedpaperActivity.class);
                MyPurseActivity.this.startActivity(intent);
            }
        });
        mypurse_win_integral_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ECJiaBaseIntent intent = new ECJiaBaseIntent(MyPurseActivity.this, InvitationWinRewardActivity.class);
                MyPurseActivity.this.startActivity(intent);
            }
        });

        if (mApp.getUser() != null && !TextUtils.isEmpty(mApp.getUser().getId())) {
            setData();
            UserInfoModel model = new UserInfoModel(this);
            model.addResponseListener(this);
            model.getUserInfo();
        }
    }

    private void setData() {
        my_purse.setText(mApp.getUser().getFormated_user_money());
        redpapper.setText(mApp.getUser().getUser_bonus_count());
        my_integral.setText(mApp.getUser().getUser_points());
        mypurseitem.setEnabled(true);
        redpaper_item.setEnabled(true);
        integral_item.setEnabled(true);
        if ("0".equals(mApp.getUser().getUser_bonus_count())) {
            redpaper_item.setEnabled(false);
            iv_redpager.setVisibility(View.GONE);
        } else {
            redpaper_item.setEnabled(true);
            my_integral.setVisibility(View.VISIBLE);
        }


    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("changed".equals(event.getMsg())) {
            my_purse.setText(mApp.getUser().getFormated_user_money());
        }
        if ("red_paper_refresh".equals(event.getMsg())) {
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.USERINFO) {
            if (status.getSucceed() == 1) {
                setData();
            } else {
                mypurseitem.setEnabled(false);
                redpaper_item.setEnabled(false);
                integral_item.setEnabled(false);
                iv_redpager.setVisibility(View.INVISIBLE);
            }
        }
    }
}
