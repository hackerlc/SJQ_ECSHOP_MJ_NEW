package com.ecjia.view.order.fragment;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.OrderModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecjia.consts.EventKeywords;
import com.ecjia.view.adapter.TradeAdapter;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class OrderListFragment extends Fragment implements IXListViewListener, HttpResponse {

    private String flag;
    private XListView xlistView;
    private TradeAdapter tradeAdapter;
    private View null_paView;

    private OrderModel orderModel;
    public boolean orderupdate = false;
    private boolean isreset = false;
    private int selectcolor;
    private String keywords = "";

    @SuppressLint("ValidFragment")
    public OrderListFragment(String ordertype) {
        super();
        flag = ordertype;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orderlist, null);
        final Resources resource = (Resources) getResources();
        selectcolor = resource.getColor(R.color.public_theme_color_normal);
        null_paView = view.findViewById(R.id.null_pager);
        xlistView = (XListView) view.findViewById(R.id.trade_list);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime();
        orderModel = new OrderModel(getActivity());
        tradeAdapter = new TradeAdapter(getActivity(), orderModel.order_list, flag);
        xlistView.setAdapter(tradeAdapter);
        xlistView.setXListViewListener(this, 1);
        return view;
    }


    public void setOrder() {
        if (orderModel.order_list.size() == 0) {
            null_paView.setVisibility(View.VISIBLE);
            xlistView.setVisibility(View.GONE);
            return;
        } else {
            null_paView.setVisibility(View.GONE);
            xlistView.setVisibility(View.VISIBLE);

        }
        tradeAdapter.list = orderModel.order_list;
        tradeAdapter.flag = flag;
        tradeAdapter.notifyDataSetChanged();
        if (isreset) {
            isreset = false;
        }
    }

    @Override
    public void onRefresh(int id) {
        orderModel.getOrder(flag, keywords, false);
    }

    @Override
    public void onLoadMore(int id) {
        orderModel.getOrderMore(flag, keywords);
    }

    @Override
    public void onResume() {
        super.onResume();
        LG.i(flag + "订单重新刷新");
        orderModel.addResponseListener(this);
        orderModel.getOrder(flag, keywords, true);
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        orderModel.removeAllResponseListener();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ORDER_LIST)) {
            if (status.getSucceed() == 1) {
                xlistView.setRefreshTime();
                if (orderModel.paginated.getMore() == 0) {
                    xlistView.setPullLoadEnable(false);
                } else {
                    xlistView.setPullLoadEnable(true);
                }
            }
            setOrder();
        }
    }

    public void onEvent(MyEvent event) {
        if (event.getMsg().equals(EventKeywords.UPDATE_ORDER)) {
            orderupdate = true;
        }
    }
}
