package com.ecjia.widgets.home;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.NewHomeEventBinding;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/10/18.
 */
public class NewEventView extends HomeView<QUICK> implements View.OnClickListener {
    NewHomeEventBinding mBinding;

    ArrayList<QUICK> dataList;

    public NewEventView(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.new_home_event, null, false);
        mBinding.setOnClick(this);
    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(mBinding.getRoot());
    }

    @Override
    public void createView(ArrayList<QUICK> dataList) {
        this.dataList = dataList;
        mBinding.setList(this.dataList);
        if (dataList.size() > 0) {
            String img = dataList.get(0).getImg();
            String url = dataList.get(0).getUrl();
            if ("".equals(img) || img == null
                    || "".equals(url) || url == null) {
                mBinding.ivImg1.setVisibility(View.GONE);
            } else {
                mBinding.ivImg1.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(dataList ==null){
            return;
        }
        switch (v.getId()) {
            case R.id.iv_img_1:
                if(dataList.size() >= 1) {
                    if (dataList.get(0) != null) {
                        ECJiaOpenType.getDefault().startAction(mActivity, dataList.get(0).getUrl());
                    }
                }
                break;
            case R.id.iv_img_2:
                if(dataList.size() >= 2) {
                    if (dataList.get(1) != null) {
                        ECJiaOpenType.getDefault().startAction(mActivity, dataList.get(1).getUrl());
                    }
                }
                break;
            case R.id.iv_img_3:
                if(dataList.size() >= 3) {
                    if (dataList.get(2) != null) {
                        ECJiaOpenType.getDefault().startAction(mActivity, dataList.get(2).getUrl());
                    }
                }
                break;
            case R.id.iv_img_4:
                if(dataList.size() >= 4) {
                    if (dataList.get(3) != null) {
                        ECJiaOpenType.getDefault().startAction(mActivity, dataList.get(3).getUrl());
                    }
                }
                break;
        }
    }
}
