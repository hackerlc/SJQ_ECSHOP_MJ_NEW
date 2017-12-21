package com.ecjia.view.activity.goodsdetail.fragment;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.GoodsDetailModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ErrorView;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.ecjia.view.adapter.GoodPropertyAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 商品详情的详情页
 */
public class ProductDetailFragment extends GoodsDetailBaseFragment implements HttpResponse {

    private WebView webView;
    private FrameLayout webview_item;
    private FrameLayout property_item;
    private ListView property_list;
    private ErrorView no_info;

    GoodsDetailModel goodsDetailModel;
    private String goods_id;
    private GoodPropertyAdapter propertyAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_layout, null);
        initView(view);
        goods_id = getActivity().getIntent().getStringExtra("goods_id");

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            initModel();
        }

        LG.i("ProductDetailFragment onResume");
    }

    private void initView(View view) {

        setGoodsDesc(view);
        setGoodProperty(view);
        setTabView(view);
    }

    private void initModel() {

        if (goodsDetailModel == null) {
            goodsDetailModel = new GoodsDetailModel(getActivity());
            goodsDetailModel.addResponseListener(this);
        }
        goodsDetailModel.goodsDesc(goods_id);
    }


    private RelativeLayout taboneitem, tabtwoitem;
    private TextView tabonetext, tabtwotext;

    //商品详细介绍
    void setTabView(View view) {
        taboneitem = (RelativeLayout) view.findViewById(R.id.tabOne_item);
        tabtwoitem = (RelativeLayout) view.findViewById(R.id.tabTwo_item);
        tabonetext = (TextView) view.findViewById(R.id.tabone_text);
        tabtwotext = (TextView) view.findViewById(R.id.tabtwo_text);
        taboneitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected("one");
            }
        });
        tabtwoitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected("two");
            }
        });
//-----------------------------------------------------------------------------

        selected("one");
    }


    //webview数据
    public void setGoodsDesc(View view) {

        webview_item = (FrameLayout) view.findViewById(R.id.webview_item);
        webView = (WebView) view.findViewById(R.id.my_web);
        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                webView.loadDataWithBaseURL(null, goodsDetailModel.goodsWeb, "text/html", "utf-8", null);
//                super.onReceivedError(view, request, error);
//            }
        });
        webView.setInitialScale(25);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }


    //商品规格表格
    public void setGoodProperty(View view) {
        property_item = (FrameLayout) view.findViewById(R.id.property_item);
        property_list = (ListView) view.findViewById(R.id.property_list);
        no_info = (ErrorView) view.findViewById(R.id.no_features);
        no_info.setVisibility(View.VISIBLE);
        property_list.setVisibility(View.GONE);
    }


    //TAB的点击效果
    private void selected(String position) {
        Resources resource = getResources();
        ColorStateList selectedTextColor = (ColorStateList) resource.getColorStateList(R.color.filter_text_color);
        if ("one".equals(position)) {
            tabonetext.setTextColor(Color.RED);
            tabtwotext.setTextColor(selectedTextColor);

            webview_item.setVisibility(View.VISIBLE);
            property_item.setVisibility(View.GONE);


        } else if ("two".equals(position)) {

            tabonetext.setTextColor(selectedTextColor);
            tabtwotext.setTextColor(Color.RED);

            webview_item.setVisibility(View.GONE);
            property_item.setVisibility(View.VISIBLE);

            /**
             * 商品属性
             */
            if (null != GoodDetailDraft.getInstance().goodDetail && GoodDetailDraft.getInstance().goodDetail
                    .getProperties().size() > 0) {
                property_list.setVisibility(View.VISIBLE);
                no_info.setVisibility(View.GONE);
                propertyAdapter = new GoodPropertyAdapter(getActivity(), GoodDetailDraft.getInstance().goodDetail.getProperties());
                property_list.setAdapter(propertyAdapter);
            } else {
                no_info.setVisibility(View.VISIBLE);
                property_list.setVisibility(View.GONE);
            }

        }

    }

    boolean isFirst = true;

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        switch (url) {
            case ProtocolConst.GOODSDESC://商品详情
                if (status.getSucceed() == 1) {
                    if (!TextUtils.isEmpty(goodsDetailModel.goodsWeb)) {
                        webView.loadDataWithBaseURL(null, goodsDetailModel.goodsWeb, "text/html", "utf-8", null);
                        isFirst = false;
                    } else {
                        isFirst = true;
                    }
                }
                break;

        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
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
}
