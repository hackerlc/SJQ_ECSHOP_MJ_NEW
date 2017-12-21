package com.ecjia.view.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.RedPaperModel;
import com.ecjia.view.adapter.RedPapperAdapter;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;


public class RedpapperListActivity extends BaseActivity {


    private TextView tittle;
    private ImageView back;
    private ListView redpapper_list;
    RedPapperAdapter adapter;
    FrameLayout nullpager;
    private RedPaperModel redPaperModel;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_redpapper_list);

        PushAgent.getInstance(this).onAppStart();

        redPaperModel=new RedPaperModel(this);
        redPaperModel.addResponseListener(this);

        setinfo();

        redPaperModel.getRedPaperList("");
    }

    private void setinfo() {
        //返回事件
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置标题
        Resources resource = (Resources) getBaseContext()
                .getResources();
        String detail = resource.getString(R.string.redpaper_detail);
        tittle = (TextView) findViewById(R.id.top_view_text);
        tittle.setText(detail);
        nullpager = (FrameLayout) findViewById(R.id.null_pager);
        redpapper_list = (ListView) findViewById(R.id.redpapper_list);

        adapter = new RedPapperAdapter(redPaperModel.bonuslist, this);
        redpapper_list.setAdapter(adapter);

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if(url== ProtocolConst.USER_BONUS){
            if(status.getSucceed()==1){
                if (redPaperModel.bonuslist.size() > 0) {
                    nullpager.setVisibility(View.GONE);
                    redpapper_list.setVisibility(View.VISIBLE);

                } else {
                    nullpager.setVisibility(View.VISIBLE);
                    redpapper_list.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}

