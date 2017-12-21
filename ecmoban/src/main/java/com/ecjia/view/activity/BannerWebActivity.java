package com.ecjia.view.activity;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.umeng.message.PushAgent;

public class BannerWebActivity extends BaseActivity {

    private TextView title;
    private ImageView back;
    private WebView webView;
    private LinearLayout payweb_wap;
    private Resources resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_web);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        String url = intent.getStringExtra(IntentKeywords.WEB_URL);
        title = (TextView) findViewById(R.id.top_view_text);
        payweb_wap = (LinearLayout) findViewById(R.id.payweb_wap);
        payweb_wap.setVisibility(View.VISIBLE);
        resource = AppConst.getResources(this);
        String bro = resource.getString(R.string.browser);
        title.setText(bro);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.pay_web);
        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        if (TextUtils.isEmpty(url)) {
            webView.loadUrl(ConfigModel.getInstance().config.getSite_url());
        } else {
            webView.loadUrl(url);
        }

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        webView.setInitialScale(25);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    BannerWebActivity.this.title.setText(title);
                }
            }

        });


    }

}
