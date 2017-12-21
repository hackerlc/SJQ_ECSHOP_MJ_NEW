package com.ecjia.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.USERMENBERCHILDTYPE;
import com.ecjia.util.common.T;
import com.ecjia.view.adapter.UserApplyMenberChildAdapter;
import com.ecjia.view.adapter.UserCouponAdapter;
import com.ecmoban.android.sijiqing.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：采购商认证，主营类目选择
 * Created by sun
 * Created time 2017-02-21.
 */

public class UserApplyMenberChildActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    UserApplyMenberChildAdapter mAdapter;
    private List<USERMENBERCHILDTYPE> datas;
    String menbertype = "";

    private String[] typeNames = {"男装", "女装", "童装", "其他"};
    private String[] intentTypeNames;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_applymenber_child;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        menbertype = getIntent().getStringExtra(IntentKeywords.MENBER_TYPE);
        if (!TextUtils.isEmpty(menbertype)) {
            if (menbertype.split("/").length > 0) {
                intentTypeNames = menbertype.split("/");
            }
        }
        textView_title.setText("主营类目");
        textView_more.setText("完成");
        datas = new ArrayList<>();
        datas.clear();
        initdataList();
        mAdapter = new UserApplyMenberChildAdapter(mActivity, datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.imageView_back, R.id.textView_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.textView_more:
                String str = typeStr();
                if (str.length() > 1) {
                    str = str.substring(0, str.length() - 1);
                } else {
                    T.show(mActivity, "请选择类目");
                    return;
                }
                Intent mIntent = new Intent();
                mIntent.putExtra(IntentKeywords.MENBER_TYPE, str);
                T.show(mActivity,str);
                // 设置结果，并进行传送
                setResult(1001, mIntent);
                finish();
                break;
        }
    }

    @Override
    public void dispose() {

    }

    private void initdataList() {
        for (String t : typeNames) {
            boolean isselect = false;
            if (!TextUtils.isEmpty(menbertype)) {
                for (String intentT : intentTypeNames) {
                    if (t.equals(intentT)) {
                        isselect = true;
                    }
                }
            }
            datas.add(initdata(t, isselect));
        }
    }

    private String typeStr() {
        String str = "";
        for (USERMENBERCHILDTYPE menberType : datas) {
            if (menberType.isSelect()) {
                str =str+ menberType.getTagName() + "/";
            }
        }
        return str;
    }

    private USERMENBERCHILDTYPE initdata(String typeName, boolean isSelect) {
        USERMENBERCHILDTYPE typeData = new USERMENBERCHILDTYPE();
        typeData.setTagName(typeName);
        typeData.setSelect(isSelect);
        return typeData;
    }
}
