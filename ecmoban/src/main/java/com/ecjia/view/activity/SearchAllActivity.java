package com.ecjia.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SearchNewModel;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.GoodsListAdapter;
import com.ecjia.view.adapter.ShopSearchListAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.CommonMethod;
import com.ecjia.util.EventBus.Event;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 指定店铺内的商品搜索界面
 */

@SuppressLint("CommitPrefEdits")
public class SearchAllActivity extends BaseActivity implements XListView.IXListViewListener, HttpResponse, OnClickListener {

    private TextView search_input;
    private TextView tv_search_cancel;
    private LinearLayout ll_search;
    private View null_pager;
    private FrameLayout fl_search_top;
    private XListView goodlistView;
    private SellerModel sellerModel;
    private GoodsListAdapter listAdapter1;
    private SearchNewModel searchNewModel;
    private ShopSearchListAdapter shopSearchListAdapter;
    private float y;
    private String keywords;
    private Handler messageHandler;
    private int itemid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);
        PushAgent.getInstance(this).onAppStart();
        // 初始化

        EventBus.getDefault().register(this);
        messageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    itemid = msg.arg1;
                    sellerModel.sellerCollectCreate(shopSearchListAdapter.getList().get(msg.arg1).getId());

                }
                if (msg.what == 2) {
                    itemid = msg.arg1;
                    sellerModel.sellerCollectDelete(shopSearchListAdapter.getList().get(msg.arg1).getId());

                }
            }
        };

        Intent intent = getIntent();
        keywords = intent.getStringExtra(IntentKeywords.KEYWORDS);

        if (null == searchNewModel) {
            searchNewModel = new SearchNewModel(this);
        }
        searchNewModel.addResponseListener(this);
        if (null == sellerModel) {
            sellerModel = new SellerModel(this);
        }
        sellerModel.addResponseListener(this);

        initView();


        if (!TextUtils.isEmpty(keywords)) {
            search_input.setText(keywords);
            searchNewModel.getSearchALLList(keywords);
        } else {
            setContent();
        }


    }

    private void initView() {
        goodlistView = (XListView) findViewById(R.id.good_list);

        goodlistView.setPullLoadEnable(false);
        goodlistView.setRefreshTime();
        goodlistView.setXListViewListener(this, 1);

        null_pager = (View) findViewById(R.id.null_pager);

        fl_search_top = (FrameLayout) findViewById(R.id.fl_search_top);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        search_input = (TextView) findViewById(R.id.et_search_input);
        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);

        search_input.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.search_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            Resources resources = getBaseContext().getResources();
            String please_input = resources.getString(R.string.search_please_input);
            CloseKeyBoard();
            String inputstr = search_input.getText().toString();
            CommonMethod.getInstance(SearchAllActivity.this).setSeachStringToShared(inputstr);

            //为空时不能跳转
            if (inputstr == null || "".equals(inputstr)) {
                ToastView toast = new ToastView(SearchAllActivity.this, please_input);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                keywords = inputstr;
                searchNewModel.getSearchALLList(keywords);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setContent() {
        if (searchNewModel.type == 1) {
            if (!TextUtils.isEmpty(keywords)) {
                CommonMethod.getInstance(SearchAllActivity.this).setSellersToShared(keywords);
            }
            if (searchNewModel.sellerlist.size() == 0) {
                ToastView toast = new ToastView(this, res.getString(R.string.search_nothing_seller));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                null_pager.setVisibility(View.VISIBLE);
                goodlistView.setVisibility(View.GONE);
            } else {
                goodlistView.setVisibility(View.VISIBLE);
                null_pager.setVisibility(View.GONE);
            }
            if (null == shopSearchListAdapter) {
                shopSearchListAdapter = new ShopSearchListAdapter(this, searchNewModel.sellerlist);
                shopSearchListAdapter.messageHandler = messageHandler;
                goodlistView.setAdapter(shopSearchListAdapter);
            } else {
                shopSearchListAdapter.notifyDataSetChanged();
            }

        } else if (searchNewModel.type == 2) {
            if (!TextUtils.isEmpty(keywords)) {
                CommonMethod.getInstance(SearchAllActivity.this).setGoodsToShared(keywords);
            }
            if (searchNewModel.searchGood.size() == 0) {
                ToastView toast = new ToastView(this, res.getString(R.string.search_nothing_good));
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                null_pager.setVisibility(View.VISIBLE);
                goodlistView.setVisibility(View.GONE);
            } else {
                goodlistView.setVisibility(View.VISIBLE);
                null_pager.setVisibility(View.GONE);
            }

            if (null == listAdapter1) {
                listAdapter1 = new GoodsListAdapter(this, searchNewModel.searchGood);
                goodlistView.setAdapter(listAdapter1);
            } else {
                listAdapter1.notifyDataSetChanged();
            }

        } else {
            null_pager.setVisibility(View.VISIBLE);
            goodlistView.setVisibility(View.GONE);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        Resources resources = getBaseContext().getResources();
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                CloseKeyBoard();
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    // 关闭键盘
    public void CloseKeyBoard() {
        search_input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_input.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            CloseKeyBoard();
            finish();
            return false;
        }
        return true;
    }


    @Override
    public void onRefresh(int id) {
        searchNewModel.getSearchALLList(keywords);
    }

    @Override
    public void onLoadMore(int id) {
        searchNewModel.getSearchALLListMore(keywords);
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.SELLER_COLLECTCREATE)) {
            if (status.getSucceed() == 1) {
                shopSearchListAdapter.getList().get(itemid).setIs_follower("1");
                shopSearchListAdapter.getList().get(itemid).setFollower(shopSearchListAdapter.getList().get(itemid).getFollower() + 1);
                shopSearchListAdapter.notifyDataSetChanged();
            }
        } else if (url.equals(ProtocolConst.SELLER_COLLECTDELETE)) {
            if (status.getSucceed() == 1) {
                shopSearchListAdapter.getList().get(itemid).setIs_follower("0");
                shopSearchListAdapter.getList().get(itemid).setFollower(shopSearchListAdapter.getList().get(itemid).getFollower() - 1);
                shopSearchListAdapter.notifyDataSetChanged();
            }
        } else if (url.equals(ProtocolConst.SEARCH_NEW)) {
            if (status.getSucceed() == 1) {
                goodlistView.stopRefresh();
                goodlistView.stopLoadMore();
                goodlistView.setRefreshTime();
                setContent();
                PAGINATED paginated = searchNewModel.paginated;
                if (0 == paginated.getMore()) {
                    goodlistView.setPullLoadEnable(false);
                } else {
                    goodlistView.setPullLoadEnable(true);
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(Event event) {
        if ("collectrefresh".equals(event.getMsg())) {
            if (null != searchNewModel) {
                if (searchNewModel.type == 1) {
                    searchNewModel.getSearchALLList(keywords);
                }
            }
        }
    }
}
