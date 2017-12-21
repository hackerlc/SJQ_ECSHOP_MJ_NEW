package com.ecjia.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

public class AppOutActivity extends BaseActivity {

	private ImageView bg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appout);
        PushAgent.getInstance(this).onAppStart();
		bg = (ImageView) findViewById(R.id.bg);
		
		Intent intent = getIntent();
		int flag = intent.getIntExtra("flag", 0);
		if(flag == 1)
        {
			bg.setBackgroundResource(R.drawable.closed_ip4);
		}
		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
         
        return true;
    }
    
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	finish();
    }
	
}
