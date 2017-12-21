package com.ecjia.view.activity;


import android.content.res.Resources;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.GoodsDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsDescActivity extends BaseActivity {


    private TextView title;
    private ImageView back;
    private GoodsDetailModel goodsModel;
    private WebView webView;
    public Handler Intenthandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpweb);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        title = (TextView) findViewById(R.id.top_view_text);
        Resources resource = (Resources) getBaseContext().getResources();
        String det = resource.getString(R.string.gooddetail_product);
        title.setText(det);

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView = (WebView) findViewById(R.id.help_web);

        goodsModel = new GoodsDetailModel(this);
        goodsModel.goodsDesc(id + "");

        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setInitialScale(25);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url.equals(ProtocolConst.GOODSDESC)) {
            if (status.getSucceed() == 1) {
                webView.loadDataWithBaseURL(null, goodsModel.goodsWeb, "text/html", "utf-8", null);
            }
        }
    }
}
