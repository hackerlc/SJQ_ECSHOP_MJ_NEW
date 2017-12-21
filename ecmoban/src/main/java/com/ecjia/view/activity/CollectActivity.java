package com.ecjia.view.activity;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;

import com.ecjia.view.adapter.CollectAdapter;
import com.ecjia.network.netmodle.CollectListModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.COLLECT_LIST;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;

import com.ecjia.widgets.dialog.MyDialog;
import com.umeng.message.PushAgent;

import de.greenrobot.event.EventBus;

/**
 * 收藏页面
 */
public class CollectActivity extends BaseActivity implements
        IXListViewListener {

    private ImageView back;
    private TextView edit;
    private XListView xlistView;
    private CollectAdapter collectAdapter;
    private boolean isEdit = false;
    COLLECT_LIST collect;
    private CollectListModel collectListModel;
    private ProgressDialog pd;
    public Handler messageHandler;
    private View null_pager;
    public Handler Internethandler;
    public Resources resource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect);
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        back = (ImageView) findViewById(R.id.collect_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                CollectActivity.this.overridePendingTransition(
                        R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        if (AppConst.datalist == null) {
            AppConst.datalist = new ArrayList<HashMap<String, String>>();
        }
        null_pager = findViewById(R.id.null_pager);
        edit = (TextView) findViewById(R.id.collect_edit);
        edit.setEnabled(false);
        xlistView = (XListView) findViewById(R.id.collect_list);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this, 1);

        Internethandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.obj == ProtocolConst.COLLECT_LIST) {
                    if (msg.what == 1) {
                        xlistView.setRefreshTime();
                        xlistView.stopRefresh();
                        xlistView.stopLoadMore();
                        if (collectListModel.paginated.getMore() == 0) {
                            xlistView.setPullLoadEnable(false);
                        } else {
                            xlistView.setPullLoadEnable(true);
                        }
                        setCollectList();
                    }
                }
                if (msg.obj == ProtocolConst.COLLECT_DELETE) {
                    if (AppConst.datalist.size() == 0) {
                        edit.setEnabled(false);
                        null_pager.setVisibility(View.VISIBLE);
                        xlistView.setVisibility(View.GONE);
                    }
                }

            }
        };

        edit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                resource = (Resources) getBaseContext()
                        .getResources();
                String fin = resource.getString(R.string.collect_done);// 完成
                String cancel_collect = resource.getString(R.string.collect_cancel_collect);
                String cancel_sure = resource.getString(R.string.collect_cancel_sure);
                String delete_success = resource.getString(R.string.collect_delete_success);
                // String fin = "删除";// 完成
                final String com = resource.getString(R.string.collect_compile);// 编辑
                if (!isEdit) {
                    collectAdapter.flag = 2;
                    collectAdapter.notifyDataSetChanged();
                    xlistView.setPullRefreshEnable(false);

                    isEdit = true;
                    edit.setText(fin);
                } else {
                    xlistView.setPullRefreshEnable(true);
                    isEdit = false;
                    if (haveselect()) {
                        for (int i = 0; i < AppConst.datalist.size(); i++) {
                            LG.i(i + "需要删除====" + AppConst.datalist.get(i).get("flag").equals("true"));
                        }

                        final MyDialog dialog = new MyDialog(CollectActivity.this, cancel_collect, cancel_sure);
                        dialog.show();
                        dialog.positive.setOnClickListener(new OnClickListener() {
                            Resources resource = (Resources) getBaseContext()
                                    .getResources();
                            String delete_success = resource.getString(R.string.collect_delete_success);

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                deleteCollectitems();
                                EventBus.getDefault().post(new MyEvent("userinfo_refresh"));
                                ToastView toast = new ToastView(CollectActivity.this, delete_success);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                collectAdapter.flag = 1;
                                collectAdapter.notifyDataSetChanged();
                                edit.setText(com);
                            }
                        });
                        dialog.negative
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        initdatalist();
                                        collectAdapter.flag = 1;
                                        collectAdapter.notifyDataSetChanged();
                                        edit.setText(com);
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        collectAdapter.flag = 1;
                        collectAdapter.notifyDataSetChanged();
                        edit.setText(com);
                    }

                }
            }

        });
        messageHandler = new Handler() {
            // 判断是否有需要删除的商品
            public void handleMessage(Message msg) {
                resource = (Resources) getBaseContext()
                        .getResources();
                String fin = resource.getString(R.string.collect_done);
                String del = resource.getString(R.string.collect_delete);

                if (msg.arg1 == 0) {
                    edit.setText(fin);
                } else if (msg.arg1 == 1) {
                    edit.setText(del);
                }
            }
        };

        collectListModel = new CollectListModel(this);
        collectListModel.getCollectList(Internethandler, true);

    }

    // 是否有需要删除的选项
    private boolean haveselect() {
        for (int i = 0; i < AppConst.datalist.size(); i++) {
            if (AppConst.datalist.get(i).get("flag").equals("true")) {
                return true;
            }
        }
        return false;
    }

    // 删除收藏商品
    void deleteCollectitems() {
        if (pd == null) {
            pd = new ProgressDialog(CollectActivity.this);
        }
        pd.show();
        for (int i = 0; i < AppConst.datalist.size(); i++) {
            if (AppConst.datalist.get(i).get("flag").equals("true")) {
                try {
                    collect = COLLECT_LIST.fromJson(new JSONObject(
                            AppConst.datalist.get(i).get("content")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                collectListModel.collectDelete(collect.getRec_id() + "", Internethandler);
                AppConst.datalist.remove(i);
                i--;
            }
        }
        collectAdapter.list = AppConst.datalist;
        pd.dismiss();
    }

    public void setCollectList() {
        Resources resource = (Resources) getBaseContext().getResources();

        if (collectListModel.collectList.size() == 0) {
            edit.setEnabled(false);
            edit.setVisibility(View.GONE);
            if (collectAdapter != null) {
                collectAdapter.list = AppConst.datalist;
                collectAdapter.notifyDataSetChanged();
            }
            null_pager.setVisibility(View.VISIBLE);
            xlistView.setVisibility(View.GONE);

        } else {
            xlistView.setVisibility(View.VISIBLE);
            null_pager.setVisibility(View.GONE);
            getData(collectListModel.collectList);
            edit.setEnabled(true);
            edit.setVisibility(View.VISIBLE);
            if (collectAdapter == null) {
                collectAdapter = new CollectAdapter(this, AppConst.datalist,
                        1);
                xlistView.setAdapter(collectAdapter);
            } else {

                collectAdapter.list = AppConst.datalist;
                collectAdapter.notifyDataSetChanged();
            }
            collectAdapter.parentHandler = messageHandler;
        }

    }

    // 重组数据
    private void getData(ArrayList<COLLECT_LIST> collectList) {
        if (AppConst.datalist.size() == 0) {// 首次获得数据
            for (int i = 0; i < collectList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    map.put("content", collectList.get(i).toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("flag", "false");
                AppConst.datalist.add(map);
            }
        } else {// 追加数据
            for (int i = 0 + AppConst.datalist.size(); i < collectList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    map.put("content", collectList.get(i).toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.put("flag", "false");
                AppConst.datalist.add(map);
            }
        }
    }

    // 初始化数据
    private void initdatalist() {
        for (int i = 0; i < AppConst.datalist.size(); i++) {
            if (AppConst.datalist.get(i).get("flag").equals("true")) {
                AppConst.datalist.get(i).put("flag", "false");
            }
        }
    }


    @Override
    public void onRefresh(int id) {
        resource = getBaseContext().getResources();
        String compile = resource.getString(R.string.collect_compile);
        AppConst.datalist.clear();
        collectAdapter.flag = 1;
        edit.setText(compile);
        collectListModel.getCollectList(Internethandler, false);
    }

    @Override
    public void onLoadMore(int id) {
        collectListModel.getCollectListMore(Internethandler);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        AppConst.datalist.clear();
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {

    }
}
