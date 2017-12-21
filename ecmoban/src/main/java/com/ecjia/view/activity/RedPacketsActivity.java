package com.ecjia.view.activity;

import android.annotation.SuppressLint;
import android.widget.AdapterView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.RedPacketsAdapter;
import com.ecjia.entity.responsemodel.BONUS;
import com.ecjia.widgets.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.message.PushAgent;

import java.util.ArrayList;

public class RedPacketsActivity extends BaseActivity {

    private ImageView back;
    private TextView submit;
    XListView redPacketsList;
    RedPacketsAdapter redPacketsAdapter;
    ArrayList<BONUS> dataList = new ArrayList<BONUS>();
    BONUS selectedBONUS = null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_paper);

        PushAgent.getInstance(this).onAppStart();

        back = (ImageView) findViewById(R.id.red_papaer_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        redPacketsList = (XListView) findViewById(R.id.red_packet_list);
        redPacketsList.setPullLoadEnable(false);
        redPacketsList.setPullRefreshEnable(false);
        redPacketsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedBONUS = dataList.get(position - 1);
                    redPacketsAdapter.setSelectedPosition(position);
                    redPacketsAdapter.notifyDataSetInvalidated();
                }

            }
        });

        redPacketsAdapter = new RedPacketsAdapter(this, dataList);

        redPacketsList.setAdapter(redPacketsAdapter);

        submit = (TextView) findViewById(R.id.red_papaer_submit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    if (null != selectedBONUS) {
                        intent.putExtra("bonus", selectedBONUS.toJson().toString());
                    }
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Intent intent = getIntent();
        String payment = intent.getStringExtra("payment");

        if (null != payment) {
            try {
                JSONObject jo = new JSONObject(payment);
                JSONArray dataJsonArray = jo.optJSONArray("bonus");
                if (null != dataJsonArray && dataJsonArray.length() > 0) {
                    dataList.clear();
                    for (int i = 0; i < dataJsonArray.length(); i++) {
                        JSONObject bonusJsonObject = dataJsonArray
                                .getJSONObject(i);
                        BONUS bonus_list_Item = BONUS.fromJson(bonusJsonObject);
                        dataList.add(bonus_list_Item);
                    }
                } else {
                    redPacketsList.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
