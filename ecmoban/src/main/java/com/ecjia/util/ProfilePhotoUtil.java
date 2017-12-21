package com.ecjia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.ecjia.consts.AndroidManager;
import com.ecjia.entity.responsemodel.SESSION;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ProfilePhotoUtil {

    public static ProfilePhotoUtil instance;

    public ProfilePhotoUtil() {
        instance = this;
    }

    public static ProfilePhotoUtil getInstance() {
        if (instance == null) {
            synchronized (ProfilePhotoUtil.class) {
                if (instance == null) {
                    instance = new ProfilePhotoUtil();
                }
            }
        }
        return instance;
    }

    public void clearBitmap() {
        bitmap = null;
    }

    private Bitmap bitmap;

    public void downLoadProfilePhoto(String url, final String localPath) {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.download(url, localPath, false, true, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> fileResponseInfo) {
                bitmap = BitmapFactory.decodeFile(localPath);
                if (mListener != null && !TextUtils.isEmpty(SESSION.getInstance().getUid())) {
                    mListener.downLoadSuccess();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (mListener != null) {
                    mListener.downLoadFailue();
                }

            }
        });
    }

    OnDownLoadProfilePhotoListener mListener;


    public interface OnDownLoadProfilePhotoListener {

        void downLoadSuccess();

        void downLoadFailue();
    }

    public void addDownLoadProfilePhotoListener(OnDownLoadProfilePhotoListener listener) {
        this.mListener = listener;
    }

    public boolean isProfilePhotoExist(String uid) {
        File file = new File(AndroidManager.PROFILE_PHOTO + "/" + uid + ".jpg");
        if (file.exists()) {
            LG.i("该用户头像存在");
            return true;
        } else {
            LG.i("该用户头像不存在");
            return false;
        }
    }

    public String getProfilePhotoPath(String uid) {
        return AndroidManager.PROFILE_PHOTO + "/" + uid + ".jpg";
    }


    public void saveProfilePhoto(Bitmap photo, String uid, Handler handler) {
        bitmap = photo;
        File file = new File(AndroidManager.PROFILE_PHOTO);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(file + "/" + uid + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
            Message msg = new Message();
            msg.obj = "save_profile_succeed";
            msg.what = 0;
            handler.sendMessage(msg);
            LG.i("保存头像图片成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Bitmap getProfileBitmap(String uid) {
        if (bitmap != null) {
            return bitmap;
        } else {
            if (isProfilePhotoExist(uid)) {
                bitmap = BitmapFactory.decodeFile(getProfilePhotoPath(uid));
                LG.i("重新创建头像的bitmap对象");
                return bitmap;
            }
        }
        return null;
    }

}

