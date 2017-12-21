package com.ecjia.view.fragment.homefragment.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.AppConst;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.adapter.HomeShopListAdapter;
import com.ecjia.base.BaseFragment;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;


import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/13.
 * 商家列表页
 */
@SuppressLint("ValidFragment")
public class ShopListFragment extends BaseFragment implements HttpResponse, XListView.IXListViewListener {
    private View shoplistView;
    private XListView xlistview;
    private FrameLayout nullpage;
    private HomeShopListAdapter sellerAdapter;

    private SellerModel model;
    private SharedPreferences shared;

    private Handler messageHandler;
    private int itemid = -1;
    private String checkedcategoryid = "0";

    @SuppressLint("ValidFragment")
    public ShopListFragment(String checkedcategoryid) {
        super();
        this.checkedcategoryid = checkedcategoryid;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != shoplistView) {
            ViewGroup parent = (ViewGroup) shoplistView.getParent();
            if (null != parent) {
                parent.removeView(shoplistView);
            }
        } else {
            shoplistView = inflater.inflate(R.layout.fragment_other_shop, null);
            shared = getActivity().getSharedPreferences("userInfo", 0);
            setinfo(shoplistView);
            if (model == null) {
                model = new SellerModel(getActivity());
                model.addResponseListener(this);
            }
            model.getSellerList(checkedcategoryid);
        }

        return shoplistView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ShopList");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ShopList");
    }

    private void setinfo(View view) {

        if (model == null) {
            model = new SellerModel(getActivity());
        }

        messageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    itemid = msg.arg1;
                    model.sellerCollectCreate(sellerAdapter.getList().get(msg.arg1).getId());
                    EventBus.getDefault().post(new MyEvent("add_collect_seller", sellerAdapter.getList().get(msg.arg1).getId()));

                }
                if (msg.what == 2) {
                    itemid = msg.arg1;
                    model.sellerCollectDelete(sellerAdapter.getList().get(msg.arg1).getId());
                    EventBus.getDefault().post(new MyEvent("minus_collect_seller", sellerAdapter.getList().get(msg.arg1).getId()));
                }
            }
        };

        xlistview = (XListView) view.findViewById(R.id.shoplist_xlist);
        xlistview.setXListViewListener(this, 1);
        xlistview.setRefreshTime();
        xlistview.setPullLoadEnable(false);
        xlistview.setPullRefreshEnable(true);


        sellerAdapter = new HomeShopListAdapter(this.getActivity(), model.sellerinfos, getDisplayMetricsWidth1());
        sellerAdapter.messageHandler = messageHandler;
        xlistview.setAdapter(sellerAdapter);
        model.addResponseListener(this);
        nullpage = (FrameLayout) view.findViewById(R.id.null_pager);
        model.sellerAdsense();

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onEvent(MyEvent event) {
        if ("collectrefresh".equals(event.getMsg())) {
            if (null != model) {
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
        } else if (ProtocolConst.SELLER_COLLECTCREATE == url) {
            if (status.getSucceed() == 1) {
                ToastView toastView = new ToastView(getActivity(), getResources().getString(R.string.collection_success));
                toastView.show();
                sellerAdapter.getList().get(itemid).setIs_follower("1");
                sellerAdapter.getList().get(itemid).setFollower(sellerAdapter.getList().get(itemid).getFollower() + 1);
                sellerAdapter.notifyDataSetChanged();
            } else {
                if (status.getError_code() == AppConst.InvalidSession) {
                    Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                    startActivity(intent);
                    this.getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    ToastView toast = new ToastView(this.getActivity(), status.getError_desc());
                    toast.show();
                }

            }
        } else if (ProtocolConst.SELLER_COLLECTDELETE == url) {
            if (status.getSucceed() == 1) {
                ToastView toastView = new ToastView(getActivity(), getResources().getString(R.string.del_collection_success));
                toastView.show();
                sellerAdapter.getList().get(itemid).setIs_follower("0");
                sellerAdapter.getList().get(itemid).setFollower(sellerAdapter.getList().get(itemid).getFollower() - 1);
                sellerAdapter.notifyDataSetChanged();
            } else {
                if (status.getError_code() == AppConst.InvalidSession) {
                    Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                    startActivity(intent);
                    this.getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    ToastView toast = new ToastView(this.getActivity(), status.getError_desc());
                    toast.show();
                }

            }
        }
    }

    //设置商家列表功能
    private void setSellerInfo() {
        if (model.sellerinfos.size() == 0) {
        } else {
            sellerAdapter.setList(model.sellerinfos);
        }
        sellerAdapter.notifyDataSetChanged();
    }

    public int getDisplayMetricsWidth1() {
        int i = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int j = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j) - ((int) getResources().getDimension(R.dimen.eight_margin)) * 2;
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
