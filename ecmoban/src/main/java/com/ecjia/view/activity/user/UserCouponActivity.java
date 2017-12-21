package com.ecjia.view.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.OrderType;
import com.ecjia.entity.responsemodel.USERCOUPON;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.common.T;
import com.ecjia.view.activity.ShopListActivity;
import com.ecjia.view.adapter.UserCouponAdapter;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.widgets.MyPullRefreshFrameLayout;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.subscribers.ResourceSubscriber;

import static com.taobao.accs.ACCSManager.mContext;

/**
 * 类名介绍：用户优惠券页面
 * Created by sun
 * Created time 2017-03-02.
 */
public class UserCouponActivity extends LibActivity {
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.imageView_back)
    ImageView imageView_back;
    @BindView(R.id.textView_more)
    TextView textView_more;

    @BindView(R.id.null_pager)
    FrameLayout null_pager;

    @BindView(R.id.group_top)
    RadioGroup group_top;
    @BindView(R.id.my_pullrefresh)
    MyPullRefreshFrameLayout my_pullrefresh;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private UserCouponAdapter mAdapter;
    private List<USERCOUPON> datas;
    private String nowTypeStr = OrderType.ALLOW_USE;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userconupon;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        textView_title.setText("我的优惠券");
        datas = new ArrayList<>();
        datas.clear();
        mAdapter = new UserCouponAdapter(mActivity, datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(mAdapter);

        RadioButton radioButton = (RadioButton) group_top.getChildAt(0);
        radioButton.setChecked(true);
        getHttpData(OrderType.ALLOW_USE);

        my_pullrefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //执行刷新操作
                getHttpData(nowTypeStr);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, mRecyclerView == null ? content : mRecyclerView, header);
                //return super.checkCanDoRefresh(frame, 需要刷新的当前View == null ? content : 需要刷新的当前View , header);
            }
        });

        group_top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getChildAt(0).getId() == checkedId) {
                    nowTypeStr = OrderType.ALLOW_USE;//可使用
                } else if (group.getChildAt(1).getId() == checkedId) {
                    nowTypeStr = OrderType.IS_USED;//已使用
                } else if (group.getChildAt(2).getId() == checkedId) {
                    nowTypeStr = OrderType.EXPIRED;//已过期
                } else {
                    nowTypeStr = OrderType.ALLOW_USE;
                }
                getHttpData(nowTypeStr);
            }
        });
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<USERCOUPON>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (nowTypeStr.equals(OrderType.ALLOW_USE)) {
                    Intent intent = new Intent(mActivity, ShopListActivity.class);
                    intent.putExtra(IntentKeywords.MERCHANT_ID, datas.get(position).getRu_id());
                    startActivity(intent);
                }
            }
        });
    }


    private void getHttpData(String typeStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIUser()
                .getUserCoupon(UserQuery.getInstance().getUserCoupon(typeStr))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<List<USERCOUPON>>() {
                    @Override
                    public void onNext(List<USERCOUPON> usercoupon) {
                        datas.clear();
                        if (usercoupon.size() > 0) {
                            datas.addAll(usercoupon);
                            mAdapter.notifyDataSetChanged();
                            null_pager.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            null_pager.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        my_pullrefresh.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable t) {
                        null_pager.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        T.show(mActivity, t.getMessage());
                        my_pullrefresh.refreshComplete();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.imageView_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
