package com.ecjia.view.activity.push;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.ecjia.base.NewBaseActivity;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.activity.ecchat.ECChatActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityPushMessageBinding;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.ArrayList;
import java.util.List;

public class PushActivity extends NewBaseActivity{
    ActivityPushMessageBinding mBinding;
    private Boolean isrefresh;

    private PushAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    private PushFragment pushFragment;
    private EaseConversationListFragment conversationListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_push_message);
        mBinding.setOnClick(this);
        isrefresh = getIntent().getBooleanExtra("refresh", false);
        initData();
        initUI();
    }

    private void initData() {
        mTitles.add("系统消息");
        mTitles.add("会话消息");

        pushFragment = new PushFragment();
        conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(conversation ->
                startActivity(new Intent(PushActivity.this, ECChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId())));
        mFragmentList.add(pushFragment);
        mFragmentList.add(conversationListFragment);

        mAdapter = new PushAdapter(getSupportFragmentManager(), mFragmentList, mTitles);
    }


    private void initUI() {
        mBinding.icHead.tvTitle.setText("消息列表");

        mBinding.vpMessage.setAdapter(mAdapter);
        mBinding.icTab.tlTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mBinding.icTab.tlTablayout.setupWithViewPager(mBinding.vpMessage);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        isrefresh = intent.getBooleanExtra("refresh", false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isrefresh) {
                Intent intent = new Intent(PushActivity.this, ECJiaMainActivity.class);
                startActivity(intent);
                PushActivity.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.iv_back_button:finish();
        }
    }
}
