package com.ecjia.view.activity.goodsdetail.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.XListView;
import com.ecjia.view.activity.goodsdetail.adapter.GoodsdetailCommentAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 商品详情的商品介绍页
 */
@SuppressLint("ValidFragment")
public class ProductCommonListFragment extends GoodsDetailBaseFragment implements XListView.IXListViewListener, HttpResponse {

    private CommentModel commentModel;
    private String goods_id;
    private GoodsdetailCommentAdapter commentAdapter;
    private FrameLayout fnocomment;
    private XListView xlistView;

    String type;
    private View mainView;

    @SuppressLint("ValidFragment")
    public ProductCommonListFragment(String type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != mainView) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (null != parent) {
                parent.removeView(mainView);
            }
        } else {
            mainView = inflater.inflate(R.layout.fragment_goodsdetail_comment, null);
            init();
            initView(mainView);
        }

        return mainView;
    }

    private void init() {

        getData();
        initData();
    }


    private void initView(View view) {
        //新加入控件
        fnocomment = (FrameLayout) view.findViewById(R.id.no_comment);
        xlistView = (XListView) view.findViewById(R.id.comment_list);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this, 1);
        commentAdapter = new GoodsdetailCommentAdapter(getActivity(), commentModel.goods_comment_list);
        xlistView.setAdapter(commentAdapter);
    }


    private void initData() {

        commentModel = new CommentModel(getActivity());
        commentModel.addResponseListener(this);
        commentModel.getCommentList(goods_id, type, false);

    }

    private void getData() {
        goods_id = getActivity().getIntent().getStringExtra("goods_id");
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        switch (url) {

            case ProtocolConst.GOODS_COMMENT_LIST: //评论
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
                break;

        }
    }


    public void setComment() {

        if (commentModel.goods_comment_list.size() > 0) {
            xlistView.setVisibility(View.VISIBLE);
            fnocomment.setVisibility(View.GONE);
            commentAdapter.notifyDataSetChanged();
        } else {
            xlistView.setVisibility(View.GONE);
            fnocomment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        commentModel.removeAllResponseListener();
    }

    @Override
    public void onRefresh(int id) {
        commentModel.getCommentList(goods_id, type, true);
    }

    @Override
    public void onLoadMore(int id) {
        commentModel.getCommentsMore(goods_id, type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
