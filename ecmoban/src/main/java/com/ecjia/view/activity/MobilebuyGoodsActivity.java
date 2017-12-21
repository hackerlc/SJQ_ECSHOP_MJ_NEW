package com.ecjia.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.MobileGoodModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.MobileGoodListAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 手机专享页面
 * Created by Administrator on 2015/8/18.
 */
public class MobilebuyGoodsActivity extends BaseActivity implements HttpResponse, XListView.IXListViewListener {
    private ImageView back;
    private TextView title;
    private XListView listView;
    private MobileGoodListAdapter adapter;
    private MobileGoodModel model;
    private ErrorView nullpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobilegoods);
        initView();
    }

    //初始化布局
    private void initView() {
        PushAgent.getInstance(this).onAppStart();
        model = new MobileGoodModel(this);
        back = (ImageView) findViewById(R.id.top_view_back);
        title = (TextView) findViewById(R.id.top_view_text);
        listView = (XListView) findViewById(R.id.mobile_listview);
        nullpage = (ErrorView) findViewById(R.id.mobile_null_pager);
        title.setText(res.getString(R.string.mobile_buy));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this, 1);
        model.addResponseListener(this);
        model.getMobileGoods(true);
        adapter = new MobileGoodListAdapter(this, model.mobilegoodsArrayList);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.GOODS_MOBILEBUY) {
            if (status.getSucceed() == 1) {
                listView.setRefreshTime();
                listView.stopRefresh();
                listView.stopLoadMore();
                PAGINATED paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                if (paginated.getMore() == 1) {
                    listView.setPullLoadEnable(true);
                } else {
                    listView.setPullLoadEnable(false);
                }
                setGoodInfo();

            } else {
                listView.setVisibility(View.GONE);
                nullpage.setVisibility(View.VISIBLE);
                ToastView toastView=new ToastView(this,getResources().getString(R.string.payment_network_problem));
                toastView.show();
            }
        }
    }
    //设置列表信息
    private void setGoodInfo() {
        if (model.mobilegoodsArrayList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            nullpage.setVisibility(View.GONE);
            adapter.setData(model.mobilegoodsArrayList);
            adapter.notifyDataSetChanged();
        } else {
            listView.setVisibility(View.GONE);
            nullpage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh(int id) {
        model.getMobileGoods(false);
    }

    @Override
    public void onLoadMore(int id) {
        model.getMobileGoodsMore();
    }
}
