package com.ecjia.widgets.home;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.ecjia.entity.responsemodel.QUICK;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.NewQuickNewItemBinding;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/7/18.
 */
public class NewQuickEnter extends HomeView<QUICK> implements View.OnClickListener{
    NewQuickNewItemBinding mBinding;
    ArrayList<QUICK> dataList ;
    public NewQuickEnter(Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity),R.layout.new_quick_new_item,null,false);
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
        this.dataList=dataList;
        //调整数据为四个超出不管
        if(this.dataList==null){
            this.dataList=new ArrayList<>();
        }
        if(this.dataList.size()<4){
            for(int i= 0;i<4-this.dataList.size();i++){
                this.dataList.add(new QUICK());
            }
        }
        mBinding.setList(this.dataList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_div_1:
                ECJiaOpenType.getDefault().startAction(mActivity,dataList.get(0).getUrl());
                break;
            case R.id.rl_div_2:
                ECJiaOpenType.getDefault().startAction(mActivity,dataList.get(1).getUrl());
                break;
            case R.id.rl_div_3:
                ECJiaOpenType.getDefault().startAction(mActivity,dataList.get(2).getUrl());
                break;
            case R.id.rl_div_4:
                ECJiaOpenType.getDefault().startAction(mActivity,dataList.get(3).getUrl());
                break;
        }
    }
}
