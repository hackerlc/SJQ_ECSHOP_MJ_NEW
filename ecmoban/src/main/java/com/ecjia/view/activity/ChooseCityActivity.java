package com.ecjia.view.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ecjia.base.BaseActivity;
import com.ecjia.view.ECJiaMainActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.MyGridView;
import com.ecjia.widgets.MyLetterListView;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AppConst;
import com.ecjia.view.adapter.ChooseCityAdapter;
import com.ecjia.view.adapter.HotCityAdapter;
import com.ecjia.entity.responsemodel.CITY;
import com.ecjia.util.LG;

import org.json.JSONException;

import java.util.Comparator;
import java.util.List;

public class ChooseCityActivity extends BaseActivity {
    private ListView personList;
    private MyLetterListView letterListView; // A-Z listview
    private Handler handler;
    private TextView overlay; // ĸtextview
    private TextView tv_refresh;

    private OverlayThread overlayThread;
    ChooseCityAdapter adapter;
    WindowManager windowManager;
    private View localView;
    MyGridView hotgridView;
    private LinearLayout hot_city_item;
    HotCityAdapter hotcityAdapter;
    private AddressModel model;
    Handler internethandler;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private String chooseagain = "";
    private EditText et_search_input;
    private ProgressBar progressBar;
    private TextView localtext;
    public MyLocationListenner myListener = new MyLocationListenner();
    public LocationClient mLocClient;
    public LocationClientOption option;
    public boolean isFirstLoc = true;// 是否首次定位
    private Location l = null;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        chooseagain = getIntent().getStringExtra("chooseagain");
        internethandler = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.obj == ProtocolConst.REGION) {
                    if (msg.what == 1) {
                        tv_refresh.setVisibility(View.GONE);
                        setInfo();
                    } else if (msg.what == 0) {
                        String no_network = res.getString(R.string.main_no_network);
                        ToastView toast = new ToastView(ChooseCityActivity.this, no_network);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        tv_refresh.setVisibility(View.VISIBLE);
                    }
                }

            }
        };

        tv_refresh = (TextView) findViewById(R.id.tv_refresh);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.regionall(2 + "", internethandler);
            }
        });
        model = new AddressModel(this);
        model.regionall(2 + "", internethandler);
    }

    private void setInfo() {
        initLocal();
        back = (ImageView) findViewById(R.id.choosecity_back);
        if ("chooseagain".equals(chooseagain)) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != windowManager) {
                    windowManager.removeView(overlay);
                }
                finish();
            }
        });
        //搜索按钮监听
        et_search_input = (EditText) findViewById(R.id.et_city_input);
        et_search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(et_search_input.getText().toString())) {
                        int size = model.allCityList.size();
                        for (int i = 0; i < size; i++) {
                            if (et_search_input.getText().toString().equals(model.allCityList.get(i).getName())) {
                                try {
                                    editor.putString("localString", model.allCityList.get(i).toJson().toString());
                                    editor.putString("sendArea", model.allCityList.get(i).getName());
                                    editor.putString("area_id", model.allCityList.get(i).getId());
                                    editor.commit();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if ("chooseagain".equals(chooseagain)) {
                                    Intent intent = new Intent();
                                    setResult(100, intent);
                                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                } else {
                                    ChooseCityActivity.this.startActivity(new Intent(ChooseCityActivity.this, ECJiaMainActivity.class));
                                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                }
                                if (null != windowManager) {
                                    windowManager.removeView(overlay);
                                }
                                CloseKeyBoard();
                                finish();
                                break;
                            }
                        }
                    } else {
                        ToastView toastView = new ToastView(ChooseCityActivity.this, getResources().getString(R.string.search_input));
                        toastView.show();
                    }

                }
                return false;
            }
        });
        //头部布局
        localView = getLayoutInflater().inflate(R.layout.choosecity_localitem, null);
        progressBar = (ProgressBar) localView.findViewById(R.id.city_progressBar);
        localtext = (TextView) localView.findViewById(R.id.local_city_text);
        localtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(localtext.getText().toString())) {
                    for (CITY c : model.allCityList) {
                        if (localtext.getText().toString().contains(c.getName())) {
                            try {
                                editor.putString("localString", c.toJson().toString());
                                editor.putString("sendArea", c.getName());
                                editor.putString("area_id", c.getId());
                                editor.commit();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if ("chooseagain".equals(chooseagain)) {
                                Intent intent = new Intent();
                                setResult(100, intent);
                                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                            } else {
                                ChooseCityActivity.this.startActivity(new Intent(ChooseCityActivity.this, ECJiaMainActivity.class));
                                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                            }
                            if (null != windowManager) {
                                windowManager.removeView(overlay);
                            }
                            finish();
                            break;
                        }
                    }

                } else {

                }
            }
        });

        //热门城市列表
        hot_city_item = (LinearLayout) localView.findViewById(R.id.hot_city_item);
        if (model.hotCityList.size() > 0) {
            hot_city_item.setVisibility(View.VISIBLE);
            //添加热门城市
            hotgridView = (MyGridView) localView.findViewById(R.id.hot_topview);
            hotcityAdapter = new HotCityAdapter(this, model.hotCityList);
            hotgridView.setAdapter(hotcityAdapter);
            hotgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        editor.putString("localString", model.hotCityList.get(i).toJson().toString());
                        editor.putString("sendArea", model.hotCityList.get(i).getName());
                        editor.putString("area_id", model.hotCityList.get(i).getId());
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if ("chooseagain".equals(chooseagain)) {
                        Intent intent = new Intent();
                        setResult(100, intent);
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    } else {
                        ChooseCityActivity.this.startActivity(new Intent(ChooseCityActivity.this, ECJiaMainActivity.class));
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                    if (null != windowManager) {
                        windowManager.removeView(overlay);
                    }
                    finish();
                }
            });
        } else {
            hot_city_item.setVisibility(View.GONE);
        }


        //城市列表
        personList = (ListView) findViewById(R.id.list_view);
        personList.addHeaderView(localView);
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView);
        letterListView
                .setOnTouchingLetterChangedListener(new LetterListViewListener());

        personList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if(arg2!=0) {
                    try {
                        editor.putString("sendArea", model.allCityList.get(arg2 - 1).getName());
                        editor.putString("localString", model.allCityList.get(arg2 - 1).toJson().toString());
                        editor.putString("area_id", model.allCityList.get(arg2 - 1).getId());
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if ("chooseagain".equals(chooseagain)) {
                        Intent intent = new Intent();
                        setResult(100, intent);
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    } else {
                        ChooseCityActivity.this.startActivity(new Intent(ChooseCityActivity.this, ECJiaMainActivity.class));
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                    if (null != windowManager) {
                        windowManager.removeView(overlay);
                    }
                    finish();
                }
            }
        });
        setLocateIn(new GetCityName());
        handler = new Handler();
        overlayThread = new OverlayThread();
        initOverlay();
        //初始化覆盖视图
        setAdapter(model.allCityList);
    }

    private void setAdapter(List<CITY> list) {
        if (adapter == null) {
            adapter = new ChooseCityAdapter(this, list);
        }
        personList.setAdapter(adapter);
    }


    Comparator comparator = new Comparator<CITY>() {
        @Override
        public int compare(CITY lhs, CITY rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }

        }
    };

    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.choosecity_overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
    }

    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }

    }

    private class LetterListViewListener implements
            MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            if (adapter.alphaIndexer.get(s) != null) {
                int position = adapter.alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(adapter.sections[position]);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                handler.postDelayed(overlayThread, 1500);
            }
        }

    }

    private class GetCityName implements LocateIn {
        @Override
        public void getCityName(String name) {
            System.out.println(name);

            adapter.notifyDataSetChanged();
        }
    }

    static LocateIn tin;

    public static void setLocateIn(LocateIn in) {
        tin = in;
    }

    public interface LocateIn {
        public void getCityName(String name);
    }


    @Override
    public void finish() {
        super.finish();
        CloseKeyBoard();
        if ("chooseagain".equals(chooseagain)) {
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        } else {
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (TextUtils.isEmpty(chooseagain) || !"chooseagain".equals(chooseagain)) {
                return false;
            }
            if (null != windowManager) {
                windowManager.removeView(overlay);
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    // 关闭键盘
    public void CloseKeyBoard() {
        et_search_input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_search_input.getWindowToken(), 0);
    }

    private void initLocal() {
        mLocClient = new LocationClient(this);
        option = new LocationClientOption();
        option.setAddrType("all");
        option.setLocationNotify(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setOpenGps(true);// 打开gps
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();


    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                getGPS();
            } else {
                if (isFirstLoc) {
                    //设置默认配送区域
                    isFirstLoc = false;
                    localtext.setVisibility(View.VISIBLE);
                    if (location.getCity() != null) {
                        localtext.setText(location.getCity().replace("市", ""));

                        if ("".equals(shared.getString("sendArea", ""))) {
                            for (CITY c : model.allCityList) {
                                if (location.getCity().contains(c.getName())) {
                                    editor.putString("sendArea", location.getAddrStr());
                                    editor.putString("area_id", c.getId());
                                    try {
                                        editor.putString("localString", c.toJson().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    editor.commit();
                                    break;
                                }
                            }
                        }
                    } else {
                        localtext.setVisibility(View.GONE);
                    }
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public boolean getGPS() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

		/* 循环读取providers,如果有地址信息, 退出循环 */
        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
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
    protected void onDestroy() {
        if (mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
            mLocClient = null;
        }
        myListener = null;
        super.onDestroy();
    }
}
