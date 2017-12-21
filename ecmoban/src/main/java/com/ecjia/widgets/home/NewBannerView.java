package com.ecjia.widgets.home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.banner.BannerConfig;
import com.ecjia.widgets.banner.BannerView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.activity.GoodsDetailActivity;
import com.ecjia.view.activity.GoodsListActivity;
import com.ecjia.entity.responsemodel.PHOTO;
import com.ecjia.entity.responsemodel.PLAYER;
import com.ecjia.util.ecjiaopentype.ECJiaOpenType;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 轮播图
 * Created by Adam on 2016/10/10.
 */
public class NewBannerView extends HomeView<PLAYER> implements AutoPlayInterface, IgnoredInterface {
    private LinearLayout bannerView;
    private RelativeLayout bannerView_in;
    private BannerView<PHOTO> bannerViewPager;

    private LinearLayout group;
    private ArrayList<PHOTO> bannerList;

    public NewBannerView(Activity activity) {
        super(activity);
    }


    @Override
    protected void init() {
        super.init();
        bannerView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.new_banner_scroll_view, null);
        bannerView_in = (RelativeLayout) bannerView.findViewById(R.id.banner_layout_in);
        bannerViewPager = (BannerView<PHOTO>) bannerView.findViewById(R.id.home_banner);
        ViewGroup.LayoutParams params1 = bannerViewPager.getLayoutParams();
        params1.width = getDisplayMetricsWidth();
        params1.height = (int) (params1.width * 1.0 / 2);
        bannerViewPager.setLayoutParams(params1);

    }

    @Override
    public void setVisible() {

    }

    @Override
    public void addToListView(ListView listView) {
        listView.addHeaderView(bannerView);
    }

    @Override
    public void createView(ArrayList<PLAYER> dataList) {
        if (dataList == null && dataList.size() == 0) {
            return;
        }
        mDataList = dataList;
        addBannerView();
    }

    ImageView viewOne;

    public void addBannerView() {
        int size = mDataList.size();
        bannerList = new ArrayList<PHOTO>();

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                bannerList.add(mDataList.get(i).getPhoto());
            }
            bannerViewPager.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

            /**
             * 添加数据
             */
            bannerViewPager.setImages(bannerList);

            bannerViewPager.setOnBannerImageListener(new BannerView.OnLoadImageListener<PHOTO>() {
                @Override
                public void OnLoadImage(ImageView view, PHOTO url) {
                    ImageLoader.getInstance().displayImage(url.getUrl(), view);
                }
            });

            bannerViewPager.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
                @Override
                public void OnBannerClick(View view, int position) {
                    if(position <= -1){
                        position = 0;
                    }
                    if(position >= mDataList.size()){
                        return;
                    }
                    final PLAYER player = mDataList.get(position);
                    if (null == player.getAction()) {
                        if (null != player.getUrl()) {
                            ECJiaOpenType.getDefault().startAction(mActivity, player.getUrl());
                        }
                    } else {
                        if (player.getAction().equals("goods")) {
                            Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                            intent.putExtra(IntentKeywords.GOODS_ID, player.getAction_id() + "");
                            mActivity.startActivity(intent);
                            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        } else if (player.getAction().equals("category")) {
                            Intent intent = new Intent(mActivity, GoodsListActivity.class);
                            intent.putExtra(IntentKeywords.CATEGORY_ID, player.getAction_id() + "");
                            mActivity.startActivity(intent);
                            mActivity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        } else if (null != player.getUrl()) {
                            ECJiaOpenType.getDefault().startAction(mActivity, player.getUrl());
                        }
                    }
                }
            });


        }

    }


    /**
     * 自动播放轮播图
     */
    @Override
    public void startPlay() {
        bannerViewPager.setAutoPlayEnable(true);
        bannerViewPager.setDelayTime(5000);
        bannerViewPager.isAutoPlay(true);
    }


    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onDetach() {
    }

    @Override
    public View getIgnoredView() {
        return bannerViewPager;
    }

    public int getMeasuredHeight() {
        return bannerViewPager.getMeasuredHeight();
    }


    public LinearLayout getBannerView() {
        return bannerView;
    }
}

