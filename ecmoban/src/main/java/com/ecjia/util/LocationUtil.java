package com.ecjia.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ecjia.consts.AppConst;

import java.io.File;

/**

 * 
 * @author zihao
 * 
 */
public class LocationUtil {

	//调用百度地图客户端
	public static  void StartBaiduGuide(Context context,String shopname, String distance, String lat, String lng) {
		if (TextUtils.isEmpty(distance)) {
			distance = "0";
		}
		int dis = Integer.valueOf(distance);
		String type = "";
		if (dis > 2000) {
			type = "driving";
		} else {
			type = "walking";
		}

		try {
			Intent intent = Intent
					.getIntent("intent://map/direction?origin=latlng:"
							+ AppConst.LNG_LAT[1]
							+ ","
							+ AppConst.LNG_LAT[0]
							+ "|name:"
							+ AppConst.ADDRESS[1]
							+ "&destination=" + shopname + "|latlng:" + lat + "," + lng + "&mode=" + type + "&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

			if (isInstallByread("com.baidu.BaiduMap")) {
				context.startActivity(intent);
			} else {
				Toast.makeText(context,
						"您尚未安装百度地图app或app版本过低，请安装最新版的百度地图", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context,
					"您尚未安装百度地图app或app版本过低，请安装最新版的百度地图", Toast.LENGTH_LONG).show();
		}
	}

	private static boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

}