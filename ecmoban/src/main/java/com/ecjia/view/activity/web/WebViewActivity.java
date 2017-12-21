package com.ecjia.view.activity.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.ecmoban.android.sijiqing.R;

import gear.yc.com.gearlibrary.utils.web.BaseWeb;

/**
 * GearApplication
 * Created by YichenZ on 2016/3/29 15:11.
 */
public class WebViewActivity extends NewBaseActivity{
    WebView dataWv;
    private TextView title;
    private ImageView back;

    final String[] BASE_URL={"http://www.sjq.cn"};
    final String[] BASE_URL_TOP={"http://www.sjq.cn"};

    //缓存当前URL
    String cacheUrl="";

    WebSettings settings;
    BaseWeb baseWeb;

    String defUrl;
    String defTitle;

    boolean isFirst;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFirst = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(baseWeb!=null) {
            settings=null;
            baseWeb = null;
        }
    }

    public void initUI() {
        setContentView(R.layout.activity_web_view);
        dataWv=(WebView)findViewById(R.id.wv_web_view);
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(getResources().getString(R.string.browser));
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(v -> finish());
    }

    public void initData() {
        defUrl=getIntent().getStringExtra(IntentKeywords.WEB_URL);
        if(defUrl!=null && !defUrl.equals("")){
            BASE_URL[0]=defUrl;
            BASE_URL_TOP[0]=defUrl;
        }
        baseWeb=new BaseWeb();
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(isFirst) {
                    isFirst = false;
                    if (url.contains("ecjiaopen://app?open_type")) {
                        ECJiaOpenType.getDefault().startAction(WebViewActivity.this, url);
                        if (!url.contains("goods_detail")) {
                            finish();
                        }
                    } else {
                        view.loadUrl(url);
                    }
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                cacheUrl=url;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(settings !=null &&!settings.getLoadsImagesAutomatically()) {
                    settings.setLoadsImagesAutomatically(true);
                }
            }
        };
        WebChromeClient webChromeClient =new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    WebViewActivity.this.title.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        };
        dataWv.setWebViewClient(webViewClient);
        dataWv.setWebChromeClient(webChromeClient);
        dataWv.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        baseWeb.setSettings(dataWv.getSettings());
        settings=baseWeb.setWeb(this);
        dataWv.loadUrl(BASE_URL[0]);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && dataWv.canGoBack()) {
            return isGoBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isGoBack(){
        for(String url:BASE_URL){
            if(cacheUrl.equals(url)){
                finish();
                return true;
            }
        }
        dataWv.goBack();
        return true;
    }
}
