package com.ecjia.view.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.view.activity.push.PushActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.FindModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.view.activity.ConsultActivity;
import com.ecjia.view.activity.FindHotNewsActivity;
import com.ecjia.view.activity.GroupbuyGoodsActivity;
import com.ecjia.view.activity.HelpListActivity;
import com.ecjia.view.activity.LastBrowseActivity;
import com.ecjia.view.activity.MapActivity;
import com.ecjia.view.activity.MobilebuyGoodsActivity;
import com.ecjia.view.activity.MyCaptureActivity;
import com.ecjia.view.adapter.MyFindFragmentAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFindFragment extends Fragment implements HttpResponse, View.OnClickListener {
    private ImageView back;
    private LinearLayout zxing_item, lastbrowse, Newsitem, myfind_map, myfind_help, groupbuy_item,myfind_hot_item,myfind_mobile_item;
    private TextView title;
    private View view;
    private FindModel model;
    private ListView customlistivew;
    private MyFindFragmentAdapter adapter;

    private LinearLayout message, consult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_find, container, false);
        setInfo();
        return view;
    }

    private void setInfo() {
        customlistivew = (ListView) view.findViewById(R.id.customer_find_list);
        if (model == null) {
            model = new FindModel(getActivity());
        }
        adapter = new MyFindFragmentAdapter(getActivity(), model.findItemlist);
        model.addResponseListener(this);//添加回调监听
        model.getFindCacheList();
        customlistivew.setAdapter(adapter);
        title = (TextView) view.findViewById(R.id.top_view_text);
        back = (ImageView) view.findViewById(R.id.top_view_back);
        zxing_item = (LinearLayout) view.findViewById(R.id.myfind_zxing_item);
        lastbrowse = (LinearLayout) view.findViewById(R.id.myfind_lastbrowse);
        myfind_hot_item = (LinearLayout) view.findViewById(R.id.myfind_hot_item);
        myfind_mobile_item = (LinearLayout) view.findViewById(R.id.myfind_mobile_item);
        Newsitem = (LinearLayout) view.findViewById(R.id.myfind_push);
        myfind_map = (LinearLayout) view.findViewById(R.id.myfind_map);
        myfind_help = (LinearLayout) view.findViewById(R.id.myfind_help);
        groupbuy_item = (LinearLayout) view.findViewById(R.id.myfind_groupbuy_item);
        Resources resources = getActivity().getResources();

        message = (LinearLayout) view.findViewById(R.id.myfind_message_item);
        consult = (LinearLayout) view.findViewById(R.id.myfind_consult_item);
        message.setOnClickListener(this);
        consult.setOnClickListener(this);


        back.setVisibility(View.GONE);
        title.setText(resources.getString(R.string.find_find));

        groupbuy_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupbuyGoodsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        //今日热点
        myfind_hot_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindHotNewsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        //手机专享
        myfind_mobile_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MobilebuyGoodsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });

        zxing_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), MyCaptureActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        // 跳转至浏览记录页面
        lastbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LastBrowseActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        Newsitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        //跳转至ditu页面
        myfind_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        myfind_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HelpListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (ProtocolConst.HOME_DISCOVER.equals(url)) {
            if (status.getSucceed() == 1) {
                LG.i("=======" + model.findItemlist.size());
                if (model.findItemlist.size() > 0) {
                    customlistivew.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else {
                    customlistivew.setVisibility(View.GONE);
                }
            } else {
                customlistivew.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.myfind_consult_item:
                intent = new Intent(getActivity(), ConsultActivity.class);
                intent.putExtra("type", "all_consult");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;

            case R.id.myfind_message_item:
                intent = new Intent(getActivity(), PushActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;

            default:
                break;
        }
    }
}