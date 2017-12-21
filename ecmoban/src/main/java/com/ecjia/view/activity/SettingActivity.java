package com.ecjia.view.activity;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.AndroidManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.network.netmodle.ConfigModel;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.util.DataCleanManager;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.FileSizeUtil;
import com.ecjia.util.LG;
import com.ecjia.util.CheckInternet;
import com.ecjia.view.adapter.Sqlcl;

import com.ecjia.widgets.dialog.MyDialog;
import com.umeng.message.PushAgent;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;

import de.greenrobot.event.EventBus;

public class SettingActivity extends BaseActivity implements OnClickListener {

    private TextView title;
    private ImageView back;

    private LinearLayout type1; //智能模式


    private TextView mobile, version, versiondate, cachesize;
    private LinearLayout official_web;
    private LinearLayout settingMobileLayout;
    private LinearLayout settingVersionLayout;
    private LinearLayout setting_version_update;
    private LinearLayout setting_new_function;

    private SharedPreferences shared, sharedPreference;
    ;
    private SharedPreferences.Editor editor;
    private Resources resource;
    private MyDialog mDialog;
    public Handler Internethandler;
    private View new_function_line;
    private LinearLayout languageitem;
    private TextView used_lan;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cachesize.setText(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();
        Internethandler = new Handler() {
            public void handleMessage(Message msg) {

                if (msg.obj == ProtocolConst.CONFIG) {
                    if (msg.what == 1) {
                        if (null != ConfigModel.getInstance().config && null != ConfigModel.getInstance().config.getService_phone()) {
                            mobile.setText(ConfigModel.getInstance().config.getService_phone());
                        }
                    }
                }

            }
        };
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        if (null == ConfigModel.getInstance()) {
            ConfigModel configModel = new ConfigModel(this);

            ConfigModel.getInstance().getConfig(Internethandler);
        } else if (null == ConfigModel.getInstance().config) {

            ConfigModel.getInstance().getConfig(Internethandler);
        }

        title = (TextView) findViewById(R.id.top_view_text);
        resource = (Resources) getBaseContext().getResources();
        String set = resource.getString(R.string.setting);
        title.setText(set);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(this);

        type1 = (LinearLayout) findViewById(R.id.setting_type1);
        cachesize = (TextView) findViewById(R.id.setting_cachesize);

        new Thread() {
            public void run() {
                Message msg = new Message();

                try {
                    String size = FileSizeUtil.getAutoFileOrFilesSize(AndroidManager.APPCACHEPATH);

                    msg.obj = size;

                } catch (Exception e) {
                }
                myHandler.sendMessage(msg);
            }
        }.start();


        languageitem = (LinearLayout) findViewById(R.id.setting_language);
        used_lan = (TextView) findViewById(R.id.used_language);
        Configuration config = resource.getConfiguration();

        if ("zh".equalsIgnoreCase(sharedPreference.getString("language", null))) {
            used_lan.setText(getResources().getString(R.string.Chinese));
        } else if ("en".equalsIgnoreCase(sharedPreference.getString("language", null))) {
            used_lan.setText(getResources().getString(R.string.English));
        } else {
            used_lan.setText(getResources().getString(R.string.local));
        }

        settingMobileLayout = (LinearLayout) findViewById(R.id.setting_mobile_layout);
        settingVersionLayout = (LinearLayout) findViewById(R.id.setting_version_layout);
        setting_version_update = (LinearLayout) findViewById(R.id.setting_version_update);
        setting_new_function = (LinearLayout) findViewById(R.id.setting_new_function);

        new_function_line = (View) findViewById(R.id.new_function_line);

        if (!AndroidManager.SUPPORT_GALLERY) {
            setting_new_function.setVisibility(View.GONE);
            new_function_line.setVisibility(View.GONE);
        }

        settingMobileLayout.setOnClickListener(this);
        mobile = (TextView) findViewById(R.id.setting_mobile);
        version = (TextView) findViewById(R.id.setting_version);
        versiondate = (TextView) findViewById(R.id.setting_version_date);
        PackageManager manager;
        PackageInfo info = null;
        manager = this.getPackageManager();
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText(info.versionName);

        int i = info.versionCode;
        try {
            versiondate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(i + "")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (null != ConfigModel.getInstance().config && null != ConfigModel.getInstance().config.getService_phone()) {
            mobile.setText(ConfigModel.getInstance().config.getService_phone());
        }

        official_web = (LinearLayout) findViewById(R.id.setting_official_web);


        type1.setOnClickListener(this);
        setting_version_update.setOnClickListener(this);
        setting_new_function.setOnClickListener(this);
        languageitem.setOnClickListener(this);
        official_web.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        String off = resource.getString(R.string.setting_website);
        String tech = resource.getString(R.string.setting_tech);
        String call = resource.getString(R.string.setting_call_or_not);
        String call_cannot_empty = resource.getString(R.string.setting_call_cannot_empty);
        String network_problem = resource.getString(R.string.goodlist_network_problem);
        String zoo_introduce = resource.getString(R.string.setting_zoo_introduce);

        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.setting_type1:
                final MyDialog dialog = new MyDialog(this, resource.getString(R.string.point), resource.getString(R
                        .string.setting_clear_notify));
                dialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                dialog.setNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Runnable runnable = new Runnable() {
                            public void run() {
                                Message msg = new Message();
                                try {
                                    DataCleanManager.deleteFolderFile(AndroidManager.APPCACHEPATH, false);
                                    Sqlcl sqlcl = new Sqlcl(SettingActivity.this);
                                    sqlcl.delete();
                                    String size = FileSizeUtil.getAutoFileOrFilesSize(AndroidManager.APPCACHEPATH);
                                    msg.obj = size;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                myHandler.sendMessage(msg);
                            }
                        };
                        new Thread(runnable).start();
                    }
                });
                dialog.show();

                break;
            case R.id.setting_official_web: {
                if (!CheckInternet.isConnectingToInternet(SettingActivity.this) || ConfigModel.getInstance().config == null) {//检查网络连接
                    ToastView toast = new ToastView(this, network_problem);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent it = new Intent(SettingActivity.this, WebViewActivity.class);
                    it.putExtra(IntentKeywords.WEB_URL, ConfigModel.getInstance().config.getSite_url());
                    it.putExtra(IntentKeywords.WEB_TITLE, off);
                    startActivity(it);
                }
                break;
            }
            case R.id.setting_mobile_layout: {
                if (StringUtils.isNotEmpty(mobile.getText().toString())) {

                    mDialog = new MyDialog(SettingActivity.this, call, ConfigModel.getInstance().config.getService_phone());
                    mDialog.show();
                    mDialog.positive.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ConfigModel.getInstance().config.getService_phone()));
                            startActivity(intent);
                        }
                    });
                    mDialog.negative.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });
                } else {
                    ToastView toast = new ToastView(this, call_cannot_empty);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                break;
            }
            case R.id.setting_new_function: {
                shared = getSharedPreferences("userInfo", 0);
                editor = shared.edit();
                editor.putBoolean("isSettingGo", true);
                editor.commit();
                Intent intent = new Intent(SettingActivity.this, GalleryImageActivity.class);
                startActivity(intent);
                SettingActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            }

            case R.id.setting_language: {
                Intent intent = new Intent(SettingActivity.this, LanguageActivity.class);
                startActivity(intent);
                SettingActivity.this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            }

            case R.id.setting_version_update: {
                Intent intent = new Intent(this, UpdateActivity.class);
                startActivity(intent);
                break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEvent(MyEvent event) {
        if ("changelanguage".equals(event.getMsg())) {
            LG.i("运行==");
            mDialog = null;
            this.finish();
        }

    }
}