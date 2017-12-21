package com.ecjia.view.activity.goodsdetail.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.goodsdetail.model.CommentType;
import com.ecjia.view.activity.goodsdetail.view.CommentViewPager;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 商品详情的商品介绍页
 */
@SuppressLint("ValidFragment")
public class ProductCommonFragment extends GoodsDetailBaseFragment implements HttpResponse {

    private CommentModel commentModel;
    private String goods_id;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private CommentViewPager viewPager;
    private ArrayList<String> titles = new ArrayList<>();

    private ArrayList<String> commentNumbers = new ArrayList<>();

    private LinearLayout tablayout;
    private ProductCommonTabHelper tabHelper;


    Activity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common, null);
        init();
        initView(view);
        return view;
    }

    private void init() {
        getData();
        initData();
    }


    private void initView(View view) {
        //新加入控件
        viewPager = (CommentViewPager) view.findViewById(R.id.comment_list_vierpager);
        tablayout = (LinearLayout) view.findViewById(R.id.comment_list_tablayout);
        initBottomLayout();
        tabHelper.selectItem(1);
    }

    private void initBottomLayout() {
        tabHelper = new ProductCommonTabHelper(this, tablayout) {
            @Override
            public void tabChanged(int tabId) {
                switch (tabId) {
                    case 1:
                        switchContent(fragments.get(0), "one");
                        break;

                    case 2:
                        switchContent(fragments.get(1), "two");
                        break;

                    case 3:
                        switchContent(fragments.get(2), "three");
                        break;

                    case 4:
                        switchContent(fragments.get(3), "four");

                        break;

                    case 5:
                        switchContent(fragments.get(4), "five");
                        break;

                }
            }
        };
    }

    private Fragment mContent;

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(Fragment to, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (mContent == null) {
            transaction.add(R.id.comment_list_frame, fragments.get(0)).commit();
        } else {
            if (mContent != to) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(mContent).add(R.id.comment_list_frame, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
                }
            }
        }
        mContent = to;
    }

    private void initData() {
        commentModel = new CommentModel(getActivity());
        commentModel.addResponseListener(this);
        commentModel.getCommentList(goods_id, CommentType.ALL_COMMENT, false);
    }

    private void getData() {
        goods_id = getActivity().getIntent().getStringExtra(IntentKeywords.GOODS_ID);
        fragments.add(new ProductCommonListFragment(CommentType.ALL_COMMENT));
        fragments.add(new ProductCommonListFragment(CommentType.POSITIVE_COMMENT));
        fragments.add(new ProductCommonListFragment(CommentType.MODERATE_COMMENT));
        fragments.add(new ProductCommonListFragment(CommentType.NEGATIVE_COMMENT));
        fragments.add(new ProductCommonListFragment(CommentType.SHOWORDER_COMMENT));
    }


    @Override
    public void onPause() {
        commentModel.removeAllResponseListener();
        super.onPause();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.GOODS_COMMENT_LIST)) {
            if (status.getSucceed() == 1) {
                titles.clear();
                titles.add(activity.getResources().getString(R.string.comment_type_all));
                titles.add(activity.getResources().getString(R.string.comment_type_positive));
                titles.add(activity.getResources().getString(R.string.comment_type_moderate));
                titles.add(activity.getResources().getString(R.string.comment_type_negative));
                titles.add(activity.getResources().getString(R.string.comment_type_showorder));
                commentNumbers.clear();
                commentNumbers.add(commentModel.goods_comment_count + "");
                commentNumbers.add(commentModel.goods_comment_positive + "");
                commentNumbers.add(commentModel.goods_comment_moderate + "");
                commentNumbers.add(commentModel.goods_comment_negative + "");
                commentNumbers.add(commentModel.goods_comment_showorder + "");
                tabHelper.setTitles(titles, commentNumbers);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        commentModel.addResponseListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
