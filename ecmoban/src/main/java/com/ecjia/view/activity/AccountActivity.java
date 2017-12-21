package com.ecjia.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.consts.AppConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class AccountActivity extends BaseActivity {
    private ImageView back;
    private TextView title, mymoney, tv_rechangelist;
    private Button btn_rechange, btn_withdraw;
    private UserInfoModel userInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        userInfoModel = new UserInfoModel(this);
        userInfoModel.addResponseListener(this);
        userInfoModel.getUserInfo();
        initView();
    }

    void initView() {
        Resources res = AppConst.getResources(this);
        title = (TextView) findViewById(R.id.top_view_text);
        back = (ImageView) findViewById(R.id.top_view_back);
        mymoney = (TextView) findViewById(R.id.account_money);
        tv_rechangelist = (TextView) findViewById(R.id.top_right_save);
        tv_rechangelist.setText(res.getString(R.string.accoubt_record));
        tv_rechangelist.setVisibility(View.VISIBLE);
        btn_rechange = (Button) findViewById(R.id.account_rechange);
        btn_withdraw = (Button) findViewById(R.id.account_withdraw);
        title.setText(res.getString(R.string.account_user));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_rechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, YueRechangeActivity.class);
                AccountActivity.this.startActivity(intent);
            }
        });
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, WithdrawalActivity.class);
                AccountActivity.this.startActivity(intent);
            }
        });
        tv_rechangelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, RechargeHistoryActivity.class);
                AccountActivity.this.startActivity(intent);
            }
        });
        setInfo();
    }

    private void setInfo() {
        mymoney.setText(mApp.getUser().getFormated_user_money());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(MyEvent event) {
        if ("changed".equals(event.getMsg())) {
            mymoney.setText(mApp.getUser().getFormated_user_money());
        }

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);

        if (url.equals(ProtocolConst.USERINFO)) {
            if (status.getSucceed() == 1) {
                setInfo();
            }
        }
    }
}
