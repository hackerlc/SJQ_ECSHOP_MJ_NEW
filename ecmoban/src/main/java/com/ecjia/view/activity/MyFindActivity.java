package com.ecjia.view.activity;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.FindModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.view.adapter.MyFindFragmentAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFindActivity extends BaseActivity implements OnClickListener, HttpResponse {

    private ImageView back;
    private LinearLayout zxing_item, lastbrowse, Newsitem, myfind_map, myfind_help, groupbuy_item, myfind_hot_item, myfind_mobile_item;
    private TextView title;
    private ScrollView scroll;
    private FindModel model;
    private ListView customlistivew;
    private MyFindFragmentAdapter adapter;

    private LinearLayout message, consult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_find);
        PushAgent.getInstance(this).onAppStart();

        setInfo();
        model.getFindCacheList();
    }

    private void setInfo() {
        customlistivew = (ListView) findViewById(R.id.customer_find_list);
        if (model == null) {
            model = new FindModel(this);
        }
        adapter = new MyFindFragmentAdapter(this, model.findItemlist);
        model.addResponseListener(this);//添加回调监听
        customlistivew.setAdapter(adapter);

        title = (TextView) findViewById(R.id.top_view_text);
        scroll = (ScrollView) findViewById(R.id.scroll);
        back = (ImageView) findViewById(R.id.top_view_back);
        zxing_item = (LinearLayout) findViewById(R.id.myfind_zxing_item);
        lastbrowse = (LinearLayout) findViewById(R.id.myfind_lastbrowse);
        Newsitem = (LinearLayout) findViewById(R.id.myfind_push);
        myfind_map = (LinearLayout) findViewById(R.id.myfind_map);
        myfind_help = (LinearLayout) findViewById(R.id.myfind_help);
        groupbuy_item = (LinearLayout) findViewById(R.id.myfind_groupbuy_item);
        myfind_hot_item = (LinearLayout) findViewById(R.id.myfind_hot_item);
        myfind_mobile_item = (LinearLayout) findViewById(R.id.myfind_mobile_item);

        message = (LinearLayout) findViewById(R.id.myfind_message_item);
        consult = (LinearLayout) findViewById(R.id.myfind_consult_item);
        message.setOnClickListener(this);
        consult.setOnClickListener(this);

        Resources resources = getBaseContext().getResources();

        title.setText(resources.getString(R.string.find_find));
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        groupbuy_item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFindActivity.this, GroupbuyGoodsActivity.class);
                startActivity(intent);
            }
        });

        //手机专享
        myfind_mobile_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFindActivity.this, MobilebuyGoodsActivity.class);
                startActivity(intent);
            }
        });

        //今日热点
        myfind_hot_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFindActivity.this, FindHotNewsActivity.class);
                startActivity(intent);
            }
        });

        zxing_item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFindActivity.this, MyCaptureActivity.class);
                startActivity(intent);
            }
        });
        // 跳转至浏览记录页面
        lastbrowse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFindActivity.this, LastBrowseActivity.class);
                startActivity(intent);
            }
        });
        Newsitem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFindActivity.this, PushActivity.class);
                startActivity(intent);
            }
        });
        //跳转至ditu页面
        myfind_map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFindActivity.this, MapActivity.class);
                startActivity(intent);

            }
        });
        myfind_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFindActivity.this, HelpListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.myfind_consult_item:
                intent = new Intent(this, ConsultActivity.class);
                intent.putExtra("type", "all_consult");
                startActivity(intent);
                break;

            case R.id.myfind_message_item:
                intent = new Intent(this, PushActivity.class);
                startActivity(intent);
                break;

            default:
                break;

        }

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (ProtocolConst.HOME_DISCOVER.equals(url)) {
            if (status.getSucceed() == 1) {
                LG.i("=======" + model.findItemlist.size());
                if (model.findItemlist.size() > 0) {
                    customlistivew.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    scroll.smoothScrollTo(0, 0);
                } else {
                    customlistivew.setVisibility(View.GONE);
                }
            } else {
                customlistivew.setVisibility(View.GONE);
            }
        }
    }
}
