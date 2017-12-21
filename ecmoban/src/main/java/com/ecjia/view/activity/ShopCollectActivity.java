package com.ecjia.view.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.SellerModel;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.XListView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.ShopCollectAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopCollectActivity extends BaseActivity implements XListView.IXListViewListener,HttpResponse {


    private ImageView collect_back;
    private TextView collect_edit;
    private XListView collect_list;
    private SellerModel sellerModel;
    private boolean isEdit = false;
    private ShopCollectAdapter shopCollectAdapter;
    private ProgressDialog pd;
    private View null_pager;
    private Handler messageHandler;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_collect);
        PushAgent.getInstance(this).onAppStart();

        if(null==sellerModel){
            sellerModel=new SellerModel(this);
        }
        sellerModel.addResponseListener(this);
        initView();
        sellerModel.getShopCollectList();

        // 判断是否有需要删除的商品
        messageHandler = new Handler() {
            public void handleMessage(Message msg) {
                String fin = res.getString(R.string.collect_done);
                String del = res.getString(R.string.collect_delete);

                if (msg.arg1 == 0) {
                    collect_edit.setText(fin);
                } else if (msg.arg1 == 1) {
                    collect_edit.setText(del);
                }else if(msg.arg1==100){
                    shopCollectAdapter.notifyDataSetChanged();//刷新页面
                }
            }
        };
    }

    private void initView() {
        collect_back=(ImageView)findViewById(R.id.collect_back);
        collect_edit=(TextView)findViewById(R.id.collect_edit);
        collect_edit.setEnabled(false);
        null_pager = findViewById(R.id.null_pager);
        collect_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collect_list = (XListView) findViewById(R.id.collect_list);
        if(null==shopCollectAdapter){
            shopCollectAdapter=new ShopCollectAdapter(this,sellerModel.sellercollects,1);
        }
        collect_list.setXListViewListener(this,1);
        collect_list.setAdapter(shopCollectAdapter);

        collect_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isEdit){
                    Intent intent=new Intent(ShopCollectActivity.this,ShopListActivity.class);
                    intent.putExtra(IntentKeywords.MERCHANT_ID,shopCollectAdapter.list.get(position-1).getId());
                    startActivity(intent);
                }
            }
        });

        collect_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fin = res.getString(R.string.collect_done);// 完成
                String cancel_collect = res.getString(R.string.collect_cancel_collect);
                String cancel_sure = res.getString(R.string.collect_cancel_sure);
                final String com = res.getString(R.string.collect_compile);// 编辑
                if (!isEdit) {
                    shopCollectAdapter.flag = 2;
                    shopCollectAdapter.notifyDataSetChanged();
                    collect_list.setPullRefreshEnable(false);

                    isEdit = true;
                    collect_edit.setText(fin);
                } else {
                    collect_list.setPullRefreshEnable(true);
                    isEdit = false;
                    if (haveselect()) {
                        for (int i = 0; i < sellerModel.sellercollects.size(); i++) {
                            LG.i(i + "需要删除====" + sellerModel.sellercollects.get(i).isChoose());
                        }

                        final MyDialog dialog = new MyDialog(
                                ShopCollectActivity.this, cancel_collect,cancel_sure);
                        dialog.show();
                        dialog.positive
                                .setOnClickListener(new View.OnClickListener() {
                                    Resources resource = (Resources) getBaseContext()
                                            .getResources();
                                    String delete_success = resource.getString(R.string.collect_delete_success);
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        deleteCollectitems();
                                        ToastView toast = new ToastView(ShopCollectActivity.this,delete_success);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                        shopCollectAdapter.flag = 1;
                                        shopCollectAdapter.notifyDataSetChanged();
                                        collect_edit.setText(com);
                                    }
                                });
                        dialog.negative
                                .setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        initdatalist();
                                        shopCollectAdapter.flag = 1;
                                        shopCollectAdapter.notifyDataSetChanged();
                                        collect_edit.setText(com);
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        shopCollectAdapter.flag = 1;
                        shopCollectAdapter.notifyDataSetChanged();
                        collect_edit.setText(com);
                    }

                }
            }

        });


        collect_list.setPullRefreshEnable(true);
        collect_list.setPullLoadEnable(false);
    }

    // 是否有需要删除的选项
    private boolean haveselect() {
        for (int i = 0; i < sellerModel.sellercollects.size(); i++) {
            if (sellerModel.sellercollects.get(i).isChoose()) {
                return true;
            }
        }
        return false;
    }

    // 初始化数据
    private void initdatalist() {
        for (int i = 0; i <  sellerModel.sellercollects.size(); i++) {
            if (sellerModel.sellercollects.get(i).isChoose()) {
                sellerModel.sellercollects.get(i).setChoose(false);
            }
        }
    }

    // 删除收藏商品
    void deleteCollectitems() {
        if (pd == null) {
            pd = new ProgressDialog(ShopCollectActivity.this);
        }
        pd.show();
        for (int i = 0; i < sellerModel.sellercollects.size(); i++) {
            if (sellerModel.sellercollects.get(i).isChoose()) {
                sellerModel.sellerCollectDelete(sellerModel.sellercollects.get(i).getId());
                sellerModel.sellercollects.remove(i);
                i--;
            }
        }
        pd.dismiss();
    }

    //初始化显示
    public void setCollectList() {
        if (sellerModel.sellercollects.size() == 0) {
            collect_edit.setEnabled(false);
            collect_edit.setVisibility(View.GONE);
            if (shopCollectAdapter != null) {
                shopCollectAdapter.notifyDataSetChanged();
            }
            null_pager.setVisibility(View.VISIBLE);
            collect_list.setVisibility(View.GONE);

        } else {
            collect_list.setVisibility(View.VISIBLE);
            null_pager.setVisibility(View.GONE);
            collect_edit.setEnabled(true);
            collect_edit.setVisibility(View.VISIBLE);
            if (shopCollectAdapter != null) {
                shopCollectAdapter.notifyDataSetChanged();
            }
            shopCollectAdapter.parentHandler = messageHandler;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return true;
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url.equals(ProtocolConst.SELLER_COLLECT)){
            if(status.getSucceed()==1){
                collect_list.setRefreshTime();
                collect_list.stopRefresh();
                collect_list.stopLoadMore();
                if (sellerModel.paginated.getMore() == 0) {
                    collect_list.setPullLoadEnable(false);
                } else {
                    collect_list.setPullLoadEnable(true);
                }
                setCollectList();
            }
        }else if(url.equals(ProtocolConst.SELLER_COLLECTDELETE)){
            if(status.getSucceed()==1){
                if (sellerModel.sellercollects.size() == 0) {
                    collect_edit.setEnabled(false);
                    null_pager.setVisibility(View.VISIBLE);
                    collect_list.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        sellerModel.getShopCollectList();
    }

    @Override
    public void onLoadMore(int id) {
        sellerModel.getShopCollectListMore();
    }
}
