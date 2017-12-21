package com.ecjia.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.netmodle.SearchModel;
import com.ecjia.view.activity.SearchNewActivity;
import com.ecjia.view.adapter.adapter.my.MyFragmentPagerAdapter;
import com.ecmoban.android.sijiqing.R;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements HttpResponse {

    private View view;
    private LinearLayout ly_choose;
    private SearchModel searchModel;

    private List<Fragment> fragmentList;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private TabLayout tablayout;
    private ViewPager viewPager_search;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TextView search_input;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search, null);//加载整个Fragment布局
        findView();
        //if (searchModel == null) {
        searchModel = new SearchModel(getActivity());
        searchModel.searchCategory();
        //}
        searchModel.addResponseListener(this);
        fragmentList = new ArrayList<>();
        return view;
    }

    private void findView() {
        tablayout = (TabLayout) view.findViewById(R.id.tablayout);
        viewPager_search = (ViewPager) view.findViewById(R.id.viewPager_search);
        ly_choose = (LinearLayout) view.findViewById(R.id.ly_choose);
        search_input = (TextView) view.findViewById(R.id.search_input);
        search_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), SearchNewActivity.class);
                intent2.putExtra("filter", new FILTER().toString());
                startActivityForResult(intent2, 100);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                //                startActivity(new Intent(getActivity(), SearchActivity.class))
            }
        });
    }

    private void initView() {
//        if(){}
//        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList, mTitleList);
        viewPager_search.setAdapter(myFragmentPagerAdapter);//给ViewPager设置适配器
        tablayout.setupWithViewPager(viewPager_search);//将TabLayout和ViewPager关联起来。
        tablayout.setTabsFromPagerAdapter(myFragmentPagerAdapter);//给Tabs设置适配器
        viewPager_search.setOffscreenPageLimit(fragmentList.size());
        viewPager_search.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Search");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Search");
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.CATEGORY) {
            if (status.getSucceed() == 1) {
                if (searchModel.categoryArrayList.size() > 0) {
                    searchModel.categoryArrayList.get(0).setChoose(true);
                    ly_choose.setVisibility(View.VISIBLE);
                    fragmentList.clear();
                    mTitleList.clear();
                    tablayout.removeAllTabs();
                    for (CATEGORY cateParent : searchModel.categoryArrayList) {
                        mTitleList.add(cateParent.getName());
                        fragmentList.add(new SearchChildFragment(cateParent));
                    }
                    initView();
                } else {
                    ly_choose.setVisibility(View.GONE);
                }
            } else {
                ly_choose.setVisibility(View.GONE);
            }
        }
    }
}
