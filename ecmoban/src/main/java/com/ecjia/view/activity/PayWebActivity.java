package com.ecjia.view.activity;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AppConst;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.umeng.message.PushAgent;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;

import de.greenrobot.event.EventBus;

public class PayWebActivity extends BaseActivity {
    private TextView title;
    private ImageView back;
    private WebView webView;
    private Handler handler;
    private String data;
    String paycode;
    Intent intent;
    private LinearLayout wapitem, bankitem;
    private TextView banktext;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_web);

        PushAgent.getInstance(this).onAppStart();

        EventBus.getDefault().register(this);


        initView();

        intent = getIntent();
        data = intent.getStringExtra("html");// 网页支付
        paycode = intent.getStringExtra("code");
        if (AppConst.BANK.equals(paycode)) {
            wapitem.setVisibility(View.GONE);
            bankitem.setVisibility(View.VISIBLE);
            setBankPay();
        } else if (AppConst.ALIPAY_MOBILE.equals(paycode)) {
            wapitem.setVisibility(View.VISIBLE);
            bankitem.setVisibility(View.GONE);
            setWapPay();
        } else {

        }

    }

    private void setBankPay() {
        banktext.setText(data);
    }

    @SuppressLint("JavascriptInterface")
    private void setWapPay() {
        webView.loadUrl(data);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + " ECJiaBrowse/1.2.0");

        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LG.i("点击weburl:" + url);
                if (url.contains("ecjiaopen://app?open_type")) {
                    ECJiaOpenType.getDefault().startAction(PayWebActivity.this, url);
                    finish();
                    return true;
                }
                return super.shouldOverrideUrlLoading(view,url);
            }

        });

        webSettings.setBuiltInZoomControls(true);// 保留屏幕放缩按钮
        // 2013-12-2修改
        webView.addJavascriptInterface(new Object() {//pc支付宝监听
            @JavascriptInterface
            public void back() {
                handler = new Handler();
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        EventBus.getDefault().post(new MyEvent("wappay"));
                        finish();
                    }
                });
            }
        }, "ecmoban");// obj要和windows.obj中的obj一样

    }

    private void initView() {
        title = (TextView) findViewById(R.id.top_view_text);
        Resources resource = (Resources) getBaseContext().getResources();
        String pay = resource.getString(R.string.pay);
        title.setText(pay);

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PayWebActivity.this.finish();
                PayWebActivity.this.overridePendingTransition(
                        R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        webView = (WebView) findViewById(R.id.pay_web);
        wapitem = (LinearLayout) findViewById(R.id.payweb_wap);
        bankitem = (LinearLayout) findViewById(R.id.payweb_bank);
        banktext = (TextView) findViewById(R.id.pay_banktext);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return true;

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Event event) {

    }
}
