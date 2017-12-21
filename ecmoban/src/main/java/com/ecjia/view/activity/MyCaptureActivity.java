package com.ecjia.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.SWEEP_HISTORY;
import com.ecjia.util.LG;
import com.ecjia.view.adapter.SweepSql;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.google.zxing.Result;
import com.umeng.message.PushAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.itguy.zxingportrait.CaptureActivity;

public class MyCaptureActivity extends CaptureActivity {
    MyDialog dialog;
    private final String TOGOODDETAIL = AndroidManager.OFFOCIALWEB + "/goods.php?id=";
    private final String TOGOODCATEGORY = AndroidManager.OFFOCIALWEB + "/category.php?id=";
    private SweepSql sweepSql;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        capture_topview.setBackgroundColor(getResources().getColor(R.color.public_theme_color_normal));
        LG.i("======");
        right_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCaptureActivity.this, SweepRecordActivity.class);
                startActivity(intent);
            }
        });
        sweepSql = SweepSql.getIntence(this);
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        // 震动
        beepManager.playBeepSoundAndVibrate();
        final String getresult = rawResult.getText();//扫描的字符串
        String resultstring = getresult.toLowerCase();//转小写
        checkUrl(getresult);

    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        super.finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            this.finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    public void checkUrl(String checkurl, String url) {

        SWEEP_HISTORY sweep_history = new SWEEP_HISTORY();
        sweep_history.setSweep_content(url);
        if (checkurl.indexOf(TOGOODDETAIL) == 0) {//商品
            sweep_history.setSweep_title("商品详情");
            gotoGoodsDetail(url);
        } else if (checkurl.indexOf(TOGOODCATEGORY) == 0) {//列表
            sweep_history.setSweep_title("商品列表");
            gotoGoodList(url);
        } else {
            sweep_history.setSweep_title("链接");
            gotoWebView(url);
        }
        sweepSql.insertSweepHistory(sweep_history);
    }

    // 店铺 http://www.sjqxa.cn/mobile/index.php?r=store/index/shop_info&id=48850
    String pattCheckShop = ".*r=store/index/shop_info&id=.*";
    // 商品 http://test.sjq.cn/mobile/index.php?r=goods&id=6394
    String pattCheckGoods = ".*r=goods&id=.*";
    private void checkUrl(String url){
        SWEEP_HISTORY sweep_history = new SWEEP_HISTORY();
        sweep_history.setSweep_content(url);
        if(Pattern.matches(pattCheckShop,url)){
            sweep_history.setSweep_title("商铺详情");
            Pattern p = Pattern.compile("[^\\d]+([\\d]+).*");
            Matcher m = p.matcher(url);
            boolean result = m.find();
            String find_result = "";
            if (result) {
                find_result = m.group(1);
            }
            gotoShopDetail(find_result);
        } else if(Pattern.matches(pattCheckGoods,url)) {
            sweep_history.setSweep_title("商品详情");
            Pattern p = Pattern.compile("[^\\d]+([\\d]+).*");
            Matcher m = p.matcher(url);
            boolean result = m.find();
            String find_result = "";
            if (result) {
                find_result = m.group(1);
            }
            gotoGoodsDetail(find_result);
        } else {
            sweep_history.setSweep_title("链接");
            gotoWebView(url);
        }
        sweepSql.insertSweepHistory(sweep_history);
    }

    public void gotoGoodsDetail(String id) {
        Intent intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra(IntentKeywords.GOODS_ID, id);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void gotoShopDetail(String id) {
        Intent intent = new Intent(this, ShopListActivity.class);
        intent.putExtra(IntentKeywords.MERCHANT_ID, id);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    public void gotoGoodList(String url) {
        Intent intent = new Intent(this, GoodsListActivity.class);
        intent.putExtra("filter", url.replace(TOGOODCATEGORY, ""));
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void gotoWebView(final String url) {
        Intent intent = new Intent(MyCaptureActivity.this, com.ecjia.view.activity.web.WebViewActivity.class);
        intent.putExtra(IntentKeywords.WEB_URL, url);
        MyCaptureActivity.this.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
