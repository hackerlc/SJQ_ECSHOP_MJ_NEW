package com.ecjia.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.InviteModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ErrorView;
import com.ecjia.widgets.XListView;
import com.ecjia.widgets.clipviewpager.ClipViewPager;
import com.ecjia.widgets.clipviewpager.ScalePageTransformer;
import com.ecjia.view.adapter.MyInvitationAdapter;
import com.ecjia.view.adapter.MyRecordAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.INVITE_RECORD;
import com.ecjia.entity.responsemodel.INVITE_REWARD;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class InvitationRewardActivity extends BaseActivity implements HttpResponse,XListView.IXListViewListener {


    private ArrayList<INVITE_REWARD> invite_rewards =new ArrayList<INVITE_REWARD>();
    private ArrayList<INVITE_RECORD> invite_records =new ArrayList<INVITE_RECORD>();
    private XListView listView;
    private ErrorView nullpager;
    private InviteModel inviteModel;
    private RelativeLayout page_container;
    private ClipViewPager viewpager;
    private MyInvitationAdapter myInvitationAdapter;
    private MyRecordAdapter myRecordAdapter;
    private String date;
    private int lastposition;
    private int nowselect;
    private boolean isChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_reward);
        PushAgent.getInstance(this).onAppStart();

        ArrayList<INVITE_REWARD> temp=(ArrayList<INVITE_REWARD>)getIntent().getSerializableExtra("rewards");

        if(temp!=null){
            invite_rewards.addAll(temp);
        }

        inviteModel = new InviteModel(this);
        inviteModel.addResponseListener(this);

        initView();

        inviteModel.getInviteRecord(invite_rewards.get(invite_rewards.size()-1).getInvite_data(), true);
    }

    private void initView() {
        initTopView();

        page_container=(RelativeLayout)findViewById(R.id.page_container);
        viewpager=(ClipViewPager)findViewById(R.id.viewpager);

        viewpager.setPageTransformer(true, new ScalePageTransformer());

        //需要将整个页面的事件分发给ViewPager，不然的话只有ViewPager中间的view能滑动，其他的都不能滑动，这是肯定的，
        //因为ViewPager总体布局就是中间那一块大小，其他的子布局都跑到ViewPager外面来了
        page_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewpager.dispatchTouchEvent(event);
            }
        });


        myInvitationAdapter=new MyInvitationAdapter(this, invite_rewards);
        viewpager.setAdapter(myInvitationAdapter);
        viewpager.setOffscreenPageLimit(invite_rewards.size());
        viewpager.setCurrentItem(invite_rewards.size() - 1);
        myInvitationAdapter.notifyDataSetChanged();

        nullpager = (ErrorView) findViewById(R.id.null_pager);
        listView = (XListView) findViewById(R.id.xlv_invitation);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this, 1);

        myRecordAdapter=new MyRecordAdapter(this,inviteModel.invite_records);
        listView.setAdapter(myRecordAdapter);



        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                nowselect = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    if (lastposition != nowselect) {
                        viewpager.setScrollble(false);
                        inviteModel.getInviteRecord(invite_rewards.get(nowselect).getInvite_data(), true);
                    }
                }
            }
        });
    }


    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.reward_topview);
        topView.setTitleText(R.string.invitation_reward_detail);
        topView.setLeftBackImage(BACKIMAGE_ID, new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.INVITE_RECORD) {
            viewpager.setScrollble(true);
            if (status.getSucceed() == 1) {
                lastposition = nowselect;
                listView.setRefreshTime();
                listView.stopRefresh();
                listView.stopLoadMore();
                PAGINATED paginated = inviteModel.paginated;
                if (paginated.getMore() == 1) {
                    listView.setPullLoadEnable(true);
                } else {
                    listView.setPullLoadEnable(false);
                }

                if(inviteModel.invite_records.size()>0){
                    listView.setVisibility(View.VISIBLE);
                    nullpager.setVisibility(View.GONE);
                }else{
                    listView.setVisibility(View.GONE);
                    nullpager.setVisibility(View.VISIBLE);
                }

                myRecordAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onRefresh(int id) {
        inviteModel.getInviteRecord(invite_rewards.get(nowselect).getInvite_data(), false);
    }

    @Override
    public void onLoadMore(int id) {
        inviteModel.getInviteRecordMore(invite_rewards.get(nowselect).getInvite_data());
    }
}

