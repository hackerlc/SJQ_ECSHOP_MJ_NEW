package com.ecjia.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.view.View;
import android.view.WindowManager;

import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.model.City;
import com.ecjia.entity.responsemodel.CONFIG;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.MYMESSAGE;
import com.ecjia.entity.responsemodel.PAYMENT;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.entity.responsemodel.SHOPINFO;
import com.ecjia.entity.responsemodel.USER;
import com.ecjia.network.base.RetrofitHttpServices;
import com.ecjia.util.ECJIAFileUtil;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.PushUtil;
import com.ecjia.util.common.SPUtils;
import com.ecjia.util.gallery.GalleryImageUtils;
import com.ecjia.view.activity.WebViewActivity;
import com.ecjia.view.activity.push.PushActivity;
import com.ecjia.view.adapter.MsgSql;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecmoban.android.sijiqing.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gear.yc.com.gearlibrary.network.http.OkHttpManager;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2014/12/3.
 */
public class ECJiaApplication extends Application {

    public ActivityManager mActivityManager;
    public String mPackageName;
    private static Context context;
    public static String initCityName = "";//选择产业带选择以后记录
    public static String cityName = "浙江省";
    public static List<City> cityList = new ArrayList<>(); // 大区缓存
    // 记录是否已经初始化
    private boolean isHXInit = false;

    public static Context getContext() {
        return context;
    }

    /**
     * 消息处理判断
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            UMessage uMessage = null;
            try {
                uMessage = new UMessage(new JSONObject(msg.obj.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (msg.what == 0) {
                final MyDialog myDialog = new MyDialog(getApplicationContext(), uMessage.title, uMessage.text);
                myDialog.setStyle(MyDialog.STYLE_ONLY_SURE);
                myDialog.sureText.setText("确定");
                myDialog.setSureOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (("ComponentInfo{" + mPackageName + "/" + mPackageName + ".PushActivity}").equals
                                (getTopActivity(getApplicationContext()))) {
                            MyEvent event = new MyEvent("refresh_push_adpter");
                            EventBus.getDefault().post(event);
                        }
                        myDialog.dismiss();
                    }
                });
                myDialog.mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                myDialog.show();
            }

            if (msg.what == 1) {
                final MyDialog myDialog = new MyDialog(getApplicationContext(), uMessage.title, uMessage.text);
                myDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                myDialog.positiveText.setText("打开");
                myDialog.negativeText.setText("忽略");
                final UMessage finalUMessage = uMessage;
                myDialog.setPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (("ComponentInfo{" + mPackageName + "/" + mPackageName + ".PushActivity}").equals
                                (getTopActivity(getApplicationContext()))) {
                            MyEvent event = new MyEvent("refresh_push_adpter");
                            EventBus.getDefault().post(event);
                        }
                        myDialog.dismiss();
                        MsgSql.getIntence(getApplicationContext()).updateMessageReadStatus(finalUMessage.msg_id, 0);
                        PushUtil.oPendAction(ECJiaApplication.this, finalUMessage);
                    }
                });
                myDialog.setNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (("ComponentInfo{" + mPackageName + "/" + mPackageName + ".PushActivity}").equals
                                (getTopActivity(getApplicationContext()))) {
                            MyEvent event = new MyEvent("refresh_push_adpter");
                            EventBus.getDefault().post(event);
                        }
                        myDialog.dismiss();

                    }
                });
                myDialog.mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                myDialog.show();
            }
        }
    };

    private ArrayList<SHOPINFO> shopinfos = new ArrayList<SHOPINFO>();

    private DEVICE device;
    private USER user;//存储用户信息的
    private int goodsNum = 0;//购物车的商品数量
    private String shop_token; //未登录状态下的token

    public DEVICE getDevice() {
        return device;
    }

    public void setDevice(DEVICE device) {
        this.device = device;
    }

    public USER getUser() {
        return user;
    }

    public void setUser(USER user) {
        this.user = user;
    }

    public String getShop_token() {
        return shop_token;
    }

    public void setShop_token(String shop_token) {
        this.shop_token = shop_token;
    }

    public ArrayList<SHOPINFO> getShopinfos() {
        return shopinfos;
    }

    public void setShopinfos(ArrayList<SHOPINFO> shopinfos) {
        this.shopinfos = shopinfos;
    }

    public int getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        context = getApplicationContext();
        initSocial();
        SPUtils.init(context);
        initSession();
        initNetwork();
        initPush();//初始化推送
        initImageLoader();//初始化ImageLoader
        // 初始化环信SDK
        initEasemob();
        GalleryImageUtils.configGallery(this);
    }

    private void initEasemob() {
        if (EaseUI.getInstance().init(this, initOptions())) {

            // 设置开启debug模式
            EMClient.getInstance().setDebugMode(true);

            // 设置初始化已经完成
            isHXInit = true;
        }
    }

    private void initNetwork() {
//        device = DEVICE.getInstance();
//        if (TextUtils.isEmpty(device.getUdid()) || TextUtils.isEmpty(device.getClient())) {
//            DeviceInfoUtil.getDevice(context);
//        }
//        //拦截器 (1)--需要添加全局参数时可调用
//        Interceptor drivenCode = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request()//
//                        .newBuilder()//
//                        .header("User-Agent", Verification.getUserAgent(context))
//                        .addHeader("Device-client", device.getClient())//
//                        .addHeader("Device-code", device.getCode())//
//                        .addHeader("Device-udid", device.getUdid())//
//                        .addHeader("Api-vesion", "1.7.0")//
//                        .build();//
//                return chain.proceed(request);
//            }
//        };
        OkHttpClient.Builder builder = OkHttpManager.getInstance().build().getClient().newBuilder();
//        builder.addInterceptor(drivenCode);
        RetrofitHttpServices.getInstance()
                .setBaseUrl(AndroidManager.SERVICE_URL)
                .build(builder.build());
    }

    private void initSession() {
        SharedPreferences shared = this.getSharedPreferences(SharedPKeywords.SPUSER, 0);
        SESSION session = SESSION.getInstance();
        session.setUid(shared.getString(SharedPKeywords.SPUSER_KUID, ""));
        session.setSid(shared.getString(SharedPKeywords.SPUSER_KSID, ""));
    }

    private void initSocial() {
        PlatformConfig.setWeixin(AndroidManager.WXAPPID, AndroidManager.WXAPPSECRET);
        //微信 appid appsecret
        PlatformConfig.setQQZone(AndroidManager.QQAPPID, AndroidManager.QQAPPSECRET);
    }


    private void initImageLoader() {

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .showImageOnLoading(R.drawable.default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (3 * 1024 * 1024))
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheExtraOptions(300, 300)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initPush() {

        /**用户自定义消息接收
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPackageName = getPackageName();
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String device_token = PushAgent.getInstance(this).getRegistrationId();
        LG.i("===umeng-deviceToken0===" + device_token);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LG.i("===umeng-deviceToken===" + deviceToken);
                PushUtil.setDeviceToken(ECJiaApplication.this, deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LG.e("===umeng-deviceToken===" + s + s1);
            }
        });


        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void handleMessage(Context context, UMessage msg) {

                LG.e("===handleMessage===");
                MYMESSAGE mymsg = new MYMESSAGE();
                mymsg.setUn_read(MYMESSAGE.UN_READ);
                mymsg.setTitle(msg.title);
                mymsg.setContent(msg.text);
                mymsg.setCustom(msg.custom);
                mymsg.setMsg_id(msg.msg_id);
                mymsg.setType(msg.after_open);
                mymsg.setUrl(msg.url);
                mymsg.setGotoActivity(msg.activity);

                if (null != msg.extra) {
                    mymsg.setOpen_type(msg.extra.get("open_type"));
                    if ("webview".equals(mymsg.getOpen_type())) {
                        mymsg.setWebUrl(msg.extra.get("url"));
                    } else if ("goods_list".equals(mymsg.getOpen_type())) {
                        mymsg.setCategory_id(msg.extra.get("category_id"));
                    } else if ("goods_comment".equals(mymsg.getOpen_type())) {
                        mymsg.setGoods_id_comment(msg.extra.get("goods_id"));
                    } else if ("goods_detail".equals(mymsg.getOpen_type())) {
                        mymsg.setGoods_id(msg.extra.get("goods_id"));
                    } else if ("orders_detail".equals(mymsg.getOpen_type())) {
                        mymsg.setOrder_id(msg.extra.get("order_id"));
                    } else if ("search".equals(mymsg.getOpen_type())) {
                        mymsg.setKeyword(msg.extra.get("keyword"));
                    }
                }
                MsgSql.getIntence(context).insertMessage(mymsg);

                /**
                 * handler通知自己弹框提示
                 */
                if (isAppOnForeground()) {
                    /**
                     * 如果当前在消息中心页面
                     */
                    if (("ComponentInfo{" + mPackageName + "/" + mPackageName + ".PushActivity}").equals
                            (getTopActivity(getApplicationContext()))) {
                        MyEvent event = new MyEvent(EventKeywords.UPDATE_MESSAGE);
                        event.setMyMessage(mymsg);
                        EventBus.getDefault().post(event);
                    } else if (("ComponentInfo{" + mPackageName + "/" + "com.ecjia.hamster.activity.ECJiaMainActivity}").equals
                            (getTopActivity(getApplicationContext()))) {
                        MyEvent event = new MyEvent(EventKeywords.UPDATE_MESSAGE);
                        event.setMyMessage(mymsg);
                        EventBus.getDefault().post(event);
                    }
//
//                    /**
//                     *
//                     */
//                    Message message = new Message();
//                    message.obj = msg.getRaw().toString();
//                    if (null != msg.extra) {
//                        message.what = 1;
//                    } else {
//                        message.what = 0;
//                    }
//                    handler.sendMessage(message);

                }
                super.handleMessage(context, msg);
            }

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                    }
                });
            }


        };
        mPushAgent.setMessageHandler(messageHandler);

//        mPushAgent.setNotificaitonOnForeground(false);//前台不显示通知栏消息

        /**
         * 通知栏事件监听
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
                MsgSql.getIntence(getApplicationContext()).updateMessageReadStatus(uMessage.msg_id, 0);
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra(IntentKeywords.WEB_URL, uMessage.url);
                intent.putExtra(IntentKeywords.WEB_TITLE, uMessage.title);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                MsgSql.getIntence(getApplicationContext()).updateMessageReadStatus(uMessage.msg_id, 0);
                Intent intent = new Intent(context, PushActivity.class);
                intent.putExtra("refresh", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
                MsgSql.getIntence(getApplicationContext()).updateMessageReadStatus(uMessage.msg_id, 0);
                LG.i("uMessage.open_type==" + uMessage.extra.get("signin"));
                PushUtil.oPendAction(context, uMessage);
                super.launchApp(context, uMessage);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    public boolean isAppOnForeground() {
        List<ActivityManager.RunningTaskInfo> tasksInfo = mActivityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            LG.i("top Activity = " + tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (mPackageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                LG.i("在前台1");
                return true;
            }
        }
        LG.i("在后台1");
        return false;
    }

    public String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString();
        } else {
            return null;
        }
    }


    public ArrayList<PAYMENT> paymentlist = new ArrayList<PAYMENT>();

    public ArrayList<PAYMENT> onlinepaymentlist = new ArrayList<PAYMENT>();

    public ArrayList<PAYMENT> uplinepaymentlist = new ArrayList<PAYMENT>();

    public ArrayList<PAYMENT> Rechargepaymentlist = new ArrayList<PAYMENT>();

    private CONFIG config;

    public CONFIG getConfig() {
        if (config == null) {
            try {
                JSONObject jsonObject = new JSONObject(ECJIAFileUtil.readFile(AndroidManager.SHOPCONFIG, CONFIG.SHOP_CONFIG_FILE_NAME));
                config = CONFIG.fromJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public static String getCityName() {
        String str;
        str = cityName;
        str.replace("站", "");
        str.replace("市", "");

        return str;
    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }

}
