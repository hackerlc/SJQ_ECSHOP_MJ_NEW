package com.ecjia.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.ecjia.base.ECJiaApplication;
import com.ecjia.consts.AppConst;
import com.ecjia.consts.ClassName;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.ECJIAVERSION;
import com.ecjia.entity.responsemodel.FUNCTION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.NetworkStateService;
import com.ecjia.network.netmodle.ADPicModel;
import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.network.netmodle.ShoppingCartModel;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.network.netmodle.VersionUpdateUtil;
import com.ecjia.util.CheckInternet;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FunctionUtil;
import com.ecjia.util.LG;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecjia.util.city.CityUril;
import com.ecjia.util.update.UpdateManager;
import com.ecjia.view.activity.SettingActivity;
import com.ecjia.view.activity.user.CustomercenterActivity;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.fragment.TabsFragment;
import com.ecjia.widgets.ScrollView_Main;
import com.ecjia.widgets.ToastView;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.dragLayout.DragLayout;
import com.ecjia.widgets.dragLayout.DragLayout.DragListener;
import com.ecmoban.android.sijiqing.R;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.RxBus;
import gear.yc.com.gearlibrary.rxjava.rxbus.annotation.Subscribe;


public class ECJiaMainActivity extends AppCompatActivity implements OnClickListener, TabsFragment.FragmentListener, HttpResponse {

    // 左滑菜单新加入
    private boolean isRefresh = false;
    private boolean islogin = false;
    private UserInfoModel userInfoModel;
    private SharedPreferences shared, sharedPreference;
    public DragLayout dl;
    private TextView set;
    private TextView user_name;
    private TextView user_level;
    //    private TextView collect_num;
    private TextView no_login;
    private ImageView user_img, img_set;
    private LinearLayout profile_head, information, myhome, xian;
    //    public LinearLayout collect_item;
    public Handler closemenuhandler;
    private Resources resources;
    private MyDialog myDialog;
    private SharedPreferences.Editor editor;
    private boolean firstrun = true;

    private ECJiaApplication mApp;

    // 定位相关
    public MyLocationListenner myListener = new MyLocationListenner();
    public LocationClient mLocClient;
    public LocationClientOption option;
    public boolean isFirstLoc = true;// 是否首次定位
    private Location l = null;


    public Bitmap profilePhotoBitmap;//用户头像
    private boolean isProfilePhotoChanged = false;
    private ADPicModel adpicModel;
    private FrameLayout profile_head_text;
    private LinearLayout setLL;
    private View backgroudImage;
    private View topEmptyView;
    private ScrollView_Main sv;
    private LinearLayout sv_ll;
    private ConfigModel configModel;
    private TabsFragment tabsFragment;
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        try {
//            return super.dispatchTouchEvent(ev);
//        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
//        }
//
//        return false;
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (ECJiaApplication) getApplication();
        RxBus.getInstance().register(this);
        EventBus.getDefault().register(this);
        resources = getBaseContext().getResources();
        SDKInitializer.initialize(getApplicationContext());
        Chooselanguage();
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        findViewById(R.id.fragment_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        tabsFragment = (TabsFragment) getSupportFragmentManager().findFragmentById(R.id.tabs_fragment);
//        初始化定位信息
        initLocal();
        shared = this.getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        String no_network = resources.getString(R.string.main_no_network);

        if (!CheckInternet.isConnectingToInternet(this)) {// 检查网络连接
            ToastView toast = new ToastView(ECJiaMainActivity.this, no_network);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        // 启动server服务连接 ，检测网络的类型
        startService(new Intent(this, NetworkStateService.class));

        //所有新建activity必须有。。。
        PushAgent.getInstance(this).onAppStart();
        // 版本更新提醒功能
        new UpdateManager(this).checkUpdate();
//        initUpdate();


        adpicModel = new ADPicModel(this);
        adpicModel.getADPIC();

        initDragLayout();

        if (null == userInfoModel) {
            userInfoModel = new UserInfoModel(this);
        }

        /**
         * 头像下载监听
         */
        ProfilePhotoUtil.getInstance().addDownLoadProfilePhotoListener(new ProfilePhotoUtil.OnDownLoadProfilePhotoListener() {
            @Override
            public void downLoadSuccess() {
                profilePhotoBitmap = ProfilePhotoUtil.getInstance().getProfileBitmap(shared.getString(SharedPKeywords.SPUSER_KUID, ""));
                try {
                    user_img.setImageBitmap(profilePhotoBitmap);
                    EventBus.getDefault().post(new MyEvent(EventKeywords.USER_PHOTO_DOWNLOAD_SUCCESS));
                } catch (Exception e) {
                    e.printStackTrace();
                    LG.i("错误");
                }
                setUserProfilePhoto(1);
            }

            @Override
            public void downLoadFailue() {
                LG.i("下载失败");
            }
        });
        //侧滑菜单进入子条目之后重新返回时关闭菜单
        closemenuhandler = new Handler() {
            public void handleMessage(Message msg) {

                if (islogin) {
                    dl.close();
                }
            }

        };

        initConfig();

    }

    private void initConfig() {
        if (ConfigModel.getInstance() == null) {
            configModel = new ConfigModel(this);
        }
        ConfigModel.getInstance().addResponseListener(this);
        ConfigModel.getInstance().getConfig(new Handler());
        ConfigModel.getInstance().getPayment();
    }

    /**
     * 自动更新
     */
    private void initUpdate() {

        VersionUpdateUtil.UpdateVersionListener listener = new VersionUpdateUtil.UpdateVersionListener() {
            @Override
            public void checkFail() {

            }

            @Override
            public void checkSuccess(int code, ECJIAVERSION ecjiaversion) {

                switch (code) {
                    case -1:
                        break;
                    case 0:
                        break;
                    case 1:
                        VersionUpdateUtil.showUpdateDialog(ECJiaMainActivity.this, ecjiaversion);
                        break;
                }
            }
        };
        VersionUpdateUtil.getInstance().addUdpateVersionListener(listener);
        VersionUpdateUtil.getInstance().update(this);
    }

    /**
     * 添加功能列表
     */
    void addFirstData() {
        FunctionUtil.getDefault().register();
        supprotFunctions.addAll(FunctionUtil.getDefault().getAllFunctions());
    }


    /**
     * 获取状态栏的高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 初始化侧滑大布局
     */
    private void initDragLayout() {
        dl = (DragLayout) findViewById(R.id.dl);
        dl.setDragListener(new DragListener() {
            @Override
            public void onOpen() {
                if (TextUtils.isEmpty(shared.getString("uid", ""))) {
                    setUser(false);
                }
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onDrag(float percent) {
                animate(percent);
            }
        });

        addFirstData();

        LinearLayout firstLL = (LinearLayout) findViewById(R.id.left_first_group_item);
        for (int i = 0; i < 5; i++) {
            final View view = LayoutInflater.from(this).inflate(R.layout.item_main_left, null);
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_40));
            view.setLayoutParams(params);
            ((ImageView) view.findViewById(R.id.main_left_item_image)).setImageResource(supprotFunctions.get(i).getIcon_white());
            ((TextView) view.findViewById(R.id.main_left_item_text)).setText(supprotFunctions.get(i).getName());

            final int position = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    functionStart(position);
                }
            });

            firstLL.addView(view);
        }

        LinearLayout secondLL = (LinearLayout) findViewById(R.id.left_second_group_item);
        for (int i = 5; i < supprotFunctions.size(); i++) {
            final View view = LayoutInflater.from(this).inflate(R.layout.item_main_left, null);
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.dp_40));
            view.setLayoutParams(params);
            ((ImageView) view.findViewById(R.id.main_left_item_image)).setImageResource(supprotFunctions.get(i).getIcon_white());
            ((TextView) view.findViewById(R.id.main_left_item_text)).setText(supprotFunctions.get(i).getName());

            final int position = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    functionStart(position);
                }
            });
            secondLL.addView(view);
        }
        profile_head = (LinearLayout) dl.findViewById(R.id.profile_head);

        profile_head_text = (FrameLayout) dl.findViewById(R.id.profile_head_text);
        setLL = (LinearLayout) dl.findViewById(R.id.profile_setting_ll);
        set = (TextView) dl.findViewById(R.id.profile_newset);
        img_set = (ImageView) dl.findViewById(R.id.profile_setting);

        user_img = (ImageView) dl.findViewById(R.id.profile_newuser_img);
        user_name = (TextView) dl.findViewById(R.id.user_name);
        user_level = (TextView) dl.findViewById(R.id.user_level);
        no_login = (TextView) dl.findViewById(R.id.no_login);
        no_login.setVisibility(View.VISIBLE);

        profile_head.setOnClickListener(this);
        set.setOnClickListener(this);
        img_set.setOnClickListener(this);
        setLL.setOnClickListener(this);


        findViewById(R.id.myfind_home_item).setOnClickListener(this);//主页

        backgroudImage = findViewById(R.id.main_below_background);

        topEmptyView = findViewById(R.id.myfind_top_empty);
        topEmptyView.getLayoutParams().height = (int) (getWindowManager().getDefaultDisplay().getWidth() * 9.5f / 32);
        final int height;
        final int marginTop;
        height = getWindowManager().getDefaultDisplay().getHeight() - getStatusBarHeight();
        findViewById(R.id.main_below_top).setVisibility(View.GONE);
        marginTop = getWindowManager().getDefaultDisplay().getWidth() * 8 / 32;


        final int marginBottom = 0;

        LG.i("height:" + height);
        final float distance = getWindowManager().getDefaultDisplay().getWidth() * 8 / 32;
        final float imageHeight = getWindowManager().getDefaultDisplay().getWidth() * 7 / 32;

        ViewHelper.setPivotX(profile_head, 0);
        ViewHelper.setPivotY(profile_head, 0);
        ViewHelper.setPivotX(profile_head_text, 0);
        ViewHelper.setPivotY(profile_head_text, 0);

        //可上下滑动
        sv = (ScrollView_Main) findViewById(R.id.main_sv);
        sv.setOnScrollListener(new ScrollView_Main.OnScrollListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                LG.i("l:" + l + "  t:" + t + "  oldl:" + oldl + "  odlt:" + oldt + "  scaleY:" + user_img.getScaleY() + "  sv.getScrollY()+" + sv.getScrollY());
                if (sv.getScrollY() <= distance) {

                    ViewHelper.setPivotX(user_img, 0);
                    ViewHelper.setPivotY(user_img, imageHeight / 2);
                    ViewHelper.setScaleX(user_img, 1.0f - (sv.getScrollY() / distance) * 0.4f);
                    ViewHelper.setScaleY(user_img, 1.0f - (sv.getScrollY() / distance) * 0.4f);
                    ViewHelper.setScaleX(profile_head_text, 1.0f - (sv.getScrollY() / distance) * 0.2f);
                    ViewHelper.setScaleY(profile_head_text, 1.0f - (sv.getScrollY() / distance) * 0.2f);
                    ViewHelper.setPivotX(backgroudImage, 0);
                    ViewHelper.setPivotY(backgroudImage, height * 0.308f - sv.getScrollY() * 0.8f);
                    ViewHelper.setScaleX(backgroudImage, 1.0f - (sv.getScrollY() / distance) * 0.20f);
                    ViewHelper.setScaleY(backgroudImage, 1.0f - (sv.getScrollY() / distance) * 0.20f);
                    ViewHelper.setTranslationX(profile_head_text, -imageHeight * (sv.getScrollY() / distance) * 0.4f);
                    ViewHelper.setTranslationY(profile_head_text, -sv.getScrollY() * 0.55f);
                    ViewHelper.setTranslationY(user_img, -sv.getScrollY() * 0.63f);
                    ViewHelper.setTranslationY(backgroudImage, -sv.getScrollY() * 0.8f);
                } else if (sv.getScrollY() > distance) {
                    ViewHelper.setPivotX(user_img, 0);
                    ViewHelper.setPivotY(user_img, imageHeight / 2);
                    ViewHelper.setScaleX(user_img, 0.6f);
                    ViewHelper.setScaleY(user_img, 0.6f);
                    ViewHelper.setTranslationY(user_img, -distance * 0.63f);
                    ViewHelper.setScaleX(profile_head_text, 0.8f);
                    ViewHelper.setScaleY(profile_head_text, 0.8f);
                    ViewHelper.setPivotX(backgroudImage, 0);
                    ViewHelper.setPivotY(backgroudImage, height * 0.308f - distance * 0.8f);
                    ViewHelper.setScaleX(backgroudImage, 0.80f);
                    ViewHelper.setScaleY(backgroudImage, 0.80f);
                    ViewHelper.setTranslationX(profile_head_text, -imageHeight * 0.4f);
                    ViewHelper.setTranslationY(profile_head_text, -distance * 0.55f);
                    ViewHelper.setTranslationY(backgroudImage, -distance * 0.8f);

                }
            }
        });

        sv_ll = (LinearLayout) findViewById(R.id.scorllview_ll);


        ((FrameLayout.LayoutParams) sv.getLayoutParams()).topMargin = marginTop;
        ((FrameLayout.LayoutParams) sv.getLayoutParams()).topMargin = marginTop;
        ((FrameLayout.LayoutParams) sv.getLayoutParams()).bottomMargin = marginBottom;
        sv_ll.setMinimumHeight((int) (height - marginTop - marginBottom + distance));

    }

    private void functionStart(final int position) {
        if (supprotFunctions.get(position).isNeedLogin() && (mApp
                .getUser() == null || (TextUtils.isEmpty(mApp
                .getUser().getId())))) {
            startActivityForResult(new Intent(ECJiaMainActivity.this, LoginActivity.class), supprotFunctions.get(position).getLoginRequestcode());
            overridePendingTransition(R.anim.push_buttom_in, R.anim
                    .push_buttom_out);
        } else {
            try {
                Intent intent = new Intent();
                intent.setClass(ECJiaMainActivity.this, Class.forName(supprotFunctions.get(position).getAction()
                        .getActivityName()));
                if (supprotFunctions.get(position).getAction().equals(ClassName.ActivityName.QRSHARE)) {
                    intent.putExtra("startType", 1);
                }
                if (supprotFunctions.get(position).getCode().equals("promotion")) {
                    intent.putExtra("type", "promotion");
                }
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void animate(float percent) {
        ViewGroup vg_left = dl.getVg_left();
        ViewGroup vg_main = dl.getVg_main();
        float wid = vg_left.getWidth();
        ViewHelper.setTranslationX(vg_left, -wid / 2.7f + wid / 2.7f * percent);
        ViewHelper.setTranslationX(vg_main, -wid / 4.5f * percent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean isExit = false;

    // 退出操作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (dl.isOpen()) {
                dl.close();
            } else {
                Resources resource = (Resources) getBaseContext().getResources();
                String main_exit = resource.getString(R.string.main_exit);
                String main_exit_content = resource.getString(R.string.main_exit_content);
                myDialog = new MyDialog(this, main_exit, main_exit_content);
                myDialog.show();
                myDialog.negative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.positive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                        stopService(new Intent(ECJiaMainActivity.this, NetworkStateService.class));
                        finish();
                    }
                });

                return true;
            }
        }
        return true;
    }


    ArrayList<FUNCTION> supprotFunctions = new ArrayList<>();


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LG.i("uid::" + shared.getString("uid", ""));
        if (!TextUtils.isEmpty(shared.getString("uid", ""))) {
            islogin = true;
            if (firstrun) {
                userInfoModel.getUserInfo();
                firstrun = false;
            } else if (isRefresh) {
                userInfoModel.getUserInfo();
            } else {
                setUser(true);
            }
        } else {
            islogin = false;
            setUser(false);
        }
        isRefresh = false;
        MobclickAgent.onResume(this);
        if (userInfoModel != null) {
            userInfoModel.addResponseListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mLocClient) {
            mLocClient.unRegisterLocationListener(myListener);
            if (mLocClient != null) {
                mLocClient.stop();
                mLocClient = null;
            }
            myListener = null;
        }

        closemenuhandler.sendMessageDelayed(new Message(), 200);
        MobclickAgent.onPause(this);
        if (userInfoModel != null) {
            userInfoModel.removeResponseListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {


            case R.id.myfind_home_item://主页
                if (dl.isOpen()) {
                    tabsFragment.one();
                    dl.close();
                }
                break;


            case R.id.profile_setting_ll:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.profile_newset:// 设置
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.profile_setting:// 设置
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.profile_head://头像
                if (TextUtils.isEmpty(shared.getString("uid", ""))) {
                    isRefresh = true;
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    this.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                } else {
                    intent = new Intent(this, CustomercenterActivity.class);
                    if (mApp.getUser() != null && mApp.getUser().getName() != null && mApp
                            .getUser().getRank_name() != null) {
                        intent.putExtra("username", mApp.getUser().getName());
                        intent.putExtra("level", mApp.getUser().getRank_name());
                        intent.putExtra("profile_photo", mApp.getUser().getAvatar_img());
                        startActivity(intent);
                        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    }
                }
                break;

        }
    }


    private void setUser(boolean flag) {
        resources = getBaseContext().getResources();
        if (flag) {
            if (mApp.getUser() != null) {
                if (user_name != null) user_name.setText(mApp.getUser().getName());
                if (user_level != null) user_level.setText(mApp.getUser().getRank_name());
                no_login.setVisibility(View.INVISIBLE);
            }
        } else {
            //2.6.0
            if (user_name != null) user_name.setText("");
            if (user_level != null) user_level.setText("");
            no_login.setVisibility(View.VISIBLE);
            profilePhotoBitmap = null;
            ProfilePhotoUtil.getInstance().clearBitmap();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //选择加载的语言
    private void Chooselanguage() {
        Configuration config = resources.getConfiguration();
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        if ("zh".equalsIgnoreCase(sharedPreference.getString("language", null))) {
            config.locale = Locale.CHINA;
        } else if ("en".equalsIgnoreCase(sharedPreference.getString("language", null))) {
            config.locale = Locale.ENGLISH;
        } else {
            sharedPreference.edit().putString("language", "auto").commit();
            config.locale = Locale.getDefault();
        }
        getBaseContext().getResources().updateConfiguration(config, null);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        RxBus.getInstance().unRegister(this);
        super.onDestroy();
    }


    private void initLocal() {
        mLocClient = new LocationClient(this);
        option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setAddrType("all");
        option.setLocationNotify(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setOpenGps(true);// 打开gps
//        option.setScanSpan(0);
        mLocClient.setLocOption(option);
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();
    }

    @Override
    public void addIgnoredView(View v) {
        dl.addIgnoredView(v);
    }

    @Override
    public void removeIgnoredView(View v) {
        dl.removeIgnoredView(v);
    }

    @Override
    public void Open() {
        dl.open();
    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url.equals(ProtocolConst.USERINFO)) {
            if (status.getSucceed() == 1) {
                setUser(true);
                if (mApp.getUser().getAvatar_img() != null) {
                    ProfilePhotoUtil.getInstance().downLoadProfilePhoto(mApp.getUser().getAvatar_img(), ProfilePhotoUtil.getInstance().getProfilePhotoPath(mApp.getUser().getId()));
                }
            } else {
                setUser(false);
            }
        } else if (url.equals(ProtocolConst.CARTLIST)) {
            LG.i("获取到了购物车数据" + mApp.getGoodsNum());
            tabsFragment.setShoppingcartNum();
        }
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
//                getGPS();
            } else {
                if (location.getProvince() != null) {
                    if(ECJiaApplication.cityName.equals(location.getProvince())){
                        return;
                    }
                    ECJiaApplication.cityName = location.getProvince();
                    RxBus.getInstance().post(RxBus.TAG_CHANGE, "city");
                    if(ECJiaApplication.cityList.size() > 0){
                        //根据当前省份获取大区值，并设置数据
                        String alias = CityUril.getAlias(ECJiaApplication.cityList, ECJiaApplication.cityName);
                        DEVICE.getInstance().setCity(alias);
                        RxBus.getInstance().post(RxBus.TAG_UPDATE, -1);
                    } else {
                        //TODO:是否应该再获取一次大区数据
                    }
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

    public void onEvent(MyEvent event) {
        if ("changelanguage".equals(event.getMsg())) {
            this.finish();
        }
        if ("userinfo_refresh".equals(event.getMsg())) {
            isRefresh = true;
            userInfoModel.getUserInfo();
        }
        if ("refreshlocal".equals(event.getMsg())) {
            isFirstLoc = true;
        }

        /**
         * 条到分类
         */
        if (event.getMsg().equals(EventKeywords.OPENTYPE_SELLER)) {
            LG.i("首页收到消息==" + event.getValue());
            if (TextUtils.isEmpty(event.getValue())) {
                tabsFragment.one();
            } else {
                tabsFragment.one(event.getValue());
            }
        }

        if (event.getMsg().equals(EventKeywords.WINREWARD_ECJIAMAIN)) {
            tabsFragment.one();
        }

        if (event.getMsg().equals(EventKeywords.ECJIAMAIN_MAIN)) {
            tabsFragment.one();
        }

        if (event.getMsg().equals(EventKeywords.USER_CHANGE_PHOTO)) {
            isProfilePhotoChanged = true;
            setUser(true);
            setUserProfilePhoto(1);
        }

        if (event.getMsg().equals(EventKeywords.USER_SINOUT)) {
            ProfilePhotoUtil.getInstance().clearBitmap();
            profilePhotoBitmap = null;
            setUser(false);
            setUserProfilePhoto(2);
        }
        if (event.getMsg().equals(EventKeywords.ECJIAMAIN_FIND)) {
            tabsFragment.OnTabSelected("tab_five");
        }
        if (event.getMsg().equals(EventKeywords.USER_LOGIN_SUCCESS)) {
            setUser(true);
            setUserProfilePhoto(1);
            if (cartModel == null) {
                cartModel = new ShoppingCartModel(this);
                cartModel.addResponseListener(this);
            }
            cartModel.cartList(false);
        }
    }

    ShoppingCartModel cartModel;

    void setUserProfilePhoto(int type) {
        switch (type) {
            case 0://登录无头像
                user_img.setImageResource(R.drawable.profile_no_avarta_icon_light);
                break;
            case 1://登录有头像
                user_img.setImageBitmap(ProfilePhotoUtil.getInstance().getProfileBitmap(shared.getString("uid", "")));
                break;
            default://未登录
                user_img.setImageResource(R.drawable.profile_no_avarta_icon);
                break;
        }
    }

    @Subscribe(tag = RxBus.TAG_OTHER)
    public void exit(String str){
        stopService(new Intent(ECJiaMainActivity.this, NetworkStateService.class));
        finish();
    }
}
