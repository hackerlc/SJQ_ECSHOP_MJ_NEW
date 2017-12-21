package com.ecjia.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.netmodle.HelpModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 团批帮助页面
 */
public class GroupHelpWebActivity extends BaseActivity implements HttpResponse {

    private HelpModel helpModel;
    private WebView webView;
    private TextView tv_confirm;

    private String titleStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_group_helpweb);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        titleStr = intent.getStringExtra("title");
        Window lp = getWindow();
        lp.setGravity(Gravity.BOTTOM);
        lp.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        tv_confirm= (TextView) findViewById(R.id.tv_confirm);
        webView = (WebView) findViewById(R.id.help_web);
        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        tv_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        webView.setInitialScale(25);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        helpModel = new HelpModel(this);
        helpModel.addResponseListener(this);
        helpModel.helpArticle(id);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
//            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        }
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();


    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.SHOP_HELP_DETAIL) {
            if (status.getSucceed() == 1) {
                webView.loadDataWithBaseURL(null, helpModel.web, "text/html", "utf-8", null);
            }
        }
    }

//    @Override
//    public void initTopView() {
//        super.initTopView();
//        topView = (ECJiaTopView) findViewById(R.id.goodsdesc_topview);
//        topView.setTitleText(titleStr);
//        topView.setLeftBackImage(R.drawable.login_back, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//            }
//        });
//    }
}
