package com.ecjia.view.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.network.netmodle.AdvanceSearchModel;
import com.ecjia.network.netmodle.GoodsListModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.view.adapter.GoodlistMenuAdapter;
import com.ecjia.view.adapter.GoodsListAdapter;
import com.ecjia.view.adapter.NewGoodlistAdapter;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PRICE_RANGE;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecmoban.android.sijiqing.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.message.PushAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/*
商品搜索列表
 */
public class GoodsListActivity extends BaseActivity implements
        IXListViewListener, OnClickListener {
    private ImageView backImageButton;

    private ImageView item_grid_button;
    private ImageView shopping_cart;

    private XListView goodlistView;
    private GoodsListModel dataModel;
    private NewGoodlistAdapter listAdapter1;
    //    private NewGoodlistBigAdapter listAdapter2;
    private GoodsListAdapter listAdapter3;

    private ImageView bg;
    private int flag = IS_NEW_INT;
    private boolean isSetAdapter = true;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private View null_pager;

    public static final String KEYWORD = "keyword";
    public static final String CATEGORY_ID = "category_id";
    public static final String TITLE = "title";

    public static String PRICE_DESC = "price_desc";
    public static String PRICE_ASC = "price_asc";
    public static String IS_HOT = "is_hot";
    public static String IS_NEW = "is_new";

    public static final int IS_HOT_INT = 0;
    public static final int PRICE_DESC_INT = 1;
    public static final int PRICE_ASC_INT = 2;
    public static final int IS_NEW_INT = -1;

    public static final int IS_FILTER = 3;

    public String predefine_category_id;
    private ExpandableListView menulist;
    private LinearLayout nofilter, resetfilter;
    public Handler Intenthandler;
    private static LinearLayout shopping_cart_num_bg_one;
    private static TextView shopping_cart_num;
    //    public int type = 1;
    public int type;

    public boolean isFirst = false;
    private String category_id;

    protected class TitleCellHolder {
        public ImageView triangleImageView;
        public TextView titleTextView;
        public ImageView orderIconImageView;
        public RelativeLayout tabRelative;

        private Resources resource;
    }

    private TextView reset;

    TitleCellHolder tabOneCellHolder;
    TitleCellHolder tabTwoCellHolder;
    TitleCellHolder tabThreeCellHolder;
    TitleCellHolder tabFourCellHolder;


    FILTER filter = new FILTER();

    private ImageView search;
    private EditText input;
    private ImageView searchFilter;
    //  private View bottom_line,title_line;

    private View one, two, three, four;// 底部三条线
    SlidingMenu menu;
    int screenWidth;
    private GoodlistMenuAdapter menuadapter;
    AdvanceSearchModel advanceModel;
    private ArrayList<CATEGORY> catlist;
    TextView fliterfinish;
    boolean t1 = false, t2 = false, t3 = false;
    private Resources resources;
    private List<String> list;
    //   private FrameLayout ll_search_top;
    private float y;
    private LinearLayout ll_search, dreamcategory;
    //   private TextView tv_search_cancel;
    private Resources res;
    //  private TextView goodlist_title,search_tv;
    private String keyword;

    public GoodsListActivity() {
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.goodslist_activity);
        input = (EditText) findViewById(R.id.search_input);
        PushAgent.getInstance(this).onAppStart();
        searchFilter = (ImageView) findViewById(R.id.search_filter);

        // 底部横线
        one = findViewById(R.id.filter_one);
        two = findViewById(R.id.filter_two);
        three = findViewById(R.id.filter_three);
        four = findViewById(R.id.filter_four);
        goodlistView = (XListView) findViewById(R.id.goods_listview);
        res = getResources();
        ll_search = (LinearLayout) findViewById(R.id.ll_goodlist_top);
        search = (ImageView) findViewById(R.id.search_search);
        search.setOnClickListener(this);
        input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        input.setFocusable(false);
        input.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(keyword)) {
                    Intent intent = new Intent();
                    intent.setClass(GoodsListActivity.this, SearchNewActivity.class);
                    intent.putExtra("filter", GoodsListActivity.this.filter.toString());
                    startActivityForResult(intent, 100);
                } else {
                    finish();
                }
            }
        });
        keyword = getIntent().getStringExtra(IntentKeywords.KEYWORDS);
        if (StringUtils.isNotEmpty(keyword)) {
            input.setText(keyword);
            filter.setKeywords(keyword);
        }

        category_id = getIntent().getStringExtra(IntentKeywords.CATEGORY_ID);

        if (!TextUtils.isEmpty(category_id)) {
            filter.setCategory_id(category_id);
            predefine_category_id = category_id;
        }

        searchFilter.setOnClickListener(this);
        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        type = shared.getInt(SharedPKeywords.SPUSER_KLISTTYPE, 3);
        if(type!=1 || type!=3){
            type=3;
        }

        Intenthandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.obj == ProtocolConst.SEARCH) {
                    if (msg.what == 1) {
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
                if (msg.obj == ProtocolConst.BRAND) {
                    if (msg.what == 1) {
                        t1 = true;
                        if (advanceModel.brandList.size() > 0) {
                            if (AppConst.brandlist == null
                                    || AppConst.brandlist.size() == 0) {
                                AppConst.brandlist = new ArrayList<HashMap<String, Boolean>>();
                            }
                            AppConst.brandlist.clear();
                            for (int k = 0; k < advanceModel.brandList.size(); k++) {
                                HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                                map.put("flag", false);
                                AppConst.brandlist.add(map);
                            }
                        }
                    }
                }
                if (msg.obj == ProtocolConst.PRICE_RANGE) {
                    if (msg.what == 1) {
                        t2 = true;
                        if (advanceModel.priceRangeArrayList.size() > 0) {
                            if (AppConst.pricelist == null
                                    || AppConst.pricelist.size() == 0) {
                                AppConst.pricelist = new ArrayList<HashMap<String, Boolean>>();
                            }
                            AppConst.pricelist.clear();
                            for (int k = 0; k < advanceModel.priceRangeArrayList.size(); k++) {
                                HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                                map.put("flag", false);
                                AppConst.pricelist.add(map);
                            }
                        }
                    }
                }
                if (msg.obj == ProtocolConst.CATEGORY) {
                    if (msg.what == 1) {
                        t3 = true;
                        catlist = new ArrayList<CATEGORY>();// 分类数据
                        if (predefine_category_id != null
                                && advanceModel.categoryArrayList.size() > 0) {
                            for (int i = 0; i < advanceModel.categoryArrayList.size(); i++) {
                                if (predefine_category_id
                                        .equals(advanceModel.categoryArrayList.get(i).getId()
                                                + "")) {
                                    catlist = advanceModel.categoryArrayList.get(i).getChildren();
                                    if (AppConst.categorylist == null
                                            || AppConst.categorylist.size() == 0) {
                                        AppConst.categorylist = new ArrayList<HashMap<String, Boolean>>();
                                    }
                                    AppConst.categorylist.clear();
                                    for (int k = 0; k < catlist.size(); k++) {
                                        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                                        map.put("flag", false);
                                        AppConst.categorylist.add(map);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }

            }
        };
        backImageButton = (ImageView) findViewById(R.id.nav_back_button);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //通知搜索页面关闭
                EventBus.getDefault().post(new MyEvent("search_back"));
                CloseKeyBoard();
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        shopping_cart = (ImageView) findViewById(R.id.good_list_shopping_cart);
        shopping_cart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
                CloseKeyBoard();
                if (uid.equals("")) {
                    Intent intent = new Intent(GoodsListActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,
                            R.anim.push_buttom_out);
                    Resources resource = (Resources) getBaseContext()
                            .getResources();
                    String nol = resource.getString(R.string.no_login);
                    ToastView toast = new ToastView(GoodsListActivity.this, nol);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent it = new Intent(GoodsListActivity.this,
                            ShoppingCartActivity.class);
                    startActivity(it);
                }
            }
        });

        shopping_cart_num = (TextView) findViewById(R.id.shopping_cart_num);
        shopping_cart_num_bg_one = (LinearLayout) findViewById(R.id.shopping_cart_num_bg_one);
        bg = (ImageView) findViewById(R.id.goodslist_bg);
        null_pager = findViewById(R.id.null_pager);


        goodlistView.setPullLoadEnable(true);
        goodlistView.setRefreshTime();
        goodlistView.setXListViewListener(this, 1);

        dataModel = new GoodsListModel(this);


        if (null == listAdapter1) {
            listAdapter1 = new NewGoodlistAdapter(GoodsListActivity.this, dataModel.simplegoodsList);
        }
        if (null == listAdapter3) {
            listAdapter3 = new GoodsListAdapter(GoodsListActivity.this, dataModel.simplegoodsList);
        }
        isFirst = true;
        if (isFirst == true) {
            if (type == 1) {
                searchFilter.setBackgroundResource(R.drawable.goodlist_choose1);
                goodlistView.setAdapter(listAdapter1);
            } else if (type == 3) {
                searchFilter.setBackgroundResource(R.drawable.goodlist_choose3);
                goodlistView.setAdapter(listAdapter3);
            }
            isFirst = false;
        }

        tabOneCellHolder = new TitleCellHolder();
        tabTwoCellHolder = new TitleCellHolder();
        tabThreeCellHolder = new TitleCellHolder();
        tabFourCellHolder = new TitleCellHolder();

        tabOneCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabone);
        tabOneCellHolder.orderIconImageView = (ImageView) findViewById(R.id.filter_order_tabone);
        tabOneCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabOne);
        tabOneCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTab(IS_NEW_INT);
                    }
                });

        tabTwoCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabtwo);
        tabTwoCellHolder.orderIconImageView = (ImageView) findViewById(R.id.filter_order_tabtwo);
        tabTwoCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabTwo);
        tabTwoCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTab(IS_HOT_INT);

                    }
                });

        tabThreeCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabthree);
        tabThreeCellHolder.orderIconImageView = (ImageView) findViewById(R.id.filter_order_tabthree);
        tabThreeCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabThree);
        tabThreeCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flag = PRICE_DESC_INT;
                        selectedTab(PRICE_DESC_INT);

                    }
                });
        tabFourCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabfour);
        tabFourCellHolder.orderIconImageView = (ImageView) findViewById(R.id.filter_order_tabfour);
        tabFourCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabfour);
        tabFourCellHolder.tabRelative.setVisibility(View.GONE);
        tabFourCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CloseKeyBoard();
                        flag = IS_FILTER;
                        selectedTab(IS_FILTER);
                        if (t1 & t2 & t3) {

                            if (advanceModel.brandList.size() > 0 || catlist.size() > 0
                                    || advanceModel.priceRangeArrayList.size() > 0) {
                                if (menuadapter == null) {
                                    Resources resources = getBaseContext().getResources();
                                    String brand = resources.getString(R.string.goodlist_brand);
                                    String classify = resources.getString(R.string.goodlist_classify);
                                    String price = resources.getString(R.string.goodlist_price);
                                    String[] menuItemsName = new String[]{brand, classify, price};
                                    menuadapter = new GoodlistMenuAdapter(GoodsListActivity.this,
                                            menuItemsName, predefine_category_id,
                                            advanceModel, catlist);
                                    menulist.setAdapter(menuadapter);
                                    for (int i = 0; i < menuadapter.getGroupCount(); i++) {
                                        menulist.expandGroup(i);
                                    }
                                    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                                } else {
                                    menulist.setAdapter(menuadapter);
                                    for (int i = 0; i < menuadapter.getGroupCount(); i++) {
                                        menulist.expandGroup(i);
                                    }
                                }
                            } else {
                                menulist.setVisibility(View.GONE);
                                fliterfinish.setVisibility(View.GONE);
                                nofilter.setVisibility(View.VISIBLE);
                            }
                            menu.toggle();
                        } else {
                            menulist.setVisibility(View.GONE);
                            fliterfinish.setVisibility(View.GONE);
                            nofilter.setVisibility(View.VISIBLE);
                            menu.toggle();
                        }

                    }
                });

        selectedTab(IS_NEW_INT);
        // flag = PRICE_DESC_INT;

        advanceModel = new AdvanceSearchModel(this);

        advanceModel.getCategory(Intenthandler);

        initMenuRight();
        //     }
    }

    void selectedTab(int index) {
        LG.i("运行==");
        isSetAdapter = true;
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList selectedTextColor = (ColorStateList) resource
                .getColorStateList(R.color.filter_text_color);

        if (index == IS_NEW_INT) {
            tabOneCellHolder.orderIconImageView
                    .setImageResource(R.drawable.item_grid_filter_down_active_arrow);
            tabOneCellHolder.orderIconImageView.setWillNotCacheDrawing(true);
            tabOneCellHolder.titleTextView.setTextColor(Color.BLACK);


            tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

            tabFourCellHolder.titleTextView.setTextColor(selectedTextColor);

            tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);
            filter.setSort_by(IS_NEW);

            dataModel.fetchPreSearch(filter, Intenthandler, true);

            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.GONE);
            four.setVisibility(View.GONE);

        } else if (index == IS_HOT_INT) {
            tabTwoCellHolder.orderIconImageView
                    .setImageResource(R.drawable.item_grid_filter_down_active_arrow);
            tabTwoCellHolder.titleTextView.setTextColor(Color.BLACK);


            tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

            tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            tabFourCellHolder.titleTextView.setTextColor(selectedTextColor);

            filter.setSort_by(IS_HOT);
            dataModel.fetchPreSearch(filter, Intenthandler, true);

            one.setVisibility(View.GONE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.GONE);
            four.setVisibility(View.GONE);

        } else if (index == PRICE_DESC_INT) {

            tabThreeCellHolder.titleTextView.setTextColor(Color.BLACK);

            tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);

            tabFourCellHolder.titleTextView.setTextColor(selectedTextColor);

            tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);
            if (filter.getSort_by().equals("price_asc")) {
                filter.setSort_by(PRICE_DESC);
                tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_buttom);
            } else if (filter.getSort_by().equals("price_desc")) {
                filter.setSort_by(PRICE_ASC);
                tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_top);
            } else {
                filter.setSort_by(PRICE_ASC);
                tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_top);
            }
            dataModel.fetchPreSearch(filter, Intenthandler, true);

            one.setVisibility(View.GONE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.VISIBLE);
            four.setVisibility(View.GONE);
        } else if (index == IS_FILTER) {

        }
    }

    public void setContent() {


        if (dataModel.simplegoodsList.size() == 0) {
            bg.setVisibility(View.GONE);
            null_pager.setVisibility(View.VISIBLE);
            goodlistView.setVisibility(View.GONE);
        } else {
            goodlistView.setVisibility(View.VISIBLE);
            bg.setVisibility(View.GONE);
            null_pager.setVisibility(View.GONE);
            if (type == 3) {
                listAdapter3.notifyDataSetChanged();
                listAdapter3.setDate(dataModel.simplegoodsList);
            } else if (type == 1) {
                listAdapter1.notifyDataSetChanged();
                listAdapter1.setDate(dataModel.simplegoodsList);
            }
        }


        String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
        if ("".equals(uid) || mApp.getGoodsNum() == 0) {
            shopping_cart_num_bg_one.setVisibility(View.GONE);
            shopping_cart_num.setVisibility(View.GONE);
        } else {
            shopping_cart_num_bg_one.setVisibility(View.VISIBLE);
            shopping_cart_num.setVisibility(View.VISIBLE);
            if (mApp.getGoodsNum() < 10) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() < 100 && mApp.getGoodsNum() > 9) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() > 99) {
                shopping_cart_num.setText("99+");
            }

        }


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String tag;
        Intent intent;
        resources = getBaseContext().getResources();
        String net_problem = resources.getString(R.string.goodlist_network_problem);
        switch (v.getId()) {
            case R.id.search_search:

                break;
            case R.id.search_filter: {
                CloseKeyBoard();
                changeLayout();

                break;

            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (menu.isMenuShowing()) {
                menu.toggle();
            } else {
                finish();
                overridePendingTransition(R.anim.push_left_in,
                        R.anim.push_left_out);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRefresh(int id) {
        isSetAdapter = true;
        dataModel.fetchPreSearch(filter, Intenthandler, false);
        advanceModel.getCategory(Intenthandler);
        advanceModel.getAllBrand(predefine_category_id, Intenthandler);
        if (predefine_category_id == null || StringUtils.isEmpty(predefine_category_id)) {
            advanceModel.getPriceRange(0, Intenthandler);
        } else {
            advanceModel.getPriceRange(Integer.valueOf(predefine_category_id), Intenthandler);
        }
    }

    @Override
    public void onLoadMore(int id) {
        isSetAdapter = false;
        dataModel.fetchPreSearchMore(filter, Intenthandler);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (null != data) {
                    String filter_string = data.getStringExtra("filter");
                    if (null != filter_string) {
                        try {
                            JSONObject filterJSONObject = new JSONObject(
                                    filter_string);
                            FILTER filter = FILTER
                                    .fromJson(filterJSONObject);
                            this.filter.setCategory_id(filter.getCategory_id());
                            this.filter.setPrice_range(filter.getPrice_range());
                            this.filter.setBrand_id(filter.getBrand_id());
                            dataModel.fetchPreSearch(this.filter, Intenthandler, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        TranslateAnimation animation = new TranslateAnimation(0, 0, -y, 0);
        ScaleAnimation animation1 = new ScaleAnimation(0.85f, 1f, 1f, 1f);
        TranslateAnimation animation2 = new TranslateAnimation(-150, 0, 0, 0);
        animation.setDuration(200);
        animation1.setDuration(150);
        animation2.setDuration(150);
        animation.setFillAfter(true);
        animation1.setFillAfter(true);
        animation2.setFillAfter(true);
        ll_search.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (mApp.getGoodsNum() == 0) {
            shopping_cart_num_bg_one.setVisibility(View.GONE);
            shopping_cart_num.setVisibility(View.GONE);
        } else {
            shopping_cart_num_bg_one.setVisibility(View.VISIBLE);
            shopping_cart_num.setVisibility(View.VISIBLE);
            if (mApp.getGoodsNum() < 10) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() < 100 && mApp.getGoodsNum() > 9) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() > 99) {
                shopping_cart_num.setText("99+");
            }
        }
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        String s = getIntent().getStringExtra(IntentKeywords.KEYWORDS);
        if (StringUtils.isEmpty(s)) {
            input.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }

    @SuppressWarnings("deprecation")
    private void initMenuRight() {
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        menu.setShadowDrawable(R.drawable.new_good_distance);
        menu.setShadowWidthRes(R.dimen.slidingmenu_offset);
        menu.setBehindWidth((int) (screenWidth * (5.0 / 6.0)));
        menu.setFadeDegree(0.35f);
        menu.setMenu(R.layout.activity_goodlist_menu);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        initmenuView();
    }

    // 初始化菜单内容
    private void initmenuView() {
        menulist = (ExpandableListView) menu
                .findViewById(R.id.goodlist_filterlist);

        resetfilter = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.goodlist_menu_foot, null);
        reset = (TextView) resetfilter.findViewById(R.id.menu_reset);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConst.brandlist != null && AppConst.brandlist.size() > 0) {
                    for (int k = 0; k < AppConst.brandlist.size(); k++) {
                        if (AppConst.brandlist.get(k).get("flag"))
                            AppConst.brandlist.get(k).put("flag", false);
                    }
                }
                if (AppConst.pricelist != null && AppConst.pricelist.size() > 0) {
                    for (int k = 0; k < AppConst.pricelist.size(); k++) {
                        if (AppConst.pricelist.get(k).get("flag"))
                            AppConst.pricelist.get(k).put("flag", false);
                    }
                }
                if (AppConst.categorylist != null && AppConst.categorylist.size() > 0) {
                    for (int k = 0; k < AppConst.categorylist.size(); k++) {
                        if (AppConst.categorylist.get(k).get("flag"))
                            AppConst.categorylist.get(k).put("flag", false);
                    }

                }
                filter.setBrand_id("");
                filter.setCategory_id(predefine_category_id);
                filter.setPrice_range(null);
                menuadapter.notifyDataSetChanged();
            }
        });
        menulist.addFooterView(resetfilter);
        nofilter = (LinearLayout) menu.findViewById(R.id.goodlist_null);
        fliterfinish = (TextView) menu.findViewById(R.id.filter_finish);
        advanceModel.getAllBrand(predefine_category_id, Intenthandler);
        if (StringUtils.isNotEmpty(predefine_category_id))
            advanceModel.getPriceRange(Integer.valueOf(predefine_category_id), Intenthandler);
        else
            t2 = true;
        menulist.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (groupPosition == 0) {
                    for (int i = 0; i < AppConst.brandlist.size(); i++) {
                        if (childPosition == i) {
                            AppConst.brandlist.get(i).put("flag", true);
                            filter.setBrand_id(advanceModel.brandList
                                    .get(childPosition).getBrand_id() + "");
                        } else {
                            AppConst.brandlist.get(i).put("flag", false);
                        }
                    }
                } else if (groupPosition == 1) {
                    for (int i = 0; i < AppConst.categorylist.size(); i++) {
                        if (i == childPosition) {
                            AppConst.categorylist.get(i).put("flag", true);
                            filter.setCategory_id(catlist.get(childPosition).getId()
                                    + "");
                        } else {
                            AppConst.categorylist.get(i).put("flag", false);

                        }
                    }
                } else if (groupPosition == 2) {

                    for (int i = 0; i < AppConst.pricelist.size(); i++) {
                        if (childPosition == i) {
                            AppConst.pricelist.get(i).put("flag", true);
                            PRICE_RANGE price_range = advanceModel.priceRangeArrayList
                                    .get(childPosition);
                            filter.setPrice_range(price_range);
                        } else {
                            AppConst.pricelist.get(i).put("flag", false);
                        }
                    }
                }
                menuadapter.notifyDataSetChanged();
                dataModel.fetchPreSearch(GoodsListActivity.this.filter, Intenthandler, true);
                menu.toggle();
                return true;
            }
        });

        fliterfinish.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppConst.brandlist != null && AppConst.brandlist.size() > 0) {
                    for (int k = 0; k < AppConst.brandlist.size(); k++) {
                        if (AppConst.brandlist.get(k).get("flag"))
                            AppConst.brandlist.get(k).put("flag", false);
                    }
                }
                if (AppConst.pricelist != null && AppConst.pricelist.size() > 0) {
                    for (int k = 0; k < AppConst.pricelist.size(); k++) {
                        if (AppConst.pricelist.get(k).get("flag"))
                            AppConst.pricelist.get(k).put("flag", false);
                    }
                }
                if (AppConst.categorylist != null && AppConst.categorylist.size() > 0) {
                    for (int k = 0; k < AppConst.categorylist.size(); k++) {
                        if (AppConst.categorylist.get(k).get("flag"))
                            AppConst.categorylist.get(k).put("flag", false);
                    }

                }
                filter.setBrand_id("");
                filter.setCategory_id(predefine_category_id);
                filter.setPrice_range(null);
                menuadapter.notifyDataSetChanged();
                dataModel.fetchPreSearch(GoodsListActivity.this.filter, Intenthandler, true);
                menu.toggle();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void changeLayout() {
        if (type == 1) {
            type = 3;
            goodlistView.setAdapter(listAdapter3);
            searchFilter.setBackgroundResource(R.drawable.goodlist_choose3);
        } else if (type == 3) {
            type = 1;
            goodlistView.setAdapter(listAdapter1);
            searchFilter.setBackgroundResource(R.drawable.goodlist_choose1);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt("goodlist_type", type);
        editor.commit();
    }
}

