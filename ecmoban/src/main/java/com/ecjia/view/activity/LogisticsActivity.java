package com.ecjia.view.activity;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.OrderModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.view.adapter.ExpressAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

/*
物流信息页面
 */
public class LogisticsActivity extends BaseActivity implements HttpResponse {

    private TextView company_name, status, number;
    private WebView webView;
    private String order_id;
    private OrderModel model;
    private ListView listView;
    private View headview;
    private LinearLayout view, ll_logistic_top;
    private ExpressAdapter adapter;

    private View null_pager;

    private String  shipping_name;//快递公司名称
    private String  invoice_no;//快递单号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics);
        PushAgent.getInstance(this).onAppStart();
        initView();
        model = new OrderModel(this);
        model.addResponseListener(this);
        shipping_name=getIntent().getStringExtra("shippingname");
        invoice_no=getIntent().getStringExtra("shipping_number");

        model.getExpressInfo(shipping_name,invoice_no);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setJsoninfo() {
        if (model.kuaidilist.size() == 0 || model.kuaidilist == null) {
            view.setVisibility(View.GONE);
        }
//        listView.addHeaderView(headview);
        listView.setVisibility(View.VISIBLE);
        adapter = new ExpressAdapter(this, model.express_list);
        listView.setAdapter(adapter);
    }

    private void initView() {
        Resources res = getResources();

        initTopView();

        ll_logistic_top = (LinearLayout) findViewById(R.id.ll_logistic_top);
        view = (LinearLayout) findViewById(R.id.head_below);
        status = (TextView) findViewById(R.id.logistics_status);
        company_name = (TextView) findViewById(R.id.company_name);
        number = (TextView) findViewById(R.id.log_no);

        null_pager = findViewById(R.id.null_pager);


        listView = (ListView) findViewById(R.id.log_list);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("order_status"))) {
            status.setText(getIntent().getStringExtra("order_status"));
        }
        if (!TextUtils.isEmpty(shipping_name)) {
            company_name.setText(shipping_name);
        }
        if (!TextUtils.isEmpty(invoice_no)) {
            number.setText(invoice_no);
        }
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.logistics_topview);
        topView.setTitleText(R.string.logistics_info);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS sstatus) throws JSONException {
        if (url == ProtocolConst.ORDER_EXPRESS) {
            if (sstatus.getSucceed() == 1) {
                ll_logistic_top.setVisibility(View.VISIBLE);

                setJsoninfo();

                if (!TextUtils.isEmpty(model.status)) {
                    status.setText(model.status);
                }
                if (!TextUtils.isEmpty(model.shippingname)) {
                    company_name.setText(model.shippingname);
                }
                if (!TextUtils.isEmpty(model.shipping_number)) {
                    number.setText(model.shipping_number);
                }

                if (model.kuaidilist.size() > 0) {
                    null_pager.setVisibility(View.VISIBLE);
                } else {
                    null_pager.setVisibility(View.GONE);
                }


            } else {
                null_pager.setVisibility(View.VISIBLE);
                ll_logistic_top.setVisibility(View.GONE);
            }
        }
    }
}