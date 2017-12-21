package com.ecjia.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.util.LG;
import com.ecjia.view.adapter.GoodPropertyAdapter;
import com.ecjia.view.adapter.GoodDetailDraft;
import com.umeng.message.PushAgent;


public class GoodPropertyActivity extends BaseActivity
{
    private ListView propertyListView;
    private GoodPropertyAdapter listAdapter;

    private TextView title;
    private ImageView back;
    private int info;
    private FrameLayout no_info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_property_activity);
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        info=intent.getIntExtra("info",0);
        LG.i("获取到的值====="+info);


        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(GoodDetailDraft.getInstance().goodDetail.getGoods_name());
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        propertyListView = (ListView)findViewById(R.id.property_list);
        no_info=(FrameLayout)findViewById(R.id.no_info);
        if(info==0){
            no_info.setVisibility(View.VISIBLE);
            propertyListView.setVisibility(View.GONE);
        }else{
            no_info.setVisibility(View.GONE);
            propertyListView.setVisibility(View.VISIBLE);
        }

        if(GoodDetailDraft.getInstance().goodDetail.getProperties().size() > 0) {
        	propertyListView.setVisibility(View.VISIBLE);
        	listAdapter = new GoodPropertyAdapter(this, GoodDetailDraft.getInstance().goodDetail.getProperties());
            propertyListView.setAdapter(listAdapter);
        } else {
        	propertyListView.setVisibility(View.GONE);
        }
        
        
    }
}
