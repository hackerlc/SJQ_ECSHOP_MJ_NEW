package com.ecjia.view.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.SHOPDATA;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.ImageLoaderForLV;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
/**
 * 类名介绍：店铺详情
 * Created by sun
 * Created time 2017-02-13.
 */
public class ShopDetailActivity extends BaseActivity implements View.OnClickListener, HttpResponse {

    private TextView top_view_text;
    private TextView shopdetail_shop_name, shopdetail_shop_colloct, tv_allgoods, tv_newgoods, tv_best, tv_hot, tv_collect;
    private TextView tv_goodsscore, tv_logisticsscore, tv_servicescore, tv_company_phone, tv_company_name, tv_company_area, tv_company_notice;
    private ImageView top_view_back, shopdetail_shop_img, iv_collect;
    private LinearLayout ll_collect, ll_company_phone;
    private SellerModel sellerModel;
    private String isfollow;
    private String sellerid;
    private MyDialog mDialog;
    private SHOPDATA shopHomeData;
    private String uid;
    private SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail2);

        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        JSONObject jsonObject = null;
        shopHomeData = new SHOPDATA();
        try {
            jsonObject = new JSONObject(intent.getStringExtra("shopHomeData"));
            shopHomeData = SHOPDATA.fromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sellerid = shopHomeData.getId();

        shared = getSharedPreferences("userInfo", 0);

        initView();

        initData();

        if (null == sellerModel) {
            sellerModel = new SellerModel(this);
        }

        sellerModel.addResponseListener(this);
    }

    private void initView() {
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getString(R.string.shop_detail));

        shopdetail_shop_name = (TextView) findViewById(R.id.shopdetail_shop_name);
        shopdetail_shop_colloct = (TextView) findViewById(R.id.shopdetail_shop_colloct);
        tv_allgoods = (TextView) findViewById(R.id.tv_allgoods);
        tv_newgoods = (TextView) findViewById(R.id.tv_newgoods);
        tv_best = (TextView) findViewById(R.id.tv_promote);
        tv_hot = (TextView) findViewById(R.id.tv_news);
        tv_goodsscore = (TextView) findViewById(R.id.tv_goodsscore);
        tv_logisticsscore = (TextView) findViewById(R.id.tv_logisticsscore);
        tv_servicescore = (TextView) findViewById(R.id.tv_servicescore);
        tv_company_phone = (TextView) findViewById(R.id.tv_company_phone);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_company_area = (TextView) findViewById(R.id.tv_company_area);
        tv_company_notice = (TextView) findViewById(R.id.tv_company_notice);
        tv_collect = (TextView) findViewById(R.id.tv_collect);

        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        top_view_back = (ImageView) findViewById(R.id.top_view_back);
        top_view_back.setOnClickListener(this);
        shopdetail_shop_img = (ImageView) findViewById(R.id.shopdetail_shop_img);

        ll_collect = (LinearLayout) findViewById(R.id.ll_collect);
        ll_collect.setOnClickListener(this);
        ll_company_phone = (LinearLayout) findViewById(R.id.ll_company_phone);
        ll_company_phone.setOnClickListener(this);

    }

    private void initData() {
        isfollow = shopHomeData.getIs_follower();
        if ("0".equals(isfollow)) {
            ll_collect.setBackgroundResource(R.drawable.shape_shopuncollect);
            tv_collect.setTextColor(res.getColor(R.color.public_theme_color_normal));
            tv_collect.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            iv_collect.setVisibility(View.VISIBLE);
            tv_collect.setText(res.getString(R.string.shop_uncollected));
        } else {
            ll_collect.setBackgroundResource(R.drawable.shape_shopcollect);
            tv_collect.setTextColor(res.getColor(R.color.white));
            tv_collect.setGravity(Gravity.CENTER);
            iv_collect.setVisibility(View.GONE);
            tv_collect.setText(res.getString(R.string.shop_collected));
        }

        ImageLoaderForLV.getInstance(this).setImageRes(shopdetail_shop_img, shopHomeData.getSeller_logo());

        shopdetail_shop_name.setText(shopHomeData.getSeller_name());
        shopdetail_shop_colloct.setText(shopHomeData.getFollower() + res.getString(R.string.follower_num));


        if (TextUtils.isEmpty(shopHomeData.getGoods_count().getCount())){
            tv_allgoods.setText("0");
        } else {
            tv_allgoods.setText(shopHomeData.getGoods_count().getCount());
        }

        if (TextUtils.isEmpty(shopHomeData.getGoods_count().getNew_goods())){
            tv_newgoods.setText("0");
        } else {
            tv_newgoods.setText(shopHomeData.getGoods_count().getNew_goods());
        }

        if (TextUtils.isEmpty(shopHomeData.getGoods_count().getBest_goods())){
            tv_best.setText("0");
        } else {
            tv_best.setText(shopHomeData.getGoods_count().getBest_goods());
        }

        if (TextUtils.isEmpty(shopHomeData.getGoods_count().getHot_goods())){
            tv_hot.setText("0");
        } else {
            tv_hot.setText(shopHomeData.getGoods_count().getHot_goods());
        }

        if (TextUtils.isEmpty(shopHomeData.getComment().getComment_goods())){
            tv_goodsscore.setText("暂无评分");
        } else {
            tv_goodsscore.setText(shopHomeData.getComment().getComment_goods());
        }

        if (TextUtils.isEmpty(shopHomeData.getComment().getComment_delivery())){
            tv_logisticsscore.setText("暂无评分");
        } else {
            tv_logisticsscore.setText(shopHomeData.getComment().getComment_delivery());
        }

        if (TextUtils.isEmpty(shopHomeData.getComment().getComment_server())){
            tv_servicescore.setText("暂无评分");
        } else {
            tv_servicescore.setText(shopHomeData.getComment().getComment_server());
        }

        if (TextUtils.isEmpty(shopHomeData.getTelephone())){
            tv_company_phone.setText("暂无");
        } else {
            tv_company_phone.setText(shopHomeData.getTelephone());
        }

        if (TextUtils.isEmpty(shopHomeData.getShop_name())){
            tv_company_name.setText("暂无");
        } else {
            tv_company_name.setText(shopHomeData.getShop_name());
        }

        if (TextUtils.isEmpty(shopHomeData.getShop_address())){
            tv_company_area.setText("暂无");
        } else {
            tv_company_area.setText(shopHomeData.getShop_address());
        }

        if (TextUtils.isEmpty(shopHomeData.getSeller_description())){
            tv_company_notice.setText("暂无");
        } else {
            tv_company_notice.setText(shopHomeData.getSeller_description());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.ll_collect:
                uid = shared.getString("uid", "");
                if ("".equals(uid)) {
                    Intent intent = new Intent(ShopDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    if ("0".equals(isfollow)) {
                        sellerModel.sellerCollectCreate(sellerid);
                        EventBus.getDefault().post(new MyEvent("add_collect_seller", sellerid));
                    } else {
                        sellerModel.sellerCollectDelete(sellerid);
                        EventBus.getDefault().post(new MyEvent("minus_collect_seller", sellerid));
                    }
                }
                break;
            case R.id.ll_company_phone:
                final String number = tv_company_phone.getText().toString();
                String call = res.getString(R.string.setting_call_or_not);
                if (!TextUtils.isEmpty(number)) {
                    mDialog = new MyDialog(ShopDetailActivity.this, call, number);
                    mDialog.show();
                    mDialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                            startActivity(intent);
                        }
                    });
                    mDialog.negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDialog.dismiss();
                        }
                    });
                }
                break;
        }

    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.SELLER_COLLECTCREATE)) {
            if (status.getSucceed() == 1) {
                ll_collect.setBackgroundResource(R.drawable.shape_shopcollect);
                tv_collect.setTextColor(res.getColor(R.color.white));
                tv_collect.setGravity(Gravity.CENTER);
                iv_collect.setVisibility(View.GONE);
                tv_collect.setText(res.getString(R.string.shop_collected));
                isfollow = "1";
                shopHomeData.setFollower(shopHomeData.getFollower() + 1);
                shopdetail_shop_colloct.setText(shopHomeData.getFollower() + res.getString(R.string.follower_num));
                EventBus.getDefault().post(new MyEvent("collectrefresh"));
                ToastView toastView = new ToastView(this, getResources().getString(R.string.collection_success));
                toastView.show();
            }
        } else if (url.equals(ProtocolConst.SELLER_COLLECTDELETE)) {
            if (status.getSucceed() == 1) {
                ToastView toastView = new ToastView(this, getResources().getString(R.string.del_collection_success));
                toastView.show();
                ll_collect.setBackgroundResource(R.drawable.shape_shopuncollect);
                tv_collect.setTextColor(res.getColor(R.color.public_theme_color_normal));
                tv_collect.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                iv_collect.setVisibility(View.VISIBLE);
                tv_collect.setText(res.getString(R.string.shop_uncollected));
                isfollow = "0";
                shopHomeData.setFollower(shopHomeData.getFollower() - 1);
                shopdetail_shop_colloct.setText(shopHomeData.getFollower() + res.getString(R.string.follower_num));
                EventBus.getDefault().post(new MyEvent("collectrefresh"));
            }
        }
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Event event) {

    }
}

