package com.ecjia.view.activity;


import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ecjia.base.BaseActivity;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;
import com.ecjia.view.activity.goodsdetail.model.CommentType;
import com.ecjia.view.adapter.CommentAdapter;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.consts.ProtocolConst;

import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentActivity extends BaseActivity implements IXListViewListener, HttpResponse {

    private XListView xlistView;
    private CommentAdapter commentAdapter;
    ArrayList<COMMENT_LIST> list = new ArrayList<>();
    private String goods_id;
    private ImageView noresult;
    private CommentModel commentModel;
    private FrameLayout fnocomment;

    //新加入控件
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        initTopView();
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        goods_id = intent.getStringExtra(IntentKeywords.GOODS_ID);

        //新加入控件
        fnocomment = (FrameLayout) this.findViewById(R.id.no_comment);


        xlistView = (XListView) findViewById(R.id.comment_list);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this, 1);

        commentModel = new CommentModel(this);
        commentModel.addResponseListener(this);
        commentModel.getCommentList(goods_id , CommentType.POSITIVE_COMMENT, true);

    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.comment_topview);
        topView.setTitleText(R.string.gooddetail_commit);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onRefresh(int id) {
        list.clear();
        commentModel.getCommentList(goods_id  , CommentType.POSITIVE_COMMENT, true);
    }

    @Override
    public void onLoadMore(int id) {
        commentModel.getCommentsMore(goods_id  , CommentType.POSITIVE_COMMENT);
    }

    public void setComment() {

        if (commentModel.goods_comment_list.size() > 0) {
            xlistView.setVisibility(View.VISIBLE);
            if (commentAdapter == null) {
                for (int i = 0; i < commentModel.goods_comment_list.size(); i++) {
                    list.add(commentModel.goods_comment_list.get(i));
                }
                commentAdapter = new CommentAdapter(this, list);
                xlistView.setAdapter(commentAdapter);
            } else {
                for (int i = 0; i < commentModel.goods_comment_list.size(); i++) {
                    list.add(commentModel.goods_comment_list.get(i));
                }
                commentAdapter.notifyDataSetChanged();
            }
        } else {
            xlistView.setVisibility(View.GONE);
            fnocomment.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.COMMENTS) {
            if (status.getSucceed() == 1) {
                xlistView.setRefreshTime();
                xlistView.stopRefresh();
                xlistView.stopLoadMore();


                if (commentModel.paginated.getMore() == 0) {
                    xlistView.setPullLoadEnable(false);
                } else {
                    xlistView.setPullLoadEnable(true);
                }
                setComment();
            }

        }
    }
}
