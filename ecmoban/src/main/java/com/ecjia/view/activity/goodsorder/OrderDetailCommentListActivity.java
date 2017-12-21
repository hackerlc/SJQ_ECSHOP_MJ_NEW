package com.ecjia.view.activity.goodsorder;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecjia.view.adapter.OrderDetailCommentAdapter;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;
/*
评价晒单
 */
public class OrderDetailCommentListActivity extends BaseActivity implements IXListViewListener {

    private TextView title;
    private ImageView back;

    private XListView xlistView;
    private String order_id;
    private CommentModel commentModel;
    private OrderDetailCommentAdapter mAdapter;
    private View null_pager;

    //新加入控件
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail_comment);
        initView();
        xlistView = (XListView) findViewById(R.id.orderdetail_comment_list);
        xlistView.setPullRefreshEnable(false);
        xlistView.setRefreshTime();
        xlistView.setPullLoadEnable(false);
        xlistView.setXListViewListener(this, 0);
        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        commentModel = new CommentModel(this);
        commentModel.addResponseListener(this);

        mAdapter = new OrderDetailCommentAdapter(OrderDetailCommentListActivity.this, commentModel.comment_order_list);
        xlistView.setAdapter(mAdapter);
    }


    @Override
    public void onRefresh(int id) {
    }

    @Override
    public void onLoadMore(int id) {
    }


    public void initView() {
        null_pager = findViewById(R.id.null_pager);
        null_pager.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.top_view_back);
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(R.string.create_comment);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        commentModel.getOrderCommentList(order_id);
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        xlistView.stopRefresh();
        xlistView.stopLoadMore();
        if (url.equals(ProtocolConst.ORDERS_COMMENT)) {
            if (status.getSucceed() == 1) {
                if (commentModel.comment_order_list.size() == 0) {
                    null_pager.setVisibility(View.VISIBLE);
                } else {
                    null_pager.setVisibility(View.GONE);
                }

                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
