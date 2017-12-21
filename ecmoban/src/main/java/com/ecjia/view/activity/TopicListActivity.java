package com.ecjia.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.TopicModel;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.TopiclistAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class TopicListActivity extends BaseActivity implements HttpResponse,XListView.IXListViewListener{
    private XListView topic_listview;
    private FrameLayout null_page;
    private TopicModel topicModel;
    private TopiclistAdapter topiclistAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        initView();
    }

    private void initView() {
        initTopView();
        topicModel=new TopicModel(this);
        topicModel.getTopicList();
        topicModel.addResponseListener(this);
        topiclistAdapter=new TopiclistAdapter(this,topicModel.topics);
        topic_listview= (XListView) findViewById(R.id.topic_list);
        topic_listview.setPullLoadEnable(false);
        topic_listview.setXListViewListener(this, 0);
        topic_listview.setAdapter(topiclistAdapter);
        null_page= (FrameLayout) findViewById(R.id.null_pager);

    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.topiclist_topview);
        if (getResources().getConfiguration().locale.equals(Locale.CHINA)) {
            topView.setTitleImage(R.drawable.theme_icon_chinese_white);
        } else {
            topView.setTitleImage(R.drawable.theme_icon_english_white);
        }
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url== ProtocolConst.TOPIC_LIST){
            if(status.getSucceed()==1){
                topiclistAdapter.notifyDataSetChanged();
                topic_listview.setRefreshTime();
                topic_listview.stopLoadMore();
                topic_listview.stopRefresh();
                PAGINATED paginated = topicModel.paginated;
                if (paginated.getMore() == 0) {
                    topic_listview.setPullLoadEnable(false);
                } else {
                    topic_listview.setPullLoadEnable(true);
                }

                if(topicModel.topics.size()>0){
                    topic_listview.setVisibility(View.VISIBLE);
                    null_page.setVisibility(View.GONE);
                }else{
                    topic_listview.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                }

            }else{
                topic_listview.setVisibility(View.GONE);
                null_page.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        topicModel.getTopicList();
    }

    @Override
    public void onLoadMore(int id) {
        topicModel.getTopicListMore();
    }
}
