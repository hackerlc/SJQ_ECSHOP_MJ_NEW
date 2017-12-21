package com.ecjia.network.netmodle;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ecjia.base.ECJiaApplication;
import com.ecjia.consts.AndroidManager;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.entity.responsemodel.DEVICE;
import com.ecjia.entity.responsemodel.ECJIAVERSION;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.LG;
import com.ecjia.view.activity.UpdateActivity;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecmoban.android.sijiqing.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Adam on 2015/6/18.
 */
public class VersionUpdateUtil {


    static VersionUpdateUtil instance;
    static final String CHANNEL = "ecjia";

    public static VersionUpdateUtil getInstance() {
        if (instance == null) {
            synchronized (VersionUpdateUtil.class) {
                if (instance == null) {
                    instance = new VersionUpdateUtil();
                }
            }
        }
        return instance;
    }

    public void update(final Context context) {
        ECJiaApplication mApp = (ECJiaApplication) context.getApplicationContext();
        final String url = ProtocolConst.APP_UPGRADE_CHECK;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", "");
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("channel_code", CHANNEL);
            requestJsonObject.put("app_key", AndroidManager.APP_UPDATE_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===update传入===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.CLOUD_URL + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {

            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===update===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
//                    if (responseStatus.getSucceed() == 1) {
                    if (true) {
                        JSONObject data = jo.optJSONObject("data");
//                        ECJIAVERSION version = ECJIAVERSION.fromJson(data);
                        ECJIAVERSION version = new ECJIAVERSION();
                        version.setName("测试");
                        version.setVersion("200");
                        version.setChangelog("2.0.0");
                        version.setInstall_url("http://120.27.118.74/download/sijiqing-release-v2.0.0-200.apk");
                        version.setUpdate_url("120.27.118.74/download/sijiqing-release-v2.0.0-200.apk");
                        String ignoreVersion = context.getSharedPreferences("version_update", 0).getString("ignore_version", "");
//                        if (version.getVersion().equals(ignoreVersion)) {
//                            return;
//                        }
//                        int result = compareVersion(version.getVersion(), getVerCode(context));
//                        if (result > 0) {
                        if (true) {
                            if (mListener != null) {
                                mListener.checkSuccess(1, version);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===update===" + arg0.result);
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

        });

    }

    /**
     * 强制更新
     *
     * @param context
     */
    public void forceUpdate(final Context context) {
        final MyProgressDialog dialog = MyProgressDialog.createDialog(context);
        dialog.setMessage("正在检测中...");
        dialog.setCancelable(false);
        dialog.show();
//        context.getSharedPreferences("version_update", 0).edit().putString("ignore_version", "").commit();
        ECJiaApplication mApp = (ECJiaApplication) context.getApplicationContext();
        final String url = ProtocolConst.APP_UPGRADE_CHECK;
        JSONObject requestJsonObject = new JSONObject();
        try {
            requestJsonObject.put("token", "");
            requestJsonObject.put("device", DEVICE.getInstance().toJson());
            requestJsonObject.put("channel_code", CHANNEL);
            requestJsonObject.put("app_key", AndroidManager.APP_UPDATE_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("json", requestJsonObject.toString());
        LG.i("===update传入===" + requestJsonObject.toString());
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, AndroidManager.CLOUD_URL + url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dialog.dismiss();
                mListener.checkFail();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                dialog.dismiss();
                JSONObject jo;
                try {
                    jo = new JSONObject(arg0.result);
                    LG.i("===update===" + jo.toString());
                    STATUS responseStatus = STATUS.fromJson(jo.optJSONObject("status"));
                    if (responseStatus.getSucceed() == 1) {
                        JSONObject data = jo.optJSONObject("data");
                        ECJIAVERSION version = ECJIAVERSION.fromJson(data);
                        int result = compareVersion(version.getVersion(), getVerName(context));
                        if (result > 0) {
                            if (mListener != null) {
                                mListener.checkSuccess(1, version);
                            }
                        } else if (result == 0) {
                            if (mListener != null) {
                                mListener.checkSuccess(0, version);
                            }
                        } else {
                            if (mListener != null) {
                                mListener.checkSuccess(-1, version);
                            }
                        }
                    } else {
                        if (mListener != null) {
                            mListener.checkFail();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LG.i("===update===" + arg0.result);
                    mListener.checkFail();
                }

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
            }

        });

    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVerCode(Context context) {
        String verName = "";
        try {
            verName = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }


    /**
     * 比较版本号
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }

        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");

        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;

        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }

        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }

            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    private UpdateVersionListener mListener;

    public void addUdpateVersionListener(UpdateVersionListener listener) {
        mListener = listener;
    }

    public interface UpdateVersionListener {

        void checkSuccess(int code, ECJIAVERSION ecjiaversion);

        void checkFail();
    }

    public static void showUpdateDialog(Context context, ECJIAVERSION ecjiaversion) {
        UdpateDialog dialog = new UdpateDialog(context, ecjiaversion);
        dialog.show();
    }


    public static void toUpdateActivity(Context context, ECJIAVERSION ecjiaversion) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("version", ecjiaversion);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    private static class UdpateDialog {

        Dialog dialog;
        TextView log;
        Button ok;
        Button cancel;

        Button cancelThis;
        Context mContext;

        public UdpateDialog(final Context context, final ECJIAVERSION ecjiaversion) {
            this.mContext = context;
            View contentView = LayoutInflater.from(context).inflate(R.layout.umeng_update_dialog, null);
            log = (TextView) contentView.findViewById(R.id.umeng_update_content);
            log.setText(ecjiaversion.getChangelog());

            ok = (Button) contentView.findViewById(R.id.umeng_update_id_ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateActivity.class);

                    intent.putExtra("version", ecjiaversion);
                    context.startActivity(intent);
                    dismiss();
                }
            });

            cancelThis = (Button) contentView.findViewById(R.id.umeng_update_id_ignore);
            cancelThis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.getSharedPreferences("version_update", 0).edit().putString("ignore_version", ecjiaversion.getVersion()).commit();
                    dismiss();
                }
            });

            cancel = (Button) contentView.findViewById(R.id.umeng_update_id_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            dialog = new Dialog(context, R.style.dialog);
            dialog.setContentView(contentView);
            dialog.setCanceledOnTouchOutside(false);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int h = (int) (((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.6);
            if (metrics.heightPixels < h) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, h);
            }

        }

        public void show() {
            if (dialog != null) {
                dialog.show();
            }
        }

        public void dismiss() {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }


    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void intoDownloadManager(Context context, ECJIAVERSION ecjiaversion) {
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(ecjiaversion.getInstall_url());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置下载路径和文件名
        request.setDestinationInExternalPublicDir("download", "ecjia-v" + ecjiaversion.getVersion() + ".apk");
        request.setDescription("ECJia移动商城新版本下载");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
        // 把当前下载的ID保存起来
        SharedPreferences sPreferences = context.getSharedPreferences("downloadplato", 0);
        sPreferences.edit().putLong("plato", refernece).commit();
    }
}
