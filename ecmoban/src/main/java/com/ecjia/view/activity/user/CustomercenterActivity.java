package com.ecjia.view.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.EventKeywords;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.network.netmodle.UserInfoModel;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.ecjia.util.ProfilePhotoUtil;
import com.ecjia.widgets.dialog.EditPictureDialog;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.dialog.MyProgressDialog;
import com.ecjia.widgets.paycenter.alipay.alipayutil.Base64;
import com.ecmoban.android.sijiqing.R;
import com.hyphenate.chat.EMClient;
import com.umeng.message.PushAgent;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import de.greenrobot.event.EventBus;

/**
 * 类名介绍：个人资料页面
 * Created by sun
 * Created time 2017-02-13.
 */
public class CustomercenterActivity extends BaseActivity implements OnClickListener {
    private LinearLayout exitLogin;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    private TextView title, usernameTV, levelTV;
    private MyDialog mDialog;
    private ImageView back;
    private LinearLayout change_pwd;
    private Bitmap photo = null;
    String exiten;
    String exit;
    String customer;
    String getname, getlevel;
//    public static CustomercenterActivity instance = null;

    private LinearLayout editHeadPicture;

    private EditPictureDialog dialog;
    Resources resource;

    private ImageView profile_photo;
    private Handler mHandler;

    private UserInfoModel userInfoModel;

    private String rootpath, pictureName;
    private String uid;
    MyProgressDialog myProgressDialog;

    private Bitmap profilePhotoBitmap;
    private String userName;
    private String rankName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customercenter);
        EventBus.getDefault().register(this);
        userInfoModel = new UserInfoModel(this);
        userInfoModel.addResponseListener(this);
//        instance = this;
        PushAgent.getInstance(this).onAppStart();
        Intent intent = getIntent();
        shared = getSharedPreferences(SharedPKeywords.SPUSER, 0);
        uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
        userName = shared.getString("uname", "");
        rankName = shared.getString("level", "");
        editor = shared.edit();
        resource = (Resources) getBaseContext().getResources();
        exit = resource.getString(R.string.exit);
        exiten = resource.getString(R.string.ensure_exit);
        customer = resource.getString(R.string.custormercenter);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj.equals("save_profile_succeed")) {
                    if (msg.what == 0) {
                        ProfilePhotoUtil.getInstance().clearBitmap();
                        profilePhotoBitmap = ProfilePhotoUtil.getInstance().getProfileBitmap(uid);
                        profile_photo.setImageBitmap(profilePhotoBitmap);
                        EventBus.getDefault().post(new MyEvent(EventKeywords.USER_CHANGE_PHOTO));
                        myProgressDialog.dismiss();
                    }
                }
            }
        };
        profilePhotoBitmap = ProfilePhotoUtil.getInstance().getProfileBitmap(uid);
        setinfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_view_back:
                finish();
                break;
            case R.id.setting_exitLogin:

                mDialog = new MyDialog(this, exit, exiten);
                mDialog.setStyle(MyDialog.STYLE_POS_AND_NEG);
                mDialog.setPositiveListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        userInfoModel.signOut();
                        EMClient.getInstance().logout(true);
                    }
                });
                mDialog.setNegativeListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                mDialog.show();

                break;

            case R.id.customercenter_layout_img:
                dialog = new EditPictureDialog(CustomercenterActivity.this);
                dialog.takePhoto.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), uid + ".jpg")));
                        startActivityForResult(intent, 2);
                        dialog.dismiss();
                    }
                });
                dialog.fromPhotos.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
        }
    }

    //初始化设置
    void setinfo() {
        LG.i("setinfo已启动");
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(customer);
        exitLogin = (LinearLayout) findViewById(R.id.setting_exitLogin);
        exitLogin.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(this);
        change_pwd = (LinearLayout) findViewById(R.id.change_password);
        change_pwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomercenterActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        usernameTV = (TextView) findViewById(R.id.customercenter_username);
        levelTV = (TextView) findViewById(R.id.customercenter_level);

        usernameTV.setText(mApp.getUser().getName());
//        if (StringUtils.isNotEmpty(rankName)) {
//            LG.i("执行了");
//            levelTV.setText(rankName);
//        }

        if ("2".equals(mApp.getUser().getPurchaser_valid())) {
            levelTV.setText("认证会员");
        } else {
            levelTV.setText("普通会员");
        }

        editHeadPicture = (LinearLayout) findViewById(R.id.customercenter_layout_img);
        profile_photo = (ImageView) findViewById(R.id.customercenter_img);
        if (profilePhotoBitmap != null) {
            profile_photo.setImageBitmap(profilePhotoBitmap);
        } else {
            profile_photo.setImageResource(R.drawable.profile_no_avarta_icon_light);
        }
        editHeadPicture.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                }

                break;
            // 如果是调用相机拍照时
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + uid + ".jpg");
                    startPhotoZoom(Uri.fromFile(temp));
                }

                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，大家可以根据不同情况在合适的 地方做判断处理类似情况
                 */
                LG.i("我被执行1");
                if (resultCode == RESULT_OK) {
                    LG.i("我被执行2");
                    if (data != null) {
                        LG.i("我被执行3");
                        myProgressDialog = MyProgressDialog.createDialog(this);
                        myProgressDialog.setCancelable(false);
                        myProgressDialog.show();
                        setToServer(data);
                    }
                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 3);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setToServer(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            // Bitmap转二进制
            byte[] bpByte = bitmapToByte(photo);
            userInfoModel.upDateUserInfo(Base64.encode(bpByte));
        }
    }


    public byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public Bitmap byteToBitmap(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            return bitmap;
        } else {
            return null;
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {

        int width = drawable.getIntrinsicWidth();

        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height,

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, width, height);

        drawable.draw(canvas);

        return bitmap;

    }


    // 缓存数据
    private PrintStream ps = null;

    public String fileSave(Bitmap result, String name) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            ps.print(result);
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getPath();
    }


    public String writeImageToDisk(byte[] img, String fileName) {
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileName);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }


    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        super.onMessageResponse(url, jo, status);
        if (url.equals(ProtocolConst.UPDATE_USERINFO)) {
            if (status.getSucceed() == 1) {
                profile_photo.setImageBitmap(photo);
                ProfilePhotoUtil.getInstance().saveProfilePhoto(photo, uid, mHandler);
            } else if (status.getSucceed() == 2) {
                myProgressDialog.dismiss();
            }
        } else if (url.equals(ProtocolConst.SIGNOUT)) {
            editor.putString("uid", "");
            editor.putString("sid", "");

            /**
             * QQ
             */
            editor.putString("qq_id", "");
            editor.putString("myscreen_name", "");

            /**
             * 微信
             */
            editor.putString("wx_id", "");
            editor.putString("nick_name", "");

            editor.commit();
            EventBus.getDefault().post(new MyEvent("exsit"));

            finish();
        }
    }


    public void onEvent(MyEvent event) {
        if ("exsit".equals(event.getMsg())) {
            this.finish();
        }

    }
}
