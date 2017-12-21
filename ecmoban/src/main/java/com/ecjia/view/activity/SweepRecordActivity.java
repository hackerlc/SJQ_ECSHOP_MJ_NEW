package com.ecjia.view.activity;

import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.SweepRecordAdapter;
import com.ecjia.view.adapter.SweepSql;
import com.ecjia.entity.responsemodel.SWEEP_HISTORY;

import java.util.ArrayList;

public class SweepRecordActivity extends BaseActivity implements XListView.IXListViewListener {
    private ImageView back;
    private TextView top_view_text, top_right_save;
    private XListView listview;
    private FrameLayout nullpager;
    private SweepSql sweepSql;
    private SweepRecordAdapter sweepRecordAdapter;
    private ArrayList<SWEEP_HISTORY> sweepHistoryList = new ArrayList<SWEEP_HISTORY>();
    private ArrayList<SWEEP_HISTORY> All_sweepHistoryList = new ArrayList<SWEEP_HISTORY>();
    private int page = 0;
    private int count = 8;
    public Handler handler;
    MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep_record);
        initView();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        handler = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.arg1 == 1) {
                    listview.stopRefresh();
                    listview.stopLoadMore();
                    listview.setRefreshTime();
                    if (page == 0) {
                        sweepHistoryList.clear();
                    }
                    int start = page * count;
                    int end = page * count + count;
                    for (int i = start; i < end; i++) {
                        if (All_sweepHistoryList.size() > i)
                            sweepHistoryList.add(All_sweepHistoryList.get(i));
                        else {
                            break;
                        }
                    }
                    if (All_sweepHistoryList.size() > sweepHistoryList.size()) {
                        listview.setPullLoadEnable(true);
                    } else {
                        listview.setPullLoadEnable(false);
                    }
                    if (sweepRecordAdapter == null) {
                        sweepRecordAdapter = new SweepRecordAdapter(SweepRecordActivity.this, sweepHistoryList, (int) res.getDimension(R.dimen.sweep_right_width));
                        sweepRecordAdapter.setOnItemListener(new SweepRecordAdapter.onItemChangeListener() {
                            @Override
                            public void onItemChange() {
                                if (sweepHistoryList.size() == 0) {
                                    listview.setVisibility(View.GONE);
                                    nullpager.setVisibility(View.VISIBLE);
                                    top_right_save.setVisibility(View.GONE);
                                }
                            }
                        });
                        listview.setAdapter(sweepRecordAdapter);
                    } else {
                        sweepRecordAdapter.notifyDataSetChanged();
                    }
                }
            }
        }

        ;
        top_view_text = (TextView) findViewById(R.id.top_view_text);
        top_view_text.setText(res.getString(R.string.sweep_history));
        top_right_save = (TextView) findViewById(R.id.top_right_save);
        top_right_save.setText(res.getString(R.string.top_clean));
        top_right_save.setVisibility(View.VISIBLE);
        top_right_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSweepRecord();
            }
        });
        listview = (XListView) findViewById(R.id.sweep_record_list);

        listview.setXListViewListener(this, 1);
        listview.setRefreshTime();
        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(true);
        nullpager = (FrameLayout) findViewById(R.id.null_pager);

        sweepSql = SweepSql.getIntence(this);

        initData();

        addFirstData(0);

    }


    private void initData() {
        Cursor cursor = sweepSql.getAllRecord();
        while (cursor.moveToNext()) {
            SWEEP_HISTORY sweep_history = new SWEEP_HISTORY();
            sweep_history.setSweep_title(cursor.getString(1));
            sweep_history.setSweep_content(cursor.getString(2));
            sweep_history.setSweep_date(cursor.getString(3));
            All_sweepHistoryList.add(sweep_history);
        }
        sweepSql.db.close();
        if (All_sweepHistoryList.size() > 0) {
            listview.setVisibility(View.VISIBLE);
            nullpager.setVisibility(View.GONE);
            if (All_sweepHistoryList.size() > 0) {
                listview.setPullLoadEnable(true);
                listview.setRefreshTime();
            } else {
                listview.setPullLoadEnable(false);
            }

        } else {
            listview.setVisibility(View.GONE);
            nullpager.setVisibility(View.VISIBLE);
            top_right_save.setVisibility(View.GONE);
        }
    }

    private void addFirstData(int page) {
        int start = page * count;
        int end = page * count + count;
        for (int i = start; i < end; i++) {
            if (All_sweepHistoryList.size() > i) sweepHistoryList.add(All_sweepHistoryList.get(i));
            else {
                break;
            }
        }
        Message msg = new Message();
        msg.arg1 = 1;
        handler.sendMessage(msg);
    }

    //添加数据
    private void addData() {
        Message msg = new Message();
        msg.arg1 = 1;
        handler.sendMessageDelayed(msg, 1000);
    }

    //清空数据
    private void deleteSweepRecord() {
        dialog = new MyDialog(this, res.getString(R.string.qr_clear), res.getString(R.string.sure_clear));
        dialog.show();
        dialog.positive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sweepHistoryList.clear();
                All_sweepHistoryList.clear();
                sweepSql.deletedata();
                sweepRecordAdapter.notifyDataSetChanged();
                listview.setVisibility(View.GONE);
                nullpager.setVisibility(View.VISIBLE);
                top_right_save.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        dialog.negative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onRefresh(int id) {
        page = 0;
        addData();
    }

    @Override
    public void onLoadMore(int id) {
        ++page;
        addData();
    }

    @Override
    protected void onPause() {
        sweepSql.db.close();
        super.onPause();
    }
}
