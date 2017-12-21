package com.ecjia.view.activity.ecchat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ecjia.base.NewBaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityEcChatBinding;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.Goods;
import com.hyphenate.easeui.ui.EaseChatFragment;

/**
 * SJQ_ECSHOP_MJ_NEW
 * Created by YichenZ on 2017/4/1 09:08.
 */

public class ECChatActivity extends NewBaseActivity{
    ActivityEcChatBinding mBinding;
    // 当前聊天的 ID
    private String mChatId;
    private EaseChatFragment chatFragment;

    //goods
    String goodsId,goodsPrice,goodsImg,goodsTitle,hxString,hxName;
    Goods mGoods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_ec_chat);

        initdata();
        // 这里直接使用EaseUI封装好的聊天界面
        chatFragment = new EaseChatFragment();
        // 将参数传递给聊天界面
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, mChatId);
        args.putSerializable(EaseConstant.EXTRA_GOODS,mGoods);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();
    }

    private void initdata() {
        mChatId = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        if(null == mChatId || "".equals(mChatId)){
            mChatId = "imtest001";
        }
        goodsId = String.valueOf(getIntent().getIntExtra("goods_id", 0));
        goodsPrice = getIntent().getStringExtra("goods_price");
        goodsImg = getIntent().getStringExtra("goods_img");
        goodsTitle = getIntent().getStringExtra("goods_title");
        hxString = getIntent().getStringExtra("goods_hx_text");
        hxName = getIntent().getStringExtra("hxName");

        if(!"".equals(goodsId) || goodsId != null){
            mGoods = new Goods(goodsId, goodsTitle, goodsImg, goodsPrice, hxString, mChatId, hxName);
        } else {
            mGoods = null;
        }
    }
}
