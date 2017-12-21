package com.ecjia.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.view.ECJiaMainActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.LanguageAdapter;
import com.ecjia.util.EventBus.Event;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.util.LG;
import com.umeng.message.PushAgent;

import java.util.Locale;

import de.greenrobot.event.EventBus;

public class LanguageActivity extends BaseActivity {
    private TextView title, save;
    private ImageView back;
    private XListView listview;
    String[] languages = new String[]{"auto", "zh", "en"};
    Boolean[] selected = new Boolean[]{false, false, false};
    private LanguageAdapter adapter;
    Handler msghandler;
    Configuration config;
    SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initView();
    }

    private void initView() {
        config = getResources().getConfiguration();
        sharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        PushAgent.getInstance(this).onAppStart();
        EventBus.getDefault().register(this);
        title = (TextView) findViewById(R.id.top_view_text);
        title.setText(getResources().getString(R.string.language));
        save = (TextView) findViewById(R.id.top_right_save);
        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (XListView) findViewById(R.id.language_list);

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                adapter.notifyDataSetChanged();
                LG.i("" + selected[1]);
            }
        };
        initselected();//初始化选中状态
        adapter = new LanguageAdapter(this, languages);
        adapter.selected = selected;
        save.setVisibility(View.VISIBLE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setEnabled(false);
                for (int i = 0; i < selected.length; i++) { //切换默认语言
                    if (selected[i]) {
                        if ("zh".equalsIgnoreCase(languages[i])) {
                            sharedPreference.edit().putString("language", "zh").commit();
                            config.locale = Locale.CHINA;
                        } else if ("en".equalsIgnoreCase(languages[i])) {
                            sharedPreference.edit().putString("language", "en").commit();
                            config.locale = Locale.ENGLISH;
                        } else {
                            sharedPreference.edit().putString("language", "auto").commit();
                            config.locale = Locale.getDefault();
                        }

                        EventBus.getDefault().post(new MyEvent("changelanguage"));
                        getBaseContext().getResources().updateConfiguration(config, null);
                        Intent intent = new Intent();
                        intent.setClass(LanguageActivity.this, ECJiaMainActivity.class);
                        LanguageActivity.this.startActivity(intent);
                        break;
                    }

                }
                finish();
            }
        });
        listview.setPullLoadEnable(false);
        listview.setPullRefreshEnable(false);
        adapter.handler = msghandler;
        listview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    void initselected() {//初始化语言设置
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equalsIgnoreCase(sharedPreference.getString("language", null))) {
                selected[i] = true;
            } else {
                selected[i] = false;
            }
        }
    }

    public void onEvent(Event event) {

    }

}
