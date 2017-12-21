package com.ecjia.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import im.delight.android.webview.AdvancedWebView;

public class WebViewActivity extends BaseActivity implements AdvancedWebView.Listener {

    private TextView title;
    private ImageView back;

    private ImageView web_back;
    private ImageView goForward;
    private ImageView reload;

    AdvancedWebView webView;
    private MyProgressDialog pd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        String url = intent.getStringExtra(IntentKeywords.WEB_URL);
        String webTitle = intent.getStringExtra(IntentKeywords.WEB_TITLE);
        pd = MyProgressDialog.createDialog(this);
        pd.setMessage(getResources().getString(R.string.loading));
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(getResources().getString(R.string.browser));
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != pd) {
                    pd.dismiss();
                }
                finish();
            }
        });

        final Activity activity = this;

        webView = (AdvancedWebView) findViewById(R.id.webview_webView);
        webView.setWebViewClient(new WebViewClient() { //通过webView打开链接，不调用系统浏览器

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("ecjiaopen://app?open_type")) {
                    ECJiaOpenType.getDefault().startAction(WebViewActivity.this, url);
                    if(!url.contains("goods_detail")) {
                        finish();
                    }
                    return true;
                }
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)) {
                    WebViewActivity.this.title.setText(title);
                }
            }

        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + " ECJiaBrowse/1.2.0");
        class JsObject {
            @JavascriptInterface
            public String toString() {
                return "ajaxSubmit";
            }

        }
        if (Build.VERSION.SDK_INT > 17) {
            webView.addJavascriptInterface(new JsObject(), "ajaxSubmit");
        } else {
            webView.loadUrl("javascript:ajaxSubmit()");
        }


        if (null != url) {
            webView.loadUrl(url);
        }

        web_back = (ImageView) findViewById(R.id.web_back);
        web_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });


        goForward = (ImageView) findViewById(R.id.goForward);
        goForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                webView.goForward();

            }
        });

        reload = (ImageView) findViewById(R.id.reload);
        reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                webView.reload();
            }
        });


    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != pd) {
                pd.dismiss();
            }
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
    }

    @Override
    public void onPageStarted(String s, Bitmap bitmap) {

    }

    @Override
    public void onPageFinished(String s) {

    }

    @Override
    public void onPageError(int i, String s, String s1) {

    }

    @Override
    public void onDownloadRequested(String s, String s1, String s2, String s3, long l) {

    }

    @Override
    public void onExternalPageRequest(String s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);

    }
}
