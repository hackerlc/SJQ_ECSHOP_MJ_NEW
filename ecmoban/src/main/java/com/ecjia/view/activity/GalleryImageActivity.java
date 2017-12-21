package com.ecjia.view.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.AndroidManager;
import com.ecjia.view.ECJiaMainActivity;
import com.ecmoban.android.sijiqing.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryImageActivity extends BaseActivity implements View.OnClickListener {

    private List<View> list;
    private ViewPager imagePager;
    private PagerAdapter galleryImageAdapter;
    private ImageView startimage1;
    private ImageView startimage2;
    private ImageView startimage3;
//    private ImageView startimage4;
    private ImageView startimage5;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private int pager_num;
    private Button go_ecjia, skip1,skip2,skip3;
//    private Button skip4;
    boolean isSettingGo;
    boolean isFirstRun;
    private boolean isChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.gallery_image);

        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        isFirstRun = shared.getBoolean("isFirstRun", true);
        isSettingGo = shared.getBoolean("isSettingGo", true);
        String localString = shared.getString("localString", "");
        if(TextUtils.isEmpty(localString)){
            isChoose=false;
        }else {
            isChoose=true;
        }

        if(isFirstRun&& AndroidManager.SUPPORT_GALLERY) {//启动或者从设置启动
            initView();//执行动画
        } else if (isSettingGo&&AndroidManager.SUPPORT_GALLERY){
            initView();//执行动画
        }else if (isFirstRun&&!AndroidManager.SUPPORT_GALLERY) {
            editor.putBoolean("isFirstRun", false);
            editor.commit();
//                    createShortcut();//创建图标
            Intent intent = new Intent(GalleryImageActivity.this, ECJiaMainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(!isChoose){
            Intent intent = new Intent(GalleryImageActivity.this, ECJiaMainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent it = new Intent(this, ECJiaMainActivity.class);
            startActivity(it);
            finish();
        }
    }

    private void initView() {
        //先将设置页面的调为false。
        editor.putBoolean("isSettingGo", false);
        editor.commit();
        list = new ArrayList<View>();
        View lead_a = View.inflate(this, R.layout.start_a, null);
        View lead_b = View.inflate(this, R.layout.start_b, null);
        View lead_c = View.inflate(this, R.layout.start_c, null);
        View lead_d = View.inflate(this, R.layout.start_d, null);
        View lead_e = View.inflate(this, R.layout.start_e, null);
        startimage1 = (ImageView) lead_a.findViewById(R.id.starta);
        startimage2 = (ImageView) lead_b.findViewById(R.id.startb);
        startimage3=(ImageView) lead_c.findViewById(R.id.startc);
//        startimage4 = (ImageView) lead_d.findViewById(R.id.startd);
        startimage5=(ImageView) lead_e.findViewById(R.id.starte);

        skip1 = (Button) lead_a.findViewById(R.id.welcome_intent1);
        skip2 = (Button) lead_b.findViewById(R.id.welcome_intent2);
        skip3 = (Button) lead_c.findViewById(R.id.welcome_intent3);
//        skip4 = (Button) lead_d.findViewById(R.id.welcome_intent4);
        go_ecjia = (Button) lead_e.findViewById(R.id.go_ecjia);
        skip1.setOnClickListener(this);
        skip2.setOnClickListener(this);
        skip3.setOnClickListener(this);
//        skip4.setOnClickListener(this);
        go_ecjia.setOnClickListener(this);

        startimage1.setImageResource(R.drawable.start1);
        startimage2.setImageResource(R.drawable.start2);
        startimage3.setImageResource(R.drawable.start3);
//        startimage4.setImageResource(R.drawable.start4);
        startimage5.setImageResource(R.drawable.start5);




        list.add(lead_a);
        list.add(lead_b);
        list.add(lead_c);
//        list.add(lead_d);
        list.add(lead_e);
        imagePager = (ViewPager) findViewById(R.id.image_pager);
        //imagePager.setAlpha((float) 0.5);//设置透明度
        galleryImageAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));//删除页卡
            }

            public View instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡

                container.addView(list.get(position));//添加页卡
                return list.get(position);
            }
        };
        //-----------------------------------------2013-12-3修改导航界面------------------------------------------------
        imagePager.setAdapter(galleryImageAdapter);
        imagePager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                pager_num = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.welcome_intent1:
            case R.id.welcome_intent2:
            case R.id.welcome_intent3:
//            case R.id.welcome_intent4:
            case R.id.go_ecjia:
                if (isFirstRun) {
                    editor.putBoolean("isFirstRun", false);
                    editor.commit();
//                    createShortcut();//创建图标
//                    addShortCut("二位码扫描");
                    Intent intent = new Intent(GalleryImageActivity.this,ECJiaMainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                } else {
                    finish();
                }
                break;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    protected void onDestroy(){
        super.onDestroy();
        if(shared.getBoolean("isSettingGo",true)){
            editor.putBoolean("isSettingGo", false);
            editor.commit();
        }
    }


}
