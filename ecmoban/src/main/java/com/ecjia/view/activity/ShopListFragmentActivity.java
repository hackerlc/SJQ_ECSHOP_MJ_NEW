package com.ecjia.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.widgets.HorizontalListView;
import com.ecjia.widgets.MyGridView;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.AppConst;
import com.ecjia.view.adapter.PopupListAdapter;
import com.ecjia.view.adapter.ShopListAdapter;
import com.ecjia.view.adapter.ShoplistCategoryAdapter;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.MyBitmapUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/12/31.
 */
public class ShopListFragmentActivity extends BaseActivity implements HttpResponse, XListView.IXListViewListener{
    private XListView xlistview;
    private FrameLayout nullpage;
    private ShopListAdapter sellerAdapter;

    private SellerModel model;
    private SharedPreferences shared;

    private ImageView iv_search;
    private EventBus eventBuse;
    private Handler messageHandler;
    private int itemid = -1;
    //headview2
    private View headview1, headview2;
    private HorizontalListView categorylistview;
    private ShoplistCategoryAdapter categoryAdapter;
    private String checkedcategoryid="0";
    private TextView  category_num;
    private LinearLayout headview2_item;
    private LinearLayout headview2_show_more;
    private ImageView show_more_img;
    private boolean selfrefresh=true;
    //popuwindow
    private LinearLayout stick_toolbar;
    private MyGridView popup_gridview;
    private PopupListAdapter popupListAdapter;
    private PopupWindow popupWindow;
    private View popupWindow_buttom_view;
    //悬浮标题
    private HorizontalListView bodycategorylist;
    private TextView  body_category_num;
    private ImageView body_show_more_img,shoplist_iv_search;
    private LinearLayout body_show_more;
    private ImageView headviewbg;
    private ArrayList<CATEGORY> sellercategory = new ArrayList<CATEGORY>();

    private ImageLoaderForLV imageLoaderForLV;
    private ECJiaDealUtil ecJiaDealUtil;
    private ImageView view_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoplist_fragment);
        shared = getSharedPreferences("userInfo", 0);
        eventBuse = EventBus.getDefault();
        eventBuse.register(this);
        imageLoaderForLV= ImageLoaderForLV.getInstance(this);
        ecJiaDealUtil=new ECJiaDealUtil(this);
        setinfo();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (model == null) {
            model = new SellerModel(this);
        }
        MobclickAgent.onPageStart("ShopList");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ShopList");
    }
    void setBigImageView(ImageView img) {
        int width=getDisplayMetricsWidth2();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                width, width*1/3);
        img.setLayoutParams(lp);
    }

    private void setinfo() {
        view_back= (ImageView) findViewById(R.id.view_back);
        view_back.setVisibility(View.VISIBLE);
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (model == null) {
            model = new SellerModel(this);
        }
        headview1 = View.inflate(this, R.layout.shoplist_headview1, null);
        headviewbg= (ImageView) headview1.findViewById(R.id.headview1_bg);
        setBigImageView(headviewbg);

        headviewbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.adsenseList.size()>0){
                    if (model.adsenseList.get(0).getMap().isEmpty()) {
                        Intent intent = new Intent();
                        intent.setClass(ShopListFragmentActivity.this, WebViewActivity.class);
                        intent.putExtra("url", model.adsenseList.get(0).getLink());
                        intent.putExtra("webtitle", model.adsenseList.get(0).getName());
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_right_in,
                                R.anim.push_right_out);
                    } else {
                        ecJiaDealUtil.dealEcjiaAction(model.adsenseList.get(0).getMap());

                    }
                }

            }
        });

        headview2 = View.inflate(this, R.layout.shoplist_headview2, null);

        shoplist_iv_search=(ImageView)findViewById(R.id.shoplist_iv_search);
        shoplist_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShopListFragmentActivity.this,SearchNewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        initheadview2();//初始化分类
        initbodyitem();//初始化悬浮标题
        initPopWindow();//初始化popwindow
        messageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    itemid = msg.arg1;
                    model.sellerCollectCreate(sellerAdapter.getList().get(msg.arg1).getId());
                    eventBuse.post(new MyEvent("add_collect_seller",sellerAdapter.getList().get(msg.arg1).getId()));
                }
                if (msg.what == 2) {
                    itemid = msg.arg1;
                    model.sellerCollectDelete(sellerAdapter.getList().get(msg.arg1).getId());
                    eventBuse.post(new MyEvent("minus_collect_seller",sellerAdapter.getList().get(msg.arg1).getId()));
                }
            }
        };


        xlistview = (XListView) findViewById(R.id.shoplist_xlist);
        xlistview.setXListViewListener(this, 1);
        xlistview.setRefreshTime();
        xlistview.setPullLoadEnable(false);
        xlistview.setPullRefreshEnable(true);
        xlistview.addHeaderView(headview1);
        xlistview.addHeaderView(headview2);
        xlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i2, int i3) {
                if (firstVisibleItem > 1&&model.sellercategory.size()>1) {
                    stick_toolbar.setVisibility(View.VISIBLE);
                } else {
                    stick_toolbar.setVisibility(View.GONE);
                }
            }
        });

        sellerAdapter = new ShopListAdapter(this, model.sellerinfos, getDisplayMetricsWidth1());
        sellerAdapter.messageHandler = messageHandler;
        xlistview.setAdapter(sellerAdapter);
        model.addResponseListener(this);
        nullpage = (FrameLayout) findViewById(R.id.null_pager);

        model.getSellerCategory();
        model.sellerAdsense();

    }
    //初始化悬浮标题
    private void initbodyitem() {
        stick_toolbar= (LinearLayout) findViewById(R.id.body_title_item);
        bodycategorylist= (HorizontalListView) findViewById(R.id.body_categorylist);
        if(null==categoryAdapter){
            categoryAdapter = new ShoplistCategoryAdapter(this, model.sellercategory);
        }
        body_show_more= (LinearLayout) findViewById(R.id.body_show_more);
        body_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(body_show_more,0,0);
                body_category_num.setVisibility(View.VISIBLE);
                bodycategorylist.setVisibility(View.GONE);
                body_show_more_img.setBackgroundResource(R.drawable.search_hidden);
            }
        });
        body_category_num= (TextView)findViewById(R.id.body_category_num);
        body_show_more_img= (ImageView)findViewById(R.id.body_show_more_img);
        bodycategorylist.setAdapter(categoryAdapter);
        bodycategorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkedcategoryid=categoryAdapter.getDataList().get(i).getId()+"";
                categoryAdapter.setCheckitem(i);
                model.getSellerList(checkedcategoryid);
            }
        });
    }
    //headview2分类标题栏初始化
    private void initheadview2() {
        categorylistview = (HorizontalListView) headview2.findViewById(R.id.headview_categorylist);
        headview2_item= (LinearLayout) headview2.findViewById(R.id.headview2_item);
        headview2_show_more= (LinearLayout) headview2.findViewById(R.id.headview2_show_more);
        headview2_show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(headview2_show_more,0,0);
                category_num.setVisibility(View.VISIBLE);
                categorylistview.setVisibility(View.GONE);
                show_more_img.setBackgroundResource(R.drawable.search_hidden);
            }
        });
        category_num= (TextView) headview2.findViewById(R.id.category_num);
        show_more_img= (ImageView) headview2.findViewById(R.id.show_more_img);
        categoryAdapter = new ShoplistCategoryAdapter(this, model.sellercategory);
        categorylistview.setAdapter(categoryAdapter);
        categorylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkedcategoryid=categoryAdapter.getDataList().get(i).getId()+"";
                categoryAdapter.setCheckitem(i);
                model.getSellerList(checkedcategoryid);
            }
        });

    }

    //初始化popuwindow
    private void initPopWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.shoplist_popupwindow, null, true);
        popupWindow_buttom_view=view.findViewById(R.id.buttom_view);
        popup_gridview = (MyGridView) view.findViewById(R.id.popup_gridview);
        sellercategory.clear();
        popupListAdapter = new PopupListAdapter(this, sellercategory);
        popup_gridview.setAdapter(popupListAdapter);
        popup_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkedcategoryid=categoryAdapter.getDataList().get(i+1).getId()+"";
                categoryAdapter.setCheckitem(i + 1);
                model.getSellerList(checkedcategoryid);
                popupWindow.dismiss();
            }
        });
        //设置popupWindow的大小
        popupWindow = new PopupWindow(findViewById(R.id.shoplist_content), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //设置popupWindow的布局容器0
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);// 这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。当然这里很明显只能在Touchable下才能使用。
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(100));
        popupWindow_buttom_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                category_num.setVisibility(View.GONE);
                categorylistview.setVisibility(View.VISIBLE);
                show_more_img.setBackgroundResource(R.drawable.search_showchild);
                body_category_num.setVisibility(View.GONE);
                bodycategorylist.setVisibility(View.VISIBLE);
                body_show_more_img.setBackgroundResource(R.drawable.search_showchild);
            }
        });

    }


    @Override
    public void onDestroy() {
        eventBuse.unregister(this);
        super.onDestroy();
    }


    public void onEvent(MyEvent event) {
        if("collectrefresh".equals(event.getMsg())){
            if(null!=model){
                model.getSellerList(checkedcategoryid);
            }
        }
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (ProtocolConst.SELLER_LIST == url) {
            if (status.getSucceed() == 1) {
                xlistview.setRefreshTime();
                xlistview.stopRefresh();
                xlistview.stopLoadMore();
                PAGINATED paginated = PAGINATED.fromJson(jo.optJSONObject("paginated"));
                if (paginated.getMore() == 1) {
                    xlistview.setPullLoadEnable(true);
                } else {
                    xlistview.setPullLoadEnable(false);
                }
                setSellerInfo();

            } else {
                xlistview.setVisibility(View.GONE);
                nullpage.setVisibility(View.VISIBLE);
            }
        } else if (ProtocolConst.SELLER_CATEGORY == url) {
            if(model.sellercategory.size()>1) { //默认会添加一条全部所以应大于1
                for(int i=1;i<model.sellercategory.size();i++){
                    sellercategory.add(model.sellercategory.get(i));
                }
                body_category_num.setText(this.getResources().getString(R.string.at_all) + sellercategory.size() + this.getResources().getString(R.string.category_for_chioce));
                category_num.setText(this.getResources().getString(R.string.at_all) + sellercategory.size() + this.getResources().getString(R.string.category_for_chioce));
                headview2_item.setVisibility(View.VISIBLE);
            }else{
                headview2_item.setVisibility(View.GONE);
            }
            categoryAdapter.notifyDataSetChanged();
            model.getSellerList("");
        } else if (ProtocolConst.SELLER_HOMEDATA == url) {
            if(model.adsenseList.size()>0){
                MyBitmapUtils.getInstance(this).displayTanImage2(headviewbg,model.adsenseList.get(0).getImg());
            }

        } else if (ProtocolConst.SELLER_COLLECTCREATE == url) {
            if (status.getSucceed() == 1) {
                ToastView toastView=new ToastView(this,getResources().getString(R.string.collection_success));
                toastView.show();
                sellerAdapter.getList().get(itemid).setIs_follower("1");
                sellerAdapter.getList().get(itemid).setFollower(sellerAdapter.getList().get(itemid).getFollower()+1);
                sellerAdapter.notifyDataSetChanged();
            } else {
                if (status.getError_code() == AppConst.InvalidSession) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    ToastView toast = new ToastView(this, status.getError_desc());
                    toast.show();
                }

            }
        } else if (ProtocolConst.SELLER_COLLECTDELETE == url) {
            if (status.getSucceed() == 1) {
                ToastView toastView=new ToastView(this,getResources().getString(R.string.del_collection_success));
                toastView.show();
                sellerAdapter.getList().get(itemid).setIs_follower("0");
                sellerAdapter.getList().get(itemid).setFollower(sellerAdapter.getList().get(itemid).getFollower()-1);
                sellerAdapter.notifyDataSetChanged();
            } else {
                if (status.getError_code() == AppConst.InvalidSession) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    ToastView toast = new ToastView(this, status.getError_desc());
                    toast.show();
                }

            }

        }
    }

    //设置商家列表功能
    private void setSellerInfo() {
        if (model.sellerinfos.size() == 0) {
            xlistview.setVisibility(View.GONE);
            nullpage.setVisibility(View.VISIBLE);
        } else {
            xlistview.setVisibility(View.VISIBLE);
            nullpage.setVisibility(View.GONE);
            sellerAdapter.setList(model.sellerinfos);
        }
        categoryAdapter.notifyDataSetChanged();
        sellerAdapter.notifyDataSetChanged();
    }

    public int getDisplayMetricsWidth1() {
        int i = this.getWindowManager().getDefaultDisplay().getWidth();
        int j = this.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j) - ((int) getResources().getDimension(R.dimen.eight_margin)) * 2;
    }
    public int getDisplayMetricsWidth2() {
        int i = this.getWindowManager().getDefaultDisplay().getWidth();
        int j = this.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    @Override
    public void onRefresh(int id) {
        model.getSellerList(checkedcategoryid);
    }

    @Override
    public void onLoadMore(int id) {
        model.getSellerListMore(checkedcategoryid);
    }

}
