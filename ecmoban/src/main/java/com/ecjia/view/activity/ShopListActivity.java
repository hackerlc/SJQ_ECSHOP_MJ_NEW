package com.ecjia.view.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.FAVOUR;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.MENU_BUTTON;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.PRICE_RANGE;
import com.ecjia.entity.responsemodel.SHOPDATA;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.network.netmodle.ShopDetialModel;
import com.ecjia.network.query.ShopQuery;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.adapter.GoodsListAdapter;
import com.ecjia.view.adapter.NewGoodlistAdapter;
import com.ecjia.view.adapter.ShopDetailFavoursAdapter;
import com.ecjia.view.adapter.ShopMenuAdapter;
import com.ecjia.widgets.MyListView;
import com.ecjia.widgets.PopMenus;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/*
店铺详情
 */
public class ShopListActivity extends NewBaseActivity implements IXListViewListener, OnClickListener, HttpResponse {
    private ImageView backImageButton;
    private ImageView shopping_cart, iv_collect;
    private TextView top_view_text;
    private FrameLayout top_view;
    private FrameLayout top_view_bg;
    private XListView goodlistView;
    private ShopDetialModel dataModel;
    private NewGoodlistAdapter listAdapter1;
    private GoodsListAdapter listAdapter3;

//    private ImageView bg;
    private boolean selfrefresh;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private View null_pager;
    private SellerModel sellerModel;

    public static final int IS_HOT = 0;
    public static final int PRICE_DESC_INT = 1;
    public static final int PRICE_ASC_INT = 2;
    public static final int IS_NEW = -1;

    public static final int IS_FILTER = 3;

    public String predefine_category_id;
    private ExpandableListView menulist;
    private LinearLayout nofilter, resetfilter;
    private RelativeLayout tabfour;
    private static LinearLayout shopping_cart_num_bg_one;
    private static TextView shopping_cart_num;
    public int type = 3;
    boolean First = true;
    public boolean isFirst = false;
    private LinearLayout ll_head_top, ll_top_guide;
    private int follower;
    private String isfollow;
    private String sellerlogo;
    private String sellername;
    private TextView shopColloct, tv_collect;
    private String uid;
    private boolean NowIsNew, NowIsHot;
    private SHOPDATA sendData;
    private TextView reset;

    FILTER filter;

    private LinearLayout search;
    private ImageView searchFilter, prosearchFilter, iv_enter;
    private ToastView toast;
    private View one, two, three;// 底部三条线
    private View proone, protwo, prothree;// 底部三条线
    SlidingMenu menu;
    int screenWidth;
    private ShopMenuAdapter menuadapter;
    private ArrayList<CATEGORY> catlist;
    TextView fliterfinish;
    boolean t1 = false, t2 = false, t3 = false;
    private LinearLayout ll_head_all;
    private RelativeLayout rl_shop_collect;
    private LinearLayout top_category;

    private ImageView shopImg;
    private TextView shopName;

    private String sellerid;
    private View headView1;
    private LinearLayout stick_toolbar;
    private TextView tv_goodsscore, tv_logisticsscore, tv_servicescore, tv_company_area, tv_company_notice;
    private LinearLayout ll_company_phone;

    TitleCellHolder tabOneCellHolder;
    TitleCellHolder tabTwoCellHolder;
    TitleCellHolder tabThreeCellHolder;

    TitleCellHolder proOneCellHolder;
    TitleCellHolder proTwoCellHolder;
    TitleCellHolder proThreeCellHolder;
    private ImageView headview_bg;
    private MyDialog mDialog;
    private String company_phone;
    private boolean topFlag;
    private float progress = 0f;
    private boolean scrollFlag;
    private int lastVisibleItemPosition;
    private LinearLayout layout_custommenu;
    private PopMenus popupWindow_custommenu;
    private LinearLayout ll_favour_item;
    private TextView tv_first_favour_name, tv_first_favour_content;
    private ImageView iv_favour_more;
    private MyListView mlv_favour;
    private ShopDetailFavoursAdapter shopDetailFavoursAdapter;
    private boolean favourOpenFlag;
    private TextView shopDistance;
    private boolean hasFollwerChange;

    protected class TitleCellHolder {
        public TextView titleTextView;
        public ImageView orderIconImageView;
        public RelativeLayout tabRelative;
    }


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail_new);
        headView1 = View.inflate(this, R.layout.layout_shop_info, null);
        layout_custommenu = (LinearLayout) findViewById(R.id.layout_custommenu);
        initHeadView();
        stick_toolbar = (LinearLayout) findViewById(R.id.stick_toolbar);
        stick_toolbar.setVisibility(View.GONE);
        Intent intent = getIntent();
        sellerid = intent.getStringExtra(IntentKeywords.MERCHANT_ID);
        goodlistView = (XListView) findViewById(R.id.goods_listview);
        goodlistView.addHeaderView(headView1);
        goodlistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i2, int i3) {
                if (firstVisibleItem > 1) {
                    stick_toolbar.setVisibility(View.VISIBLE);
                } else {

                    stick_toolbar.setVisibility(View.GONE);
                }
            }
        });
        prosearchFilter = (ImageView) findViewById(R.id.search_filter2);
        tabfour = (RelativeLayout) findViewById(R.id.tabfour2);
        if (null == sellerModel) {
            sellerModel = new SellerModel(this);
        }
        sellerModel.addResponseListener(this);
        sellerModel.getMerchantHomeData(sellerid, AppConst.LNG_LAT[1] + "", AppConst.LNG_LAT[0] + "");
        top_category = (LinearLayout) findViewById(R.id.ll_top_category);
        top_category.setOnClickListener(this);
        headview_bg = (ImageView) headView1.findViewById(R.id.headview_bg);
        setBg(headview_bg);
        ll_top_guide = (LinearLayout) findViewById(R.id.ll_top_guide);
        ll_top_guide.setOnClickListener(this);
        search = (LinearLayout) findViewById(R.id.search_search);
        search.setOnClickListener(this);
        filter = new FILTER();
        String category = intent.getStringExtra(IntentKeywords.CATEGORY_ID);
        if (!TextUtils.isEmpty(category)) {
            filter.setCategory_id(category);
        }
        this.filter.setSort_by(dataModel.PRICE_DESC);
        if (!TextUtils.isEmpty(filter.getCategory_id())) {
            predefine_category_id = filter.getCategory_id();
            LG.i("predefine_category_id====" + predefine_category_id);
        }
        searchFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });
        tabfour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });
        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);

        top_view = (FrameLayout) findViewById(R.id.top_view);
        top_view_bg = (FrameLayout) findViewById(R.id.top_view_bg);
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        backImageButton = (ImageView) findViewById(R.id.nav_back_button);
        backImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (hasFollwerChange) {
                    Intent intent = new Intent();
                    intent.putExtra("collect_num", follower + "");
                    setResult(RESULT_OK, intent);
                }
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        shopping_cart = (ImageView) findViewById(R.id.good_list_shopping_cart);
        shopping_cart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
                if (uid.equals("")) {
                    Intent intent = new Intent(ShopListActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                    String nol = getResources().getString(R.string.no_login);
                    ToastView toast = new ToastView(ShopListActivity.this, nol);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent it = new Intent(ShopListActivity.this, ShoppingCartActivity.class);
                    startActivity(it);
                }
            }
        });

        shopping_cart_num = (TextView) findViewById(R.id.shopping_cart_num);
        shopping_cart_num_bg_one = (LinearLayout) findViewById(R.id.shopping_cart_num_bg_one);
        null_pager = findViewById(R.id.null_pager);
        goodlistView.setPullLoadEnable(false);
        goodlistView.setPullRefreshEnable(false);
        goodlistView.setRefreshTime();
        goodlistView.setXListViewListener(this, 1);
        addListener();
        dataModel = new ShopDetialModel(this);
        dataModel.addResponseListener(this);
        loadCategory();
        if (null == listAdapter1) {
            listAdapter1 = new NewGoodlistAdapter(ShopListActivity.this, dataModel.simplegoodsList);
        }
        if (null == listAdapter3) {
            listAdapter3 = new GoodsListAdapter(ShopListActivity.this, dataModel.simplegoodsList);
        }
        isFirst = true;
        if (isFirst == true) {
            if (type == 1) {
                searchFilter.setImageResource(R.drawable.shoplist_choose1);
                prosearchFilter.setImageResource(R.drawable.shoplist_choose1);
                goodlistView.setAdapter(listAdapter1);
            } else if (type == 3) {
                searchFilter.setImageResource(R.drawable.shoplist_choose3);
                prosearchFilter.setImageResource(R.drawable.shoplist_choose3);
                goodlistView.setAdapter(listAdapter3);
            }
            isFirst = false;
        }
        selectedTab(IS_NEW);
//        initMenuRight();
    }

    ShopQuery mShopQuery = new ShopQuery();
    private void loadCategory(){
        RetrofitAPIManager.getAPIShop()
                .getCategory(mShopQuery.getFollow(sellerid))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .subscribe(d -> showCategory(d), e -> showError(e));
    }

    int page = 1;
    private void loadGoods(){
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIShop()
                .getGoodsList(mShopQuery.getGoods(filter, sellerid, page))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResultJsonRet())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> {pageHandler(d.getPaginated());
                    showGoods(d.getData());}, e -> showError(e));
    }

    public void showGoods(ArrayList<SIMPLEGOODS> data){
        if (First) {
            First = false;
        }
        goodlistView.stopRefresh();
        goodlistView.stopLoadMore();
        goodlistView.setRefreshTime();
        if (data.size() == 0) {
            ToastUtil.getInstance().makeLongToast(this,"暂无商品");
            goodlistView.setVisibility(View.VISIBLE);
            null_pager.setVisibility(View.VISIBLE);
            listAdapter1.notifyDataSetChanged();
            dataModel.simplegoodsList.clear();
            if (listAdapter1 != null) {
                listAdapter1.notifyDataSetChanged();
            }
            if (listAdapter3 != null) {
                listAdapter3.notifyDataSetChanged();
            }
        } else {
            goodlistView.setVisibility(View.VISIBLE);
            null_pager.setVisibility(View.GONE);
            if(page == 1){
                dataModel.simplegoodsList.clear();
            }
            dataModel.simplegoodsList.addAll(data);
            if (type == 3) {
                listAdapter3.notifyDataSetChanged();
            } else if (type == 1) {
                listAdapter1.notifyDataSetChanged();
            }
        }
        setContent();
        page++;
    }

    private void pageHandler(BaseRes.Paginated p) {
        if (p.getMore() == 0) {
            goodlistView.setPullLoadEnable(false);
        } else {
            goodlistView.setPullLoadEnable(true);
        }
    }

    public void showCategory(ArrayList<CATEGORY> data){
        t3 = true;
        if (null == catlist) {
            catlist = new ArrayList();  // 分类数据
        }
        if (data.size() > 0) {
            catlist = data;
            if (AppConst.categorylist == null
                    || AppConst.categorylist.size() == 0) {
                AppConst.categorylist = new ArrayList();
            }

            AppConst.categorylist.clear();
            for (int k = 0; k < catlist.size(); k++) {
                HashMap<String, Boolean> map = new HashMap<String, Boolean>();
                if (!TextUtils.isEmpty(predefine_category_id)) {
                    if (predefine_category_id.equals(catlist.get(k).getId() + "")) {
                        map.put("flag", true);
                    } else {
                        map.put("flag", false);
                    }
                } else {
                    map.put("flag", false);
                }
                AppConst.categorylist.add(map);
            }

        }
        t1 = true;
        t2 = true;
        if (menuadapter != null) {
            menuadapter.notifyDataSetChanged();
        }
    }

    public void showError(Throwable e){
        ToastUtil.getInstance().makeLongToast(this,e.getMessage());
    }

    private void setBg(ImageView headview_bg) {
        int width = getDisplayMetricsWidth();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width * 1 / 2);
        headview_bg.setLayoutParams(lp);
    }

    public int getDisplayMetricsWidth() {
        int i = getWindowManager().getDefaultDisplay().getWidth();
        int j = getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    private void addListener() {
        goodlistView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        scrollFlag = false;

                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
                        scrollFlag = true;
                        if (goodlistView.mHeaderView.getVisiableHeight() > 0) {
                            stick_toolbar.setVisibility(View.GONE);
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        scrollFlag = false;
                        break;
                }
            }

            /**
             * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (scrollFlag) {
                    lastVisibleItemPosition = firstVisibleItem;
                }

                int lHeight = ll_head_all.getMeasuredHeight()
                        + stick_toolbar.getMeasuredHeight() - top_view.getMeasuredHeight();
                int t = getScrollY();
                if (t == 0) {
                    top_view_bg.setAlpha(0);
                    top_view_text.setAlpha(0);
                    stick_toolbar.setVisibility(View.GONE);
                } else {
                    if (t - lHeight < lHeight) {//=
                        progress = new Float(t - lHeight) / new Float(lHeight);//255
                        top_view_bg.setAlpha(progress);
                        top_view_text.setAlpha(progress);
                        stick_toolbar.setVisibility(View.GONE);
                    } else {
                        top_view_bg.setAlpha(1);
                        top_view_text.setAlpha(1);
                        stick_toolbar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        if (url.equals(ProtocolConst.SHOPDETIAL)) {
            if (status.getSucceed() == 1) {
                if (First) {
                    First = false;
                }
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
        } else if (url.equals(ProtocolConst.SELLER_COLLECTCREATE)) {
            if (status.getSucceed() == 1) {
                ToastView toastView = new ToastView(this, getResources().getString(R.string.collection_success));
                toastView.show();
                rl_shop_collect.setBackgroundResource(R.drawable.shape_shopcollect);
                tv_collect.setTextColor(getResources().getColor(R.color.white));
                iv_collect.setImageResource(R.drawable.shop_collected);
                tv_collect.setText(getResources().getString(R.string.shop_collected));
                isfollow = "1";
                follower = follower + 1;
                sendData.setFollower(follower);
                sendData.setIs_follower("1");
                shopColloct.setText(follower + getResources().getString(R.string.follower_num));
                EventBus.getDefault().post(new MyEvent("collectrefresh"));
                hasFollwerChange = true;
            }
        } else if (url.equals(ProtocolConst.SELLER_COLLECTDELETE)) {
            if (status.getSucceed() == 1) {
                ToastView toastView = new ToastView(this, getResources().getString(R.string.del_collection_success));
                toastView.show();
                rl_shop_collect.setBackgroundResource(R.drawable.shape_shopuncollect);
                tv_collect.setTextColor(getResources().getColor(R.color.normal_dark_gray));
                iv_collect.setImageResource(R.drawable.shop_uncollect);
                tv_collect.setText(getResources().getString(R.string.shop_uncollected));
                isfollow = "0";
                follower = follower - 1;
                sendData.setFollower(follower);
                sendData.setIs_follower("0");
                shopColloct.setText(follower + getResources().getString(R.string.follower_num));
                EventBus.getDefault().post(new MyEvent("collectrefresh"));
                hasFollwerChange = true;
            }
        } else if (url.equals(ProtocolConst.MERCHAT_HOMEDATA)) {
            if (status.getSucceed() == 1) {
                String no_data = getResources().getString(R.string.data_null);
                isfollow = sellerModel.shopHomeData.getIs_follower();
                if ("0".equals(isfollow)) {
                    rl_shop_collect.setBackgroundResource(R.drawable.shape_shopuncollect);
                    tv_collect.setTextColor(getResources().getColor(R.color.normal_dark_gray));
                    iv_collect.setImageResource(R.drawable.shop_uncollect);
                    tv_collect.setText(getResources().getString(R.string.shop_uncollected));
                } else {
                    rl_shop_collect.setBackgroundResource(R.drawable.shape_shopcollect);
                    tv_collect.setTextColor(getResources().getColor(R.color.white));
                    iv_collect.setImageResource(R.drawable.shop_collected);
                    tv_collect.setText(getResources().getString(R.string.shop_collected));
                }
                sendData = sellerModel.shopHomeData;
                sellerlogo = sellerModel.shopHomeData.getSeller_logo();
                sellername = sellerModel.shopHomeData.getSeller_name();
                follower = sellerModel.shopHomeData.getFollower();
                company_phone = sellerModel.shopHomeData.getTelephone();

                Glide.with(this).load(sellerlogo).bitmapTransform(new CropCircleTransformation(this)).crossFade(1000).into(shopImg);
//                Glide.with(this).load(sellerModel.shopHomeData.getSeller_banner()).into(headview_bg);
//                ImageLoaderForLV.getInstance(this).setImageRes(shopImg, sellerlogo);
                ImageLoaderForLV.getInstance(this).setImageRes(headview_bg, sellerModel.shopHomeData.getSeller_banner());
                shopName.setText(sellername);
                top_view_text.setText(sellername);
                shopColloct.setVisibility(View.VISIBLE);
                shopColloct.setText(follower + getResources().getString(R.string.follower_num));

                float distance = FormatUtil.formatStrToFloat(sellerModel.shopHomeData.getDistance());
                if (0 == distance) {
                    shopDistance.setText("");
                    shopDistance.setVisibility(View.GONE);
                } else {
                    shopDistance.setVisibility(View.VISIBLE);
                    if (distance < 1000) {
                        shopDistance.setText((int) distance + "m");
                    } else {
                        shopDistance.setText(FormatUtil.formatFloatTwoDecimal(distance / 1000) + "km");
                    }

                }

                if (TextUtils.isEmpty(sellerModel.shopHomeData.getComment().getComment_goods())) {
                } else {
                    tv_goodsscore.setText(sellerModel.shopHomeData.getComment().getComment_goods());
                }

                if (TextUtils.isEmpty(sellerModel.shopHomeData.getComment().getComment_delivery())) {
                } else {
                    tv_logisticsscore.setText(sellerModel.shopHomeData.getComment().getComment_delivery());
                }

                if (TextUtils.isEmpty(sellerModel.shopHomeData.getComment().getComment_server())) {
                } else {
                    tv_servicescore.setText(sellerModel.shopHomeData.getComment().getComment_server());
                }

                if (TextUtils.isEmpty(sellerModel.shopHomeData.getShop_address())) {
                    tv_company_area.setText(no_data);
                } else {
                    tv_company_area.setText(sellerModel.shopHomeData.getShop_address());
                }

                if (TextUtils.isEmpty(sellerModel.shopHomeData.getSeller_description())) {
                    tv_company_notice.setText(no_data);
                } else {
                    tv_company_notice.setText(sellerModel.shopHomeData.getSeller_description());
                }
            }

            setCustomMenu(sellerModel.shopHomeData.getMenu_button());

            int size = sellerModel.shopHomeData.getFavours().size();
            if (size > 0) {
                ll_favour_item.setVisibility(View.VISIBLE);
                tv_first_favour_name.setText(sellerModel.shopHomeData.getFavours().get(0).getType_label());
                tv_first_favour_content.setText(sellerModel.shopHomeData.getFavours().get(0).getName());
                if (size > 1) {
                    mlv_favour.setVisibility(View.VISIBLE);
                    iv_favour_more.setVisibility(View.VISIBLE);

                    ArrayList<FAVOUR> list = new ArrayList<>();
                    for (int i = 1; i < sellerModel.shopHomeData.getFavours().size(); i++) {
                        list.add(sellerModel.shopHomeData.getFavours().get(i));
                    }
                    shopDetailFavoursAdapter = new ShopDetailFavoursAdapter(ShopListActivity.this, list);
                    mlv_favour.setAdapter(shopDetailFavoursAdapter);
                } else {
                    mlv_favour.setVisibility(View.GONE);
                    iv_favour_more.setVisibility(View.INVISIBLE);
                }
            } else {
                ll_favour_item.setVisibility(View.GONE);
            }

        }
    }

    private void setCustomMenu(ArrayList<MENU_BUTTON> menu_button) {
        if (menu_button != null && menu_button.size() > 0) {
            layout_custommenu.setVisibility(View.VISIBLE);
            layout_custommenu.removeAllViews();
            int size = menu_button.size();
            size = size > 3 ? 3 : size;
            for (int i = 0; i < size; i++) {

                final MENU_BUTTON ob = menu_button.get(i);
                LinearLayout layout = (LinearLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_custommenu, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                layout.setLayoutParams(lp);
                TextView tv_custommenu_name = (TextView) layout.findViewById(R.id.tv_custommenu_name);
                ImageView iv_custommenu = (ImageView) layout.findViewById(R.id.iv_custommenu);
                tv_custommenu_name.setText(ob.getName());
                if (ob.getSub_button().size() > 0) // 显示三角
                {
                    iv_custommenu.setVisibility(View.VISIBLE);
                } else // 隐藏三角
                {
                    iv_custommenu.setVisibility(View.GONE);
                }
                layout.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (ob.getSub_button().size() == 0) {
                            ECJiaOpenType.getDefault().startAction(ShopListActivity.this, ob.getUrl());
                        } else {
                            popupWindow_custommenu = new PopMenus(ShopListActivity.this, ob.getSub_button(), v.getWidth() - 20, 0);
                            popupWindow_custommenu.showAtLocation(v);
                        }
                    }
                });
                layout_custommenu.addView(layout);
            }
        } else {
            layout_custommenu.setVisibility(View.GONE);
        }
    }

    public int getScrollY() {
        View c = goodlistView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = goodlistView.getFirstVisiblePosition();
        int top = c.getTop();
        if (firstVisiblePosition > 1) {
            return 5000;
        } else {
            return -top + firstVisiblePosition * c.getHeight();
        }
    }


    void selectedTab(int index) {
        page = 1;
        ColorStateList selectedTextColor = getResources().getColorStateList(R.color.filter_text_color);
        if (index == IS_NEW) {
            NowIsNew = true;
            NowIsHot = false;

            tabOneCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_active_arrow);
            tabOneCellHolder.orderIconImageView.setWillNotCacheDrawing(true);

            tabOneCellHolder.titleTextView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);
            tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            proOneCellHolder.titleTextView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            proTwoCellHolder.titleTextView.setTextColor(selectedTextColor);
            proThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            filter.setSort_by(dataModel.IS_NEW);

            loadGoods();
            one.setVisibility(View.VISIBLE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.GONE);

            proone.setVisibility(View.VISIBLE);
            protwo.setVisibility(View.GONE);
            prothree.setVisibility(View.GONE);

        } else if (index == IS_HOT) {
            NowIsNew = false;
            NowIsHot = true;
            tabTwoCellHolder.orderIconImageView.setImageResource(R.drawable.item_grid_filter_down_active_arrow);

            tabTwoCellHolder.titleTextView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);
            tabThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            proTwoCellHolder.titleTextView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            proOneCellHolder.titleTextView.setTextColor(selectedTextColor);
            proThreeCellHolder.titleTextView.setTextColor(selectedTextColor);

            filter.setSort_by(dataModel.IS_HOT);
            loadGoods();
            one.setVisibility(View.GONE);
            two.setVisibility(View.VISIBLE);
            three.setVisibility(View.GONE);

            proone.setVisibility(View.GONE);
            protwo.setVisibility(View.VISIBLE);
            prothree.setVisibility(View.GONE);

        } else if (index == PRICE_DESC_INT) {
            NowIsNew = false;
            NowIsHot = false;

            tabThreeCellHolder.titleTextView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            tabOneCellHolder.titleTextView.setTextColor(selectedTextColor);
            tabTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

            proThreeCellHolder.titleTextView.setTextColor(getResources().getColor(R.color.public_theme_color_normal));
            proOneCellHolder.titleTextView.setTextColor(selectedTextColor);
            proTwoCellHolder.titleTextView.setTextColor(selectedTextColor);

            if (filter.getSort_by().equals("price_asc")) {
                filter.setSort_by(dataModel.PRICE_DESC);
                tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_buttom);
                proThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_buttom);
            } else if (filter.getSort_by().equals("price_desc")) {
                filter.setSort_by(dataModel.PRICE_ASC);
                tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_top);
                proThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_top);
            } else {
                filter.setSort_by(dataModel.PRICE_DESC);
                tabThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_buttom);
                proThreeCellHolder.orderIconImageView.setImageResource(R.drawable.goodlist_buttom);
            }
            loadGoods();
            one.setVisibility(View.GONE);
            two.setVisibility(View.GONE);
            three.setVisibility(View.VISIBLE);

            proone.setVisibility(View.GONE);
            protwo.setVisibility(View.GONE);
            prothree.setVisibility(View.VISIBLE);
        }
    }

    public void setContent() {
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
        switch (v.getId()) {
            case R.id.search_search:
                Intent intent = new Intent(ShopListActivity.this, SearchSellerGoodsActivity.class);
                intent.putExtra("sellergood", true);
                intent.putExtra("sellerid", sellerid);
                startActivity(intent);
                break;
            case R.id.ll_top_guide:
                String name = sellerModel.shopHomeData.getShop_address();
                String distance = sellerModel.shopHomeData.getLocation().getDistance();
                String lat = sellerModel.shopHomeData.getLocation().getLatitude();
                String lng = sellerModel.shopHomeData.getLocation().getLongitude();

                Intent intent2 = new Intent(ShopListActivity.this, MapActivity.class);
                intent2.putExtra(IntentKeywords.MAP_ISGUIDE, true);
                intent2.putExtra(IntentKeywords.MAP_NAME, name);
                intent2.putExtra(IntentKeywords.MAP_DISTANCE, distance);
                intent2.putExtra(IntentKeywords.MAP_LAT, lat);
                intent2.putExtra(IntentKeywords.MAP_LNG, lng);
                startActivity(intent2);

                break;
            case R.id.iv_favour_more:
                if (favourOpenFlag) {
                    favourOpenFlag = false;
                    iv_favour_more.setImageResource(R.drawable.arrow_collect_down);
                    mlv_favour.setVisibility(View.VISIBLE);
                } else {
                    favourOpenFlag = true;
                    iv_favour_more.setImageResource(R.drawable.arrow_collect_up);
                    mlv_favour.setVisibility(View.GONE);
                }
                break;
            case R.id.ll_company_phone:
                final String number = company_phone;
                String call = getResources().getString(R.string.setting_call_or_not);
                if (!TextUtils.isEmpty(mApp.getConfig().getService_phone())) {
                    mDialog = new MyDialog(ShopListActivity.this, call, mApp.getConfig().getService_phone());
                    mDialog.show();
                    mDialog.positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mApp.getConfig().getService_phone()));
                            startActivity(intent);
                        }
                    });
                    mDialog.negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.rl_shop_collect:
                uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
                if ("".equals(uid)) {
                    Intent intent1 = new Intent(ShopListActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    if ("0".equals(isfollow)) {
                        sellerModel.sellerCollectCreate(sellerid);
                        EventBus.getDefault().post(new MyEvent("add_collect_seller", sellerid));
                    } else {
                        sellerModel.sellerCollectDelete(sellerid);
                        EventBus.getDefault().post(new MyEvent("minus_collect_seller", sellerid));
                    }
                }

                break;
            case R.id.ll_top_category:
                if (t1 & t2 & t3) {
                    if (catlist.size() > 0) {
                        if (menuadapter == null) {
                            String brand = getResources().getString(R.string.goodlist_brand);
                            String classify = getResources().getString(R.string.goodlist_classify);
                            String price = getResources().getString(R.string.goodlist_price);
                            String[] menuItemsName = new String[]{brand, classify, price};
                            menuadapter = new ShopMenuAdapter(ShopListActivity.this,
                                    menuItemsName, predefine_category_id,
                                    dataModel, catlist);
                            menulist.setAdapter(menuadapter);
                            menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                            for (int i = 0; i < menuadapter.getGroupCount(); i++) {
                                menulist.expandGroup(i);
                            }
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
                    ToastView toast = new ToastView(ShopListActivity.this, "网络问题");
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (hasFollwerChange) {
                    Intent intent = new Intent();
                    intent.putExtra("collect_num", follower + "");
                    setResult(RESULT_OK, intent);
                }
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRefresh(int id) {
        page = 1;
        loadGoods();
    }

    @Override
    public void onLoadMore(int id) {
        loadGoods();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (null != data) {
                    String filter_string = data.getStringExtra("filter");
                    if (null != filter_string) {
                        try {
                            JSONObject filterJSONObject = new JSONObject(filter_string);
                            FILTER filter = FILTER.fromJson(filterJSONObject);
                            this.filter.setCategory_id(filter.getCategory_id());
                            this.filter.setPrice_range(filter.getPrice_range());
                            this.filter.setBrand_id(filter.getBrand_id());
                            dataModel.fetchGoodList(filter, sellerid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            case 2: {
                sellerModel.getMerchantHomeData(sellerid, AppConst.LNG_LAT[1] + "", AppConst.LNG_LAT[0] + "");
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selfrefresh = false;
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
        menulist = (ExpandableListView) menu.findViewById(R.id.goodlist_filterlist);

        resetfilter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.goodlist_menu_foot, null);
        reset = (TextView) resetfilter.findViewById(R.id.menu_reset);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        menulist.addFooterView(resetfilter);
        nofilter = (LinearLayout) menu.findViewById(R.id.goodlist_null);
        fliterfinish = (TextView) menu.findViewById(R.id.filter_finish);
        menulist.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    for (int i = 0; i < AppConst.brandlist.size(); i++) {
                        if (childPosition == i) {
                            AppConst.brandlist.get(i).put("flag", true);
                            filter.setBrand_id(dataModel.brandList.get(childPosition).getBrand_id() + "");
                        } else {
                            AppConst.brandlist.get(i).put("flag", false);
                        }
                    }
                } else if (groupPosition == 1) {
                    for (int i = 0; i < AppConst.categorylist.size(); i++) {
                        if (i == childPosition) {
                            AppConst.categorylist.get(i).put("flag", true);
                            filter.setCategory_id(catlist.get(childPosition).getId() + "");
                        } else {
                            AppConst.categorylist.get(i).put("flag", false);
                        }
                    }
                } else if (groupPosition == 2) {

                    for (int i = 0; i < AppConst.pricelist.size(); i++) {
                        if (childPosition == i) {
                            AppConst.pricelist.get(i).put("flag", true);
                            PRICE_RANGE price_range = dataModel.priceRangeArrayList.get(childPosition);
                            filter.setPrice_range(price_range);
                        } else {
                            AppConst.pricelist.get(i).put("flag", false);
                        }
                    }
                }
                menuadapter.notifyDataSetChanged();
                dataModel.fetchGoodList(ShopListActivity.this.filter, sellerid);
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
                predefine_category_id = "";
                filter.setBrand_id("");
                filter.setCategory_id("");
                filter.setPrice_range(null);
                menuadapter.notifyDataSetChanged();
                dataModel.fetchGoodList(ShopListActivity.this.filter, sellerid);
                menu.toggle();
            }
        });

    }

    public void changeLayout() {
        if (type == 1) {
            type = 3;
            goodlistView.setAdapter(listAdapter3);
            searchFilter.setImageResource(R.drawable.shoplist_choose3);
            prosearchFilter.setImageResource(R.drawable.shoplist_choose3);
        } else if (type == 3) {
            type = 1;
            goodlistView.setAdapter(listAdapter1);
            searchFilter.setImageResource(R.drawable.shoplist_choose1);
            prosearchFilter.setImageResource(R.drawable.shoplist_choose1);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initHeadView() {
        shopImg = (ImageView) headView1.findViewById(R.id.shopdetail_shop_img);
        iv_collect = (ImageView) headView1.findViewById(R.id.iv_collect);
        tv_collect = (TextView) headView1.findViewById(R.id.tv_collect);
        shopDistance = (TextView) headView1.findViewById(R.id.shopdetail_shop_distance);
        shopName = (TextView) headView1.findViewById(R.id.shopdetail_shop_name);
        shopColloct = (TextView) headView1.findViewById(R.id.shopdetail_shop_colloct);
        ll_head_all = (LinearLayout) headView1.findViewById(R.id.ll_head_all);
        ll_head_top = (LinearLayout) headView1.findViewById(R.id.ll_head_top);
        rl_shop_collect = (RelativeLayout) headView1.findViewById(R.id.rl_shop_collect);
        rl_shop_collect.setOnClickListener(this);

        tv_goodsscore = (TextView) headView1.findViewById(R.id.tv_goodsscore);
        tv_logisticsscore = (TextView) headView1.findViewById(R.id.tv_logisticsscore);
        tv_servicescore = (TextView) headView1.findViewById(R.id.tv_servicescore);
        tv_company_area = (TextView) headView1.findViewById(R.id.tv_company_area);
        tv_company_notice = (TextView) headView1.findViewById(R.id.tv_company_notice);
        ll_company_phone = (LinearLayout) headView1.findViewById(R.id.ll_company_phone);
        ll_company_phone.setOnClickListener(this);

        ll_favour_item = (LinearLayout) headView1.findViewById(R.id.ll_favour_item);
        tv_first_favour_name = (TextView) headView1.findViewById(R.id.tv_first_favour_name);
        tv_first_favour_content = (TextView) headView1.findViewById(R.id.tv_first_favour_content);
        iv_favour_more = (ImageView) headView1.findViewById(R.id.iv_favour_more);
        iv_favour_more.setOnClickListener(this);
        mlv_favour = (MyListView) headView1.findViewById(R.id.mlv_favour);

        searchFilter = (ImageView) headView1.findViewById(R.id.search_filter);

        //筛选标题
        one = headView1.findViewById(R.id.filter_one);
        two = headView1.findViewById(R.id.filter_two);
        three = headView1.findViewById(R.id.filter_three);
        proone = findViewById(R.id.filter_one2);
        protwo = findViewById(R.id.filter_two2);
        prothree = findViewById(R.id.filter_three2);
        tabOneCellHolder = new TitleCellHolder();
        tabTwoCellHolder = new TitleCellHolder();
        tabThreeCellHolder = new TitleCellHolder();
        proOneCellHolder = new TitleCellHolder();
        proTwoCellHolder = new TitleCellHolder();
        proThreeCellHolder = new TitleCellHolder();

        tabOneCellHolder.titleTextView = (TextView) headView1.findViewById(R.id.filter_title_tabone);
        tabOneCellHolder.orderIconImageView = (ImageView) headView1.findViewById(R.id.filter_order_tabone);
        tabOneCellHolder.tabRelative = (RelativeLayout) headView1.findViewById(R.id.tabOne);

        proOneCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabone2);
        proOneCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabOne2);

        tabOneCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!NowIsNew) {
                            selectedTab(IS_NEW);
                        }
                    }
                });

        proOneCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!NowIsNew) {
                            selectedTab(IS_NEW);
                        }
                    }
                });

        tabTwoCellHolder.titleTextView = (TextView) headView1.findViewById(R.id.filter_title_tabtwo);
        tabTwoCellHolder.orderIconImageView = (ImageView) headView1.findViewById(R.id.filter_order_tabtwo);
        tabTwoCellHolder.tabRelative = (RelativeLayout) headView1.findViewById(R.id.tabTwo);

        proTwoCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabtwo2);
        proTwoCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabTwo2);

        tabTwoCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!NowIsHot) {
                            selectedTab(IS_HOT);
                        }
                    }
                });

        proTwoCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!NowIsHot) {
                            selectedTab(IS_HOT);
                        }
                    }
                });

        tabThreeCellHolder.titleTextView = (TextView) headView1.findViewById(R.id.filter_title_tabthree);
        tabThreeCellHolder.orderIconImageView = (ImageView) headView1.findViewById(R.id.filter_order_tabthree);
        tabThreeCellHolder.tabRelative = (RelativeLayout) headView1.findViewById(R.id.tabThree);

        proThreeCellHolder.titleTextView = (TextView) findViewById(R.id.filter_title_tabthree2);
        proThreeCellHolder.orderIconImageView = (ImageView) findViewById(R.id.filter_order_tabthree2);
        proThreeCellHolder.tabRelative = (RelativeLayout) findViewById(R.id.tabThree2);

        tabThreeCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTab(PRICE_DESC_INT);

                    }
                });
        proThreeCellHolder.tabRelative
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedTab(PRICE_DESC_INT);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}

