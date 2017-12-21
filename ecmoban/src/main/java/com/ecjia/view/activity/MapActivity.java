package com.ecjia.view.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ecjia.base.BaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.util.CheckInternet;
import com.ecjia.util.LG;
import com.ecjia.util.LocationUtil;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;


/**
 * Created by Adam on 2015/1/27.
 */
public class MapActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    // 定位相关
    private MyLocationConfiguration.LocationMode mCurrentMode;

    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    boolean isFirstLoc = true;// 是否首次定位
    BitmapDescriptor mCurrentMarker;

    GeoCoder mSearch = null;
    public Handler Intenthandler;
    private String address;
    private boolean isGuide;
    private String distance,lat,lng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        isGuide=getIntent().getBooleanExtra(IntentKeywords.MAP_ISGUIDE, false);
        address=getIntent().getStringExtra(IntentKeywords.MAP_NAME);
        distance=getIntent().getStringExtra(IntentKeywords.MAP_DISTANCE);
        lat=getIntent().getStringExtra(IntentKeywords.MAP_LAT);
        lng=getIntent().getStringExtra(IntentKeywords.MAP_LNG);

        initTopView();

        String no_network = res.getString(R.string.main_no_network);
        if (!CheckInternet.isConnectingToInternet(this)) {// 检查网络连接
            ToastView toast = new ToastView(MapActivity.this, no_network);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        if(isGuide){
                // 初始化搜索模块，注册事件监听
                LG.i("address1====" + address);
                mSearch.geocode(new GeoCodeOption().city(" ").address(address+""));

        }else{
            if (ConfigModel.getInstance().config != null) {
                // 初始化搜索模块，注册事件监听
                address = ConfigModel.getInstance().config.getShop_address();
                LG.i("address====" + address);
                mSearch.geocode(new GeoCodeOption().city(" ").address(address+""));
            }

        }


        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        //设置首次缩放大小
        float mZoomLevel = 17.0f;
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mZoomLevel));

    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.map_topview);
        topView.setTitleText(R.string.quick_map);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(isGuide){
            topView.setRightType(ECJiaTopView.RIGHT_TYPE_IMAGE);
            topView.setRightImage(R.drawable.icon_shop_guide, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng) || "null".equals(lng) || "null".equals(lat)) {
                        ToastView toast = new ToastView(MapActivity.this, "未找到目的地");
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        LocationUtil.StartBaiduGuide(MapActivity.this,address, distance, lat, lng);
                    }
                }
            });
        }
    }



    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Resources resources = getResources();
            String noresult = resources.getString(R.string.map_nofind);
            Toast.makeText(MapActivity.this, noresult, Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        Resources resources = getResources();
        String noresult = resources.getString(R.string.map_nofind);
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, noresult, Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
        Toast.makeText(MapActivity.this, result.getAddress(), Toast.LENGTH_LONG).show();

    }

}

