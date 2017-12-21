package com.ecjia.view.activity;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.network.netmodle.HelpModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.view.adapter.HelpNewAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;


public class HelpListActivity extends BaseActivity implements HttpResponse {
    HelpModel dataModel;
    StickyListHeadersListView helpListView;
    HelpNewAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_list);

        PushAgent.getInstance(this).onAppStart();

        initTopView();

        helpListView = (StickyListHeadersListView) findViewById(R.id.help_listview);
        helpListView.setDivider(ContextCompat.getDrawable(this,R.drawable.stick_list_diver));
        dataModel = new HelpModel(this);
        dataModel.addResponseListener(this);
        dataModel.fetchShopHelp();

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.SHOPHELP) {
            if (status.getSucceed() == 1) {
                listAdapter = new HelpNewAdapter(this, dataModel.shophelpsList);
                helpListView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.helplist_topview);
        topView.setTitleText(R.string.main_help);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
