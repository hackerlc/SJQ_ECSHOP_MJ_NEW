package com.ecjia.view.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.ecjia.base.ECJiaBaseFragment;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.RequestCodes;
import com.ecjia.entity.responsemodel.FUNCTION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.netmodle.FindModel;
import com.ecjia.util.FunctionUtil;
import com.ecjia.util.LG;
import com.ecjia.view.activity.FunctionSettingActivity;
import com.ecjia.view.activity.MyCaptureActivity;
import com.ecjia.view.activity.ShareQRCodeActivity;
import com.ecjia.view.activity.market.MarketActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.adapter.FoundFragmentAdapter;
import com.ecjia.view.adapter.FoundLocalFunctionAdapter;
import com.ecjia.widgets.ECJiaTopView;
import com.ecmoban.android.sijiqing.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class FoundFragment extends ECJiaBaseFragment implements HttpResponse, View.OnClickListener {
    private FindModel model;
    private GridView localListView;
    private GridView customlistivew;
    private FoundFragmentAdapter adapter;

    private FoundLocalFunctionAdapter localAdapter;

    private View noresult;
    private ImageView treasureTitle;
    private SharedPreferences spf;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, null, false);
        setInfo(view);
        return view;
    }

    View treasureLayout;

    ArrayList<FUNCTION> functionses = new ArrayList<>();

    private void setInfo(View view) {

        ECJiaTopView topView = (ECJiaTopView) view.findViewById(R.id.found_topview);
        topView.setLeftType(ECJiaTopView.LEFT_TYPE_BACKIMAGE);
        topView.setRightType(ECJiaTopView.RIGHT_TYPE_IMAGE);
        topView.setRightImage(R.drawable.icon_found_set, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016-06-14 去设置页面
                startActivity(new Intent(parentActivity, FunctionSettingActivity.class));
                parentActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            }
        });
        topView.setLeftBackImage(R.drawable.icon_main_list_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.dl.open();
            }
        });
        if (parentActivity.getResources().getConfiguration().locale.equals(Locale.CHINA)) {
            topView.setTitleImage(R.drawable.icon_title_found);
        } else {
            topView.setTitleImage(R.drawable.icon_title_found_english);
        }

        treasureTitle = (ImageView) view.findViewById(R.id.found_treasure_title);
        if (parentActivity.getResources().getConfiguration().locale.equals(Locale.CHINA)) {
            treasureTitle.setImageResource(R.drawable.icon_title_discover_box);
        } else {
            treasureTitle.setImageResource(R.drawable.icon_title_discover_box_english);
        }


        treasureLayout = view.findViewById(R.id.treasure_layout);

        noresult = view.findViewById(R.id.found_noresult);
        noresult.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        localListView = (GridView) view.findViewById(R.id.found_gridview_local);
        localListView.setVerticalSpacing(0);
        localListView.setHorizontalSpacing(0);
        customlistivew = (GridView) view.findViewById(R.id.found_gridview_online);
        customlistivew.setVerticalSpacing(0);
        customlistivew.setHorizontalSpacing(0);

        view.findViewById(R.id.found_scan).setOnClickListener(this);
        view.findViewById(R.id.found_shop_streets).setOnClickListener(this);
        view.findViewById(R.id.found_topic).setOnClickListener(this);

        if (model == null) {
            model = new FindModel(getActivity());
        }
        model.addResponseListener(this);//添加回调监听
        model.getFindCacheList();
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (ProtocolConst.HOME_DISCOVER.equals(url)) {
            noresult.setVisibility(View.GONE);
            if (status.getSucceed() == 1) {
                LG.i("=======" + model.findItemlist.size());
                if (model.findItemlist.size() > 0) {
                    treasureLayout.setVisibility(View.VISIBLE);
                    adapter = new FoundFragmentAdapter(getActivity(), model.findItemlist);
                    customlistivew.setAdapter(adapter);
                } else {
                    treasureLayout.setVisibility(View.GONE);
                }
            } else {
                treasureLayout.setVisibility(View.GONE);
            }
        }
    }

    ArrayList<FUNCTION> supprotFunctions = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        FunctionUtil.getDefault().register();
        functionses = FunctionUtil.getDefault().getAllFunctions();
        LG.i("functionses===原始数据的长度" + functionses.size());
        supprotFunctions.clear();
        spf = parentActivity.getSharedPreferences("function_setting", 0);
        if (spf.getBoolean("isfirst", true)) {
            addFirstData();
            JSONArray array = new JSONArray();
            for (int i = 0; i < supprotFunctions.size(); i++) {
                array.put(supprotFunctions.get(i).getCode());
            }
            LG.i("isfirst" + "true");
            spf.edit().putString("support", array.toString()).commit();
            spf.edit().putBoolean("isfirst", false).commit();
        } else {
            LG.i("isfirst" + "false");
            addData();
        }
        localAdapter = new FoundLocalFunctionAdapter(parentActivity, supprotFunctions);
        localListView.setAdapter(localAdapter);
    }


    /**
     * 首次进入页面，需要初始化数据
     */
    void addFirstData() {
        String[] supports = new String[]{
                "newgoods",
                "promotion",
                "mobilebuy",
                "groupbuy",
                "todayhot",
                "message",
                "feedback",
                "map"};

        for (int i=0;i<supports.length;i++){
            supprotFunctions.add(FunctionUtil.getDefault().getFunctions().get(supports[i]));
        }
    }

    void addData() {
        JSONArray array = null;
        try {
            array = new JSONArray(spf.getString("support", new JSONArray().toString()));
            LG.i("array===" + array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /**
         * 加载支持的功能的集合
         */
        if (array != null && array.length() > 0) {
            for (int i = 0; i < array.length(); i++) {
                supprotFunctions.add(FunctionUtil.getDefault().getFunctionsByCode(array.optString(i)));
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.found_scan:
                startActivity(new Intent(parentActivity, MyCaptureActivity.class));
                break;
            case R.id.found_shop_streets:
                if (mApp.getUser() == null || TextUtils.isEmpty(mApp.getUser().getId())) {
                    getActivity().startActivityForResult(new Intent(parentActivity, LoginActivity.class), RequestCodes.MAIN_TO_SHARE);
                } else {
                    startActivity(new Intent(parentActivity, ShareQRCodeActivity.class));
                }
                break;
            case R.id.found_topic:
                startActivity(new Intent(parentActivity, MarketActivity.class));
                break;
        }

    }

    @Override
    public void onDestroyView() {
        FunctionUtil.getDefault().unregister();
        super.onDestroyView();
    }
}