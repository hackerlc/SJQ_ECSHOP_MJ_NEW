package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.model.GroupGoods;
import com.ecjia.util.DateManager;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.together.TogetherWholesaleActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.HomeHotsellBinding;
import com.ecmoban.android.sijiqing.databinding.ItemTogetherWholesaleBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import gear.yc.com.gearlibrary.utils.ToastUtil;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Adam on 2016/10/14.
 */
public class NewPromotionView extends HomeView<GroupGoods> {
    HomeHotsellBinding mBinding;

    public NewPromotionView(final Activity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        super.init();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity),R.layout.home_hotsell,null,false);
        mBinding.icHead.tvTitle.setText("团批区");
        mBinding.icHead.tvTitleTag.setText("拼团数量越多，价格越便宜");
        mBinding.icHead.homePromoteGetmore.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, TogetherWholesaleActivity.class);
            mActivity.startActivity(intent);
            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        });
    }

    //这里沿用以前的方式，后期需要修改为列表方式
    List<TextView> tvTime=new ArrayList<>();
    long[] times;
    Disposable mDisposable;
    private void setHotCellView() {
        mBinding.myhotItem.removeAllViews();
        if (mDataList.size() > 0) {
            times=new long[mDataList.size()];
            ItemTogetherWholesaleBinding binding=null;
            tvTime.clear();
            for (int i = 0; i < mDataList.size(); i++) {
                GroupGoods data = mDataList.get(i);
                binding=DataBindingUtil.inflate(LayoutInflater.from(mActivity),R.layout.item_together_wholesale,null,false);
                ImageLoaderForLV.getInstance(mActivity).setImageRes(binding.ivGoodsImg1,data.getImg1());
                ImageLoaderForLV.getInstance(mActivity).setImageRes(binding.ivGoodsImg2,data.getImg2());
                ImageLoaderForLV.getInstance(mActivity).setImageRes(binding.ivGoodsImg3,data.getImg3());
                tvTime.add(binding.tvOverTime);
                times[i]=data.getLeft_second();
                binding.tvGoodsName.setText(data.getName());
                binding.ipPrice.setSection(data.getSections());
                binding.ipPrice.setBuyNum(data.getBuy_num());
                binding.ipPrice.setProgress();

                binding.getRoot().setOnClickListener(v -> {
                    Intent it = new Intent(mActivity, GoodsDetailActivity.class);
                    it.putExtra(IntentKeywords.GOODS_ID, data.getId());
                    it.putExtra(IntentKeywords.OBJECT_ID, data.getObjectId());
                    it.putExtra(IntentKeywords.REC_TYPE, data.getRecType());
                    mActivity.startActivity(it);
                    mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                });
                mBinding.myhotItem.addView(binding.getRoot());
            }
            //开启定时器倒计时
            if(tvTime.size()>0){
                mDisposable=Flowable.interval(0,1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            for(int i=0;i<tvTime.size();i++){
                                --times[i];
                                tvTime.get(i).setText(DateManager.formatDateTime(times[i]));
                            }
                        },e -> ToastUtil.getInstance().makeShortToast(mActivity,e.getMessage()));
            }
        }

    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(mBinding.getRoot());
    }

    @Override
    public void createView(ArrayList<GroupGoods> dataList) {
        if(mDisposable!=null){
            mDisposable.dispose();
        }
        if (dataList == null || dataList.size() == 0) {
            mBinding.icHead.hotBigItem.setVisibility(View.GONE);
            mBinding.myhotItem.setVisibility(View.GONE);
            mBinding.myhotItem.removeAllViews();
            //停止计时
            return;
        }
        mBinding.icHead.hotBigItem.setVisibility(View.VISIBLE);
        mBinding.myhotItem.setVisibility(View.VISIBLE);
        mDataList = dataList;
        setHotCellView();
    }


}
