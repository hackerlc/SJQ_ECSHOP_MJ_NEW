package com.ecjia.widgets;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecmoban.android.sijiqing.R;

public class PayToastView {
	public static Toast toast;
	private int time;
	private Timer timer;
	
	public PayToastView(Context context, String text,boolean b) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.pay_toast, null);
		TextView t = (TextView) view.findViewById(R.id.paysuccess_text);
		ImageView imgsuc=(ImageView) view.findViewById(R.id.pay_image_success);
		ImageView imgfail=(ImageView) view.findViewById(R.id.pay_image_fail);
		if(b){
			imgsuc.setVisibility(View.GONE);
			imgfail.setVisibility(View.VISIBLE);
		}
		t.setText(text);
		if(toast != null) {
			toast.cancel();
		}
		t.setText(text);
		if(toast != null) {
			toast.cancel();
		}
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
	}
	
	public PayToastView(Context context, int text,boolean b) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.pay_toast, null);
		TextView t = (TextView) view.findViewById(R.id.paysuccess_text);
		ImageView imgsuc=(ImageView) view.findViewById(R.id.pay_image_success);
		ImageView imgfail=(ImageView) view.findViewById(R.id.pay_image_fail);
		//定义显示那张图片
		if(b){
			imgsuc.setVisibility(View.GONE);
			imgfail.setVisibility(View.VISIBLE);
		}
		t.setText(text);
		if(toast != null) {
			toast.cancel();
		}
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
	}
	
	//设置toast显示位置
	public void setGravity(int gravity, int xOffset, int yOffset) {
		toast.setGravity(Gravity.CENTER, 0, 0); //居中显示
		//toast.setGravity(gravity, xOffset, yOffset);
	}
	
	//设置toast显示时间
	public void setDuration(int duration) {
		toast.setDuration(duration);
	}
	
	//设置toast显示时间(自定义时间)
	public void setLongTime(int duration) {
		//toast.setDuration(duration);
		time = duration;
		timer = new Timer();
        timer.schedule(new TimerTask(){
        	@Override
        	public void run() {
        		// TODO Auto-generated method stub
        		if(time-1000 >= 0) {
        			show();
        			time= time - 1000;
        		} else {
        			timer.cancel();
        		}
        	}
        }, 0, 1000);
	}
	
	public void show() {
		toast.show();
	}
	
	public static void cancel() {
		if(toast != null) {
			toast.cancel();
		}
	}
}
