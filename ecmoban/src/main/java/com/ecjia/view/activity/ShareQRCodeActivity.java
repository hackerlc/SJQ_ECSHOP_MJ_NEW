package com.ecjia.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ecjia.base.BaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.netmodle.InviteModel;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.widgets.ECJiaTopView;
import com.ecmoban.android.sijiqing.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;


public class ShareQRCodeActivity extends BaseActivity implements View.OnClickListener, HttpResponse {


    private TextView tv_invitation_code, tv_invitation_tips;
    private ImageView iv_share_qr;
    private InviteModel inviteModel;
    private ECJiaTopView topView;
    private Button btn_invite;
    private EditText et_invitation;

    private LinearLayout ll_share_qr;

    private Handler Intenthandler;

    public int with;
    public LinearLayout.LayoutParams lg_bg_share;

    private String invite_code, invite_content, invite_explain, invite_url;

    private String share_notice;
    private int startType;
    private ScrollView sc_invite;
    private LinearLayout ll_my_suggest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_suggest);

        EventBus.getDefault().register(this);
        registerReceiver(mHomeKeyEventReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));

        with = getWindowManager().getDefaultDisplay().getWidth() < getWindowManager().getDefaultDisplay().getHeight() ? getWindowManager().getDefaultDisplay().getWidth() : getWindowManager()
                .getDefaultDisplay().getHeight();
        initView();


        inviteModel = new InviteModel(this);
        inviteModel.addResponseListener(this);
        inviteModel.getInviteUser();


    }

    private void initView() {
        topView = (ECJiaTopView) findViewById(R.id.suggest_topview);
        topView.setTitleText(R.string.invite);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//
//        topView.setRightType(ECJiaTopView.RIGHT_TYPE_TEXT);
//        topView.setRightText(R.string.invitation_reward, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ShareQRCodeActivity.this, InvitationRecordActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
//                closeKeyBoard(et_invitation);
//            }
//        });

        iv_share_qr = (ImageView) findViewById(R.id.iv_share_qr);

        tv_invitation_code = (TextView) findViewById(R.id.tv_invitation_code);
        tv_invitation_tips = (TextView) findViewById(R.id.tv_invitation_tips);
        et_invitation = (EditText) findViewById(R.id.et_invitation);
        sc_invite = (ScrollView) findViewById(R.id.sc_invite);
        ll_my_suggest = (LinearLayout) findViewById(R.id.ll_my_suggest);

        //二维码图片的布局尺寸
        ll_share_qr = (LinearLayout) findViewById(R.id.ll_share_qr);
        lg_bg_share = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lg_bg_share.height = with * 1 / 2;
        lg_bg_share.width = with * 1 / 2;
        ll_share_qr.setLayoutParams(lg_bg_share);

        btn_invite = (Button) findViewById(R.id.btn_invite);

        btn_invite.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        controlKeyboardLayout(ll_my_suggest, btn_invite);
        startType = getIntent().getIntExtra("startType", 0);
        LG.i("startType===" + startType);
    }

    @Override
    public void finish() {
        super.finish();
        closeKeyBoard(et_invitation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_invite:
                startShare();
                break;
        }
    }


    private void startShare() {
        Intent it = new Intent(this, ShareActivity.class);
        it.putExtra(IntentKeywords.SHARE_CONTENT, et_invitation.getText().toString());
        it.putExtra(IntentKeywords.SHARE_GOODS_URL, invite_url);
        it.putExtra(IntentKeywords.SHARE_GOODS_NAME, mApp.getUser().getName() + "推荐这个实用的App给你～");
        startActivity(it);
        overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
        closeKeyBoard(et_invitation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.INVITE_USER) {
            if (status.getSucceed() == 1) {
                Glide.with(this).load(inviteModel.userInvite.getInvite_qrcode_image()).into(iv_share_qr);
//                ImageLoaderForLV.getInstance(this).setImageRes(iv_share_qr, inviteModel.userInvite.getInvite_qrcode_image());

                et_invitation.setText(inviteModel.userInvite.getInvite_template());
                if (inviteModel.userInvite.getInvite_template().length() > 0) {
                    et_invitation.setSelection(inviteModel.userInvite.getInvite_template().length());
                }
                tv_invitation_tips.setText(inviteModel.userInvite.getInvite_explain());
                tv_invitation_code.setText(inviteModel.userInvite.getInvite_code());
                invite_url = inviteModel.userInvite.getInvite_url();
            }
        }
    }

    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域
//                    ll_share_qr.setVisibility(View.GONE);
                } else {
                    //键盘隐藏
//                    ll_share_qr.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onEvent(MyEvent event) {
        if ("not_from_widget".equals(event.getMsg())) {
            if (startType == 1) {
                this.finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mHomeKeyEventReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    if (startType == 0) {
                        ShareQRCodeActivity.this.finish();
                    }
                    //表示按了home键,程序到了后台
                } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };

}

