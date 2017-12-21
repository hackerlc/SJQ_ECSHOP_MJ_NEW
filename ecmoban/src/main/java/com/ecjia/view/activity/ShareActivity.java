package com.ecjia.view.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ecjia.base.BaseActivity;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.IntentKeywords;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareActivity extends BaseActivity implements OnClickListener {
    LinearLayout sinawb, qq, weixin, circle, sms, email, back, clip;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        PushAgent.getInstance(this).onAppStart();
        Window lp = getWindow();
        lp.setGravity(Gravity.BOTTOM);
        lp.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        intent = getIntent();
        setInfo();

    }

    //基础信息
    void setInfo() {
        sinawb = (LinearLayout) findViewById(R.id.share_sinawb);
        qq = (LinearLayout) findViewById(R.id.share_qqfriend);
        weixin = (LinearLayout) findViewById(R.id.share_weixinitem);
        circle = (LinearLayout) findViewById(R.id.share_circle);
        sms = (LinearLayout) findViewById(R.id.share_smsitem);
        email = (LinearLayout) findViewById(R.id.share_emailitem);
        clip = (LinearLayout) findViewById(R.id.share_clipitem);
        back = (LinearLayout) findViewById(R.id.share_cancle);

        sinawb.setOnClickListener(this);
        qq.setOnClickListener(this);
        weixin.setOnClickListener(this);
        circle.setOnClickListener(this);
        sms.setOnClickListener(this);
        email.setOnClickListener(this);
        clip.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_sinawb:
                startShare(SHARE_MEDIA.SINA);
                break;
            case R.id.share_qqfriend:
                startShare(SHARE_MEDIA.QQ);
                break;
            case R.id.share_weixinitem:
                startShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_circle:
                startShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_smsitem:
                startShare(SHARE_MEDIA.SMS);
                break;
            case R.id.share_emailitem:
                startShare(SHARE_MEDIA.EMAIL);
                break;
            case R.id.share_clipitem:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, intent.getStringExtra(IntentKeywords.SHARE_CONTENT) + intent.getStringExtra(IntentKeywords.SHARE_GOODS_URL)));
                ToastView toast = new ToastView(ShareActivity.this, res.getString(R.string.share_clip_tips));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(200);
                toast.show();
                break;
            case R.id.share_cancle:
                this.finish();
                break;
        }
    }

    private void startShare(SHARE_MEDIA shareType) {

        UMImage umi = null;
        String share_url = intent.getStringExtra(IntentKeywords.SHARE_IMAGE_URL);
        if (!TextUtils.isEmpty(share_url)) {
            umi = new UMImage(ShareActivity.this, share_url);
        } else {
            umi = new UMImage(ShareActivity.this, R.drawable.ecmoban_logo);
        }

        new ShareAction(this)
                .setPlatform(shareType)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        if (share_media == SHARE_MEDIA.EMAIL || share_media == SHARE_MEDIA.SMS) {

                        } else {
                            ToastView toast = new ToastView(ShareActivity.this, res.getString(R.string.share_succeed));
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.setDuration(200);
                            toast.show();
                        }
                        finish();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastView toast = new ToastView(ShareActivity.this, res.getString(R.string.share_failed));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(200);
                        toast.show();
                        finish();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ToastView toast = new ToastView(ShareActivity.this, res.getString(R.string.share_cancel));
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setDuration(200);
                        toast.show();
                        finish();
                    }
                })
                .withText(intent.getStringExtra(IntentKeywords.SHARE_CONTENT))
                .withTitle(intent.getStringExtra(IntentKeywords.SHARE_GOODS_NAME))
                .withTargetUrl(intent.getStringExtra(IntentKeywords.SHARE_GOODS_URL))
                .withMedia(umi)
                .share();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    protected void onDestroy() {
        super.onDestroy();
    }


}
