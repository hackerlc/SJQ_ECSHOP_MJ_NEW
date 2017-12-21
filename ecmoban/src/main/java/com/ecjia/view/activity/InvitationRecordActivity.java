package com.ecjia.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.InviteModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.ECJiaBaseIntent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;


public class InvitationRecordActivity extends BaseActivity implements HttpResponse,OnClickListener {


    private ImageView iv_points_reward, iv_redpacket_reward,iv_cash_reward;
    private TextView tv_points_reward, tv_redpacket_reward,tv_cash_reward;
    private FrameLayout fl_invitation_detail,fl_get_reward;
    private InviteModel inviteModel;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_record);

        PushAgent.getInstance(this).onAppStart();

        initView();

        inviteModel=new InviteModel(this);
        inviteModel.addResponseListener(this);
        inviteModel.getInviteReward();

    }

    private void initView() {
        initTopView();

        tv_points_reward = (TextView) findViewById(R.id.tv_points_reward);
        tv_redpacket_reward = (TextView) findViewById(R.id.tv_redpacket_reward);
        tv_cash_reward = (TextView) findViewById(R.id.tv_cash_reward);
        iv_points_reward =(ImageView) findViewById(R.id.iv_points_reward);
        iv_redpacket_reward =(ImageView) findViewById(R.id.iv_redpacket_reward);
        iv_cash_reward =(ImageView) findViewById(R.id.iv_cash_reward);
        iv_points_reward.setOnClickListener(this);
        iv_redpacket_reward.setOnClickListener(this);
        iv_cash_reward.setOnClickListener(this);

        fl_invitation_detail =(FrameLayout) findViewById(R.id.fl_invitation_detail);
        fl_invitation_detail.setOnClickListener(this);
        fl_get_reward =(FrameLayout) findViewById(R.id.fl_get_reward);
        fl_get_reward.setOnClickListener(this);
    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.invitation_topview);
        topView.setTitleText(R.string.invitation_reward);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.INVITE_REWARD) {
            if (status.getSucceed() == 1) {
                tv_points_reward.setText(inviteModel.inviteTotal.getInvite_integral_reward()+"");
                tv_redpacket_reward.setText(inviteModel.inviteTotal.getInvite_bouns_reward()+"");
                tv_cash_reward.setText(inviteModel.inviteTotal.getInvite_balance_reward()+"");
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_points_reward:
                showTipsDialog(1);
            break;
            case R.id.iv_redpacket_reward:
                showTipsDialog(2);
            break;
            case R.id.iv_cash_reward:
                showTipsDialog(3);
            break;
            case R.id.fl_invitation_detail:
                if(inviteModel.invite_rewards.size()>0){
                    Intent intent=new Intent(InvitationRecordActivity.this,InvitationRewardActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("rewards",inviteModel.invite_rewards);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    ToastView toastView=new ToastView(InvitationRecordActivity.this,res.getString(R.string.invitation_reward_detail_tips));
                    toastView.setGravity(Gravity.CENTER,0,0);
                    toastView.show();
                }

            break;
            case R.id.fl_get_reward:
                ECJiaBaseIntent intent=new ECJiaBaseIntent(InvitationRecordActivity.this,InvitationWinRewardActivity.class);
                startActivityForResult(intent, 100);
            break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK){
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    private void showTipsDialog(int i) {
        String main = res.getString(R.string.reward_tips);
        String main_content="";
        switch (i){
            case 1:
                main_content = res.getString(R.string.reward_tips_content);
                break;
            case 2:
                main_content = res.getString(R.string.reward_tips_content2);
                break;
            case 3:
                main_content = res.getString(R.string.reward_tips_content3);
                break;
        }

        myDialog = new MyDialog(this, main, main_content);
        myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
        myDialog.setSureOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
}

