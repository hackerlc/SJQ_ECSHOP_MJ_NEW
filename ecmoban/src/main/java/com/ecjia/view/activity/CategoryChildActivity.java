package com.ecjia.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.adapter.CategoryListAdapter;
import com.ecjia.entity.responsemodel.CATEGORY;
import com.ecjia.entity.responsemodel.FILTER;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;


public class CategoryChildActivity extends BaseActivity implements View.OnClickListener {

    private ImageView search;
    private EditText input;
    private ImageButton backButton;
    private TextView childtitle;
    private LinearLayout titleshow;
    private ListView childListView;
    CategoryListAdapter childListAdapter;
    CATEGORY category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_child);
        PushAgent.getInstance(this).onAppStart();
        input = (EditText) findViewById(R.id.search_input);
        search = (ImageView) findViewById(R.id.search_search);
        childtitle = (TextView) findViewById(R.id.child_title);
        titleshow = (LinearLayout) findViewById(R.id.titleshow);
        titleshow.setVisibility(View.VISIBLE);
        input.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnClickListener(this);

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent it = new Intent(CategoryChildActivity.this, GoodsListActivity.class);
                    FILTER filter = new FILTER();
                    filter.setKeywords(input.getText().toString().toString());
                    try {
                        it.putExtra("filter", filter.toJson().toString());

                    } catch (JSONException e) {
                    }


                    startActivity(it);
                }
                return false;
            }
        });


        childListView = (ListView) findViewById(R.id.child_list);
        String categoryStr = getIntent().getStringExtra("category");

        try {
            JSONObject jsonObject = new JSONObject(categoryStr);
            CATEGORY category1 = CATEGORY.fromJson(jsonObject);
            this.category = category1;
            childtitle.setText(category.getName());
            childListAdapter = new CategoryListAdapter(this, this.category.getChildren());
            childListView.setAdapter(childListAdapter);
            childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position < category.getChildren().size()) {
                        CATEGORY item = category.getChildren().get(position);

                        try {
                            Intent intent = new Intent(CategoryChildActivity.this, GoodsListActivity.class);
                            FILTER filter = new FILTER();
                            filter.setCategory_id(String.valueOf(item.getId()));
                            intent.putExtra("filter", filter.toJson().toString());
                            intent.putExtra("search_content", category.getName());
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_right_in,
                                    R.anim.push_right_out);
                        } catch (JSONException e) {

                        }

                    }
                }
            });
        } catch (JSONException e) {

        }


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.search_search:
                break;
        }

    }

}
