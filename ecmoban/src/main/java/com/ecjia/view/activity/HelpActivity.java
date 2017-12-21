package com.ecjia.view.activity;


import java.util.ArrayList;
import java.util.List;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.HelpAdapter;
import com.ecjia.entity.responsemodel.ARTICLE;
import com.ecjia.entity.responsemodel.SHOPHELP;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HelpActivity extends BaseActivity {
    private TextView title;
    private ImageView back;
    private ListView listView;
    private HelpAdapter helpAdapter;
    private List<SHOPHELP> list_help = new ArrayList<SHOPHELP>();
    private List<ARTICLE> list_article = new ArrayList<ARTICLE>();
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        PushAgent.getInstance(this).onAppStart();
        title = (TextView) findViewById(R.id.top_view_text);

        back = (ImageView) findViewById(R.id.top_view_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();
        String s = intent.getStringExtra("data");
        p = intent.getIntExtra("position", 0);

        if (null != s && s.length() > 0) {
            try {
                JSONObject jo = new JSONObject(s);
                JSONArray contentArray = jo.optJSONArray("data");

                if (null != contentArray && contentArray.length() > 0) {
                    list_help.clear();
                    for (int i = 0; i < contentArray.length(); i++) {
                        JSONObject contentJsonObject = contentArray.getJSONObject(i);
                        SHOPHELP help_Item = SHOPHELP.fromJson(contentJsonObject);
                        list_help.add(help_Item);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list_article = list_help.get(p).article;
        title.setText(list_help.get(p).getName());

        listView = (ListView) findViewById(R.id.help_list);
        helpAdapter = new HelpAdapter(this, list_article);
        listView.setAdapter(helpAdapter);
        if (list_article.size() == 0) {
            listView.setVisibility(View.GONE);
        }


        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(HelpActivity.this, HelpWebActivity.class);
                intent.putExtra("id", list_article.get(position).getId());
                intent.putExtra("title", list_article.get(position).getTitle());
                startActivity(intent);
            }
        });

    }

}
