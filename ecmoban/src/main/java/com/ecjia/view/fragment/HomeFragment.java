package com.ecjia.view.fragment;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseFragment;
import com.ecjia.network.netmodle.HomeModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.widgets.GoodsViewPager;
import com.ecjia.widgets.MyXListView;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.RequestCodes;
import com.ecjia.view.activity.ChooseCityActivity;
import com.ecjia.view.activity.SearchNewActivity;
import com.ecjia.view.activity.ShopCategoryActivity;
import com.ecjia.view.adapter.MsgSql;
import com.ecjia.view.fragment.homefragment.adapter.HomeFragmentPageAdapter;
import com.ecjia.view.fragment.homefragment.fragment.GoodShopFragment;
import com.ecjia.view.fragment.homefragment.fragment.ShopListFragment;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.CITY;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecjia.view.activity.push.PushActivity;
import com.ecmoban.android.sijiqing.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

public class HomeFragment extends BaseFragment implements MyXListView.IXListViewListener, AppConst.RegisterApp, HttpResponse {


    String openTypeCategoryId;
    private MyXListView mListView;
    private HomeModel dataModel;
    public ImageView iv_open_menu;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String uid;
    boolean isRefresh = false;

    private View mainView;
    private ImageView input;
    private RelativeLayout topview;

    private TextView city_name;
    private CITY city;
    private LinearLayout city_item;
    private String type = "hot";

    ECJiaDealUtil ecJiaDealUtil;
    TabsFragment.FragmentListener fragmentListener;


    private Activity mActivity;

    private FrameLayout messageFL;
    public GoodsViewPager mViewPager;
    private SellerModel sellerModel;
    private TextView message_num_tv;

    @Override
    public void onAttach(Activity activity) {
        EventBus.getDefault().register(this);
        mActivity = activity;
        fragmentListener = (TabsFragment.FragmentListener) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ecJiaDealUtil = new ECJiaDealUtil(mActivity);
    }

    List<Fragment> fragments = new ArrayList<Fragment>();
    TabLayout tableLayout;

    HomeFragmentPageAdapter pageAdapter;

    String[] titles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mainView) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (null != parent) {
                parent.removeView(mainView);
            }
        } else {
            mainView = inflater.inflate(R.layout.home_fragment, null);

            initTopview();
            initConfig();
            initTabLayout();

            initShopCategory();

        }
        return mainView;
    }

    private void initShopCategory() {
        mainView.findViewById(R.id.shop_category_more).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShopCategoryActivity.class);
                ArrayList<String> strings = new ArrayList<String>();
                for (int i = 0; i < sellerModel.sellercategory.size(); i++) {
                    strings.add(sellerModel.sellercategory.get(i).getName());
                }
                strings.add(0, "精选");
                intent.putExtra("seller_category", strings);
                intent.putExtra("position", tableLayout.getSelectedTabPosition());
                startActivityForResult(intent, RequestCodes.HOME_TO_SHOPCATEGORY);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    GoodShopFragment goodShopFragment;


    void initTabLayout() {


        goodShopFragment = new GoodShopFragment(this);
        tableLayout = (TabLayout) mainView.findViewById(R.id.home_tablayout);
        tableLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.public_theme_color_normal));
        tableLayout.setTabTextColors(getResources().getColor(R.color.my_black), getResources().getColor(R.color.public_theme_color_normal));

        tableLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager = (GoodsViewPager) mainView.findViewById(R.id.home_viewpager);
        fragments.clear();
        fragments.add(new GoodShopFragment(this));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position != 0) {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initConfig() {

        if (sellerModel == null) {
            sellerModel = new SellerModel(getActivity());
            sellerModel.addResponseListener(this);

        }
        sellerModel.getSellerCategory();

    }

    private void initTopview() {

        final Resources resource = this.getResources();
        String ecmobileStr = resource.getString(R.string.ecmoban);
        shared = mActivity.getSharedPreferences("userInfo", 0);
        getCityinfo(shared.getString("localString", ""));
        topview = (RelativeLayout) mainView.findViewById(R.id.home_topvoew);
        topview.setBackgroundColor(getActivity().getResources().getColor(R.color.public_theme_color_normal_2));


        iv_open_menu = (ImageView) mainView.findViewById(R.id.top_view_list);
        iv_open_menu.setVisibility(View.VISIBLE);
        iv_open_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //首页左上点击弹出个人中心
                fragmentListener.Open();
            }
        });

        city_item = (LinearLayout) mainView.findViewById(R.id.city_item);
        city_name = (TextView) mainView.findViewById(R.id.city_name);
        city_name.setText(city.getName());
        city_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.mActivity, ChooseCityActivity.class);
                intent.putExtra("chooseagain", "chooseagain");
                startActivityForResult(intent, 100);
                mActivity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
            }
        });

        input = (ImageView) mainView.findViewById(R.id.search_input);
        input.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mActivity, SearchNewActivity.class);
                intent.putExtra("filter", new FILTER().toString());
                startActivityForResult(intent, 100);
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        messageFL = (FrameLayout) mainView.findViewById(R.id.search_frame_edit);
        messageFL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PushActivity.class));
                mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        message_num_tv = (TextView) mainView.findViewById(R.id.message_num);
    }


    public boolean isActive = false;
    public Bitmap bitmap;

    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            AppConst.registerApp(this);
        }
        MobclickAgent.onPageStart("Home");
        fragmentListener.addIgnoredView(mViewPager);
        fragmentListener.addIgnoredView(tableLayout);
        setUser();
        getMessageNumber();
        if (!TextUtils.isEmpty(openTypeCategoryId)) {
            LG.i("seller size()" + sellerModel.sellercategory.size());
            if (sellerModel.sellercategory.size() > 0) {
                for (int i = 0; i < sellerModel.sellercategory.size(); i++) {
                    LG.i("i==" + i + "  " + sellerModel.sellercategory.get(i).getName() + "==" + sellerModel.sellercategory.get(i).getId());
                    if (openTypeCategoryId.equals(sellerModel.sellercategory.get(i).getId() + "")) {
                        LG.i("category_id == tab的categoryid i==" + i);
                        tableLayout.getTabAt(i + 1).select();
                        break;
                    }
                }
            } else {

            }
            openTypeCategoryId = null;
        }
        if (fragments.size() == 1) {
            mainView.findViewById(R.id.home_good_shop).setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            getChildFragmentManager().beginTransaction().replace(R.id.home_good_shop, goodShopFragment).commit();
            TabLayout.Tab tab = tableLayout.newTab();
            tab.setText("精选");
            tableLayout.removeAllTabs();
            tableLayout.addTab(tab);
        } else {
            mainView.findViewById(R.id.home_good_shop).setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
        }
    }

    private void getMessageNumber() {
        int messageNum = MsgSql.getIntence(parentActivity).getUnReadMsgCount();
        if (messageNum == 0) {
            message_num_tv.setVisibility(View.GONE);
        } else {
            message_num_tv.setVisibility(View.VISIBLE);
            message_num_tv.setText(messageNum + "");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {//重新设置城市
            getCityinfo(shared.getString("localString", ""));
            city_name.setText(city.getName());
        }

        if (requestCode == RequestCodes.HOME_TO_SHOPCATEGORY) {
            if (resultCode == Activity.RESULT_OK) {
                if (tableLayout.getTabCount() > 0) {
                    tableLayout.getTabAt(data.getIntExtra("position", 0)).select();
                }
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onRefresh(int id) {
        isRefresh = true;
        dataModel.suggests.clear();
        mListView.setPullLoadEnable(true, true);
        dataModel.fetchHotSelling();
    }

    @Override
    public void onLoadMore(int id) {
        if (dataModel.suggests.size() == 0) {
            mListView.showFooter();
            dataModel.getSuggestList(type);
        } else {
            dataModel.getSuggestListMore("hot");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fragmentListener.removeIgnoredView(mViewPager);
        fragmentListener.removeIgnoredView(tableLayout);
        MobclickAgent.onPageEnd("Home");
    }


    @Override
    public void onEvent(MyEvent event) {
        if ("refresh_sendarea".equals(event.getMsg())) {
            getCityinfo(shared.getString("localString", ""));
            city_name.setText(city.getName());
        }

        if (event.getMsg().equals(EventKeywords.USER_LOGIN_SUCCESS)) {
            LG.i("HomeFragment 收到消息");
            setUser();
        }

        if (event.getMsg().equals(EventKeywords.USER_PHOTO_DOWNLOAD_SUCCESS)) {
            LG.i("HomeFragment 收到消息");
            setUser();
        }

        if (event.getMsg().equals(EventKeywords.USER_SINOUT)) {
            LG.i("HomeFragment 收到消息");
            setUser();
        }
        if (event.getMsg().equals(EventKeywords.USER_CHANGE_PHOTO)) {
            LG.i("HomeFragment 收到消息");
            setUser();
        }

        if (event.getMsg().equals(EventKeywords.MESSAGE)) {
            message_num_tv.setText(event.getmTag() + "");
        }

        if (event.getMsg().equals(EventKeywords.UPDATE_MESSAGE)) {
            getMessageNumber();
        }

    }

    @Override
    public void onRegisterResponse(boolean success) {

    }

    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        int j = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            // app 进入后台
            isActive = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) mActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mActivity.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (ProtocolConst.SELLER_CATEGORY.equals(url)) {
            tableLayout.removeAllTabs();
            if (sellerModel.sellercategory.size() > 0) { //默认会添加一条全部所以应大于1
                getChildFragmentManager().beginTransaction().remove(goodShopFragment).commit();
                for (int i = 1; i < sellerModel.sellercategory.size(); i++) {
                    sellercategory.add(sellerModel.sellercategory.get(i));
                }

                titles = new String[sellerModel.sellercategory.size() + 1];
                for (int i = 0; i < sellerModel.sellercategory.size(); i++) {
                    titles[i + 1] = sellerModel.sellercategory.get(i).getName();
                    fragments.add(new ShopListFragment(sellerModel.sellercategory.get(i).getId() + ""));
                }
                titles[0] = "精选";
                pageAdapter = new HomeFragmentPageAdapter(getFragmentManager(), fragments, Arrays.asList(titles));
                mViewPager.setAdapter(pageAdapter);
                tableLayout.setupWithViewPager(mViewPager);
                mViewPager.setVisibility(View.VISIBLE);
                mainView.findViewById(R.id.home_good_shop).setVisibility(View.GONE);
            } else {
                mViewPager.setVisibility(View.GONE);
                mainView.findViewById(R.id.home_good_shop).setVisibility(View.VISIBLE);
                getChildFragmentManager().beginTransaction().replace(R.id.home_good_shop, goodShopFragment).commit();
                TabLayout.Tab tab = tableLayout.newTab();
                tab.setText("精选");
                tableLayout.addTab(tab);
            }

        }
    }

    ArrayList<CATEGORY> sellercategory = new ArrayList<>();

    //设置默认的城市信息
    private void getCityinfo(String info) {
        if (!TextUtils.isEmpty(info)) {
            try {
                city = CITY.fromJson(new JSONObject(info));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            city = new CITY();
            city.setName(mActivity.getResources().getString(R.string.please_select));
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }


    void setUser() {
        uid = shared.getString("uid", "");
        if (TextUtils.isEmpty(uid)) {
            iv_open_menu.setImageResource(R.drawable.profile_no_avarta_icon);
        } else {
            if (ProfilePhotoUtil.getInstance().isProfilePhotoExist(uid)) {
                iv_open_menu.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(uid));
            } else {
                iv_open_menu.setImageResource(R.drawable.profile_no_avarta_icon_light);
            }
        }
    }

}
