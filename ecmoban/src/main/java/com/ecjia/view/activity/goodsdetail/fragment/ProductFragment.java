package com.ecjia.view.activity.goodsdetail.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.consts.AppConst;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.model.AddressData;
import com.ecjia.entity.responsemodel.CITY;
import com.ecjia.entity.responsemodel.FAVOUR;
import com.ecjia.entity.responsemodel.GOODS;
import com.ecjia.entity.responsemodel.GOODSPRICELADDER;
import com.ecjia.entity.responsemodel.GOODSVOLUMEPRICE;
import com.ecjia.entity.responsemodel.MERCHANTINFO;
import com.ecjia.entity.responsemodel.PHOTO;
import com.ecjia.entity.responsemodel.PRODUCTNUMBYCF;
import com.ecjia.entity.responsemodel.SHOOPSERVICES;
import com.ecjia.entity.responsemodel.SPECIFICATION;
import com.ecjia.entity.responsemodel.SPECIFICATION_VALUE;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.network.netmodle.CommentModel;
import com.ecjia.network.netmodle.GoodsDetailModel;
import com.ecjia.network.netmodle.ShoppingCartModel;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FormatUtil;
import com.ecjia.util.LG;
import com.ecjia.util.SaveBrowseNew;
import com.ecjia.view.activity.Address2Activity;
import com.ecjia.view.activity.AddressAddActivity;
import com.ecjia.view.activity.FullScreenViewPagerActivity;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GroupHelpWebActivity;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.view.activity.ecchat.ECChatActivity;
import com.ecjia.view.activity.goodsdetail.ChooseGoodSpecificationActivity_Builder;
import com.ecjia.view.activity.goodsdetail.fragment.interfaces.BottomShowShopDiscountDialog;
import com.ecjia.view.activity.goodsdetail.model.COMMENT_LIST;
import com.ecjia.view.activity.goodsdetail.model.CommentType;
import com.ecjia.view.activity.goodsdetail.view.BottomElasticScrollView;
import com.ecjia.view.activity.goodsdetail.view.GoodsCommetView;
import com.ecjia.view.activity.goodsdetail.view.OnScrollViewChangeListener;
import com.ecjia.view.activity.goodsdetail.view.ScrollViewWrapper;
import com.ecjia.view.activity.goodsdetail.view.TopElasticScrollView;
import com.ecjia.view.activity.goodsorder.balance.NewBalanceActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.activity.user.UserApplyMenberActivity;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.ecjia.view.adapter.GoodPropertyAdapter;
import com.ecjia.view.adapter.Sqlcl;
import com.ecjia.widgets.FlowLayout;
import com.ecjia.widgets.MySeekBar;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.ToastView_ForReceiveRedpaper;
import com.ecjia.widgets.banner.BannerConfig;
import com.ecjia.widgets.banner.BannerView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.timecount.CountDownNewRedView;
import com.ecjia.widgets.timecount.CountDownNewView;
import com.ecmoban.android.sijiqing.R;
import com.hyphenate.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * 商品详情的商品介绍页
 */
public class ProductFragment extends GoodsDetailBaseFragment implements HttpResponse, View.OnClickListener {

    BannerView<PHOTO> detailBanner;
    public GoodsDetailModel dataModel;
    private OnScrollTabChangeListener listener = null;
    boolean issave = false;// 商品是否保存到浏览记录

    View headerView;
    View footerView;
    private TextView goodBriefTextView;
    private LinearLayout ll_mobile_buy;
    private TextView tv_mobile_discount;
    public ScrollViewWrapper sc;
    private TextView load_text;
    boolean isFirst = true;//第一次进入下拉刷新
    private LinearLayout ly_coupons;//优惠券布局页面
    private LinearLayout ly_discount;//[店铺活动]布局页面
    private TextView tv_discount;

    private LinearLayout goodCategoryitem;
    private TextView goodCategoryTextView;
    private TextView goodNumTextView;
    private LinearLayout goodsComment;
    private TextView gooddetail_comment_num;
    private View headline;
    private LinearLayout comment_consult;
    private String priceString;
    private boolean isLogin = false;
    private CommentModel commentmodel;
    private GoodPropertyAdapter propertyAdapter;
    private ListView property_list;
    private View no_info;
    private int position;
    private String goods_id;
    private ShoppingCartModel cartModel;
    private boolean isBuyNow = false;
    private AddressModel addressModel;
    private String address_id;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private TextView tv_server;
    private String promoteTime;
    private TextView gooddetail_comment_positive;

    //    CountDownNewView countdownView;//团批倒计时----------
    //    private TextView goodPromotePriceTextView;//原来红色大价格-------
    //    private TextView goodMarketPriceTextView;//原来-划掉价格-------
    //    private MySeekBar myseekbar;//原来-团批倒计时-------
    //团批相关
//    @BindView(R.id.ll_tuanpi_top)
    LinearLayout ll_tuanpi_top;//团批顶部父布局
    //    @BindView(R.id.myseekbar_tp)
    MySeekBar myseekbar_tp;//团批进度条
    //    @BindView(R.id.tv_tuanpi_valid_goods)
    TextView tv_tuanpi_valid_goods;//已团20件
    //    @BindView(R.id.tv_tuanpi_batch_amount)
    TextView tv_tuanpi_batch_amount;//10件起批

    //    @BindView(R.id.ll_tuanpi_price)
    LinearLayout ll_tuanpi_price;//团批 价格展示 父布局
    //    @BindView(R.id.tv_tuanpi_market_price)
    TextView tv_tuanpi_market_price;//团批划掉价格
    //    @BindView(R.id.tv_tuanpi_promote_price)
    TextView tv_tuanpi_promote_price;//团批定金价格

    LinearLayout ly_tuanpi_price_content;
    //    @BindView(R.id.tv_tuanpi_price_content)
    TextView tv_tuanpi_price_content;//团批定金价格描述
    //    @BindView(R.id.ll_tuanpi_promotion_time)
    CountDownNewRedView ll_tuanpi_promotion_time;//团批倒计时----------
    //    @BindView(R.id.tv_tuanpi_dinjin)
    TextView tv_tuanpi_dinjin;//团批定金标签VViewiew

    //抢批相关
//    @BindView(R.id.ll_qiangpi_top)
    LinearLayout ll_qiangpi_top;//抢批顶部父布局
    //    @BindView(R.id.tv_qiangpi_promote_price)
    TextView tv_qiangpi_promote_price;//抢批价格
    //    @BindView(R.id.tv_qiangpi_market_price)
    TextView tv_qiangpi_market_price;//抢批-划掉价格--市场价
    //    @BindView(R.id.tv_qiangpi_batch_amount)
    TextView tv_qiangpi_batch_amount;//10件起批
    //    @BindView(R.id.ll_promotion_time_qiangpi)
    CountDownNewView ll_promotion_time_qiangpi;//抢批倒计时----------

    //普通商品相关
    //--未认证
//    @BindView(R.id.ll_price_normal)
    LinearLayout ll_price_normal;//普通商品--未认证--父布局
    //    @BindView(R.id.tv_market_price_normal)
    TextView tv_market_price_normal;//市场价格

    TextView tv_price_content_normal;//市场价格描述
    //    @BindView(R.id.tv_apply_menber)
    TextView tv_apply_menber;//认证按钮

    //--已经 认证
//    @BindView(R.id.ll_price_menber)
    LinearLayout ll_price_menber;//普通商品--未认证--父布局
    //    @BindView(R.id.tv_menber_promote_price1)
    TextView tv_menber_promote_price1;//阶梯价格1
    //    @BindView(R.id.tv_menber_promote_price1_content)
    TextView tv_menber_promote_price1_content;//价格描述1
    //    @BindView(R.id.line1)
    View line1;//价格描述后面的线
    //    @BindView(R.id.tv_menber_promote_price2)
    TextView tv_menber_promote_price2;//阶梯价格2
    //    @BindView(R.id.tv_menber_promote_price2_content)
    TextView tv_menber_promote_price2_content;//价格描述2
    //    @BindView(R.id.line2)
    View line2;//价格描述后面的线
    //    @BindView(R.id.tv_menber_promote_price3)
    TextView tv_menber_promote_price3;//阶梯价格3
    //    @BindView(R.id.tv_menber_promote_price3_content)
    TextView tv_menber_promote_price3_content;//价格描述3
    //价格描述--划掉的零售价 layout
    LinearLayout ll_shopprice_hinit;
    //价格描述--划掉的零售价
    TextView tv_shopprice_hinit;

    TextView tv_goodsfee;//运费

    TextView tv_goodsdetail_warrant1, tv_goodsdetail_warrant2, tv_goodsdetail_warrant3, tv_goodsdetail_warrant4, tv_goodsdetail_warrant5;//保障

    private String sendMesagePrice = "0";

    private boolean isHaveSpec = true;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (GoodsDetailActivity) activity;
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        shared = parentActivity.getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        area_id = shared.getString("area_id", "");
        View view = inflater.inflate(R.layout.fragment_product_layout, null);
        initView(view);
        init();
        return view;
    }

    private void init() {
        initData();
        getData();
    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int j = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }


    private void initView(View view) {
        /**
         * 这一段需要修改 主要是布局的高宽以及间距
         */
        itemWith = (getDisplayMetricsWidth() - (int) getResources().getDimension(R.dimen.dp_10) * 4) / 3;
        initScrooll(view);
        initHeader(view);
        initComment(view);
        initFooter(view);
        setGoodsDesc(view);
        setGoodProperty(view);
        setTabView(view);
    }

    private void initComment(View view) {
        commentView_in = (LinearLayout) view.findViewById(R.id.commentView_in);
    }

    private TextView addresstext;
    private LinearLayout address_item;

    void initHeader(View view) {
        headerView = view.findViewById(R.id.pageone_headerview);
        detailBanner = (BannerView<PHOTO>) headerView.findViewById(R.id.detail_banner);
        ViewGroup.LayoutParams params1 = detailBanner.getLayoutParams();
        params1.height = getDisplayMetricsWidth();
        params1.width = getDisplayMetricsWidth();
        //轮播图+商品介绍+属性+评价
        goodBriefTextView = (TextView) headerView.findViewById(R.id.good_brief);//商品简介
        ll_mobile_buy = (LinearLayout) headerView.findViewById(R.id.ll_mobile_buy);
        tv_mobile_discount = (TextView) headerView.findViewById(R.id.tv_mobile_discount);
        ly_coupons = (LinearLayout) headerView.findViewById(R.id.ly_coupons);//优惠券点击页面
        ly_coupons.setVisibility(View.GONE);
        ly_coupons.setOnClickListener(this);
        ly_discount = (LinearLayout) headerView.findViewById(R.id.ly_discount);//"[店铺活动]"点击页面
        tv_discount = (TextView) headerView.findViewById(R.id.tv_discount);//"[店铺活动]"
        ly_discount.setVisibility(View.GONE);
        ly_discount.setOnClickListener(this);

        goodCategoryitem = (LinearLayout) headerView.findViewById(R.id.good_category_item);//商品属性
        goodCategoryTextView = (TextView) headerView.findViewById(R.id.specification_text);
        goodNumTextView = (TextView) headerView.findViewById(R.id.good_category);
        goodCategoryTextView.setSingleLine(false);
        //进入商品属性页面
        goodCategoryitem.setOnClickListener(this);
        addresstext = (TextView) headerView.findViewById(R.id.good_cityinfo);
        addresstext.setText(shared.getString("sendArea", ""));
        address_item = (LinearLayout) headerView.findViewById(R.id.good_city_item);
        address_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Address2Activity.class);
                startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        });
        tv_server = (TextView) headerView.findViewById(R.id.good_serviceinfo);
        goodsComment = (LinearLayout) headerView.findViewById(R.id.goods_comment);//商品评价
        gooddetail_comment_num = (TextView) headerView.findViewById(R.id.gooddetail_commit_num);//商品评价
        gooddetail_comment_positive = (TextView) headerView.findViewById(R.id.positive_comment);//商品评价
        goodsComment.setOnClickListener(this);
        headline = headerView.findViewById(R.id.headline);
        //团批相关
        ll_tuanpi_top = (LinearLayout) headerView.findViewById(R.id.ll_tuanpi_top);//团批顶部父布局
        myseekbar_tp = (MySeekBar) headerView.findViewById(R.id.myseekbar_tp);//团批进度条
        tv_tuanpi_valid_goods = (TextView) headerView.findViewById(R.id.tv_tuanpi_valid_goods);//已团20件
        tv_tuanpi_batch_amount = (TextView) headerView.findViewById(R.id.tv_tuanpi_batch_amount);//10件起批
        ll_tuanpi_price = (LinearLayout) headerView.findViewById(R.id.ll_tuanpi_price);//团批 价格展示 父布局
        tv_tuanpi_market_price = (TextView) headerView.findViewById(R.id.tv_tuanpi_market_price);//团批划掉价格
        tv_tuanpi_promote_price = (TextView) headerView.findViewById(R.id.tv_tuanpi_promote_price);//团批定金价格
        tv_tuanpi_price_content = (TextView) headerView.findViewById(R.id.tv_tuanpi_price_content);//团批定金价格描述
        ly_tuanpi_price_content = (LinearLayout) headerView.findViewById(R.id.ly_tuanpi_price_content);//团批定金价格描述
        ll_tuanpi_promotion_time = (CountDownNewRedView) headerView.findViewById(R.id.ll_tuanpi_promotion_time);//团批倒计时----------
        tv_tuanpi_dinjin = (TextView) headerView.findViewById(R.id.tv_tuanpi_dinjin);//团批定金标签VViewiew
        //抢批相关
        ll_qiangpi_top = (LinearLayout) headerView.findViewById(R.id.ll_qiangpi_top);//抢批顶部父布局
        tv_qiangpi_promote_price = (TextView) headerView.findViewById(R.id.tv_qiangpi_promote_price);//抢批价格
        tv_qiangpi_market_price = (TextView) headerView.findViewById(R.id.tv_qiangpi_market_price);//抢批-划掉价格--市场价
        tv_qiangpi_batch_amount = (TextView) headerView.findViewById(R.id.tv_qiangpi_batch_amount);//10件起批
        ll_promotion_time_qiangpi = (CountDownNewView) headerView.findViewById(R.id.ll_promotion_time_qiangpi);//抢批倒计时----------
        //普通商品相关
        //--未认证
        ll_price_normal = (LinearLayout) headerView.findViewById(R.id.ll_price_normal);//普通商品--未认证--父布局
        tv_market_price_normal = (TextView) headerView.findViewById(R.id.tv_market_price_normal);//市场价格
        tv_price_content_normal = (TextView) headerView.findViewById(R.id.tv_price_content_normal);//市场价格描述
        tv_apply_menber = (TextView) headerView.findViewById(R.id.tv_apply_menber);//认证按钮
        //--已经 认证
        ll_price_menber = (LinearLayout) headerView.findViewById(R.id.ll_price_menber);//普通商品--认证阶梯价--父布局
        tv_menber_promote_price1 = (TextView) headerView.findViewById(R.id.tv_menber_promote_price1);//阶梯价格1
        tv_menber_promote_price1_content = (TextView) headerView.findViewById(R.id.tv_menber_promote_price1_content);//价格描述1
        line1 = headerView.findViewById(R.id.line1);//价格描述后面的线
        tv_menber_promote_price2 = (TextView) headerView.findViewById(R.id.tv_menber_promote_price2);//阶梯价格2
        tv_menber_promote_price2_content = (TextView) headerView.findViewById(R.id.tv_menber_promote_price2_content);//价格描述2
        line2 = headerView.findViewById(R.id.line2);//价格描述后面的线
        tv_menber_promote_price3 = (TextView) headerView.findViewById(R.id.tv_menber_promote_price3);//阶梯价格3
        tv_menber_promote_price3_content = (TextView) headerView.findViewById(R.id.tv_menber_promote_price3_content);//价格描述3

        ll_shopprice_hinit = (LinearLayout) headerView.findViewById(R.id.ll_shopprice_hinit);//价格描述3
        tv_shopprice_hinit = (TextView) headerView.findViewById(R.id.tv_shopprice_hinit);//价格描述3

        tv_goodsfee = (TextView) headerView.findViewById(R.id.tv_goodsfee);//运费

        tv_goodsdetail_warrant1 = (TextView) headerView.findViewById(R.id.tv_goodsdetail_warrant1);//
        tv_goodsdetail_warrant2 = (TextView) headerView.findViewById(R.id.tv_goodsdetail_warrant2);//
        tv_goodsdetail_warrant3 = (TextView) headerView.findViewById(R.id.tv_goodsdetail_warrant3);//
        tv_goodsdetail_warrant4 = (TextView) headerView.findViewById(R.id.tv_goodsdetail_warrant4);//
        tv_goodsdetail_warrant5 = (TextView) headerView.findViewById(R.id.tv_goodsdetail_warrant5);//

    }

    void initFooter(View view) {
        footerView = view.findViewById(R.id.pageone_footerview);
        //商品咨询+上拉查看详情
        footerView.findViewById(R.id.foottext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc.scrollToPageTwo();
            }
        });
        comment_consult = (LinearLayout) footerView.findViewById(R.id.gooddetail_consult);
        comment_consult.setOnClickListener(this);
        load_text = (TextView) footerView.findViewById(R.id.load_text);
        initMyLove(footerView);
        initHistory(footerView);
        initMerchantView();
    }

    private LinearLayout toshop_item;
    private LinearLayout seller_item;
    private ImageView seller_logo;
    private TextView seller_name, seller_collected_num, seller_comment, seller_service, seller_time;

    private void initMerchantView() {
        seller_item = (LinearLayout) footerView.findViewById(R.id.seller_item);
        seller_logo = (ImageView) footerView.findViewById(R.id.seller_logo);
        seller_name = (TextView) footerView.findViewById(R.id.seller_name);
        seller_collected_num = (TextView) footerView.findViewById(R.id.collected_num);
        seller_comment = (TextView) footerView.findViewById(R.id.seller_comment);
        seller_service = (TextView) footerView.findViewById(R.id.seller_service);
        seller_time = (TextView) footerView.findViewById(R.id.seller_time);
        //进入店铺按钮
        toshop_item = (LinearLayout) footerView.findViewById(R.id.foot_seller_toshop_item);
        ImageView imageView = (ImageView) footerView.findViewById(R.id.foot_seller_toshop_icon);
        imageView.setColorFilter(Color.parseColor("#FFFFFF"));
        toshop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShopListActivity.class);
                intent.putExtra(IntentKeywords.MERCHANT_ID, dataModel.seller_id);
                startActivityForResult(intent, SHOP_LIST);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }


    private void updateMerchantView() {
        if (dataModel.merchantinfo != null) {
            seller_item.setVisibility(View.VISIBLE);
            MERCHANTINFO minfo = dataModel.merchantinfo;
            ImageLoader.getInstance().displayImage(minfo.getSeller_logo(), seller_logo);
            seller_name.setText(minfo.getSeller_name());
            seller_collected_num.setText(minfo.getFollower() + getResources().getString(R.string.follower_num));
            seller_comment.setText(minfo.getComment());
            seller_service.setText(minfo.getComment_server());
            seller_time.setText(minfo.getComment_delivery());
        } else {
            seller_item.setVisibility(View.GONE);
        }
    }

    private void initData() {
        commentmodel = new CommentModel(getActivity());
        commentmodel.addResponseListener(this);
        dataModel = new GoodsDetailModel(getActivity());
        dataModel.addResponseListener(this);
        addressModel = new AddressModel(getActivity());
        addressModel.addResponseListener(this);
    }

    private String area_id = "";//配送区域
    private String object_id = "", rec_type = "";

    private void getData() {
        goods_id = getActivity().getIntent().getStringExtra(IntentKeywords.GOODS_ID);
        object_id = getActivity().getIntent().getStringExtra(IntentKeywords.OBJECT_ID);
        rec_type = getActivity().getIntent().getStringExtra(IntentKeywords.REC_TYPE);
        dataModel.fetchGoodDetail(goods_id, area_id, object_id, rec_type, true);
        getGoodeShippingFee();
        getBrowse();
    }

    private void initBanner() {
        int size = dataModel.goodDetail.getPictures().size();
        PHOTO[] bannerList = new PHOTO[dataModel.goodDetail.getPictures().size()];
        if (bannerList.length > 0) {
            for (int i = 0; i < size; i++) {
                bannerList[i] = dataModel.goodDetail.getPictures().get(i);
            }
            detailBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            detailBanner.setOnBannerImageListener(new BannerView.OnLoadImageListener<PHOTO>() {
                @Override
                public void OnLoadImage(ImageView view, PHOTO url) {
                    ImageLoader.getInstance().displayImage(url.getUrl(), view);
                }
            });
            detailBanner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
                @Override
                public void OnBannerClick(View view, int position) {
                    Intent it = new Intent(getActivity(), FullScreenViewPagerActivity.class);
                    it.putExtra("position", position);
                    ArrayList<String> pictures = new ArrayList<String>();
                    for (int i = 0; i < dataModel.goodDetail.getPictures().size(); i++) {
                        pictures.add(dataModel.goodDetail.getPictures().get(i).getUrl());
                    }
                    it.putExtra("size", dataModel.goodDetail.getPictures().size());
                    it.putExtra("pictures", pictures);
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.my_scale_action, R.anim.my_scale_action);
                }
            });
            /**
             * 添加数据
             */
            detailBanner.setImages(dataModel.goodDetail.getPictures());
        }
    }


    private void initScrooll(View view) {
        listener = (OnScrollTabChangeListener) getActivity();
        sc = (ScrollViewWrapper) view.findViewById(R.id.gooddetail_sc);
        sc.setOnScrollChangeListener(new OnScrollViewChangeListener() {
            @Override
            public void change(int tabId) {
                switch (tabId) {
                    case 1: {
                        pageTwoTab.setVisibility(View.VISIBLE);
                        load_text.setText(R.string.detail_refresh_list_pull);
                        if (isFirst && TextUtils.isEmpty(dataModel.goodsWeb)) {
                            isFirst = false;
                            dataModel.goodsDesc(goods_id + "");
                        }
                        parentActivity.removeTablayoutListener();
                        break;
                    }
                    case 0: {
                        load_text.setText(R.string.detail_refresh_list_push);
                        parentActivity.addTablayoutListener();
                        break;
                    }
                }
            }
        });

        page_one_goodsdetail = (BottomElasticScrollView) view.findViewById(R.id.page_one_goodsdetail);
        page_two_errorview = (TopElasticScrollView) view.findViewById(R.id.page_two_errorview);
        page_two_webview = (TopElasticScrollView) view.findViewById(R.id.page_two_webview);
        page_two_listview = (TopElasticScrollView) view.findViewById(R.id.page_two_listview);
        page_two_errorview.setParentScrollView(sc);
        page_two_webview.setParentScrollView(sc);
        page_two_listview.setParentScrollView(sc);
        page_one_goodsdetail.setParentScrollView(sc);
    }

    BottomElasticScrollView page_one_goodsdetail;
    TopElasticScrollView page_two_listview;
    TopElasticScrollView page_two_webview;
    TopElasticScrollView page_two_errorview;
    private RelativeLayout taboneitem, tabtwoitem;
    private TextView tabonetext, tabtwotext;
    //-------------------------------------
    private WebView webView;
    View pageTwoTab;

    //商品详细介绍
    void setTabView(View view) {
        pageTwoTab = view.findViewById(R.id.buttom_toolbar);
        pageTwoTab.setVisibility(View.INVISIBLE);
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

    //TAB的点击效果
    private void selected(String position) {
        Resources resource = (Resources) getResources();
        ColorStateList selectedTextColor = (ColorStateList) resource.getColorStateList(R.color.filter_text_color);
        if ("one".equals(position)) {
            tabonetext.setTextColor(Color.RED);
            tabtwotext.setTextColor(selectedTextColor);
            page_two_errorview.setVisibility(View.GONE);
            page_two_webview.setVisibility(View.VISIBLE);
            page_two_listview.setVisibility(View.GONE);
        } else if ("two".equals(position)) {
            tabonetext.setTextColor(selectedTextColor);
            tabtwotext.setTextColor(Color.RED);
            page_two_webview.setVisibility(View.GONE);
            page_two_errorview.setVisibility(View.GONE);
            if (null != GoodDetailDraft.getInstance().goodDetail && GoodDetailDraft.getInstance().goodDetail.getProperties().size() > 0) {
                page_two_listview.setVisibility(View.VISIBLE);
                property_list.setSelection(0);
                page_two_errorview.setVisibility(View.GONE);
            } else {
                page_two_listview.setVisibility(View.GONE);
                page_two_errorview.setVisibility(View.VISIBLE);
            }
        }
    }

    void initMyLove(View view) {
        mylove_layout = (FlowLayout) view.findViewById(R.id.goodsdetail_mylove_goods_layout);
        mylove_layout_in = view.findViewById(R.id.goodsdetail_mylove_in);
        mylove_layout_in.setVisibility(View.GONE);
    }

    FlowLayout mylove_layout;
    View mylove_layout_in;

    /**
     * 猜你喜欢数据填充
     */
    void addMyLoveToLayout() {
        mylove_layout.removeAllViews();
        int size = dataModel.related_list.size() >= 6 ? 6 : dataModel.related_list.size();
        if (size == 0) {
            mylove_layout_in.setVisibility(View.GONE);
            return;
        } else {
            mylove_layout_in.setVisibility(View.VISIBLE);
        }
        mylove_layout.setBackgroundColor(Color.parseColor("#ffffff"));
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < size; i++) {
            LinearLayout rowLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_goodsdetail_mylove, null);
            RelativeLayout imageViewLL = (RelativeLayout) rowLayout.findViewById(R.id.goods_image_ll);
            ImageView goodsImageIV = (ImageView) rowLayout.findViewById(R.id.goods_image);
            TextView goodsNameText = (TextView) rowLayout.findViewById(R.id.goods_name);
            TextView goodsPriceText = (TextView) rowLayout.findViewById(R.id.goods_price);
            ImageLoader.getInstance().displayImage(dataModel.related_list.get(i).getImg().getThumb(), goodsImageIV);
            goodsNameText.setText(dataModel.related_list.get(i).getGood_name());
            if (TextUtils.isEmpty(dataModel.related_list.get(i).getPromote_price()) || dataModel.related_list.get(i).getPromote_price().equals("null")) {
                goodsPriceText.setText(dataModel.related_list.get(i).getShop_price());
                if (0 == FormatUtil.formatStrToFloat(dataModel.related_list.get(i).getShop_price())) {
                    goodsPriceText.setText("免费");
                } else {
                    goodsPriceText.setText(dataModel.related_list.get(i).getShop_price());
                }
            } else {
                if (0 == FormatUtil.formatStrToFloat(dataModel.related_list.get(i).getPromote_price())) {
                    goodsPriceText.setText("免费");
                } else {
                    goodsPriceText.setText(dataModel.related_list.get(i).getPromote_price());
                }
            }
            final int position = i;
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("goods_id", dataModel.related_list.get(position).getId() + "");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            /**
             * 这一段需要修改 主要是布局的高宽以及间距
             * **/
            LinearLayout.LayoutParams imageParamas = new LinearLayout.LayoutParams(itemWith, itemWith);
            imageViewLL.setLayoutParams(imageParamas);
            ViewGroup.MarginLayoutParams params3 = new ViewGroup.MarginLayoutParams(itemWith, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params3.setMargins((int) getResources().getDimension(R.dimen.dp_10), (int) getResources().getDimension(R.dimen.dp_10), 0, 0);
            rowLayout.setLayoutParams(params3);
            mylove_layout.addView(rowLayout, params3);
        }
    }

    //商品规格表格
    public void setGoodProperty(View view) {
        property_list = (ListView) view.findViewById(R.id.property_list);
        property_list.getLayoutParams().height = getGoodsDesHeight();
    }

    private int getGoodsDesHeight() {
        int screemHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        int topViewHeight = (int) getResources().getDimension(R.dimen.main_items_height);
        int topBarHeight = (int) getResources().getDimension(R.dimen.height_goodsdetail_topbar);
        LG.i("screemHeight:  " + screemHeight + "   getGoodsDesHeight:  " + (screemHeight - 2 * topViewHeight - topBarHeight - getStatusBarHeight()));
        return screemHeight - 3 * topViewHeight - getStatusBarHeight();
    }


    //webview数据
    public void setGoodsDesc(View view) {
        no_info = view.findViewById(R.id.not_info);
        no_info.setVisibility(View.VISIBLE);
        no_info.getLayoutParams().height = getGoodsDesHeight();
        webView = (WebView) view.findViewById(R.id.my_web);
        webView.getLayoutParams().height = getGoodsDesHeight();
        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            //            @Override
            //            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //                webView.loadDataWithBaseURL(null, dataModel.goodsWeb, "text/html", "utf-8", null);
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

    public boolean noGoods = false;

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {

        switch (url) {
            case ProtocolConst.GOODSDETAIL:
                if (status.getSucceed() == 0) {
                    noGoods = true;
                    parentActivity.showNoGoodsView(status.getError_desc());
                } else {
                    parentActivity.showViewPager();
                    initBanner();
                    updateGoodsDetailDraft();
                    goodBriefTextView.setText(dataModel.goodDetail.getGoods_name());
                    rec_type = dataModel.goodDetail.getActivity_type();
                    switch (rec_type) {
                        case AppConst.MOBILEBUY_GOODS:
                            setMobilebuyView();
                            break;
                        case AppConst.PROMOTE_GOODS:
//                        promote_new_tag    parentActivity.addToCartTextView.setBackgroundColor(getResources().getColor(R.color.TextColorGray));
                            SpannableStringBuilder style = new SpannableStringBuilder(" 促销  " + dataModel.goodDetail.getGoods_name());
                            style.setSpan(new BackgroundColorSpan(Color.RED), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            style.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            goodBriefTextView.setText(style);
                            //goodBriefTextView.setText(Html.fromHtml("<font color=#ffffff  background-color=#ff0000 >促销</>  "+dataModel.goodDetail.getGoods_name()));
                            setQiangpiView();
                            break;
                        case AppConst.GRAB_GOODS://为抢批商品类型
                            SpannableStringBuilder styleQ = new SpannableStringBuilder(" 抢批  " + dataModel.goodDetail.getGoods_name());
                            styleQ.setSpan(new BackgroundColorSpan(Color.RED), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            styleQ.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                            goodBriefTextView.setText(styleQ);
                            parentActivity.addToCartTextView.setBackgroundColor(getResources().getColor(R.color.TextColorGray));
                            setQiangpiView();
                            break;
                        case AppConst.GROUPBUY_GOODS://团批商品详情
                            parentActivity.addToCartTextView.setBackgroundColor(getResources().getColor(R.color.white));
                            parentActivity.addToCartTextView.setText("单人购买");
                            //暂时不支持
                            setPromoteView();
                            break;
                        default://普通商品
                            setGeneralView();
                            break;
                    }
                    /**
                     * 送积分+包邮
                     */
                    if (dataModel.goodDetail.getGive_integral().equals("0") && dataModel.goodDetail.getIs_shipping().equals("0")) {

                    } else {
                        if (dataModel.goodDetail.getIs_shipping().equals("1")) {
                            FAVOUR favour = new FAVOUR();
                            favour.setType_label("包邮");
                            dataModel.goodDetail.getFavours().add(0, favour);
                        }
                        if (!TextUtils.isEmpty(dataModel.goodDetail.getGive_integral()) && 0 != FormatUtil.formatStrToFloat(dataModel.goodDetail.getGive_integral())) {
                            FAVOUR favour = new FAVOUR();
                            favour.setType_label("送");
                            favour.setName(dataModel.goodDetail.getGive_integral() + getResources().getString(R.string.balance_exp));
                            dataModel.goodDetail.getFavours().add(0, favour);
                        }
                    }
                    if (dataModel.goodDetail.getFavours().size() > 0) {
                        ly_discount.setVisibility(View.VISIBLE);
                        tv_discount.setText(dataModel.goodDetail.getFavours().get(0).getType_label() + dataModel.goodDetail.getFavours().get(0).getName() + "");
                    } else {
                        ly_discount.setVisibility(View.GONE);
                    }

                    /**
                     * 优惠券 todo
                     */
                    if (dataModel.goodDetail.getCoupons().size() > 0) {
                        if (AppConst.GROUPBUY_GOODS.equals(rec_type) || AppConst.GRAB_GOODS.equals(rec_type)) {
                            ly_coupons.setVisibility(View.GONE);
                        } else {
                            ly_coupons.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ly_coupons.setVisibility(View.GONE);
                    }
                    /**
                     * 商品属性
                     */
                    if (!TextUtils.isEmpty(getSpeciationDesc())) {
                        goodCategoryTextView.setVisibility(View.VISIBLE);
                        goodCategoryTextView.setText(getSpeciationDesc());
                    } else {
                        goodCategoryTextView.setVisibility(View.GONE);
                    }
                    if (!isHaveSpec) {
                        setGroupViewIU("2");
                    }
                    goodNumTextView.setText(GoodDetailDraft.getInstance().goodAllQuantity + "");

                    tv_server.setText(dataModel.server_desc + "");

                    /**
                     * 是否收藏
                     */
                    if (dataModel.goodDetail.getCollected() == 0) {
                        collectFlag = false;
                        parentActivity.collectionButton.setImageResource(R.drawable.item_info_collection_disabled_btn);
                    } else {
                        collectFlag = true;
                        parentActivity.collectionButton.setImageResource(R.drawable.item_info_collection_btn);
                    }
                    /**
                     * 加载轮播视图
                     * 获取评价
                     * 由于登录和非登录状态这些数据不会有任何改变，所以不需要多次刷新
                     * */
                    if (!isLogin) {
                        if (dataModel.goodDetail.getPictures() != null && dataModel.goodDetail.getPictures().size() > 0) {
                            initBanner();
                        }
                        //获取商品评价
                        commentmodel.getCommentList(goods_id, CommentType.ALL_COMMENT, false);
                    }
                    // 保存浏览记录
                    saveBrowse();

                    /**
                     *加载猜你喜欢
                     */
                    addMyLoveToLayout();


                    updateMerchantView();

                    /**
                     * 商品属性
                     */
                    if (null != GoodDetailDraft.getInstance().goodDetail && GoodDetailDraft.getInstance().goodDetail.getProperties().size() > 0) {
                        propertyAdapter = new GoodPropertyAdapter(getActivity(), GoodDetailDraft.getInstance().goodDetail.getProperties());
                        property_list.setAdapter(propertyAdapter);
                    }
                    if (needShow) {
                        needShow = false;
                    }
                    List<SHOOPSERVICES> shop_services = dataModel.goodDetail.getShop_services();
                    if (shop_services.size() > 0) {
                        tv_goodsdetail_warrant1.setText(shop_services.get(0).getService_name());
                        if (shop_services.size() > 1) {
                            tv_goodsdetail_warrant2.setText(shop_services.get(1).getService_name());
                            if (shop_services.size() > 2) {
                                tv_goodsdetail_warrant3.setText(shop_services.get(2).getService_name());
                                if (shop_services.size() > 3) {
                                    tv_goodsdetail_warrant4.setText(shop_services.get(3).getService_name());
                                    if (shop_services.size() > 4) {
                                        tv_goodsdetail_warrant5.setText(shop_services.get(4).getService_name());
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case ProtocolConst.GOODS_COMMENT_LIST: //评论
                if (status.getSucceed() == 1) {
                    addCommentView();
                } else {
                    LG.i("=====获取评论失败======");
                }
                break;

            case ProtocolConst.GOODSDESC://商品详情
                if (status.getSucceed() == 1) {
                    if (!TextUtils.isEmpty(dataModel.goodsWeb)) {
                        webView.loadDataWithBaseURL(null, dataModel.goodsWeb, "text/html", "utf-8", null);
                    } else {
                        isFirst = true;
                    }
                }
                break;

            case ProtocolConst.ADDRESS_LIST:// 获取用户地址列表
                if (status.getSucceed() == 1) {
                    //地址长度>0表示有地址，可以直接去结算，没有则去添加
                    if (addressModel.addressList.size() > 0) {
                        isBuyNow = true;
                        for (int i = 0; i < addressModel.addressList.size(); i++) {
                            if (addressModel.addressList.get(i).getDefault_address() == 1) {
                                address_id = addressModel.addressList.get(i).getId() + "";
                                break;
                            }
                        }
                        if (TextUtils.isEmpty(address_id)) {
                            address_id = addressModel.addressList.get(0).getId() + "";
                        }
                        // 商品马上购买立马给商品属性状态重新设定，清空所选属性
//                        GoodDetailDraft.getInstance().clear();
//                        GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;
                        toBalance();
                    } else {
                        String main = getResources().getString(R.string.point);
                        String main_content = getResources().getString(R.string.address_add_first);
                        final MyDialog myDialog = new MyDialog(getActivity(), main, main_content);
                        myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                        myDialog.setNegativeListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                            }
                        });
                        myDialog.setPositiveListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myDialog.dismiss();
                                toAddAddress();
                            }
                        });
                        myDialog.show();
                    }
                }
                break;

            case ProtocolConst.CARTCREATE:// 添加到购物车
                LG.i("isBuyNow" + isBuyNow);
                //加入购物车回调
                if (status.getSucceed() == 1) {
                    if (isBuyNow) { //点击立即购买加入的购物车
                        addressModel.getAddressList();
                    } else {//点击加入购物车
                        // 商品马上购买立马给商品属性状态重新设定，清空所选属性
//                        GoodDetailDraft.getInstance().clear();
//                        GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;
//                        getData();
                        ToastView toast = new ToastView(getActivity(), R.string.add_to_cart_success);
                        toast.setGravity(Gravity.CENTER);
                        toast.show();
                    }
                    if (!AppConst.GROUPBUY_GOODS.equals(rec_type)) {
                        cartModel.cartList(false);
                    }
                } else {
                    ToastView toastView = new ToastView(getActivity(), status.getError_desc());
                    toastView.show();
                }
                break;

            case ProtocolConst.COLLECTION_CREATE:// 收藏
                if (status.getSucceed() == 1) {
                    parentActivity.collectionButton.setImageResource(R.drawable.item_info_collection_btn);
                    collectFlag = true;
                    EventBus.getDefault().post(new MyEvent("userinfo_refresh"));
                    ToastView toast = new ToastView(getActivity(), R.string.collection_success);
                    toast.setGravity(Gravity.CENTER);
                    toast.show();
                } else if (status.getError_code() == AppConst.UnexistInformation) {
                    String registration_closed = getResources().getString(R.string.unexist_information);
                    ToastView toast = new ToastView(getActivity(), registration_closed);
                    toast.setGravity(Gravity.CENTER);
                    toast.show();
                } else if (status.getError_code() == AppConst.AlreadyCollected) {
                    String col = getResources().getString(R.string.collected);
                    ToastView toast = new ToastView(getActivity(), col);
                    toast.setGravity(Gravity.CENTER);
                    toast.show();
                }
                break;

            case ProtocolConst.COLLECTION_DELETE:// 取消收藏
                if (status.getSucceed() == 1) {
                    parentActivity.collectionButton.setImageResource(R.drawable.item_info_collection_disabled_btn);
                    collectFlag = false;
                    EventBus.getDefault().post(new MyEvent("userinfo_refresh"));
                    ToastView toast = new ToastView(getActivity(), R.string.del_collection_success);
                    toast.setGravity(Gravity.CENTER);
                    toast.show();
                }
                break;

            case ProtocolConst.CARTLIST:// 更新购物车数量
//                GoodDetailDraft.getInstance().clear();
//                GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;
                parentActivity.setShoppingcartNum();
                break;

            case ProtocolConst.REDPAPER_RECEIVE:
                if (status.getSucceed() == 1) {
                    ToastView_ForReceiveRedpaper toast = new ToastView_ForReceiveRedpaper(getActivity(), R.string.goodsdetail_getredpaper_secceed);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    dataModel.goodDetail.getGoods_coupon().get(position).setReceived_coupon("1");
                }
                break;
        }
    }


    private void setMobilebuyView() {
        ll_tuanpi_top.setVisibility(View.GONE);
        ll_qiangpi_top.setVisibility(View.GONE);
        ll_price_normal.setVisibility(View.GONE);
        ll_price_menber.setVisibility(View.GONE);
        ll_tuanpi_price.setVisibility(View.VISIBLE);
        try {
            priceString = dataModel.goodDetail.getPromote_price();
            if (Float.parseFloat(priceString) == 0) {
                tv_tuanpi_promote_price.setText("免费");
            } else {
                tv_tuanpi_promote_price.setText(FormatUtil.formatToSymbolPrice(FormatUtil.formatStrToFloat(dataModel.goodDetail.getPromote_price()) + FormatUtil.formatStrToFloat(specification_price_one + "")));
            }
            tv_tuanpi_market_price.setVisibility(View.VISIBLE);
            tv_tuanpi_market_price.setText(resources.getString(R.string.original_price) + FormatUtil.formatToSymbolPrice(dataModel.goodDetail.getUnformatted_shop_price()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 手机专享标签
         */
        tv_mobile_discount.setText(dataModel.goodDetail.getFormatted_saving_price());
        ll_mobile_buy.setVisibility(View.VISIBLE);

    }

    //团批-商品
    private void setPromoteView() {
        ll_qiangpi_top.setVisibility(View.GONE);
        ll_price_normal.setVisibility(View.GONE);
        ll_price_menber.setVisibility(View.GONE);

        ll_tuanpi_top.setVisibility(View.VISIBLE);
        ll_tuanpi_price.setVisibility(View.VISIBLE);
        tv_tuanpi_dinjin.setVisibility(View.VISIBLE);
        tv_tuanpi_price_content.setVisibility(View.VISIBLE);
        List<GOODSPRICELADDER> price_ladder = dataModel.goodDetail.getPrice_ladder();
//        myseekbar_tp.setData(100,80,50,1,50,100,70);

        if (price_ladder.size() > 2) {
            myseekbar_tp.setData(price_ladder.get(0).getPrice(), price_ladder.get(1).getPrice(), price_ladder.get(2).getPrice(), price_ladder.get(0).getAmount(), price_ladder.get(1).getAmount(), price_ladder.get(2).getAmount(), dataModel.goodDetail.getValid_goods());
        } else if (price_ladder.size() > 1 && price_ladder.size() <= 2) {
            myseekbar_tp.setData(price_ladder.get(0).getPrice(), price_ladder.get(1).getPrice(), 0, price_ladder.get(0).getAmount(), price_ladder.get(1).getAmount(), 0, dataModel.goodDetail.getValid_goods());
        } else if (price_ladder.size() > 0 && price_ladder.size() <= 1) {
            myseekbar_tp.setData(price_ladder.get(0).getPrice(), 0, 0, price_ladder.get(0).getAmount(), 0, 0, dataModel.goodDetail.getValid_goods());
        } else {
            myseekbar_tp.setVisibility(View.GONE);
        }
        //tv_tuanpi_valid_goods;//已团20件
        tv_tuanpi_valid_goods.setText("已团" + dataModel.goodDetail.getValid_goods() + "件");
        //tv_tuanpi_batch_amount;//10件起批
        tv_tuanpi_batch_amount.setText("" + dataModel.goodDetail.getBatch_amount() + "件起批");
        try {
            priceString = dataModel.goodDetail.getDeposit() + "";//定金
            if (Float.parseFloat(priceString) == 0) {
                tv_tuanpi_promote_price.setText("免费");
            } else {
                tv_tuanpi_promote_price.setText(FormatUtil.formatToSymbolPrice(FormatUtil.formatStrToFloat(dataModel.goodDetail.getDeposit() + "") + FormatUtil.formatStrToFloat(specification_price_one + "")));
            }
            //goodMarketPriceTextView.setText(resources.getString(R.string.original_price) + FormatUtil.formatToSymbolPrice(dataModel.goodDetail.getUnformatted_shop_price()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_tuanpi_price_content.setText("先支付定金,成团后补差价.团批活动结束后,定金不予退还!");
        //点击帮助
        ly_tuanpi_price_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupHelpWebActivity.class);
                intent.putExtra("id", 49);
                intent.putExtra("title", "团批说明");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        });

        Handler myTimeHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 2000://时间结束
                        getData();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        /**
         * 促销时间
         * getGroup_status 团批抢批状态 0为未开始，1为进行中，2为过期
         */
        setGroupViewIU(dataModel.goodDetail.getGroup_status());
        if ("0".equals(dataModel.goodDetail.getGroup_status())) {
            ll_tuanpi_promotion_time.setVisibility(View.VISIBLE);
            if (null != dataModel.goodDetail.getStarttime() && dataModel.goodDetail.getStarttime().length() > 0) {
//                if (TimeUtil.timeLeft(dataModel.goodDetail.getStarttime()).length() == 0) {
////                    ll_tuanpi_promotion_time.setVisibility(View.GONE);
//                } else {
//                    long remainTime = TimeUtil.getRemainTime(dataModel.goodDetail.getStarttime());
                long remainTime = Long.parseLong(dataModel.goodDetail.getStarttime()) * 1000;
                if (remainTime > 0) {
                    ll_tuanpi_promotion_time.setTime(remainTime, myTimeHandler);
                    ll_tuanpi_promotion_time.start();
                }
//                    ll_tuanpi_promotion_time.setVisibility(View.VISIBLE);
//                }
            }
//            else{
////                ll_tuanpi_promotion_time.setVisibility(View.GONE);
//            }
        } else if ("1".equals(dataModel.goodDetail.getGroup_status())) {
            ll_tuanpi_promotion_time.setVisibility(View.VISIBLE);
            if (null != dataModel.goodDetail.getEndtime() && dataModel.goodDetail.getEndtime().length() > 0) {
                long remainTime = Long.parseLong(dataModel.goodDetail.getEndtime()) * 1000;
                if (remainTime > 0) {
                    ll_tuanpi_promotion_time.setTime(remainTime, myTimeHandler);
                    ll_tuanpi_promotion_time.start();
                }
            } else {
            }
        } else {
            ll_tuanpi_promotion_time.setVisibility(View.VISIBLE);
            ll_tuanpi_promotion_time.setActivityEnd(true);
        }
    }

    //抢批   and  促销-商品
    private void setQiangpiView() {
        tv_qiangpi_market_price.getPaint().setAntiAlias(true);//中划线并清晰
        tv_qiangpi_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//中划线并清晰
        ll_price_normal.setVisibility(View.GONE);
        ll_price_menber.setVisibility(View.GONE);
        ll_tuanpi_top.setVisibility(View.GONE);
        ll_tuanpi_price.setVisibility(View.GONE);
        ll_qiangpi_top.setVisibility(View.VISIBLE);

        try {
            priceString = dataModel.goodDetail.getPromote_price() + "";//活动价格
            if (Float.parseFloat(priceString) == 0) {
                tv_qiangpi_promote_price.setText("免费");
            } else {
                tv_qiangpi_promote_price.setText(FormatUtil.formatToSymbolPrice(FormatUtil.formatStrToFloat(dataModel.goodDetail.getPromote_price() + "") + FormatUtil.formatStrToFloat(specification_price_one + "")));
            }
            tv_qiangpi_market_price.setText(resources.getString(R.string.original_price) + FormatUtil.formatToSymbolPrice(dataModel.goodDetail.getMarket_price()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (AppConst.PROMOTE_GOODS.equals(rec_type)) {
            tv_qiangpi_batch_amount.setVisibility(View.INVISIBLE);
        } else {
            tv_qiangpi_batch_amount.setText("" + dataModel.goodDetail.getBatch_amount() + "件起批");
        }
        Handler myTimeHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 2000://时间结束
                        getData();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        /**
         * 促销时间
         * getGroup_status 抢批状态 0为未开始，1为进行中，2为过期
         */
        parentActivity.buyNowTextView.setBackgroundColor(getResources().getColor(R.color.TextColorGray));
        isBuyNow = false;
        setGroupViewIU(dataModel.goodDetail.getGroup_status());
        if ("0".equals(dataModel.goodDetail.getGroup_status())) {
            ll_qiangpi_top.setBackgroundColor(getResources().getColor(R.color._26a96d));
            ll_promotion_time_qiangpi.setVisibility(View.VISIBLE);
            if (null != dataModel.goodDetail.getStarttime() && dataModel.goodDetail.getStarttime().length() > 0) {
                long remainTime = Long.parseLong(dataModel.goodDetail.getStarttime()) * 1000;
                if (remainTime > 0) {
                    ll_promotion_time_qiangpi.setTime(remainTime, myTimeHandler);
                    ll_promotion_time_qiangpi.start();
                }
            } else {
                ll_promotion_time_qiangpi.setVisibility(View.GONE);
            }
        } else if ("1".equals(dataModel.goodDetail.getGroup_status())) {
            ll_qiangpi_top.setBackgroundColor(getResources().getColor(R.color.public_theme_color_normal));
            ll_promotion_time_qiangpi.setVisibility(View.VISIBLE);
            if (null != dataModel.goodDetail.getEndtime() && dataModel.goodDetail.getEndtime().length() > 0) {
                long remainTime = Long.parseLong(dataModel.goodDetail.getEndtime()) * 1000;
                if (remainTime > 0) {
                    ll_promotion_time_qiangpi.setTime(remainTime, myTimeHandler);
                    ll_promotion_time_qiangpi.start();
                }
            }
        } else {
            ll_promotion_time_qiangpi.setVisibility(View.VISIBLE);
            ll_promotion_time_qiangpi.setActivityEnd(true);
            ll_qiangpi_top.setBackgroundColor(getResources().getColor(R.color.TextColorGray));
        }
    }

    //普通-商品
    private void setGeneralView() {
        ll_tuanpi_top.setVisibility(View.GONE);
        ll_tuanpi_price.setVisibility(View.GONE);
        ll_qiangpi_top.setVisibility(View.GONE);
        ll_price_normal.setVisibility(View.GONE);
        ll_price_menber.setVisibility(View.GONE);
        ll_price_menber.setVisibility(View.GONE);

        tv_shopprice_hinit.getPaint().setAntiAlias(true);//中划线并清晰
        tv_shopprice_hinit.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//中划线并清晰
        //显示-未认证价格
        try {
            priceString = dataModel.goodDetail.getShop_price();
            if (!TextUtils.isEmpty(dataModel.goodDetail.getShop_price())) {
                if ("免费".equals(dataModel.goodDetail.getShop_price())) {
                    tv_market_price_normal.setText("免费");
                    tv_shopprice_hinit.setText("0");
                } else {
                    tv_market_price_normal.setText(FormatUtil.formatToSymbolPrice(dataModel.goodDetail.getShop_price()));
                    tv_shopprice_hinit.setText(FormatUtil.formatToSymbolPrice(dataModel.goodDetail.getShop_price()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (mApp.getUser() != null) {
        //采购商验证 0未提交 1认证已提交未审核 2已审核认证   3认证不通过
//            String strReal = TextUtils.isEmpty(mApp.getUser().getPurchaser_valid()) ? "0" : mApp.getUser().getPurchaser_valid();
        String strReal = "2";
        switch (strReal) {
            case "0":
                ll_price_normal.setVisibility(View.VISIBLE);
                tv_apply_menber.setText("马上认证");
                tv_apply_menber.setOnClickListener(v -> {
                    //跳转认证
                    startActivity(new Intent(getActivity(), UserApplyMenberActivity.class));
                });
                break;
            case "1":
                ll_price_normal.setVisibility(View.VISIBLE);
                tv_apply_menber.setText("审核中");
                tv_apply_menber.setOnClickListener(v -> {
                });
                break;
            case "2":
                if (dataModel.goodDetail.getVolume_price() != null) {
                    List<GOODSVOLUMEPRICE> volume_price = dataModel.goodDetail.getVolume_price();
                    try {
                        if (volume_price.size() > 0) {
                            ll_price_menber.setVisibility(View.VISIBLE);
                            ll_shopprice_hinit.setVisibility(View.GONE);
                            priceString = FormatUtil.formatToSymbolPrice(volume_price.get(0).getPrice());
                            tv_menber_promote_price1.setVisibility(View.VISIBLE);
                            tv_menber_promote_price1_content.setVisibility(View.VISIBLE);
                            tv_menber_promote_price1.setText(FormatUtil.formatToSymbolPrice(volume_price.get(0).getPrice()));
                            tv_menber_promote_price1_content.setText(volume_price.get(0).getNumber() + "件起批");
                            if (volume_price.size() > 1) {
                                line1.setVisibility(View.VISIBLE);
                                tv_menber_promote_price2.setVisibility(View.VISIBLE);
                                tv_menber_promote_price2_content.setVisibility(View.VISIBLE);
                                tv_menber_promote_price2.setText(FormatUtil.formatToSymbolPrice(volume_price.get(1).getPrice()));
                                tv_menber_promote_price2_content.setText(volume_price.get(1).getNumber() + "件起批");
                                if (volume_price.size() > 2) {
                                    line2.setVisibility(View.VISIBLE);
                                    tv_menber_promote_price3.setVisibility(View.VISIBLE);
                                    tv_menber_promote_price3_content.setVisibility(View.VISIBLE);
                                    tv_menber_promote_price3.setText(FormatUtil.formatToSymbolPrice(volume_price.get(2).getPrice()));
                                    tv_menber_promote_price3_content.setText(volume_price.get(2).getNumber() + "件起批");
                                }
                            }
                        } else {
                            tv_price_content_normal.setVisibility(View.GONE);
                            tv_apply_menber.setVisibility(View.GONE);
                            ll_price_normal.setVisibility(View.VISIBLE);

                            ll_price_menber.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ll_price_normal.setVisibility(View.VISIBLE);
                    tv_apply_menber.setVisibility(View.GONE);
                }
                break;
            case "3":
                ll_price_normal.setVisibility(View.VISIBLE);
                tv_apply_menber.setText("马上认证");
                tv_apply_menber.setOnClickListener(v -> {
                    //跳转认证
                    startActivity(new Intent(getActivity(), UserApplyMenberActivity.class));
                });
                break;
        }
//        } else {
//            ll_price_normal.setVisibility(View.VISIBLE);
//            tv_apply_menber.setText("登陆查看");
//            tv_apply_menber.setOnClickListener(v -> {
//                toLogin(LOGIN_COLLECT);
//            });
//        }
    }

    LinearLayout commentView_in;

    private void addCommentView() {
        commentView_in.removeAllViews();
        gooddetail_comment_num.setText("(" + commentmodel.goods_comment_count + resources.getString(R.string.comment_num) + ")");
        int positivenum = (int) ((commentmodel.goods_comment_positive / (float) commentmodel.goods_comment_count) * 100);
        String contentString = "<font color=#d8261b>" + positivenum + "%" + "</font>" + "<font color=#333333>" + parentActivity.getResources().getString(R.string.comment_type_positive) + "</font>";
        Spanned htmlString = Html.fromHtml(contentString);
        gooddetail_comment_positive.setText(htmlString);
        int count = 0;
        if (commentmodel.goods_comment_list.size() >= 5) {
            count = 5;
        } else {
            count = commentmodel.goods_comment_list.size();
        }
        if (count == 0) {
            headline.setVisibility(View.GONE);
            return;
        } else {
            headline.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < count; i++) {
            GoodsCommetView view = (GoodsCommetView) LayoutInflater.from(parentActivity).inflate(R.layout.goods_comment_item, null);
            final COMMENT_LIST comment_list = commentmodel.goods_comment_list.get(i);
            view.bindData(comment_list);
            if (i == 0 || count == 1) {
                view.setDivliverVisible(View.GONE);
            } else {
                view.setDivliverVisible(View.VISIBLE);
            }
            commentView_in.addView(view);
        }
    }

    /**
     * 去添加地址
     */
    private void toAddAddress() {
        Intent intent = new Intent(getActivity(), AddressAddActivity.class);
        intent.putExtra("isfirst", true);
        startActivityForResult(intent, ADDRESS_BALANCE);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private boolean needShow = false;


    private float specification_price_one = 0.00f;

    /**
     * 更新商品属性
     */
    void updateGoodsDetailDraft() {
        // 拿到商品返回值立马给商品属性状态设定
//        GoodDetailDraft.getInstance().clear();
        GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;
        if (null != dataModel.goodDetail.specification && dataModel.goodDetail.specification.size() > 0) {
            for (int i = 0; i < dataModel.goodDetail.specification.size(); i++) {
                SPECIFICATION specification = dataModel.goodDetail.specification.get(i);
                if (null != specification.getAttr_type() && 0 == specification.getAttr_type().compareTo(SPECIFICATION.SINGLE_SELECT)) {
                    SPECIFICATION_VALUE specification_value_one = specification.value.get(0);
                    String op;
                    if (specification_value_one.getPrice().equals("")) {
                        op = "0";
                    } else {
                        op = specification_value_one.getPrice();
                    }
                    specification_price_one += Float.parseFloat(op);
                    GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_one);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LG.i("isLogin:" + isLogin);
        if (isLogin) {
            if (dataModel == null) {
                dataModel = new GoodsDetailModel(getActivity());
                dataModel.addResponseListener(this);
            }
            dataModel.fetchGoodDetail(goods_id, area_id, object_id, rec_type, true);
            getGoodeShippingFee();
        }
    }

    public String getSpeciationDesc() {
        String none = getResources().getString(R.string.none);
        String speciationDesc = "";
        if (dataModel.goodDetail.getProducts().size() <= 0) {
            isHaveSpec = false;
        } else {
            if (dataModel.goodDetail.specification.size() > 1) {
                isHaveSpec = true;
                //GoodDetailDraft.getInstance().goodDetail.getSizeListByColorId(dataModel.goodDetail.specification.get(0).getValue().get(0).getId()+"")
                GoodDetailDraft.getInstance().goodDetail.getProducts().get(0).setSizeSelectCount(1);
                speciationDesc = speciationDesc + GoodDetailDraft.getInstance().getSelectAllCount();
            } else {
                isHaveSpec = false;
            }
        }
        return speciationDesc;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_comment://去评论列表
                if (listener != null) {
                    listener.currentTab(2);
                }
                break;
            case R.id.gooddetail_consult://咨询
                toConsult();
                break;
            case R.id.good_category_item://去商品属性
                toSpecification();
                break;
            case R.id.ly_discount://"[店铺活动]"
                BottomShowShopDiscountDialog.newInstance().showDialog(getFragmentManager(), dataModel.goodDetail.getFavours());
                break;
            case R.id.ly_coupons://优惠券相关
                if (mApp.getUser() != null) {
                    BottomChooseCouponsDialog.newInstance().showDialog(getFragmentManager(), goods_id);
                } else {
                    toLogin(100);
                }
                break;

        }

    }

    /**
     * 去登录
     */
    void toLogin(int requsetCode) {
        Intent intent = new Intent(parentActivity, LoginActivity.class);
        startActivityForResult(intent, requsetCode);
        parentActivity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
        String nol = getResources().getString(R.string.no_login);
        ToastView toast = new ToastView(parentActivity, nol);
        toast.setGravity(Gravity.CENTER);
        toast.show();
    }

    /**
     * 去商品属性页
     */
    void toSpecification() {
        if (!isHaveSpec) {
            ToastUtil.getInstance().makeLongToast(getActivity(), "没有库存");
            return;
        }
        for (int i = 0; i < dataModel.goodDetail.specification.size(); i++) {
            SPECIFICATION specification = dataModel.goodDetail.specification.get(i);
            if (null != specification.getAttr_type() && 0 == specification.getAttr_type().compareTo(SPECIFICATION.SINGLE_SELECT) && false == GoodDetailDraft.getInstance().isHasSpecName(specification.getName())) {
                SPECIFICATION_VALUE specification_value_one = specification.value.get(0);
                GoodDetailDraft.getInstance().addSelectedSpecification(specification_value_one);
            }
        }
        if (dataModel.goodDetail.getGoods_number() != null) {
            if (dataModel != null && null != dataModel.goodDetail) {
                GoodDetailDraft.getInstance().goodDetail = dataModel.goodDetail;
            }
            String urlImgStr = "";
            if (dataModel.goodDetail.getPictures().size() > 0) {
//                it.putExtra("imgurl", dataModel.goodDetail.getPictures().get(0).getSmall());
                urlImgStr = dataModel.goodDetail.getPictures().get(0).getSmall();
            }
            ChooseGoodSpecificationActivity_Builder.intent(getActivity()).price(priceString).goodDes(dataModel.goodDetail.getGoods_name()).imgurl(urlImgStr).starForResult(SPACIFICATION);
            parentActivity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
        } else {
            String che = getResources().getString(R.string.check_the_network);
            ToastView toast = new ToastView(parentActivity, che);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void collectCreate() {
        dataModel.collectCreate(dataModel.goodDetail.getId() + "");
    }

    public void collectDelete() {
        dataModel.collectDelete(dataModel.goodDetail.getId() + "");
    }

    // 获取本地浏览历史
    private void getBrowse() {
        Runnable getHistoryRunnable = new Runnable() {
            @Override
            public void run() {
                Sqlcl s = new Sqlcl(parentActivity);
                Cursor c = s.getGoodBrowse();
                int i = 0;
                while (c.moveToNext()) {
                    int goodid = c.getInt(1);
                    String goodsString = c.getString(2);

                    GOODS goods = null;
                    if (!TextUtils.isEmpty(goodsString)) {
                        try {
                            goods = GOODS.fromJson(new JSONObject(goodsString));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (i < 8) {
                        historyList.add(goods);
                    } else {
                        break;
                    }
                    i++;
                }
                c.close();
                Message msg = new Message();
                msg.obj = "get_history_succeed";
                historyHandler.sendMessage(msg);
            }
        };
        new Thread(getHistoryRunnable).start();
    }

    /**
     * 保存浏览记录
     */
    private void saveBrowse() {
        if (!issave) {
            SaveBrowseNew.getInfo(parentActivity, dataModel.goodDetail);
            issave = true;
        }
        LG.i("保存浏览记录");
    }

    ArrayList<GOODS> historyList = new ArrayList<>();
    View historyLayout;
    View historyLayout_empty;
    FlowLayout mHListView;

    /**
     * 猜你喜欢数据填充
     */
    void initHistory(View view) {

        historyLayout = view.findViewById(R.id.goodsdetail_history);
        historyLayout_empty = historyLayout.findViewById(R.id.history_layout_empty);
        historyLayout_empty.setVisibility(View.GONE);
        mHListView = (FlowLayout) historyLayout.findViewById(R.id.history_hlistview);
        ((FrameLayout) mHListView.getParent()).setMinimumHeight(7 * (int) getResources().getDimension(R.dimen.dp_10) + itemWith);
        historyLayout_empty.setMinimumHeight(7 * (int) getResources().getDimension(R.dimen.dp_10) + itemWith);
    }

    int itemWith = 0;

    void addHistoryToLayout() {
        mHListView.removeAllViews();
        int size = historyList.size() >= 6 ? 6 : historyList.size();
        if (size == 0) {
            historyLayout_empty.setVisibility(View.VISIBLE);
            mHListView.setVisibility(View.GONE);
            return;
        } else {
            historyLayout_empty.setVisibility(View.GONE);
            mHListView.setVisibility(View.VISIBLE);
        }
        mHListView.setBackgroundColor(Color.parseColor("#ffffff"));
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < size; i++) {
            LinearLayout rowLayout = (LinearLayout) LayoutInflater.from(parentActivity).inflate(R.layout.item_goodsdetail_mylove, null);
            RelativeLayout imageViewLL = (RelativeLayout) rowLayout.findViewById(R.id.goods_image_ll);
            ImageView goodsImageIV = (ImageView) rowLayout.findViewById(R.id.goods_image);
            TextView goodsNameText = (TextView) rowLayout.findViewById(R.id.goods_name);
            TextView goodsPriceText = (TextView) rowLayout.findViewById(R.id.goods_price);
            ImageLoader.getInstance().displayImage(historyList.get(i).getImg().getThumb(), goodsImageIV);
            goodsNameText.setText(historyList.get(i).getGoods_name());
            if (FormatUtil.formatStrToFloat(historyList.get(i).getFormated_promote_price()) == 0) {
                if (FormatUtil.formatStrToFloat(historyList.get(i).getShop_price()) == 0) {
                    goodsPriceText.setText("免费");
                } else {
                    goodsPriceText.setText(historyList.get(i).getShop_price());
                }
            } else {
                goodsPriceText.setText(historyList.get(i).getFormated_promote_price());
            }
            final int position = i;
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parentActivity, GoodsDetailActivity.class);
                    intent.putExtra("goods_id", historyList.get(position).getId() + "");
                    startActivity(intent);
                    parentActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }
            });
            LinearLayout.LayoutParams imageParamas = new LinearLayout.LayoutParams(itemWith, itemWith);
            imageViewLL.setLayoutParams(imageParamas);
            ViewGroup.MarginLayoutParams params3 = new ViewGroup.MarginLayoutParams(itemWith, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params3.setMargins((int) getResources().getDimension(R.dimen.dp_10), (int) getResources().getDimension(R.dimen.dp_10), 0, 0);
            rowLayout.setLayoutParams(params3);
            mHListView.addView(rowLayout, params3);

        }
    }

    private Handler historyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            addHistoryToLayout();
        }
    };


    private final int LOGIN_COLLECT = 10010;//收藏登录
    private final int LOGIN_REDPAPER = 10011;//红包登录
    private final int LOGIN_ADDCART = 10012;//加入购物车登录
    private final int LOGIN_BALANCE = 10013;//去结算登录
    private final int LOGIN_SHOPCART = 10014;//购物车登录
    private final int SPACIFICATION = 10015;//商品属性
    private final int SPACIFICATION_ADDCART = 10016;//商品属性
    private final int SPACIFICATION_BALANCE = 10017;//商品属性
    private final int ADDRESS_BALANCE = 10018;//去结算地址;
    private final int SHOP_LIST = 10019;//去店铺地址;
    private final int SEND_MESSAGE = 10020;//去聊天页面

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_COLLECT) {
            if (resultCode == Activity.RESULT_OK) {
                isLogin = true;
                dataModel.fetchGoodDetail(goods_id + "", area_id, object_id, rec_type, false);//刷新商品详情
                getGoodeShippingFee();
            }
        } else if (requestCode == LOGIN_REDPAPER) {
            if (resultCode == Activity.RESULT_OK) {
                isLogin = true;
                needShow = true;
            }
        } else if (requestCode == LOGIN_ADDCART) {//加入购物车前的登录
            if (resultCode == Activity.RESULT_OK) {
                isLogin = true;
                cartCreate(false);
            }
        } else if (requestCode == LOGIN_BALANCE) {//立即购买前的登录
            if (resultCode == Activity.RESULT_OK) {
                isLogin = true;
                cartCreate(true);
            }
        } else if (requestCode == LOGIN_SHOPCART) {//进入购物车前的登录
            if (resultCode == Activity.RESULT_OK) {
                isLogin = true;
                parentActivity.toShopCart();
            }
        } else if (requestCode == SPACIFICATION) {//商品属性页
            goodCategoryTextView.setText(GoodDetailDraft.getInstance().getSelectAllCount());
            goodNumTextView.setText(GoodDetailDraft.getInstance().goodAllQuantity + "");
            LG.i("resultCode>>:" + resultCode + "requestCode+>>>" + requestCode);
            if (resultCode == SPACIFICATION_ADDCART) {
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(LOGIN_ADDCART);
                } else {
                    cartCreate(false);
                }
            } else if (resultCode == SPACIFICATION_BALANCE) {
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    toLogin(LOGIN_BALANCE);
                } else {
                    cartCreate(true);
                }
            }
        } else if (requestCode == ADDRESS_BALANCE) {//地址车页面的返回
            if (resultCode == Activity.RESULT_OK) {
                addressModel.getAddressList();
            }
        } else if (requestCode == SHOP_LIST) {//地址车页面的返回
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    seller_collected_num.setText(data.getStringExtra("collect_num") + getResources().getString(R.string.follower_num));
                }
            }
        } else if (requestCode == 2) {
            if (data != null) {
                StringBuffer sbf = new StringBuffer();
                sbf.append(data.getStringExtra("country_name") + " ");
                sbf.append(data.getStringExtra("province_name") + " ");
                sbf.append(data.getStringExtra("city_name") + " ");
                sbf.append(data.getStringExtra("county_name"));
                area_id = data.getStringExtra("city_id");
                editor.putString("area_id", data.getStringExtra("city_id"));
                editor.putString("sendArea", sbf.toString());
                CITY c = new CITY();
                c.setId(area_id);
                c.setName(data.getStringExtra("city_name"));
                try {
                    editor.putString("localString", c.toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.commit();
                addresstext.setText(sbf.toString());
                EventBus.getDefault().post(new MyEvent("refresh_sendarea"));
                dataModel.fetchGoodDetail(goods_id, area_id, object_id, rec_type, true);
                getGoodeShippingFee();
            }
        } else if (requestCode == SHOP_LIST) {//地址车页面的返回
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    seller_collected_num.setText(data.getStringExtra("collect_num") + getResources().getString(R.string.follower_num));
                }
            }
        }
    }

    /**
     * 去结算
     */
    void toBalance() {
//        Intent intent = new Intent(parentActivity, BalanceActivity.class);
        Intent intent = new Intent(parentActivity, NewBalanceActivity.class);
        intent.putExtra("rec_ids", cartModel.rec_id);
        intent.putExtra("address_id", address_id);
        startActivity(intent);
        parentActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public boolean collectFlag = false;

    /**
     * 添加到购物车
     */
    public void cartCreate(boolean flag) {
        isBuyNow = flag;//true 立即购买
        if (noGoods) {
            ToastView toastView = new ToastView(parentActivity, "该商品已下架");
            toastView.show();
            return;
        }
        if (!isHaveSpec) {
            ToastView toastView = new ToastView(parentActivity, "暂无库存");
            toastView.show();
            return;
        }
        if (GoodDetailDraft.getInstance().goodAllQuantity <= 0) {
            ToastUtil.getInstance().makeShortToast(getActivity(), "请选择规格");
            return;
        }
//        if (AppConst.GROUPBUY_GOODS.equals(rec_type) || AppConst.GRAB_GOODS.equals(rec_type) || AppConst.PROMOTE_GOODS.equals(rec_type)) {
        if (AppConst.GROUPBUY_GOODS.equals(rec_type) || AppConst.GRAB_GOODS.equals(rec_type)) {
            if (!isBuyNow) {
                if(AppConst.GROUPBUY_GOODS.equals(rec_type)){
                    Intent intent = new Intent(getContext(), GoodsDetailActivity.class);
                    intent.putExtra(IntentKeywords.GOODS_ID, goods_id);
//                    intent.putExtra(IntentKeywords.OBJECT_ID, object_id);
//                    intent.putExtra(IntentKeywords.REC_TYPE, AppConst.GROUPBUY_GOODS);
//                    goods_id = getActivity().getIntent().getStringExtra(IntentKeywords.GOODS_ID);
//                    object_id = getActivity().getIntent().getStringExtra(IntentKeywords.OBJECT_ID);
//                    rec_type = getActivity().getIntent().getStringExtra(IntentKeywords.REC_TYPE);
                    startActivity(intent);
                }
                return;
            } else {
                if (AppConst.GROUPBUY_GOODS.equals(rec_type) && GoodDetailDraft.getInstance().goodAllQuantity < dataModel.goodDetail.getBatch_amount()) {
                    ToastUtil.getInstance().makeShortToast(getActivity(), "最小起批量" + dataModel.goodDetail.getBatch_amount() + "件");
                    return;
                }
            }
        } else if (AppConst.GENERAL_GOODS.equals(rec_type)) {
            if (dataModel.goodDetail.getVolume_price() != null) {
                List<GOODSVOLUMEPRICE> volume_price = dataModel.goodDetail.getVolume_price();
                if (volume_price.size() > 0&&GoodDetailDraft.getInstance().goodAllQuantity<volume_price.get(0).getNumber()) {
                    ToastUtil.getInstance().makeShortToast(getActivity(), "最小起批量" + volume_price.get(0).getNumber() + "件");
                    return;
                }
            }else {
                ToastUtil.getInstance().makeShortToast(getActivity(), "暂无售价，无法购买");
                return;
            }
        }
        if (cartModel == null) {
            cartModel = new ShoppingCartModel(parentActivity);
            cartModel.addResponseListener(this);
        }
        List<PRODUCTNUMBYCF> productsSublit = new ArrayList<>();
        productsSublit.hashCode();
        GoodDetailDraft.getInstance().goodDetail.getProducts().hashCode();
        productsSublit.addAll(GoodDetailDraft.getInstance().goodDetail.getProducts());
        productsSublit.hashCode();
        cartModel.cartCreate(goods_id, productsSublit, GoodDetailDraft.getInstance().goodAllQuantity, dataModel.goodDetail.getObject_id(), dataModel.goodDetail.getActivity_type());
    }

    public void toConsult() {
//        Intent intent = new Intent(parentActivity, ConsultActivity.class);
        Intent intent = new Intent(parentActivity, ECChatActivity.class);
        intent.putExtra("type", "goods_consult");
        intent.putExtra("goods_title", dataModel.goodDetail.getGoods_name());
        intent.putExtra("goods_hx_text", dataModel.goodDetail.getHx_text());
        intent.putExtra(EaseConstant.EXTRA_USER_ID, dataModel.goodDetail.getHxkf_id());
        intent.putExtra("hxName", dataModel.goodDetail.getHxkf_name());
        if (rec_type.equals(AppConst.GENERAL_GOODS)) {
            intent.putExtra("goods_price", priceString);
        } else {
            intent.putExtra("goods_price", priceString);
        }
        intent.putExtra("goods_id", dataModel.goodDetail.getId());
        if (dataModel.goodDetail.getPictures() != null && dataModel.goodDetail.getPictures().size() != 0) {
            intent.putExtra("goods_img", dataModel.goodDetail.getPictures().get(0).getThumb());
        }
        startActivity(intent);
        parentActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void onEvent(MyEvent event) {
        //接受商品属性页面传值更新商品价格
        if (1000 == event.getmTag()) {
            goodCategoryTextView.setText("" + GoodDetailDraft.getInstance().getSelectAllCount());
            goodNumTextView.setText(GoodDetailDraft.getInstance().goodAllQuantity);
        }
    }

    public void getGoodeShippingFee() {
        RetrofitAPIManager.getAPIGoods()
                .getShippingFee(GoodsQuery.getInstance().getShippingFee(goods_id, area_id))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<AddressData>() {
                    @Override
                    public void onNext(AddressData addressData) {
                        tv_goodsfee.setText("运费  " + addressData.getShipping_fee());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void setGroupViewIU(String status) {
        //团批抢批状态 0为未开始，1为进行中，2为过期
        switch (status) {
            case "0":
                parentActivity.buyNowTextView.setBackgroundColor(getResources().getColor(R.color._26a96d));
                isBuyNow = false;
                break;
            case "1":
                parentActivity.buyNowTextView.setBackgroundColor(getResources().getColor(R.color.title_bg_color));
                isBuyNow = true;
                break;
            case "2":
                parentActivity.buyNowTextView.setBackgroundColor(getResources().getColor(R.color.TextColorGray));
                isBuyNow = false;
                break;
        }

    }
}
