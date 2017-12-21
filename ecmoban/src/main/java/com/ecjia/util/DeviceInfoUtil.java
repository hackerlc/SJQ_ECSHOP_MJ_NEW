package com.ecjia.util;

import android.content.Context;
import android.telephony.TelephonyManager;


import com.ecjia.entity.responsemodel.DEVICE;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * User: howie
 * Date: 13-5-11
 * Time: 下午4:09
 */
public class DeviceInfoUtil {

    DeviceInfoUtil() {
    }

    public final static String CLIENT = "android";
    public final static String CODE = "4001";

    /**
     * 对外获取Udid
     *
     * @param context
     * @return
     */
    private static String getUdid(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            try {
                md.update(uniqueId.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] result = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
            int i = b & 0xff;
            if (i < 0xf) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(i));
        }
        uniqueId = sb.toString().toLowerCase();
        LG.i("UDID的值==============" + uniqueId);
        System.out.println(sb.toString().toUpperCase());
        LG.i("sha1加密==============" + uniqueId);
        return uniqueId;
    }

    /**
     * 全局只创建一次
     *
     * @param context
     */
    private static DEVICE createDevice(Context context) {
        DEVICE device = new DEVICE();
        device.setClient(CLIENT);
        device.setCode(CODE);//产品的唯一识别码
        String uniqueId = getUdid(context);
        device.setUdid(uniqueId);
        return device;
    }

    public static DEVICE getDevice(Context context) {
        return createDevice(context);
    }
}
