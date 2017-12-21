package com.ecjia.view.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.base.ECJiaDealUtil;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.model.City;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.HttpResponse;
import com.ecjia.network.base.RetrofitAPIManager;
import com.ecjia.network.helper.SchedulersHelper;
import com.ecjia.network.netmodle.ADPicModel;
import com.ecjia.network.netmodle.AddressModel;
import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.network.netmodle.FindModel;
import com.ecjia.network.netmodle.InfoModel;
import com.ecjia.network.query.PageQuery;
import com.ecjia.util.DeviceInfoUtil;
import com.ecjia.util.LG;
import com.ecjia.view.ECJiaMainActivity;
import com.ecjia.view.adapter.ADPagerAdapter;
import com.ecmoban.android.sijiqing.R;
import com.lidroid.xutils.BitmapUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import gear.yc.com.gearlibrary.rxjava.helper.RxSchedulersHelper;
import gear.yc.com.gearlibrary.utils.ToastUtil;

public class StartActivity extends Activity implements ViewPager.OnPageChangeListener, HttpResponse {
    private Context context;
    private SharedPreferences shared;
    private Handler handler;
    private ConfigModel configModel;
    private FindModel findModel;
    private ADPicModel adPicModel;
    private InfoModel infoModel;
    private ImageView startView;
    private ViewPager viewPager;
    private AlphaAnimation aa,animation2;
    private ECJiaDealUtil ecJiaDealUtil;
    private ArrayList<View> views = new ArrayList<View>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        //加载产业信息，如果加载失败那么使用杭州/浙江省作为初始值
        loadCity();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (shared.getBoolean("isFirstRun", true)) {
                    AddressModel model = new AddressModel(StartActivity.this);
                    model.regionall(2 + "", new Handler());
                }
            }
        };
        startView = (ImageView) findViewById(R.id.start_img);
        viewPager = (ViewPager) findViewById(R.id.start_viewpager);
        context = this;
        //渐变

        aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        aa.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initPermission();
            }
        });
        startView.setAnimation(aa);
    }

    PageQuery mQuery = new PageQuery();
    private void loadCity() {
        RetrofitAPIManager.getAPIPublic()
                .getSubstations(mQuery.getQuery(1, 100))
                .compose(RxSchedulersHelper.io_main())
                .compose(SchedulersHelper.handleResult())
                .subscribe(d -> showData(d), e -> showError(e));
    }

    private void showError(Throwable e) {
//        ToastUtil.getInstance().makeShortToast(this, e.getMessage());
    }

    /**
     * 这里要获取大区值，与后面加载数据相关
     * 如果获取失败那么就使用初始值获取
     * 如果获取成功那么就需要后面根据获取的当前地点来判断在哪个大区，从而加载到数据
     * @param datas
     */
    private void showData(List<City> datas) {
        if (datas.size() == 0) {
            return;
        }
        ECJiaApplication.cityList = datas;
    }

    private void initPermission(){
        //请求权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // 在android 6.0之前会默认返回true
                        initData();
                        redirectto();
                    } else {
                        ToastUtil.getInstance().makeShortToast(this,"程序没有被授权无法运行.");
                        finish();
                    }
                });
    }

    private void initData(){
        initDevice();
        ecJiaDealUtil=new ECJiaDealUtil(this);

        shared = getSharedPreferences("userInfo", 0);
        configModel = new ConfigModel(this);
        configModel.getConfig(handler);
        infoModel = new InfoModel(this);
        infoModel.addResponseListener(this);
        infoModel.getShopToken();
        findModel = new FindModel(this);
        findModel.getHomeDiscover();
    }


    private void initDevice() {
        /**
         * 获取device信息
         */
        ((ECJiaApplication)getApplication()).setDevice(DeviceInfoUtil.getDevice(this));
    }

    public boolean isStartAdImage() {
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        adPicModel = new ADPicModel(this);
        adPicModel.readADPICCache();
        if (adPicModel.adPicsForStartPage.size() > 0) {
            for (int i = 0; i < adPicModel.adPicsForStartPage.size(); i++) {
                long startTime = 0, endTime = 0, curentTime = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(new java.util.Date());
                try {
                    startTime = sdf.parse(adPicModel.adPicsForStartPage.get(i).getStart_time()).getTime();
                    endTime = sdf.parse(adPicModel.adPicsForStartPage.get(i).getEnd_time()).getTime();
                    curentTime = sdf.parse(date).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //比较时间控制动态加载
                if (curentTime > endTime || curentTime < startTime) {
                    break;
                }
                View view = LayoutInflater.from(this).inflate(R.layout.start_a, null);
                final int j = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(StartActivity.this, ECJiaMainActivity.class);//进入主页
                        startActivity(intent1);

                        if(adPicModel.adPicsForStartPage.get(j).getMap().isEmpty()){
                            Intent intent = new Intent(context, WebViewActivity.class);
                            intent.putExtra(IntentKeywords.WEB_URL, adPicModel.adPicsForStartPage.get(j).getAd_link());
                            intent.putExtra(IntentKeywords.WEB_TITLE,getResources().getString(R.string.browser) );
                            context.startActivity(intent);
                            finish();
                            ((Activity) context).overridePendingTransition(R.anim.push_right_in,
                                    R.anim.push_right_out);
                        }else{
                            ecJiaDealUtil.dealEcjiaAction(adPicModel.adPicsForStartPage.get(j).getMap());
                        }

                    }
                });
                Button button_toECjia = (Button) view.findViewById(R.id.go_ecjia);
                button_toECjia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartActivity.this, ECJiaMainActivity.class);//进入主页
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        finish();
                    }
                });
                ImageView imageView = (ImageView) view.findViewById(R.id.starta);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Button button_jump = (Button) view.findViewById(R.id.welcome_intent1);
                button_jump.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartActivity.this, ECJiaMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        finish();
                    }
                });
                button_toECjia.setVisibility(View.GONE);//点击进入
                button_jump.setVisibility(View.VISIBLE);//跳过

                Bitmap bitmap = BitmapFactory.decodeFile("/" + adPicModel.adPicsForStartPage.get(i).getAd_img());
                if (bitmap == null) {
                    LG.i("加载本地广告图失败");
                    continue;
                } else {
                    LG.i("加载本地广告图成功");
                    imageView.setImageBitmap(bitmap);
                    views.add(view);
                }
            }

            if (views.size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private void redirectto() {
        if (isStartAdImage()) {
            initViewPager();
            startView.setVisibility(View.GONE);
        } else {
            if (AndroidManager.SUPPORT_GALLERY) {
                Intent intent = new Intent(this, GalleryImageActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, ECJiaMainActivity.class);
                startActivity(intent);
            }
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
    }

    private void initViewPager() {
        animation2 = new AlphaAnimation(1.0f, 1.0f);
        animation2.setDuration(3000);
        animation2.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(StartActivity.this, ECJiaMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (views.size() > 0) {
            viewPager = (ViewPager) findViewById(R.id.start_viewpager);
            viewPager.setAdapter(new ADPagerAdapter(this, views));
            viewPager.setOnPageChangeListener(StartActivity.this);
            viewPager.setAnimation(animation2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private int lastX = 0;

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == viewPager.getAdapter().getCount() - 1) {
            animation2.setAnimationListener(null);
            viewPager.clearAnimation();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if (url == ProtocolConst.SHOP_TOKEN) {
            if (status.getSucceed() == 1) {
                infoModel.getShopInfo();
            }
        }
    }
}
