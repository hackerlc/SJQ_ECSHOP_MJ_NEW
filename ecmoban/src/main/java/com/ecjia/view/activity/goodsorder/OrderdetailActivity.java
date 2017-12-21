package com.ecjia.view.activity.goodsorder;

import java.util.ArrayList;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.FormatUtil;
import com.ecjia.view.activity.ChoosePayActivity;
import com.ecjia.view.activity.ConsultActivity;
import com.ecjia.view.activity.LogisticsActivity;
import com.ecjia.view.activity.ShopGoodsActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.ActionSheetDialog;
import com.ecjia.widgets.HorizontalListView;
import com.ecjia.view.adapter.OrderGoodsHoriLVAdapter;
import com.ecjia.entity.responsemodel.GOODS_LIST;
import com.ecjia.entity.responsemodel.ORDER_GOODS_LIST;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.view.adapter.OrderdetailActivityAdapter;
import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.network.netmodle.OrderModel;
import com.ecjia.consts.ProtocolConst;

import com.ecjia.widgets.ToastView;
import com.ecjia.consts.OrderType;
import com.umeng.message.PushAgent;

import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
/*
d订单详情
 */
public class OrderdetailActivity extends BaseActivity implements OnClickListener {
    private ImageView back;
    private TextView title, order_sn, pay_status, order_createtime, order_cost, order_remove, order_pay;
    private TextView order_goods_totalcost, order_traffic_cost;
    private TextView username, phone, address, paytype;
    private OrderModel orderModel;
    private ConfigModel configmodel;
    private ListView order_goods;
    private LinearLayout order_payitem;
    public String flag;
    private OrderdetailActivityAdapter adapter;
    private ArrayList<ORDER_GOODS_LIST> goodslist;
    private ArrayList<GOODS_LIST> serilist;
    private String order_id;
    Resources resource;

    public boolean orderupdate = false;
    public Handler Intenthandler;
    ActionSheetDialog dialog;

    private ScrollView contextitem;
    private TextView tv_hongbao, tv_jifen, tv_youhui, tv_shippingtime, tv_postscript, tv_order_goods_cost,tv_usercoupon;
    private FrameLayout buttom_item;
    private LinearLayout shippingtime_item;
    private TextView order_checkshipinfo;
    private HorizontalListView goodshorilistview;
    private LinearLayout ll_goodslist;
    private OrderGoodsHoriLVAdapter horiadapter;
    private LinearLayout consult;
    private TextView texAmount;
    private TextView order_createcomment;
    private int is_commentedNum;
    private LinearLayout ly_shipping,ly_cac;
    private TextView order_cac_username,order_cac_user_phone,order_cac_user_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        PushAgent.getInstance(this).onAppStart();

        EventBus.getDefault().register(this);
        resource = (Resources) getBaseContext().getResources();
        Intent intent = getIntent();
        flag = intent.getStringExtra(IntentKeywords.ORDER_TYPE);
        if (TextUtils.isEmpty(flag)) {
            flag = OrderType.AWAIT_PAY;
        }
        order_id = intent.getStringExtra(IntentKeywords.ORDER_ID);
        goodslist = new ArrayList<ORDER_GOODS_LIST>();
        serilist = new ArrayList<GOODS_LIST>();
        configmodel = ConfigModel.getInstance();
        orderModel = new OrderModel(this);
        orderModel.addResponseListener(this);
        orderModel.getOrderDetail(order_id + "");
    }

    // 初始化设置
    public void setinfo() {
        flag=orderModel.orderdetail.getStatus_code();
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(resource.getString(R.string.order_detail));
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        order_sn = (TextView) findViewById(R.id.order_item_sno);// 订单编号
        pay_status = (TextView) findViewById(R.id.order_paystatus);// 订单状态
        order_createtime = (TextView) findViewById(R.id.order_createtime);// 创建时间
        order_cost = (TextView) findViewById(R.id.order_cost);// 订单金额
        order_payitem = (LinearLayout) findViewById(R.id.order_payitem);// 付款小布局
        order_remove = (TextView) findViewById(R.id.order_remove);// 取消订单
        order_remove.setOnClickListener(this);
        order_pay = (TextView) findViewById(R.id.order_pay);// 立即支付
        order_pay.setOnClickListener(this);
        order_createcomment = (TextView) findViewById(R.id.order_createcomment);//商品评论
        order_createcomment.setOnClickListener(this);
        order_checkshipinfo = (TextView) findViewById(R.id.order_checkshipinfo);// 立即支付
        order_checkshipinfo.setOnClickListener(this);
        order_goods = (ListView) findViewById(R.id.order_goods);// 订单中的商品
        tv_order_goods_cost = (TextView) findViewById(R.id.order_goods_cost);//商品总价格
        tv_order_goods_cost.setText(orderModel.orderdetail.getGoods_total_fee());
        consult = (LinearLayout) findViewById(R.id.order_consultation);//listview底部的咨询
        consult.setOnClickListener(this);
        texAmount = (TextView) findViewById(R.id.tv_fapiao);//税费--已经付金额
        goodshorilistview = (HorizontalListView) findViewById(R.id.balance_horilistview);
        ll_goodslist = (LinearLayout) findViewById(R.id.ll_goodslist);
        goodshorilistview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                serilist.clear();
                for (int i = 0; i < goodslist.size(); i++) {
                    GOODS_LIST goods_list = new GOODS_LIST();
                    goods_list.setGoods_id(Integer.valueOf(goodslist.get(i).getGoods_id()));
                    goods_list.setGoods_name(goodslist.get(i).getName());
                    goods_list.setImg(goodslist.get(i).getImg());
                    goods_list.setGoods_price(goodslist.get(i).getFormated_shop_price());
                    goods_list.setGoods_number(Integer.valueOf(goodslist.get(i).getGoods_number()));
                    serilist.add(goods_list);
                }
                Intent intent = new Intent(OrderdetailActivity.this, ShopGoodsActivity.class);
                intent.putExtra("datalist", serilist);
                intent.putExtra("is_order", true);
                startActivity(intent);
            }
        });
        order_goods_totalcost = (TextView) findViewById(R.id.order_goods_totalcost);// 总金额
        order_traffic_cost = (TextView) findViewById(R.id.order_traffic_cost);// 运费
        // 红包积分折扣
        tv_hongbao = (TextView) findViewById(R.id.tv_hongbao);
        tv_jifen = (TextView) findViewById(R.id.tv_jifen);
        tv_youhui = (TextView) findViewById(R.id.tv_youhui);
        tv_usercoupon= (TextView) findViewById(R.id.tv_usercoupon);
        tv_shippingtime = (TextView) findViewById(R.id.tv_shippingtime);
        tv_postscript = (TextView) findViewById(R.id.tv_postscript);
        buttom_item = (FrameLayout) findViewById(R.id.buttom_item);
        contextitem = (ScrollView) findViewById(R.id.context_item);
        shippingtime_item = (LinearLayout) findViewById(R.id.shippingtime_item);

        username = (TextView) findViewById(R.id.order_username);// 收货人
        phone = (TextView) findViewById(R.id.order_user_phone);
        address = (TextView) findViewById(R.id.order_user_address);
        paytype = (TextView) findViewById(R.id.order_paytype);// 支付类型
        ly_shipping = (LinearLayout) findViewById(R.id.ly_shipping);

        ly_cac = (LinearLayout) findViewById(R.id.ly_cac);// 自提
        order_cac_username = (TextView) findViewById(R.id.order_cac_username);
        order_cac_user_phone = (TextView) findViewById(R.id.order_cac_user_phone);
        order_cac_user_address = (TextView) findViewById(R.id.order_cac_user_address);

        if("1".equals(orderModel.orderdetail.getIs_cac())){
            ly_shipping.setVisibility(View.GONE);
            ly_cac.setVisibility(View.VISIBLE);
            order_cac_username.setText(orderModel.orderdetail.getCac_user_name()+"");
            order_cac_user_phone.setText(orderModel.orderdetail.getCac_mobile()+"");
            order_cac_user_address.setText(orderModel.orderdetail.getCac_address()+"");
        }else{
            ly_shipping.setVisibility(View.VISIBLE);
            ly_cac.setVisibility(View.GONE);
            username.setText(orderModel.orderdetail.getConsignee());
            phone.setText(orderModel.orderdetail.getMobile());
            address.setText(orderModel.orderdetail.getProvince() + "  " + orderModel.orderdetail.getCity() + "  " + orderModel.orderdetail.getAddress());
        }
        tv_hongbao.setText(orderModel.orderdetail.getFormated_bonus());
        tv_jifen.setText(orderModel.orderdetail.getFormated_integral_money());
        tv_youhui.setText(orderModel.orderdetail.getFormated_discount());
        tv_usercoupon.setText(orderModel.orderdetail.getFormated_coupons());
        if (TextUtils.isEmpty(orderModel.orderdetail.getShipping_time())) {
            tv_shippingtime.setText("无");
//            shippingtime_item.setVisibility(View.GONE);
        } else {
            tv_shippingtime.setText(orderModel.orderdetail.getShipping_time());
        }
        if (!TextUtils.isEmpty(orderModel.orderdetail.getPostscript())) {
            tv_postscript.setText(orderModel.orderdetail.getPostscript());
        } else {
            tv_postscript.setText("无");
        }
        texAmount.setText(orderModel.orderdetail.getFormated_has_pay());
        order_sn.setText(orderModel.orderdetail.getOrder_sn());
        order_createtime.setText(orderModel.orderdetail.getOrder_time());


        /**
         *  取消 空白 查看物流 评价 结算（确认收货）
         *
         *  1，取消 隐藏 隐藏 不显示 计算
         *  2，不显示 隐藏 隐藏 隐藏 隐藏
         *  3，不显示 隐藏 查看物流 隐藏 收货
         *  4
         *  4.1 不显示 隐藏 查看物流 评价 隐藏
         *  4.2 不显示 不显示 查看物流 隐藏 隐藏
         */
        if (flag.equals(OrderType.AWAIT_PAY)) {// 判断进入那个界面
            pay_status.setText(resource.getString(R.string.order_await_pay));
            order_remove.setVisibility(View.VISIBLE);
            findViewById(R.id.order_empty).setVisibility(View.GONE);
            order_checkshipinfo.setVisibility(View.INVISIBLE);
            order_createcomment.setVisibility(View.GONE);
            order_pay.setVisibility(View.VISIBLE);
            order_payitem.setVisibility(View.VISIBLE);
            buttom_item.setVisibility(View.VISIBLE);

        } else if (flag.equals(OrderType.AWAIT_SHIP)) {
            pay_status.setText(resource.getString(R.string.order_await_ship));
            order_remove.setVisibility(View.INVISIBLE);
            order_createcomment.setVisibility(View.GONE);
            order_checkshipinfo.setVisibility(View.GONE);
            order_pay.setVisibility(View.GONE);
            order_payitem.setVisibility(View.GONE);
            buttom_item.setVisibility(View.GONE);
        } else if (flag.equals(OrderType.SHIPPED)) {
            pay_status.setText(resource.getString(R.string.order_shipped));
            order_remove.setVisibility(View.INVISIBLE);
            findViewById(R.id.order_empty).setVisibility(View.GONE);
            if (!TextUtils.isEmpty(orderModel.shipping_number) && !TextUtils.isEmpty(orderModel.shippingname)) {
                order_checkshipinfo.setVisibility(View.VISIBLE);
                order_checkshipinfo.setBackgroundResource(R.drawable.shape_white_stroke_corner_unpress);
            } else {
                order_checkshipinfo.setVisibility(View.VISIBLE);
            }
            order_createcomment.setVisibility(View.GONE);
            order_pay.setText(resource.getString(R.string.tradeitem_receive));
            order_pay.setVisibility(View.VISIBLE);
            order_payitem.setVisibility(View.VISIBLE);
            buttom_item.setVisibility(View.VISIBLE);

        } else if (flag.equals(OrderType.FINISHED)) {
            pay_status.setText(resource.getString(R.string.order_history));
            order_remove.setVisibility(View.INVISIBLE);
            order_createcomment.setVisibility(View.INVISIBLE);
            if (orderModel.orderdetail.getIs_commented()==0 ||
                    (orderModel.orderdetail.getIs_commented()==1&&orderModel.orderdetail.getIs_showorder()==0)) {//是否评价
                findViewById(R.id.order_empty).setVisibility(View.GONE);
                order_checkshipinfo.setVisibility(View.VISIBLE);
                order_createcomment.setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.order_empty).setVisibility(View.INVISIBLE);
                order_checkshipinfo.setVisibility(View.VISIBLE);
                order_createcomment.setVisibility(View.GONE);
            }
            order_pay.setVisibility(View.GONE);
            order_payitem.setVisibility(View.VISIBLE);
            buttom_item.setVisibility(View.VISIBLE);

        } else if (flag.equals(OrderType.ALLOW_COMMENT)) {
            pay_status.setText(resource.getString(R.string.order_await_comment));
            order_remove.setVisibility(View.INVISIBLE);
            order_createcomment.setVisibility(View.INVISIBLE);
            findViewById(R.id.order_empty).setVisibility(View.GONE);
            order_checkshipinfo.setVisibility(View.VISIBLE);
            order_createcomment.setVisibility(View.VISIBLE);
            order_pay.setVisibility(View.GONE);
            order_payitem.setVisibility(View.VISIBLE);
            buttom_item.setVisibility(View.VISIBLE);
        } else if (flag.equals(OrderType.CANCEL)) {
            buttom_item.setVisibility(View.GONE);
        } else if(flag.equals(OrderType.AWAIT_TUANPI)){
            pay_status.setText(resource.getString(R.string.order_await_group));
            buttom_item.setVisibility(View.GONE);
        }

        float order_amount = FormatUtil.formatStrToFloat(orderModel.orderdetail.getOrder_amount());
        float surplus = FormatUtil.formatStrToFloat(orderModel.orderdetail.getSurplus());
        float money_paid = FormatUtil.formatStrToFloat(orderModel.orderdetail.getMoney_paid());

        float total_cost = order_amount + surplus + money_paid;
        if (total_cost < 0) {
            total_cost = 0;
        }
        order_price = FormatUtil.formatToPrice(FormatUtil.formatFloatTwoDecimal(total_cost));

        order_cost.setText(orderModel.orderdetail.getFormated_total_fee());
        order_traffic_cost.setText(orderModel.orderdetail.getFormated_shipping_fee());


//        order_goods_totalcost.setText(order_price);
//        if("group_buy".equals(orderModel.orderdetail.getExtension_code())){
//            order_goods_totalcost.setText(orderModel.orderdetail.getTotal_fee());
//        }else{
//            order_goods_totalcost.setText(order_price);
//        }
        order_goods_totalcost.setText(FormatUtil.formatToPrice(FormatUtil.formatFloatTwoDecimal(order_amount)));
        paytype.setText("(" + orderModel.orderdetail.getPay_name() + ")");

        goodslist = orderModel.goods_list;

//        if (goodslist.size() == 1) {
            adapter = new OrderdetailActivityAdapter(this, goodslist, flag,orderModel.orderdetail.getFormated_shipping_fee());
            order_goods.setAdapter(adapter);
            setListViewHeightBasedOnChildren(order_goods);
            ll_goodslist.setVisibility(View.GONE);
//        } else if (goodslist.size() > 1) {
//            horiadapter = new OrderGoodsHoriLVAdapter(OrderdetailActivity.this, goodslist);
//            goodshorilistview.setAdapter(horiadapter);
//            order_goods.setAdapter(null);
//            ll_goodslist.setVisibility(View.VISIBLE);
//        }


    }

    String order_price = "";

    @Override
    public void onClick(View v) {
        Resources resources = getBaseContext().getResources();
        String cancel = resources.getString(R.string.order_cancel);
        String cancel_sure = resources.getString(R.string.order_cancel_sure);
        switch (v.getId()) {
            case R.id.order_remove:
                dialog = new ActionSheetDialog(OrderdetailActivity.this);
                dialog.builder().setTitle(cancel_sure).setCancelable(false).setCanceledOnTouchOutside(true).addSheetItem(resources.getString(R.string.dialog_ensure), ActionSheetDialog
                        .SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        dialog.dismiss();
                        orderModel.orderCancle(order_id + "");// 取消订单
                    }
                }).show();
                break;
            case R.id.order_pay://立即支付
                if (flag.equals(OrderType.AWAIT_PAY)) {
                    Intent intent = new Intent(OrderdetailActivity.this, ChoosePayActivity.class);
                    intent.putExtra(IntentKeywords.PAY_TYPE, IntentKeywords.ORDER_ID);
                    intent.putExtra(IntentKeywords.PAY_ID, orderModel.orderdetail.getPay_id());
                    intent.putExtra(IntentKeywords.ORDER_ID, order_id + "");
                    intent.putExtra(IntentKeywords.PAY_IS_CREATE, false);
                    intent.putExtra(IntentKeywords.PAY_AMOUNT, orderModel.orderdetail.getFormated_order_amount() + "");
                    intent.putExtra(IntentKeywords.PAY_BODY, getBody());
                    startActivity(intent);
                } else if (flag.equals(OrderType.SHIPPED)) {
                    orderModel.affirmReceived(order_id + "");// 确认收货
                }
                break;
            case R.id.order_createcomment://商品评论
                Intent commentIntent = new Intent(this, OrderDetailCommentListActivity.class);//商品评价
                commentIntent.putExtra("order_id", orderModel.orderdetail.getOrder_id());
                startActivityForResult(commentIntent, 2);
                break;

            case R.id.order_checkshipinfo://物流信息
                Intent intent2 = new Intent(OrderdetailActivity.this, LogisticsActivity.class);
                intent2.putExtra("order_status", pay_status.getText().toString());
                intent2.putExtra("shippingname", orderModel.shippingname);
                intent2.putExtra("shipping_number", orderModel.shipping_number);
                intent2.putExtra("order_id", order_id + "");
                startActivity(intent2);
                break;

            case R.id.order_consultation://订单咨询
                Intent intent = new Intent(this, ConsultActivity.class);
                intent.putExtra("type", "order_consult");
                intent.putExtra("order_id", orderModel.orderdetail.getOrder_id());//传一个订单号
                intent.putExtra("order_sn", orderModel.orderdetail.getOrder_sn());
                intent.putExtra("order_price", order_price);
                intent.putExtra("order_time", orderModel.orderdetail.getOrder_time());
                if (orderModel.goods_list.get(0).getImg() != null && orderModel.goods_list.get(0).getImg().getThumb() != null) {
                    intent.putExtra("order_goodsImg", orderModel.goods_list.get(0).getImg().getThumb());

                }
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private String getBody() {
        final String order_incloud = resource.getString(R.string.balance_order_incloud);
        final String deng = resource.getString(R.string.balance_deng);
        final String zhong_goods = resource.getString(R.string.balance_zhong_goods);
        String body = order_incloud + orderModel.goods_list.get(0).getName() + deng + orderModel.goods_list.size() + zhong_goods;
        return body;
    }


    // 设置listview让其完全显示
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderupdate) {
            orderModel.getOrderDetail(order_id + "");
            orderupdate = false;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

        if (event.getMsg().equals(EventKeywords.UPDATE_ORDER)) {
            orderupdate = true;
        }
        LG.i("运行========");
        //判断评论
        if (event.getMsg().equals("comment_succeed")) {
            orderupdate = true;
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url.equals(ProtocolConst.ORDER_DETAIL)) {
            if (status.getSucceed() == 1) {
                setinfo();
            }
        } else if (url == ProtocolConst.AFFIRMRECEIVED) {
            if (status.getSucceed() == 1) {
                ToastView toast = new ToastView(OrderdetailActivity.this, R.string.tradeitem_receive);
                toast.setDuration(3000);
                toast.show();
                OrderdetailActivity.this.finish();
            }
        } else if (url == ProtocolConst.ORDER_CANCLE) {
            if (status.getSucceed() == 1) {
                Resources resources = getBaseContext().getResources();
                String canceled = resources.getString(R.string.order_canceled);
                Intent intent = new Intent(OrderdetailActivity.this, OrderListActivity.class);
                intent.putExtra(IntentKeywords.ORDER_TYPE, OrderType.AWAIT_PAY);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ToastView toast = new ToastView(OrderdetailActivity.this, canceled);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                OrderdetailActivity.this.finish();
            }
        }
    }
}
