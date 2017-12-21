package com.ecjia.view.fragment;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseFragment;
import com.ecjia.view.activity.goodsorder.balance.NewBalanceActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.HomeModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.webimageview.WebImageView;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.adapter.YouLikeAdapter;
import com.ecjia.entity.responsemodel.ADDRESS;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.NEWGOODITEM;
import com.ecjia.entity.responsemodel.SIMPLEGOODS;
import com.ecjia.view.activity.AddressAddActivity;
import com.ecjia.view.activity.goodsorder.balance.BalanceActivity;
import com.ecjia.view.adapter.ShoppingCartAdapter;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.ShoppingCartModel;
import com.ecjia.entity.responsemodel.GOODS_LIST;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.util.EventBus.MyEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 购物车
 */
public class ShoppingCartFragment extends BaseFragment implements IXListViewListener, HttpResponse {

    private View view;
    private TextView footer_total;
    private TextView footer_balance;
    private FrameLayout shop_car_null;
    private FrameLayout shop_car_isnot;
    private XListView xlistView;
    private ShoppingCartAdapter shopCarAdapter;
    private ShoppingCartModel shoppingCartModel;
    public Handler messageHandler;
    private ImageView back;

    public static ImageView iv_open_menu;
    private AddressModel addressModel;
    private TextView footer_totalno;
    LinearLayout buttomitem, buttomleft, buttomright;
    private SharedPreferences shared;
    public Handler Intenthandler;
    private boolean isEdit = false;
    private TextView shopcar_edit, shopcar_go_home;
    private String uid;
    private LinearLayout footer_balance2;
    private TextView shopcar_delete;
    private LinearLayout linear;
    private LinearLayout youlike;
    private boolean islikeVisible = true;
    private ViewPager youlike_viewpager;
    private List<View> pagerList;
    private TextView youlike_item_name, youlike_item_price;
    private WebImageView youlike_item_photo;
    private View buttomview;
    public ArrayList<Object> mylist;
    private HomeModel dataModel;
    private ImageView youlike_arrow;
    private YouLikeAdapter youLikeAdapter;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private EventBus eventBus;
    private ArrayList<GOODS_LIST> deleteList = new ArrayList<GOODS_LIST>();//需要删除的商品集合
    private ArrayList<GOODS_LIST> cachelist = new ArrayList<GOODS_LIST>();//缓存未选中的图片
    private ArrayList<GOODS_LIST> buylist = new ArrayList<GOODS_LIST>();//结算商品集合
    private LinearLayout checkallitem;
    private CheckBox checkBoxall;
    private StringBuffer buylistStringBuffer = new StringBuffer();//结算的商品id字符串
    private String yuan;
    TabsFragment.FragmentListener fragmentListener;
    private MyDialog myDialog;

    @Override
    public void onAttach(Activity activity) {
        fragmentListener = (TabsFragment.FragmentListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shop_car, null);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        footer_balance2 = (LinearLayout) view.findViewById(R.id.shop_car_footer_balance2);
        buttomleft = (LinearLayout) view.findViewById(R.id.shop_car_buttomleft);
        buttomright = (LinearLayout) view.findViewById(R.id.shop_car_buttomright);
        shopcar_go_home = (TextView) view.findViewById(R.id.shopcar_go_home);
        shopcar_delete = (TextView) view.findViewById(R.id.shop_car_footer_delete);

        checkallitem = (LinearLayout) view.findViewById(R.id.cart_checkall_item);
        checkBoxall = (CheckBox) view.findViewById(R.id.cart_checkall_check);
        checkallitem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBoxall.isChecked()) {
                    checkBoxall.setChecked(true);
                    shopCarAdapter.CheckAll();
                } else {
                    checkBoxall.setChecked(false);
                    shopCarAdapter.unCheck();
                }
                shopCarAdapter.notifyDataSetChanged();
                refreshTotalfee();
            }
        });

        shared = getActivity().getSharedPreferences("userInfo", 0);
        setInternetHandler();
        shop_car_null = (FrameLayout) view.findViewById(R.id.shop_car_null);
        shop_car_isnot = (FrameLayout) view.findViewById(R.id.shop_car_isnot);

        xlistView = (XListView) view.findViewById(R.id.shop_car_list);
        xlistView.setPullLoadEnable(false);
        xlistView.setPullRefreshEnable(false);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this, 1);
        mylist = new ArrayList<Object>();
        dataModel = new HomeModel(getActivity());

        for (int i = 0; i < dataModel.simplegoodsList.size(); i++) {
            mylist.add(dataModel.simplegoodsList.get(i));
        }


        messageHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.what == shopCarAdapter.GOODNUM_MIN || msg.what == shopCarAdapter.GOODNUM_EDIT || msg.what == shopCarAdapter.GOODNUM_MAX) {
                    GOODS_LIST good = shopCarAdapter.lists.get(msg.arg1).getGoodslist().get(msg.arg2);
                    shoppingCartModel.updateGoods(good.getRec_id(), good.getGoods_number());
                } else if (msg.what == shopCarAdapter.GOODNUM_DEL) {
                    GOODS_LIST good = shopCarAdapter.lists.get(msg.arg1).getGoodslist().get(msg.arg2);
                    int[] rec_ids = new int[1];
                    rec_ids[0] = good.getRec_id();
                    shoppingCartModel.deleteGoods(rec_ids);
                    shopCarAdapter.getData().get(msg.arg1).getGoodslist().remove(msg.arg2);
                    if (shopCarAdapter.getData().get(msg.arg1).getGoodslist().size() == 0) {
                        shopCarAdapter.getData().remove(msg.arg1);
                    }
                    shopCarAdapter.notifyDataSetChanged();
                    refreshTotalfee();
                }

            }
        };

        if (null == shoppingCartModel) {
            shoppingCartModel = new ShoppingCartModel(getActivity());
            shoppingCartModel.addResponseListener(this);
        }
        if (shopCarAdapter == null) {
            shopCarAdapter = new ShoppingCartAdapter(this.getActivity(), shoppingCartModel.newcartList, 1);
        }
        shopCarAdapter.eventBus = eventBus;
        shopCarAdapter.setMessageHandler(messageHandler);
        xlistView.setAdapter(shopCarAdapter);

        //获取页脚所需数据
        new HomeModel(getActivity()).readHomeDataCache();
        //添加页脚（你可能会喜欢）
        buttomview = LayoutInflater.from(getActivity()).inflate(R.layout.shopcart_buttomview, null);

        if (mylist.size() == 0) {
            buttomview.setVisibility(View.GONE);
        }
        youlike = (LinearLayout) buttomview.findViewById(R.id.maybe_youlike);
        linear = (LinearLayout) buttomview.findViewById(R.id.shopcar_linear);
        youlike_arrow = (ImageView) buttomview.findViewById(R.id.youlike_arrow);

        youlike_viewpager = (ViewPager) buttomview.findViewById(R.id.youlike_viewpager);

        pagerList = new ArrayList<View>();

        //你可能会喜欢set值
        final Resources res = getResources();
        final ArrayList<SIMPLEGOODS> listData = dataModel.simplegoodsList;
        for (int i = 0; i < mylist.size(); i++) {
            View view = inflater.inflate(R.layout.youlike_item, null);

            view.setMinimumHeight((int) res.getDimension(R.dimen.youlike_photo_size));
            view.setMinimumWidth((int) res.getDimension(R.dimen.youlike_photo_size));
            youlike_item_name = (TextView) view.findViewById(R.id.youlike_item_name);
            youlike_item_price = (TextView) view.findViewById(R.id.youlike_item_price);
            youlike_item_photo = (WebImageView) view.findViewById(R.id.youlike_item_photo);

            youlike_item_name.setText(listData.get(i).getName());
            youlike_item_price.setText(listData.get(i).getPromote_price());
            imageLoader.displayImage(listData.get(i).getImg().getThumb(), youlike_item_photo);
            //“你可能会喜欢”item点击跳转
            view.setTag(i + "");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), GoodsDetailActivity.class).putExtra("good_id", listData.get(Integer.valueOf((String) view.getTag())).getId()));
                }
            });
            pagerList.add(view);
        }

        youLikeAdapter = new YouLikeAdapter(getActivity(), pagerList);
        youlike_viewpager.setAdapter(youLikeAdapter);
        youlike_viewpager.setOffscreenPageLimit(3);// 设置幕后item的缓存数目
        youlike_viewpager.setPageMargin(70);// 设置页与页之间的间距
        youlike_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {


            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //设置初始图片位置
        int currentItem = pagerList.size() * 1000;
        youlike_viewpager.setCurrentItem(currentItem);
        linear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {

                return youlike_viewpager.dispatchTouchEvent(ev);
            }
        });

        footer_total = (TextView) view.findViewById(R.id.shop_car_footer_total);
        footer_balance = (TextView) view.findViewById(R.id.shop_car_footer_balance);
        footer_totalno = (TextView) view.findViewById(R.id.shop_car_totalno);
        buttomitem = (LinearLayout) view.findViewById(R.id.shop_car_buttomitem);
        addressModel = new AddressModel(getActivity());
        addressModel.addResponseListener(this);
        //你可能会喜欢事件监听
        youlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (islikeVisible) {
                    linear.setVisibility(View.VISIBLE);
                    youlike_arrow.setImageResource(R.drawable.search_hidden);
                    islikeVisible = false;
                } else {
                    linear.setVisibility(View.GONE);
                    youlike_arrow.setImageResource(R.drawable.search_showchild);
                    islikeVisible = true;
                }
            }
        });


        footer_balance.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getCheckOrdergoods();
                if (buylist.size() > 0) {
                    writeCache();
                    addressModel.getAddressList();
                } else {
                    ToastView toastView = new ToastView(ShoppingCartFragment.this.getActivity(), res.getString(R.string.choose_nothing));
                    toastView.show();
                }

            }
        });


        //为空时增加去逛逛按钮
        shopcar_go_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(shared.getString("uid", ""))) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    TabsFragment.getInstance().one();
                }
            }
        });


        back = (ImageView) view.findViewById(R.id.top_view_back);
        yuan = getResources().getString(R.string.yuan_unit);
        iv_open_menu = (ImageView) view.findViewById(R.id.top_view_list);
        iv_open_menu.setVisibility(View.GONE);
        back.setVisibility(View.GONE);
        iv_open_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //首页左上点击弹出个人中心
                fragmentListener.Open();
            }
        });

        shopcar_edit = (TextView) view.findViewById(R.id.shopcar_edit);
        //购物车右上角编辑完成按钮
        shopcar_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopcar_delete.setClickable(false);
                String done = res.getString(R.string.shopcaritem_done);
                if (!isEdit) {
                    shopCarAdapter.flag = 2;
                    checkBoxall.setChecked(isCheckalldelete());
                    xlistView.setPullRefreshEnable(false);
                    buttomleft.setVisibility(View.INVISIBLE);
                    footer_balance.setVisibility(View.GONE);
                    footer_balance2.setVisibility(View.VISIBLE);
                    isEdit = true;
                    shopcar_edit.setText(done);
                    shopcar_delete.setClickable(true);
                } else {
                    shopCarAdapter.flag = 1;
                    checkBoxall.setChecked(isCheckall());
                    xlistView.setPullRefreshEnable(true);
                    shopCarAdapter.notifyDataSetChanged();
                    isEdit = false;
                    buttomleft.setVisibility(View.VISIBLE);
                    footer_balance.setVisibility(View.VISIBLE);
                    footer_balance2.setVisibility(View.GONE);
                    shopcar_edit.setText(res.getString(R.string.collect_compile));
                    shopcar_delete.setClickable(false);
                }
                shopCarAdapter.notifyDataSetChanged();
            }
        });

        //底部删除按钮
        shopcar_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeleteGoodList();
                Resources resources = getResources();
                final String delete = resources.getString(R.string.collect_delete);
                String shopcar_deletes = resources.getString(R.string.shopcar_deletes);
                final String deletesuccess = resources.getString(R.string.collect_delete_success);
                if (deleteList.size() > 0) {
                    final MyDialog dialog = new MyDialog(getActivity(), delete, shopcar_deletes);
                    dialog.show();
                    dialog.positive.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastView toast = new ToastView(getActivity(), deletesuccess);
                            toast.show();
                            deleteGood();
                            dialog.dismiss();
                        }


                    });
                    dialog.negative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else {

                }

            }
        });


        return view;
    }

    //批量删除商品
    private void deleteGood() {
        int size = deleteList.size();
        int[] rec_ids = new int[size];
        for (int i = 0; i < size; i++) {
            rec_ids[i] = deleteList.get(i).getRec_id();
        }
        shoppingCartModel.deleteGoods(rec_ids);
        for (int i = 0; i < shopCarAdapter.getData().size(); i++) {
            ArrayList<NEWGOODITEM> list = shopCarAdapter.getData();
            ArrayList<GOODS_LIST> good = list.get(i).getGoodslist();
            for (int j = 0; j < good.size(); j++) {
                if (good.get(j).getIscheckDelete()) {
                    shopCarAdapter.getData().get(i).getGoodslist().remove(j);
                    j--;
                }
            }
            if (list.get(i).getIscheckDelete()) {
                shopCarAdapter.getData().remove(i);
                i--;
            }
        }
        shopCarAdapter.notifyDataSetChanged();
        refreshTotalfee();
    }

    private void setInternetHandler() {
    }


    @SuppressLint("NewApi")
    public void setShopCart() {

        if (shoppingCartModel.newcartList.size() == 0) {
            shop_car_null.setVisibility(View.VISIBLE);
            shop_car_isnot.setVisibility(View.GONE);
            buttomitem.setVisibility(View.GONE);
            shopcar_edit.setVisibility(View.GONE);
        } else {
            xlistView.setPullRefreshEnable(true);
            buttomitem.setVisibility(View.VISIBLE);
            shop_car_isnot.setVisibility(View.VISIBLE);
            shop_car_null.setVisibility(View.GONE);
            shopcar_edit.setVisibility(View.VISIBLE);
            shopCarAdapter.setData(shoppingCartModel.newcartList);
            shopCarAdapter.notifyDataSetChanged();
        }
        refreshTotalfee();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(shared.getString("uid", ""))) {
            shopcar_go_home.setText(R.string.click_to_login);
            shop_car_null.setVisibility(View.VISIBLE);
            shop_car_isnot.setVisibility(View.GONE);
            buttomitem.setVisibility(View.GONE);
        } else {
            shopcar_go_home.setText(R.string.shopcar_add);
            shoppingCartModel.cartList(true);
        }
        fragmentListener.addIgnoredView(xlistView);
        if (TextUtils.isEmpty(shared.getString("uid", ""))) {
            iv_open_menu.setImageResource(R.drawable.profile_no_avarta_icon);
        } else {
            iv_open_menu.setImageResource(R.drawable.profile_no_avarta_icon_light);
        }
        MobclickAgent.onPageStart("ShopCart");
    }

    @Override
    public void onPause() {
        writeCache();
        super.onPause();
        fragmentListener.removeIgnoredView(xlistView);
        MobclickAgent.onPageEnd("ShopCart");
    }

    @Override
    public void onRefresh(int id) {
        writeCache();
        shoppingCartModel.cartList(false);
    }

    @Override
    public void onLoadMore(int id) {

    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            shoppingCartModel.readcartGoodsDataCache();
            shoppingCartModel.cartList(true);
        }else if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                addressModel.getAddressList();
            }
        }
    }

    @Override
    public void onDestroyView() {
        eventBus.unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onEvent(MyEvent event) {
        if ("RefreshGoodsTotalFee".equals(event.getMsg())) {
            refreshTotalfee();
        }
        if ("checkAll".equals(event.getMsg())) {
            checkBoxall.setChecked(true);
        }

        if ("uncheckAll".equals(event.getMsg())) {
            checkBoxall.setChecked(false);
        }

    }

    private void refreshTotalfee() {
        cachelist.clear();
        int goods_num = 0;
        int total_goods_num = 0;
        float totalprice = 0;
        ArrayList<NEWGOODITEM> list = shopCarAdapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ArrayList<GOODS_LIST> goods_list = list.get(i).getGoodslist();
            for (int j = 0; j < goods_list.size(); j++) {
                GOODS_LIST good = goods_list.get(j);
                if (good.getIsCheckedbuy()) {
                    goods_num += Integer.valueOf(good.getGoods_number());
                    totalprice += Integer.valueOf(good.getGoods_number()) * Float.valueOf(good.getGoods_price());
                }
                total_goods_num += Integer.valueOf(good.getGoods_number());
            }
        }
        shoppingCartModel.goods_num = total_goods_num;

        BigDecimal b = new BigDecimal(totalprice);
        totalprice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        if (totalprice < 0) {
            footer_total.setText(yuan + "0.00");
        } else {
            footer_total.setText(yuan + String.format("%.2f", (double) totalprice));
        }

        footer_totalno.setText("" + goods_num);
        TabsFragment.getInstance().setShoppingcartNum();
    }

    //获取需要删除集合的列表
    private void getDeleteGoodList() {
        deleteList.clear();
        ArrayList<NEWGOODITEM> list = shopCarAdapter.getData();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ArrayList<GOODS_LIST> goods_list = list.get(i).getGoodslist();
            for (int j = 0; j < goods_list.size(); j++) {
                GOODS_LIST good = goods_list.get(j);
                if (good.getIscheckDelete()) {
                    deleteList.add(good);
                }
            }
        }
    }

    //未选中商品写入缓存
    private void writeCache() {
        ArrayList<NEWGOODITEM> list = shopCarAdapter.getData();
        if (list.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ArrayList<GOODS_LIST> goods_list = list.get(i).getGoodslist();
                for (int j = 0; j < goods_list.size(); j++) {
                    GOODS_LIST good = goods_list.get(j);
                    if (!good.getIsCheckedbuy()) {
                        try {
                            jsonArray.put(good.toJson());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            shoppingCartModel.fileSave(jsonArray.toString(), "cartGoods");
        } else {
            shoppingCartModel.fileSave("", "cartGoods");
        }
    }

    //检查商品是否全部选中
    private boolean isCheckall() {
        if (shoppingCartModel.newcartList.size() > 0) {
            int size = shoppingCartModel.newcartList.size();
            for (int i = 0; i < size; i++) {
                if (!shoppingCartModel.newcartList.get(i).getIsCheckedbuy()) {
                    return false;
                } else {
                    if (i == size - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //检查商品是否全部选中
    private boolean isCheckalldelete() {
        if (shoppingCartModel.newcartList.size() > 0) {
            int size = shoppingCartModel.newcartList.size();
            for (int i = 0; i < size; i++) {
                if (!shoppingCartModel.newcartList.get(i).getIscheckDelete()) {
                    return false;
                } else {
                    if (i == size - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //筛选需要结算的商品
    private void getCheckOrdergoods() {
        buylist.clear();
        buylistStringBuffer.setLength(0);
        ArrayList<NEWGOODITEM> list = shopCarAdapter.getData();
        if (list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                ArrayList<GOODS_LIST> goods_list = list.get(i).getGoodslist();
                for (int j = 0; j < goods_list.size(); j++) {
                    GOODS_LIST good = goods_list.get(j);
                    if (good.getIsCheckedbuy()) {
                        buylist.add(good);
                        buylistStringBuffer.append(good.getRec_id());
                        buylistStringBuffer.append(",");
                    }
                }
            }
            if (buylistStringBuffer.length() > 0)
                buylistStringBuffer.deleteCharAt(buylistStringBuffer.length() - 1);
        }
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.ADDRESS_LIST)) {
            if (status.getSucceed() == 1) {
                if (addressModel.addressList.size() == 0) {
                    String main = getActivity().getResources().getString(R.string.point);
                    String main_content = getActivity().getResources().getString(R.string.address_add_first);
                    myDialog = new MyDialog(getActivity(), main, main_content);
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
                } else {
//                    Intent intent = new Intent(getActivity(), BalanceActivity.class);
                    Intent intent = new Intent(getActivity(), NewBalanceActivity.class);
                    if (addressModel.addressList.size() == 1) {
                        intent.putExtra("address_id", addressModel.addressList.get(0).getId() + "");
                    } else {
                        String localaddress_id = shared.getString("last_addressid", "");//本地存儲的地址信息
                        if (!TextUtils.isEmpty(localaddress_id)) {
                            intent.putExtra("address_id", localaddress_id);
                        } else {
                            boolean isselected = false;
                            for (ADDRESS address : addressModel.addressList) {
                                if (address.getDefault_address() == 1) {
                                    intent.putExtra("address_id", address.getId() + "");
                                    isselected = true;
                                    break;
                                }
                            }
                            if (!isselected) {//有多条地址但没有默认地址
                                intent.putExtra("address_id", addressModel.addressList.get(0).getId() + "");
                            }
                        }
                    }
                    intent.putExtra("rec_ids", buylistStringBuffer.toString());
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                }
            }
        }


        if (url.equals(ProtocolConst.CARTLIST)) {
            if (status.getSucceed() == 1) {
                xlistView.stopRefresh();
                xlistView.setRefreshTime();
                checkBoxall.setChecked(isCheckall());
                setShopCart();
                STATUS responseStatus = null;
                try {
                    responseStatus = STATUS.fromJson(shoppingCartModel.listjson.optJSONObject("status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (responseStatus.getError_code() == AppConst.InvalidSession) {
                    shop_car_null.setVisibility(View.VISIBLE);
                    shop_car_isnot.setVisibility(View.GONE);
                    buttomitem.setVisibility(View.GONE);
                }
//                ShoppingCartModel.getInstance().goods_num = shoppingCartModel.goods_num;
                TabsFragment.getInstance().setShoppingcartNum();
            }
        } else if (url.equals(ProtocolConst.CARTUPDATA)) {
            if (status.getSucceed() == 1) {
                shoppingCartModel.cartList(false);
            }
        } else if (url.equals(ProtocolConst.CARTDELETE)) {
            if (status.getSucceed() == 1) {
                TabsFragment.getInstance().setShoppingcartNum();
                if (shoppingCartModel.newcartList.size() == 0) {
                    shopCarAdapter.flag = 1;
                    shopcar_edit.setText(getResources().getString(R.string.collect_compile));
                    shopcar_edit.setVisibility(View.GONE);
                    shop_car_null.setVisibility(View.VISIBLE);
                    shop_car_isnot.setVisibility(View.GONE);
                    buttomitem.setVisibility(View.GONE);
                    shopcar_delete.setClickable(false);
                    xlistView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void toAddAddress() {
        Intent intent = new Intent(getActivity(), AddressAddActivity.class);
        intent.putExtra("isfirst", true);
        startActivityForResult(intent, 3);
        getActivity().overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

}
