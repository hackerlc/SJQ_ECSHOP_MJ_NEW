package com.ecjia.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.VersionUpdateUtil;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.ECJIAVERSION;
import com.ecjia.util.FileSizeUtil;
import com.ecjia.util.LG;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Adam on 2016/7/28.
 */
public class UpdateActivity extends BaseActivity {


    ECJIAVERSION ecjiaversion;
    private TextView appNameTV;
    private TextView size;
    private TextView old_version;
    private TextView new_version;
    private TextView updeta_log;
    private TextView update_cancel;
    private TextView update_ok;

    private FrameLayout progress_ll;


    public static final String LOG_TAG = "test";
    File rootDir = Environment.getExternalStorageDirectory();

    //定义要下载的文件名
    private ProgressBar progressBar;
    private TextView progressText;
    private boolean paused = false;
    private boolean finished = false;

    private int downloadFlag = 5;
    private final int RE_CHECK = 1;
    private final int START_DOWNLOAD = 2;
    private final int DOWNLOAD_PAUSE = 3;
    private final int DOWNLOAD_CONTINUE = 4;
    private final int START_CHECK = 5;
    private int localfileSize;
    private TextView install;

    private final String SD_PATH = "/mnt/sdcard/";//本地文件目录
    int fileSize = 0;//文件大小（服务端）
    String localfile = null;//本地文件地址

    public boolean isCancel = false;


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj.equals("filesize")) {
                if (localfileSize == 0) {
                    update_ok.setText(getResources().getString(R.string.update_size_left) + FileSizeUtil.formetFileSize(msg.what, FileSizeUtil.SIZETYPE_MB) + "M)");
                    install.setBackgroundResource(R.drawable.shape_gray_stroke_white_bg_corner);
                    install.setEnabled(false);
                    install.setVisibility(View.GONE);

                    update_cancel.setVisibility(View.GONE);
                    update_cancel.setText(R.string.update_delete_file);

                    downloadFlag = START_DOWNLOAD;
                } else if (localfileSize < msg.what) {
                    progress_ll.setVisibility(View.VISIBLE);
                    progressBar.setProgress((int) (localfileSize * 100 / (float) msg.what));
                    update_ok.setText(R.string.update_continue);
                    update_ok.setTextColor(Color.BLACK);
                    update_ok.setBackgroundResource(R.drawable.shape_gray_stroke_transparent_bg_corner);
                    install.setBackgroundResource(R.drawable.shape_gray_stroke_white_bg_corner);
                    install.setEnabled(false);
                    install.setVisibility(View.GONE);

                    update_cancel.setVisibility(View.VISIBLE);
                    update_cancel.setText(R.string.update_delete_file);

                    downloadFlag = START_DOWNLOAD;
                } else if (localfileSize == msg.what) {
                    update_ok.setText(R.string.update_re_check);
                    install.setEnabled(true);
                    install.setBackgroundResource(R.drawable.shape_public_bg);
                    install.setTextColor(Color.WHITE);
                    install.setVisibility(View.VISIBLE);

                    update_cancel.setVisibility(View.VISIBLE);
                    update_cancel.setText(R.string.update_delete_file);

                    downloadFlag = RE_CHECK;
                }
            } else if (msg.obj.equals("progress")) {
                progressText.setText(msg.what + "%");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_update);
        initData();
        initView();
    }

    MyDownLoadAsyncT mAsynT;

    private void initView() {
        initTopView();

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressText = (TextView) findViewById(R.id.progress_tex);
        progress_ll = (FrameLayout) findViewById(R.id.progress_ll);

        appNameTV = (TextView) findViewById(R.id.name);
        size = (TextView) findViewById(R.id.size);
        old_version = (TextView) findViewById(R.id.old_version);
        new_version = (TextView) findViewById(R.id.new_version);
        updeta_log = (TextView) findViewById(R.id.updeta_log);

        install = (TextView) findViewById(R.id.btn_install);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile(new File(localfile));
            }
        });

        update_cancel = (TextView) findViewById(R.id.btn_delete);
        update_ok = (TextView) findViewById(R.id.update_ok);


        appNameTV.setText(getResources().getString(R.string.app_name));
        old_version.setText(VersionUpdateUtil.getVerName(this));

        update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localfileSize = 0;
                if (mAsynT == null) {
                    delete(localfile);
                    ToastView toastView = new ToastView(UpdateActivity.this, R.string.update_have_deleted);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();
                    progressBar.setProgress(0);
                    progressText.setText("");
                    progress_ll.setVisibility(View.INVISIBLE);
                    update_ok.setText(getResources().getString(R.string.update_size_left) + FileSizeUtil.formetFileSize(fileSize, FileSizeUtil.SIZETYPE_MB) + "M)");
                    update_ok.setBackgroundResource(R.drawable.shape_public_bg);
                    update_ok.setTextColor(Color.WHITE);
                    install.setBackgroundResource(R.drawable.shape_gray_stroke_white_bg_corner);
                    install.setTextColor(Color.parseColor("#666666"));
                    install.setEnabled(false);
                    install.setVisibility(View.GONE);

                    update_cancel.setVisibility(View.GONE);


                    downloadFlag = START_DOWNLOAD;
                } else if (mAsynT.isComplated) {
                    delete(localfile);
                    progressBar.setProgress(0);
                    progressText.setText("");
                    progress_ll.setVisibility(View.INVISIBLE);
                    update_ok.setText(getResources().getString(R.string.update_size_left) + FileSizeUtil.formetFileSize(fileSize, FileSizeUtil.SIZETYPE_MB) + "M)");
                    update_ok.setBackgroundResource(R.drawable.shape_public_bg);
                    update_ok.setTextColor(Color.WHITE);
                    install.setBackgroundResource(R.drawable.shape_gray_stroke_white_bg_corner);
                    install.setTextColor(Color.parseColor("#666666"));
                    install.setEnabled(false);
                    install.setVisibility(View.GONE);

                    update_cancel.setVisibility(View.GONE);


                    downloadFlag = START_DOWNLOAD;
                } else {
                    isCancel = true;
                    paused = false;
                    finished = true;

                    update_cancel.setVisibility(View.GONE);

                }

            }
        });

        update_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancel = false;
                switch (downloadFlag) {
                    case RE_CHECK://重新检测
                        update_ok.setText(R.string.update_re_check);
                        VersionUpdateUtil.getInstance().forceUpdate(UpdateActivity.this);
                        break;
                    case START_DOWNLOAD://立即下载
                        update_ok.setBackgroundResource(R.drawable.shape_gray_stroke_transparent_bg_corner);
                        update_ok.setTextColor(Color.parseColor("#000000"));
                        update_ok.setText("");
                        progressText.setVisibility(View.VISIBLE);
                        paused = false;
                        /**
                         * create a thread to download
                         */
                        finished = false;
                        mAsynT = null;
                        mAsynT = new MyDownLoadAsyncT();
                        mAsynT.execute("0");

                        update_cancel.setVisibility(View.VISIBLE);
                        update_cancel.setText(R.string.update_cancel);
                        downloadFlag = DOWNLOAD_PAUSE;//next step action pause


                        break;
                    case DOWNLOAD_PAUSE://暂停
                        paused = true;
                        update_ok.setText(R.string.update_continue);

                        update_cancel.setVisibility(View.VISIBLE);
                        update_cancel.setText(R.string.update_cancel);

                        progressText.setVisibility(View.INVISIBLE);
                        downloadFlag = DOWNLOAD_CONTINUE;//next step action continue
                        break;
                    case DOWNLOAD_CONTINUE://继续
                        if (mAsynT.isCancelled()) {
                            mAsynT = new MyDownLoadAsyncT();
                            mAsynT.execute("0");
                        }
                        paused = false;
                        progressText.setVisibility(View.VISIBLE);
                        update_ok.setText("");

                        update_cancel.setVisibility(View.VISIBLE);
                        update_cancel.setText(R.string.update_cancel);

                        downloadFlag = DOWNLOAD_PAUSE;//next step action pause
                        break;
                    case START_CHECK://重新检测
                        update_ok.setText(R.string.update_re_check);
                        break;

                }
            }
        });


        VersionUpdateUtil.getInstance().addUdpateVersionListener(new VersionUpdateUtil.UpdateVersionListener() {
            @Override
            public void checkSuccess(int code, ECJIAVERSION ecjiaversion) {

                switch (code) {
                    case -1:
                        break;
                    case 0:
                        new_version.setText(R.string.update_now_is_newest);
                        findViewById(R.id.new_version_fearture).setVisibility(View.GONE);
                        updeta_log.setVisibility(View.GONE);
                        findViewById(R.id.update_allrealy_new).setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        UpdateActivity.this.ecjiaversion = ecjiaversion;
                        new_version.setText("("+getResources().getString(R.string.update_have_checked_new) + ecjiaversion.getVersion() + ")");
                        updeta_log.setText(ecjiaversion.getChangelog());
                        updeta_log.setVisibility(View.VISIBLE);
                        findViewById(R.id.update_allrealy_new).setVisibility(View.GONE);
                        findViewById(R.id.new_version_fearture).setVisibility(View.VISIBLE);
                        localfile = SD_PATH + AndroidManager.AppName + "-v" + ecjiaversion.getVersion() + "-" + ecjiaversion.getUpdate_time() + ".apk";
                        update_ok.setBackgroundResource(R.drawable.shape_public_bg);
                        update_ok.setTextColor(Color.WHITE);
                        init();
                        break;
                }

            }

            @Override
            public void checkFail() {
                downloadFlag = RE_CHECK;//next step action pause
                update_ok.setText(R.string.update_re_check);
                findViewById(R.id.update_allrealy_new).setVisibility(View.VISIBLE);
            }

        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mAsynT != null) {

        } else if (ecjiaversion != null) {
            size.setText("1024");//ecjiaversion.getBinary().getFsize()
            new_version.setText("("+getResources().getString(R.string.update_have_checked_new) + ecjiaversion.getVersion()+")");
            updeta_log.setText(ecjiaversion.getChangelog());
            updeta_log.setVisibility(View.VISIBLE);
            findViewById(R.id.update_allrealy_new).setVisibility(View.GONE);
            findViewById(R.id.new_version_fearture).setVisibility(View.VISIBLE);
            localfile = SD_PATH + AndroidManager.AppName + "-v" + ecjiaversion.getVersion() + "-" + ecjiaversion.getUpdate_time() + ".apk";
            init();
            update_ok.setBackgroundResource(R.drawable.shape_public_bg);
            update_ok.setTextColor(Color.WHITE);
        } else {
            VersionUpdateUtil.getInstance().forceUpdate(this);
        }

    }

    /**
     * 安装应用
     *
     * @param file
     */
    private void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
        overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.update_topview);
        topView.setTitleText(R.string.update_title);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initData() {
        ecjiaversion = (ECJIAVERSION) getIntent().getSerializableExtra("version");
    }


    /**
     * 初始化
     */
    private void init() {
        new Thread() {
            @Override
            public void run() {
                synchronized ("") {
                    try {
                        URL url = new URL(ecjiaversion.getInstall_url());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setRequestMethod("GET");
                        fileSize = connection.getContentLength();

                        connection.disconnect();

                        File file = new File(localfile);
                        if (!file.exists()) {
                            localfileSize = 0;
                        } else {
                            localfileSize = FileSizeUtil.getFileSize(file);
                        }
                        LG.i("localfileSize == " + localfileSize);

                        if (localfileSize == 0) {//本地文件不存在

                            Message message = new Message();
                            message.what = fileSize;
                            message.obj = "filesize";
                            handler.sendMessage(message);

                        } else if (localfileSize < fileSize) {
                            //
                            Message message = new Message();
                            message.what = fileSize;
                            message.obj = "filesize";
                            handler.sendMessage(message);

                        } else if (localfileSize == fileSize) {

                            Message message = new Message();
                            message.what = fileSize;
                            message.obj = "filesize";
                            handler.sendMessage(message);

                        }
                    } catch (MalformedURLException e) {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }.start();

    }


    @Override
    protected void onPause() {
        paused = true;
        if (mAsynT != null) {
            mAsynT.isCancel = true;
        }
        super.onPause();
    }


    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
//            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile()) return deleteLocalFile(fileName);
            else return false;
        }
    }


    public static boolean deleteLocalFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    class MyDownLoadAsyncT extends AsyncTask<String, String, String> {

        boolean isCancel = false;
        private boolean isComplated = false;

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setProgress(0);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_ll.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
            progressText.setText((int) ((localfileSize * 100.00f) / (float) fileSize) + "%");
            progressBar.setProgress((int) ((localfileSize * 100.00f) / (float) fileSize));
            update_cancel.setBackgroundResource(R.drawable.shape_white_stroke_corner_press);
            update_cancel.setEnabled(true);
        }


        @Override
        protected String doInBackground(String... aurl) {
            isComplated = false;
            InputStream inputStream = null; //io input
            RandomAccessFile outputStream = null;
            HttpURLConnection connection = null;
            try {
                //连接地址
                URL u = new URL(ecjiaversion.getInstall_url());
                connection = (HttpURLConnection) u.openConnection();
                connection.setRequestMethod("GET");
                String range = "bytes=" + localfileSize + "-" + fileSize;
                LG.i("range==" + range);
                connection.setRequestProperty("Range", range);
                connection.setConnectTimeout(5000);
                LG.i("length==" + fileSize);
                connection.connect();

                inputStream = connection.getInputStream();

                byte[] buf = new byte[1024];
                int read = 0;
                int curSize = localfileSize;

                outputStream = new RandomAccessFile(localfile, "rw");
                outputStream.seek(localfileSize);
                while (!finished) {
                    while (paused) {
                        Thread.sleep(1000);
                        if (isCancel) {
                            break;
                        }
                        LG.i("sleep中");
                    }

                    if (isCancel) {
                        break;
                    }

                    read = inputStream.read(buf);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(buf, 0, read);
                    curSize = curSize + read;
                    publishProgress("" + (int) ((curSize * 100.00f) / (float) fileSize));
                    if (curSize == fileSize) {
                        finished = true;
                        localfileSize = fileSize;
                        isComplated = true;
                        break;
                    }

                }
                inputStream.close();
                outputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (connection != null) {
                            connection.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            progressBar.setProgress(Integer.parseInt(progress[0]));
            progressText.setText(progress[0] + "%");
        }

        @Override
        protected void onPostExecute(String unused) {
            progress_ll.setVisibility(View.GONE);
            update_ok.setText(R.string.update_re_check);
            update_ok.setBackgroundResource(R.drawable.shape_public_bg);
            update_ok.setTextColor(Color.WHITE);
            downloadFlag = RE_CHECK;

            if (fileSize == localfileSize) {
                openFile(new File(localfile));
                finished = true;
                update_cancel.setText(R.string.update_delete_file);
                update_cancel.setVisibility(View.VISIBLE);

                install.setTextColor(Color.WHITE);
                install.setBackgroundResource(R.drawable.shape_public_bg);
                install.setEnabled(true);
                install.setVisibility(View.VISIBLE);
            } else {
                if (UpdateActivity.this.isCancel) {
                    delete(localfile);
                    progressBar.setProgress(0);
                    progressText.setText("");
                    progress_ll.setVisibility(View.INVISIBLE);
                    update_ok.setText(getResources().getString(R.string.update_size_left) +"("+ FileSizeUtil.formetFileSize(fileSize, FileSizeUtil.SIZETYPE_MB) + "M)");
                    update_ok.setBackgroundResource(R.drawable.shape_public_bg);
                    update_ok.setTextColor(Color.WHITE);

                    install.setBackgroundResource(R.drawable.shape_gray_stroke_white_bg_corner);
                    install.setTextColor(Color.parseColor("#666666"));
                    install.setEnabled(false);


                    update_cancel.setVisibility(View.GONE);

                    downloadFlag = START_DOWNLOAD;

                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        mAsynT = null;
        super.onDestroy();
    }
}
