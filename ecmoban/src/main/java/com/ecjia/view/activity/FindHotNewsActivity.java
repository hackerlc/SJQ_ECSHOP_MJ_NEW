package com.ecjia.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.FindModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.XListViewNews;
import com.ecjia.view.adapter.FindHotPointAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

public class FindHotNewsActivity extends BaseActivity implements HttpResponse, XListViewNews.IXListViewListener {
    private ImageView back;
    private TextView title;
    private XListViewNews xListView;
    private FindHotPointAdapter findHotPointAdapter;
    private FindModel model;
    private int pos = 0;
    private int size = 0;
    private ErrorView null_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_hot_news);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.top_view_back);
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(getResources().getString(R.string.find_today_hot));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        null_pager=(ErrorView)findViewById(R.id.null_pager);
        xListView = (XListViewNews) findViewById(R.id.hot_new_listview);
        xListView.setXListViewListener(this, 1);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(false);
        model = new FindModel(this);
        model.addResponseListener(this);
        findHotPointAdapter = new FindHotPointAdapter(this, model.hotnewslist);
        xListView.setAdapter(findHotPointAdapter);
        model.getHomeNews();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (ProtocolConst.HOME_HOT_NEWS.equals(url)) {
            if (status.getSucceed() == 1) {
                xListView.stopRefresh();
                xListView.setRefreshTime();//更新刷新时间
                PAGINATED paginated = model.paginated;
                if (paginated.getMore() == 0) {
                    xListView.setPullLoadEnable(false);
                } else {
                    xListView.setPullLoadEnable(true);
                }

                if(model.hotnewslist.size()>0){
                    null_pager.setVisibility(View.GONE);
                    xListView.setVisibility(View.VISIBLE);
                    setNewsInfo();
                    /**
                     * 暂时不需要做倒序
                     */
                       pos = model.hotnewslist.size();
                       pos -= size;
                       xListView.setSelection(pos);
                       size = model.hotnewslist.size();
                }else{
                    null_pager.setVisibility(View.VISIBLE);
                    xListView.setVisibility(View.GONE);
                }

            }
        }
    }

    private void setNewsInfo() {
        findHotPointAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {
        model.getHomeNewsMore();
    }
}
