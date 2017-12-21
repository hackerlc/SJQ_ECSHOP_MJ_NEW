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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SearchNewModel;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.network.netmodle.ShopDetialModel;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.GoodsListAdapter;
import com.ecjia.view.adapter.ShopSearchListAdapter;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.CommonMethod;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 指定店铺内的商品搜索界面
 */

@SuppressLint("CommitPrefEdits")
public class SearchSellerGoodsActivity extends BaseActivity implements XListView.IXListViewListener, OnClickListener {

    private EditText search_input;
    private TextView tv_search_cancel;
    private LinearLayout ll_search;
    private ErrorView null_pager;
    private FrameLayout fl_search_top;
    private XListView goodlistView;
    private ShopDetialModel dataModel;
    private SellerModel sellerModel;
    private GoodsListAdapter listAdapter1;
    private SearchNewModel searchNewModel;
    private ShopSearchListAdapter shopSearchListAdapter;
    private float y;
    private Handler Intenthandler;
    private FILTER filter;
    private String sellerid;
    private String keywords;
    private LinearLayout filter_toolbar;
    private RelativeLayout rl_filter_one, rl_filter_two, rl_filter_three, rl_filter_four;
    private View filter_one, filter_two, filter_three, filter_four;
    private TextView tv_filter_one, tv_filter_two, tv_filter_three, tv_filter_four;
    private int filtertype;
    private boolean searchseller;
    private Handler messageHandler;
    private int itemid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sellergoods);
        PushAgent.getInstance(this).onAppStart();
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

        filter = new FILTER();
        filter.setSort_by(getSort(filtertype));

        Intent intent = getIntent();
        sellerid = intent.getStringExtra("sellerid");
        keywords = intent.getStringExtra(IntentKeywords.KEYWORDS);
        searchseller = intent.getBooleanExtra("searchseller", false);

        if (searchseller) {
            if (null == searchNewModel) {
                searchNewModel = new SearchNewModel(this);
                searchNewModel.addResponseListener(this);
            }
            if (null == sellerModel) {
                sellerModel = new SellerModel(this);
                sellerModel.addResponseListener(this);
            }
        } else {
            if (null == dataModel) {
                dataModel = new ShopDetialModel(this);
                dataModel.addResponseListener(this);
            }
        }

        initView();
        initToolbar();

        if (searchseller) {
            search_input.setHint(res.getString(R.string.search_input_shop));
        } else {
            search_input.setHint(res.getString(R.string.search_input_shop_goods));
        }

        if (!TextUtils.isEmpty(keywords)) {
            filter.setKeywords(keywords);
            search_input.setText(keywords);
            if (searchseller) {
                searchNewModel.getSearchSellerList(keywords);
            } else {
                dataModel.fetchGoodList(filter, sellerid);
            }
        } else {
            setContent();
        }


    }

    private void initToolbar() {
        filter_toolbar = (LinearLayout) findViewById(R.id.filter_toolbar);

        if (searchseller) {
            filter_toolbar.setVisibility(View.GONE);
        } else {
            filter_toolbar.setVisibility(View.VISIBLE);
        }

        rl_filter_one = (RelativeLayout) findViewById(R.id.rl_filter_one);
        rl_filter_one.setOnClickListener(this);
        rl_filter_one.setEnabled(false);
        rl_filter_two = (RelativeLayout) findViewById(R.id.rl_filter_two);
        rl_filter_two.setOnClickListener(this);
        rl_filter_three = (RelativeLayout) findViewById(R.id.rl_filter_three);
        rl_filter_three.setOnClickListener(this);
        rl_filter_four = (RelativeLayout) findViewById(R.id.rl_filter_four);
        rl_filter_four.setOnClickListener(this);

        filter_one = (View) findViewById(R.id.filter_one);
        filter_two = (View) findViewById(R.id.filter_two);
        filter_three = (View) findViewById(R.id.filter_three);
        filter_four = (View) findViewById(R.id.filter_four);

        tv_filter_one = (TextView) findViewById(R.id.tv_filter_one);
        tv_filter_two = (TextView) findViewById(R.id.tv_filter_two);
        tv_filter_three = (TextView) findViewById(R.id.tv_filter_three);
        tv_filter_four = (TextView) findViewById(R.id.tv_filter_four);
    }

    public int getDisplayMetricsWidth1() {
        int i = this.getWindowManager().getDefaultDisplay().getWidth();
        int j = this.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j) - ((int) getResources().getDimension(R.dimen.eight_margin)) * 2;
    }

    private void initView() {
        goodlistView = (XListView) findViewById(R.id.good_list);
        if (searchseller) {
            shopSearchListAdapter = new ShopSearchListAdapter(this, searchNewModel.sellerlist);
            shopSearchListAdapter.messageHandler = messageHandler;
            goodlistView.setAdapter(shopSearchListAdapter);
        } else {
            listAdapter1 = new GoodsListAdapter(this, dataModel.simplegoodsList);
            goodlistView.setAdapter(listAdapter1);
        }

        goodlistView.setPullLoadEnable(false);
        goodlistView.setRefreshTime();
        goodlistView.setXListViewListener(this, 1);

        null_pager = (ErrorView) findViewById(R.id.null_pager);

        fl_search_top = (FrameLayout) findViewById(R.id.fl_search_top);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        search_input = (EditText) findViewById(R.id.et_search_input);
        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);

        search_input.setFocusable(true);
        search_input.setFocusableInTouchMode(true);
        search_input.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_input, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(search_input, 0);
                           }

                       },
                998);

        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                Resources resources = getBaseContext().getResources();
                String please_input = resources.getString(R.string.search_please_input);
                CloseKeyBoard();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputstr = search_input.getText().toString();
                    CommonMethod.getInstance(SearchSellerGoodsActivity.this).setSeachStringToShared(inputstr);

                    //为空时不能跳转
                    if (inputstr == null || "".equals(inputstr)) {
                        ToastView toast = new ToastView(SearchSellerGoodsActivity.this, please_input);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        keywords = inputstr;
                        filter.setKeywords(keywords);
                        if (searchseller) {
                            searchNewModel.getSearchSellerList(keywords);
                        } else {
                            dataModel.fetchGoodList(filter, sellerid);
                        }
                    }
                }
                return true;
            }
        });

        findViewById(R.id.search_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MyEvent("search_back"));
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
            CommonMethod.getInstance(SearchSellerGoodsActivity.this).setSeachStringToShared(inputstr);

            //为空时不能跳转
            if (inputstr == null || "".equals(inputstr)) {
                ToastView toast = new ToastView(SearchSellerGoodsActivity.this, please_input);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                keywords = inputstr;
                filter.setKeywords(keywords);
                if (searchseller) {
                    searchNewModel.getSearchSellerList(keywords);
                } else {
                    dataModel.fetchGoodList(filter, sellerid);
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setContent() {
        if (searchseller) {
            if (searchNewModel.sellerlist.size() == 0) {
                null_pager.setErrorImageResource(R.drawable.null_shop);
                null_pager.setErrorText(R.string.null_shop);
                null_pager.setVisibility(View.VISIBLE);
                goodlistView.setVisibility(View.GONE);
            } else {
                goodlistView.setVisibility(View.VISIBLE);
                null_pager.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(keywords)) {
                CommonMethod.getInstance(SearchSellerGoodsActivity.this).setSellersToShared(keywords);
            }
            shopSearchListAdapter.notifyDataSetChanged();
        } else {
            if (dataModel.simplegoodsList.size() == 0) {
                null_pager.setVisibility(View.VISIBLE);
                goodlistView.setVisibility(View.GONE);
            } else {
                goodlistView.setVisibility(View.VISIBLE);
                null_pager.setVisibility(View.GONE);
            }
            listAdapter1.notifyDataSetChanged();
        }
    }

    private String getSort(int filtertype) {
        //new最新上架商品，price_desc价格从高到底，price_asc价格从低到高，hot关注度高的商品
        String fin = "new";
        switch (filtertype) {
            case 0:
                fin = "new";
                break;
            case 1:
                fin = "hot";
                break;
            case 2:
                fin = "price_desc";
                break;
            case 3:
                fin = "price_asc";
                break;
        }

        return fin;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Resources resources = getBaseContext().getResources();
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                CloseKeyBoard();
                finish();
                break;
            case R.id.rl_filter_one:
                rl_filter_one.setEnabled(false);
                rl_filter_two.setEnabled(true);
                rl_filter_three.setEnabled(true);
                rl_filter_four.setEnabled(true);

                filter_one.setVisibility(View.VISIBLE);
                filter_two.setVisibility(View.INVISIBLE);
                filter_three.setVisibility(View.INVISIBLE);
                filter_four.setVisibility(View.INVISIBLE);

                tv_filter_one.setTextColor(res.getColor(R.color.public_theme_color_normal));
                tv_filter_two.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_three.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_four.setTextColor(res.getColor(R.color.filter_text_color));

                filtertype = 0;
                filter.setSort_by(getSort(filtertype));

                if(TextUtils.isEmpty(keywords)){
                    null_pager.setVisibility(View.VISIBLE);
                    goodlistView.setVisibility(View.GONE);
                }else{
                    dataModel.fetchGoodList(filter, sellerid);
                }

                break;
            case R.id.rl_filter_two:
                rl_filter_one.setEnabled(true);
                rl_filter_two.setEnabled(false);
                rl_filter_three.setEnabled(true);
                rl_filter_four.setEnabled(true);

                filter_one.setVisibility(View.INVISIBLE);
                filter_two.setVisibility(View.VISIBLE);
                filter_three.setVisibility(View.INVISIBLE);
                filter_four.setVisibility(View.INVISIBLE);

                tv_filter_one.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_two.setTextColor(res.getColor(R.color.public_theme_color_normal));
                tv_filter_three.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_four.setTextColor(res.getColor(R.color.filter_text_color));

                filtertype = 1;
                filter.setSort_by(getSort(filtertype));
                if(TextUtils.isEmpty(keywords)){
                    null_pager.setVisibility(View.VISIBLE);
                    goodlistView.setVisibility(View.GONE);
                }else{
                    dataModel.fetchGoodList(filter, sellerid);
                }

                break;
            case R.id.rl_filter_three:
                rl_filter_one.setEnabled(true);
                rl_filter_two.setEnabled(true);
                rl_filter_three.setEnabled(false);
                rl_filter_four.setEnabled(true);

                filter_one.setVisibility(View.INVISIBLE);
                filter_two.setVisibility(View.INVISIBLE);
                filter_three.setVisibility(View.VISIBLE);
                filter_four.setVisibility(View.INVISIBLE);

                tv_filter_one.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_two.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_three.setTextColor(res.getColor(R.color.public_theme_color_normal));
                tv_filter_four.setTextColor(res.getColor(R.color.filter_text_color));

                filtertype = 2;
                filter.setSort_by(getSort(filtertype));
                if(TextUtils.isEmpty(keywords)){
                    null_pager.setVisibility(View.VISIBLE);
                    goodlistView.setVisibility(View.GONE);
                }else{
                    dataModel.fetchGoodList(filter, sellerid);
                }

                break;
            case R.id.rl_filter_four:
                rl_filter_one.setEnabled(true);
                rl_filter_two.setEnabled(true);
                rl_filter_three.setEnabled(true);
                rl_filter_four.setEnabled(false);

                filter_one.setVisibility(View.INVISIBLE);
                filter_two.setVisibility(View.INVISIBLE);
                filter_three.setVisibility(View.INVISIBLE);
                filter_four.setVisibility(View.VISIBLE);

                tv_filter_one.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_two.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_three.setTextColor(res.getColor(R.color.filter_text_color));
                tv_filter_four.setTextColor(res.getColor(R.color.public_theme_color_normal));

                filtertype = 3;
                filter.setSort_by(getSort(filtertype));
                if(TextUtils.isEmpty(keywords)){
                    null_pager.setVisibility(View.VISIBLE);
                    goodlistView.setVisibility(View.GONE);
                }else{
                    dataModel.fetchGoodList(filter, sellerid);
                }

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
        if (searchseller) {
            searchNewModel.getSearchSellerList(keywords);
        } else {
            dataModel.fetchGoodList(filter, sellerid);
        }
    }

    @Override
    public void onLoadMore(int id) {
        if (searchseller) {
            searchNewModel.getSearchSellerListMore(keywords);
        } else {
            dataModel.fetchListMore(filter, sellerid);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.SHOPDETIAL)) {
            if (status.getSucceed() == 1) {
                goodlistView.stopRefresh();
                goodlistView.stopLoadMore();
                goodlistView.setRefreshTime();
                setContent();
                PAGINATED paginated = dataModel.paginated;
                if (0 == paginated.getMore()) {
                    goodlistView.setPullLoadEnable(false);
                } else {
                    goodlistView.setPullLoadEnable(true);
                }
            }
        }

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
        } else if (url.equals(ProtocolConst.SEARCH_SELLER)) {
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
            if (null != searchNewModel && searchseller) {
                searchNewModel.getSearchSellerList(keywords);
            }
        }
    }


}
