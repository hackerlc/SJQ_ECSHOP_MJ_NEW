package com.ecjia.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ecjia.base.BaseActivity;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.goodsorder.OrderListActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.OrderType;
import com.ecjia.util.ECJiaBaseIntent;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.message.PushAgent;

import de.greenrobot.event.EventBus;


public class InvitationWinRewardActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_win_reward1,iv_win_reward4,iv_win_reward5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_reward);

        PushAgent.getInstance(this).onAppStart();

        initView();

    }

    private void initView() {
        initTopView();

        iv_win_reward1 =(ImageView) findViewById(R.id.iv_win_reward1);
        iv_win_reward4 =(ImageView) findViewById(R.id.iv_win_reward4);
        iv_win_reward5 =(ImageView) findViewById(R.id.iv_win_reward5);

        setBigImageView(iv_win_reward1);
        setBigImageView(iv_win_reward4);
        setBigImageView(iv_win_reward5);

        iv_win_reward1.setOnClickListener(this);
        iv_win_reward4.setOnClickListener(this);
        iv_win_reward5.setOnClickListener(this);
    }

    void setBigImageView(ImageView img) {
        int ten=(int)res.getDimension(R.dimen.dp_10);
        int width=getDisplayMetricsWidth()-ten*2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width * 10 / 27);
        lp.setMargins(ten,ten,ten,0);
        img.setLayoutParams(lp);
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.invitation_topview);
        topView.setTitleText(R.string.invitation_get_reward);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_win_reward1:
                if (getIntent().getStringExtra(ECJiaBaseIntent.ACTIVITY_NAME) != null
                        && !getIntent().getStringExtra(ECJiaBaseIntent.ACTIVITY_NAME)
                        .equals(InvitationRecordActivity.class.getName())) {
                    intent = new Intent(this, ShareQRCodeActivity.class);
                    intent.putExtra("startType", 1);
                    startActivity(intent);
                }else{
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

                break;

            case R.id.iv_win_reward4:
                EventBus.getDefault().post(new MyEvent(EventKeywords.WINREWARD_ECJIAMAIN));
                intent = new Intent(this, ECJiaMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.iv_win_reward5:
                intent = new Intent(this, OrderListActivity.class);
                intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.ALLOW_COMMENT);
                startActivity(intent);
                break;
        }
    }
}

