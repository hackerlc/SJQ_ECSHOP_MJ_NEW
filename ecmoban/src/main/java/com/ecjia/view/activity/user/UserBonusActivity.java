package com.ecjia.view.activity.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ecjia.base.LibActivity;
import com.ecjia.consts.OrderType;
import com.ecjia.entity.model.AddressData;
import com.ecjia.entity.responsemodel.USERBONUS;
import com.ecjia.entity.responsemodel.USERMENBERS;
import com.ecjia.network.base.BaseRes;
import com.ecjia.network.base.RSubscriberAbstract;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.GoodsQuery;
import com.ecjia.network.query.UserQuery;
import com.ecjia.util.common.T;
import com.ecjia.util.common.VUtils;
import com.ecjia.view.adapter.UserBonusAdapter;
import com.ecjia.widgets.MyPullRefreshFrameLayout;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 类名介绍：用户优红包页面
 * Created by sun
 * Created time 2017-03-02.
 */
public class UserBonusActivity extends LibActivity {
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

    @BindView(R.id.edit_bonus_pwd)
    EditText edit_bonus_pwd;
    @BindView(R.id.tv_verification)
    TextView tv_verification;


    private UserBonusAdapter mAdapter;
    private List<USERBONUS> datas;
    private String nowTypeStr = OrderType.ALLOW_USE;

    @Override
    public int getLayoutId() {
        return R.layout.activity_userbonus;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        textView_title.setText("我的红包");
        datas = new ArrayList<>();
        datas.clear();
        mAdapter = new UserBonusAdapter(mActivity, datas);
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
                    nowTypeStr = OrderType.ALLOW_USE;
                } else if (group.getChildAt(1).getId() == checkedId) {
                    nowTypeStr = OrderType.IS_USED;
                } else if (group.getChildAt(2).getId() == checkedId) {
                    nowTypeStr = OrderType.EXPIRED;
                } else {
                    nowTypeStr = OrderType.ALLOW_USE;
                }
                getHttpData(nowTypeStr);
            }
        });
    }


    private void getHttpData(String typeStr) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIUser()
                .getUserBonus(UserQuery.getInstance().getUserBonus(typeStr))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<List<USERBONUS>>() {
                    @Override
                    public void onNext(List<USERBONUS> userbonuses) {
                        datas.clear();
                        if (userbonuses.size() > 0) {
                            datas.addAll(userbonuses);
                            mAdapter.notifyDataSetChanged();
                            null_pager.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            null_pager.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        my_pullrefresh.refreshComplete();
                        edit_bonus_pwd.setText("");
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

    private void getBonusByPwd(String bonus_password) {
        ProgressDialogUtil.getInstance().build(mActivity).show();
        RetrofitAPIManager.getAPIUser()
                .getUserBonusByPwd(UserQuery.getInstance().getUserBonusByPwd(bonus_password))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(new ResourceSubscriber<USERBONUS>() {
                    @Override
                    public void onNext(USERBONUS userbonus) {
                        ToastUtil.getInstance().makeShortToast(mActivity, "红包兑换成功");
                        RadioButton radioButton = (RadioButton) group_top.getChildAt(0);
                        radioButton.setChecked(true);
                        VUtils.showHideSoftInput(edit_bonus_pwd, false);
                        nowTypeStr = OrderType.ALLOW_USE;
                        getHttpData(nowTypeStr);
                    }

                    @Override
                    public void onError(Throwable t) {
                        ToastUtil.getInstance().makeShortToast(mActivity, t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.imageView_back, R.id.tv_verification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_back:
                finish();
                break;
            case R.id.tv_verification:
                String bonus_password = edit_bonus_pwd.getText().toString();
                if (!TextUtils.isEmpty(bonus_password)) {
                    getBonusByPwd(bonus_password);
                } else {
                    ToastUtil.getInstance().makeShortToast(mActivity, "请填写红包密码");
                }
                break;
        }
    }

    @Override
    public void dispose() {

    }
}
