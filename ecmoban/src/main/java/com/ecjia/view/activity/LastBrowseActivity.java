package com.ecjia.view.activity;

import java.util.ArrayList;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.view.adapter.GoodbrowseAdapter;
import com.ecjia.entity.responsemodel.GOODS;
import com.ecjia.view.adapter.Sqlcl;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.XListView.IXListViewListener;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class LastBrowseActivity extends BaseActivity implements IXListViewListener {
    private XListView xlistView;
    private GoodbrowseAdapter goodbrowseAdapter;
    private View null_paView;
    private MyDialog mDialog;
    private ProgressDialog pb;
    private Sqlcl s;
    private ArrayList<GOODS> list = new ArrayList<GOODS>();
    private ImageView null_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_last_browse);
        initTopView();
        // 移除浏览记录
        null_paView = findViewById(R.id.null_pager);
        xlistView = (XListView) findViewById(R.id.trade_list);
        xlistView.setPullLoadEnable(false);
        xlistView.setPullRefreshEnable(false);
        xlistView.setRefreshTime();
        xlistView.setXListViewListener(this, 1);
        null_img = (ImageView) findViewById(R.id.error_image);
        null_img.setBackgroundResource(R.drawable.no_browse);
        getBrowse();

    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.last_browse_topview);
        topView.setTitleText(R.string.lastbrowse);

        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        topView.setRightType(ECJiaTopView.RIGHT_TYPE_TEXT);
        topView.setRightText(R.string.top_clean, new OnClickListener() {
            @Override
            public void onClick(View v) {
                deletebrowse();
            }
        });
    }

    // 获取缓存
    private void getBrowse() {
        s = new Sqlcl(this);
        Cursor c = s.getGoodBrowse();
        while (c.moveToNext()) {
            int goodid = c.getInt(1);
            String goodsString = c.getString(2);

            GOODS goods = null;
            if (!TextUtils.isEmpty(goodsString)) {
                try {
                    goods = GOODS.fromJson(new JSONObject(goodsString));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            list.add(goods);
        }
        setInfo();
    }

    // 初始化设置
    public void setInfo() {
        Resources resource = (Resources) getBaseContext().getResources();
        String no_browse = resource.getString(R.string.lastbrowse_no_browse);
        if (list.size() == 0) {
            ToastView toast = new ToastView(this, no_browse);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            null_paView.setVisibility(View.VISIBLE);
            topView.getRightTextView().setVisibility(View.GONE);
            xlistView.setVisibility(View.GONE);
        } else {
            null_paView.setVisibility(View.GONE);
            xlistView.setVisibility(View.VISIBLE);
            if (goodbrowseAdapter == null) {
                goodbrowseAdapter = new GoodbrowseAdapter(this, list);
                xlistView.setAdapter(goodbrowseAdapter);
            } else {
                goodbrowseAdapter.list = list;
                goodbrowseAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRefresh(int id) {
        getBrowse();
    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Sqlcl.db.close();
        MobclickAgent.onPause(this);
    }

    //删除浏览记录的对话框
    private void deletebrowse() {
        Resources resource = (Resources) getBaseContext().getResources();
        String delete = resource.getString(R.string.lastbrowse_delete);
        String delete_sure = resource.getString(R.string.lasebrowse_delete_sure);
        mDialog = new MyDialog(this, delete, delete_sure);
        mDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
        mDialog.setPositiveListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                pb = new ProgressDialog(LastBrowseActivity.this);
                pb.show();
                if (s == null)
                    s = new Sqlcl(LastBrowseActivity.this);
                s.delete();
                list.clear();
                topView.getRightTextView().setVisibility(View.GONE);
                onRefresh(1);
                pb.dismiss();
            }
        });
        mDialog.setNegativeListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return true;

    }

}
