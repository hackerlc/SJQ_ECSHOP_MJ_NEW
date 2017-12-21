package com.ecjia.view.activity.goodsorder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.OrderType;
import com.ecjia.entity.intentmodel.OrderGoodInrtent;
import com.ecjia.util.common.JsonUtil;
import com.ecmoban.android.sijiqing.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类名介绍：申请售后主页面
 * Created by sun
 * Created time 2017-03-06.
 */

public class ApplyBaseCustomerServiceActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.ly_return_goods_and_money)
    LinearLayout ly_return_goods_and_money;//退货退款
    @BindView(R.id.ly_return_money)
    LinearLayout ly_return_money;//退款
    @BindView(R.id.ly_return_goods)
    LinearLayout ly_return_goods;//换货

    private Bundle bundle;
    private String orderTypeFlag;
    private String goodRecID;

    private OrderGoodInrtent orderGoodInrtent;


    @Override
    public void init(Bundle savedInstanceState) {
        bundle=getIntent().getExtras();
        goodRecID=bundle.getString(IntentKeywords.REC_ID);//订单retid
        orderTypeFlag=bundle.getString(IntentKeywords.ORDER_TYPE);//订单当前的状态
        orderGoodInrtent= JsonUtil.getObj(bundle.getString(IntentKeywords.ORDER_GOODINRTENT),OrderGoodInrtent.class);
        if(TextUtils.isEmpty(goodRecID)||TextUtils.isEmpty(orderTypeFlag)){
            finish();
        }
        if(orderTypeFlag.equals(OrderType.AWAIT_SHIP)){//代发货
            textView_title.setText("退款");
            ly_return_goods.setVisibility(View.GONE);
            ly_return_goods_and_money.setVisibility(View.GONE);
        }else if(orderTypeFlag.equals(OrderType.SHIPPED)){ //待收货
            ly_return_goods.setVisibility(View.VISIBLE);
            textView_title.setText("申请售后");
        }else if(orderTypeFlag.equals(OrderType.FINISHED)||orderTypeFlag.equals(OrderType.ALLOW_COMMENT)){ //已完成  //待评价
            textView_title.setText("申请售后");
            ly_return_goods_and_money.setVisibility(View.GONE);
            ly_return_money.setVisibility(View.GONE);
        }else{
            finish();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_customerservice;
    }

    @OnClick({R.id.imageView_back,R.id.ly_return_goods_and_money,R.id.ly_return_money,R.id.ly_return_goods})
    public void onClick(View view) {
        Intent intent=new Intent(mActivity,ApplySubmitCauseActivity.class);
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.ly_return_goods_and_money://退换货类型  0仅退款   1退货    2换货)
                intent.putExtra(IntentKeywords.REC_ID,goodRecID);
                intent.putExtra(IntentKeywords.RETURN_TYPE,"1");
                intent.putExtra(IntentKeywords.ORDER_TYPE,orderTypeFlag);
                orderGoodInrtent.setReturn_type("1");
                intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                startActivity(intent);
                finish();
                break;
            case R.id.ly_return_money:
                intent.putExtra(IntentKeywords.REC_ID,goodRecID);
                intent.putExtra(IntentKeywords.RETURN_TYPE,"0");
                intent.putExtra(IntentKeywords.ORDER_TYPE,orderTypeFlag);
                orderGoodInrtent.setReturn_type("0");
                intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                startActivity(intent);
                finish();
                break;
            case R.id.ly_return_goods:
                intent.putExtra(IntentKeywords.REC_ID,goodRecID);
                intent.putExtra(IntentKeywords.RETURN_TYPE,"2");
                intent.putExtra(IntentKeywords.ORDER_TYPE,orderTypeFlag);
                orderGoodInrtent.setReturn_type("2");
                intent.putExtra(IntentKeywords.ORDER_GOODINRTENT, JsonUtil.toString(orderGoodInrtent));
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
