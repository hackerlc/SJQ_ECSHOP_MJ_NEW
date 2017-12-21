package com.ecjia.view.activity.push;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.NewBaseFragment;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.EventKeywords;
import com.ecjia.entity.responsemodel.MYMESSAGE;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.view.adapter.MessageAdapter;
import com.ecjia.view.adapter.MsgSql;
import com.ecjia.widgets.XListView;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/1 14:55.
 */

public class PushFragment extends NewBaseFragment implements XListView.IXListViewListener, MessageAdapter.UpdateSql{
    private TextView title;
    private ImageView back;
    private Resources resource;
    private SharedPreferences shared;
    private int page = 0;
    private ArrayList<MYMESSAGE> list, datalist;
    public MessageAdapter adapter;
    private XListView listView;
    private Boolean isrefresh;
    private String[] twotime;
    private FrameLayout nullpage;
    protected Bundle fragmentArgs;

    private int size = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_message,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        PushAgent.getInstance(getContext()).onAppStart();
        if (datalist == null) {
            datalist = new ArrayList<MYMESSAGE>();
        }
        fragmentArgs = getArguments();
        if(fragmentArgs != null) {
            isrefresh = fragmentArgs.getBoolean("refresh", false);
        }

        setInfo();
        getData();
        addData(0);
    }

    @SuppressLint("NewApi")
    private void getData() {
        if (datalist != null) {
            datalist.clear();
        }
        if (list == null) {
            list = new ArrayList<MYMESSAGE>();
        }
        Cursor c = MsgSql.getIntence(getContext()).getAllmsg();
        while (c.moveToNext()) {
            MYMESSAGE msg = new MYMESSAGE();
            String msgtitle = c.getString(1);
            String msgcontent = c.getString(2);
            String msgcustom = c.getString(3);
            String msgtime = c.getString(4);
            String msgtype = c.getString(5);
            String msgurl = c.getString(6);
            String msgActivity = c.getString(7);
            String msg_id = c.getString(8);
            String open_type = c.getString(9);
            String category_id = c.getString(10);
            String webUrl = c.getString(11);
            String goods_id_comment = c.getString(12);
            String goods_id = c.getString(13);
            String order_id = c.getString(14);

            /**
             * 新增的字段
             */
            String keyword = c.getString(15);
            /**
             * 标记未读
             */
            int un_read = c.getInt(16);

            msg.setUn_read(un_read);
            msg.setKeyword(keyword);


            msg.setMsg_id(msg_id);

            msg.setTitle(msgtitle);
            msg.setContent(msgcontent);
            msg.setCustom(msgcustom);
            msg.setType(msgtype);
            msg.setOpen_type(open_type);
            msg.setCategory_id(category_id);
            msg.setWebUrl(webUrl);
            msg.setGoods_id_comment(goods_id_comment);
            msg.setGoods_id(goods_id);
            msg.setOrder_id(order_id);
            twotime = msgtime.split(" ");
            msg.setTime(twotime[1]);
            msg.setDate(twotime[0]);
            datalist.add(msg);
        }
        MsgSql.getIntence(getContext()).db.close();
        if (datalist.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            nullpage.setVisibility(View.GONE);
            if (datalist.size() > 8) {
                listView.setPullRefreshEnable(true);
                listView.setRefreshTime();
            } else {
                listView.setPullRefreshEnable(false);
            }
        } else {
            listView.setVisibility(View.GONE);
            nullpage.setVisibility(View.VISIBLE);
        }
        listView.setPullLoadEnable(false);
    }

    private void setInfo() {
        resource = AppConst.getResources(getContext());
        nullpage = (FrameLayout) getView().findViewById(R.id.push_null_pager);
        listView = (XListView) getView().findViewById(R.id.message_listview);
        listView.setXListViewListener(this, 1);
        listView.setRefreshTime();

    }

    private void addData(int page) {
        int start = page * 8;
        int end = page * 8 + 8;
        for (int i = start; i < end; i++) {
            if (datalist.size() > i) list.add(datalist.get(i));
            else {
                break;
            }
        }
        Message msg = new Message();
        msg.arg1 = 1;

        handler.sendMessageDelayed(msg,1000);
    }

    @Override
    public void onRefresh(int id) {
        ++page;
        addData(page);
    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        list.clear();
        datalist.clear();
    }

    public void onRefresh(){
        if (isrefresh) {
            list.clear();
            datalist.clear();
            getData();
            addData(0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void onEvent(MyEvent event) {

        if ((event.getMsg().equals(EventKeywords.UPDATE_MESSAGE))) {
            LG.i("运行");
            MYMESSAGE mymessage = null;
            try {
                mymessage = event.getMyMessage();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time = df.format(new Date()).toString();
                String[] twotime = time.split(" ");
                mymessage.setTime(twotime[1]);
                mymessage.setDate(twotime[0]);
                list.add(0, mymessage);
                adapter.list = list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ("refresh_push_adpter".equals(event.getMsg())) {
            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.list.size() - 1);
        }

    }

    /**
     * 标记为已读
     *
     * @param msg_id
     */
    @Override
    public void changeReadStatus(String msg_id) {
        changeReadStatus(msg_id, 0);
    }


    /**
     * 改变状态
     *
     * @param msg_id
     * @param un_read
     */
    void changeReadStatus(String msg_id, int un_read) {
        MsgSql.getIntence(getContext()).updateMessageReadStatus(msg_id, un_read);
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.arg1 == 1) {
                listView.stopRefresh();
                listView.setRefreshTime();
                if (adapter == null) {
                    adapter = new MessageAdapter(getContext(), list);
                    adapter.setUpdateSqlListener(PushFragment.this);
                    listView.setAdapter(adapter);
                    size = adapter.list.size();
                    listView.setSelection(adapter.list.size() - 1);
                } else {
                    adapter.list = list;
                    adapter.notifyDataSetChanged();
                    listView.setSelection(adapter.list.size() - size);
                    size = adapter.list.size();
                }
            }
        }
    };
}
