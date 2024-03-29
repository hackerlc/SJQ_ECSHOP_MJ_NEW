package com.ecjia.network;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

public class NetworkStateService extends Service {

	private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();  
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    if(name.equals("WIFI")) {

                    	editor.putString("netType", "wifi");
            			editor.commit();
                    	
                    } else {
                    	
                    	editor.putString("netType", "3g");
            			editor.commit();
                    	
                    }
                    

                    //Toast.makeText(context, "当前网络名称:"+name, 0).show();
                 
                    //doSomething()
                } else {
                    //Toast.makeText(context, "没有可用网络", 0).show();
                    //doSomething()
                }
            }
        }
    };
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		shared = getSharedPreferences("userInfo", 0); 
		editor = shared.edit();
		
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
}
