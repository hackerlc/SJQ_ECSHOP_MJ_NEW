package com.ecjia.view.activity.chooseCity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ecjia.base.ECJiaApplication;
import com.ecjia.base.NewBaseActivity;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.model.City;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.query.PageQuery;
import com.ecjia.util.LG;
import com.ecjia.util.city.CityUril;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.ecmoban.android.sijiqing.databinding.ActivityCityBinding;

import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.utils.ProgressDialogUtil;
import gear.yc.com.gearlibrary.utils.ToastUtil;

/**
 * SJQ_FXB_XiangMuDi_Application
 * 切换城市
 * Created by YichenZ on 2016/9/9 11:50.
 */

public class CityActivity extends NewBaseActivity {
    ActivityCityBinding mBinding;
    CityAdapter mAdapter;
    ArrayList<City> citys;
    PageQuery mQuery;
    protected int page = 1;
    private SharedPreferences shared;

    LinearLayoutManager mLayoutManager;
    protected MyLocationListener myListener = new MyLocationListener();
    protected LocationClient mLocClient;
    protected LocationClientOption option;
    private Location l = null;
    private City mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_city);
        initData();
        initUI();
        initLocal();
        loadData();
    }


    @Override
    protected void onDestroy() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
            mLocClient = null;
        }
        myListener = null;
        super.onDestroy();
    }

    private void initData() {
        shared = mActivity.getSharedPreferences(SharedPKeywords.SPUSER, 0);
//        ECJiaApplication.cityName = shared.getString(SharedPKeywords.LOCAL_NAME, "");
        mQuery = new PageQuery();
        citys = new ArrayList<>();
        mAdapter = new CityAdapter(this, citys);
    }

    private void initUI() {
        mBinding.head.tvTitle.setText("选择服装产业带");
        //显示当前城市
        mBinding.setAddress("默认");
        mBinding.setOnClick(this);
        mBinding.infoCity.setOnClickListener(v -> {
            if(ECJiaApplication.cityList.size() > 0){
                //根据当前省份获取大区值，并设置数据
                mCity = CityUril.getCity(ECJiaApplication.cityList, mBinding.getAddress());
                setCity();
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        //        mBinding.rvData.addItemDecoration(new GearRecyclerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mBinding.rvData.setLayoutManager(mLayoutManager);
        mAdapter.setOnItemClickListener((view, holder, position) -> {
            mCity = citys.get(position);
            setCity();
        });
        mBinding.rvData.setAdapter(mAdapter.adapter());
    }

    private void loadData() {
        //获取城市信息
        ProgressDialogUtil.getInstance().build(this).show();
        RetrofitAPIManager.getAPIPublic()
                .getSubstations(mQuery.getQuery(page, 100))
                .compose(liToDestroy())
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .doOnTerminate(() -> ProgressDialogUtil.getInstance().dismiss())
                .subscribe(d -> showData(d), e -> showError(e));
    }

    private void showError(Throwable e) {
        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    private void showData(List<City> datas) {
        if (datas.size() == 0) {
            return;
        }
        //与数据对比，获取当前城市是否已开通
        mBinding.tvInfo.setVisibility(View.INVISIBLE);
        if (page == 1) {
            citys.clear();
        }
        citys.addAll(datas);
        page++;
        mAdapter.notifyDataSetChanged();
    }

    private void initLocal() {
        mLocClient = new LocationClient(this);
        option = new LocationClientOption();
        option.setAddrType("all");
        option.setLocationNotify(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setOpenGps(true);// 打开gps
        option.setScanSpan(0);
        mLocClient.setLocOption(option);
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
//                getGPS();
                mBinding.infoCity.setVisibility(View.GONE);
            } else {
                if (location.getProvince() != null) {
//                    mBinding.tvAddress.setText(location.getProvince());
                    mBinding.setAddress(location.getProvince());
                } else {
                    mBinding.infoCity.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public boolean getGPS() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

		/* 循环读取providers,如果有地址信息, 退出循环 */
        for (int i = providers.size() - 1; i >= 0; i--) {
            //            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //                // TODO: Consider calling
            //                //    ActivityCompat#requestPermissions
            //                // here to request the missing permissions, and then overriding
            //                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                //                                          int[] grantResults)
            //                // to handle the case where the user grants the permission. See the documentation
            //                // for ActivityCompat#requestPermissions for more details.
            //                return false;
            //            }
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null)
                break;
        }

        AppConst.LNG_LAT = new double[2];
        if (l != null) {
            AppConst.LNG_LAT[0] = l.getLongitude();// 经度
            AppConst.LNG_LAT[1] = l.getLatitude(); // 纬度
        } else {
            LG.i("定位失败");
        }
        return AppConst.LNG_LAT[0] != 0 ? true : false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back_button:
                if ("".equals(ECJiaApplication.cityName)) {
                    onKeyDown(KeyEvent.KEYCODE_BACK, null);
                } else {
                    finish();
                }
                break;
        }
    }

    // 退出操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("".equals(ECJiaApplication.cityName)) {
                Resources resource = getBaseContext().getResources();
                String main_exit = resource.getString(R.string.main_exit);
                String main_exit_content = resource.getString(R.string.main_exit_city_content);
                MyDialog myDialog = new MyDialog(this, main_exit, main_exit_content);
                myDialog.show();
                myDialog.negative.setOnClickListener(view -> myDialog.dismiss());
                myDialog.positive.setOnClickListener(view -> {
                    myDialog.dismiss();
                    RxBus.getInstance().post(RxBus.TAG_OTHER, "city");
                    finish();
                });
                return true;
            }
            finish();
            return true;
        }
        return true;
    }

    void setCity() {
        DEVICE.getInstance().setCity(mCity.getAlias());
        ECJiaApplication.initCityName = mCity.getName();
        RxBus.getInstance().post(RxBus.TAG_CHANGE, "city");
        RxBus.getInstance().post(RxBus.TAG_UPDATE, -1);
        finish();
    }
}
