package com.ecjia.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.entity.responsemodel.HOTSEARCH;
import com.ecjia.network.netmodle.GoodsListModel;
import com.ecjia.util.CommonMethod;
import com.ecjia.view.adapter.SearchHistoryAdapter;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.view.adapter.search.SearchHotAdapter;
import com.ecjia.widgets.ToastView;
import com.ecmoban.android.sijiqing.R;
import com.umeng.message.PushAgent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 独立的搜索界面
 */

@SuppressLint("CommitPrefEdits")
public class SearchActivity extends BaseActivity implements OnClickListener {

    private ImageView search_back;
    private EditText search_input;
    private TextView tv_search_cancel, search_history;
    private TextView clear_history;
    private ListView lv_history;
    private LinearLayout rl_search;
    private LinearLayout layout_search, clear_history_layout;
    private FrameLayout search_null, fl_search_top;
    private SearchHistoryAdapter adapter;
    private List<String> list;
    private float y;
    FILTER filter = new FILTER();
    private GoodsListModel dataModel;
    // 标志位
    private boolean needRefresh = false;
    private RecyclerView mRecyclerView;
    private SearchHotAdapter searchHotAdapter;
    private List<HOTSEARCH> hotSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        PushAgent.getInstance(this).onAppStart();
        hotSearchList = new ArrayList<>();
        searchHotAdapter = new SearchHotAdapter(mActivity, hotSearchList);
        // 初始化
        rl_search = (LinearLayout) findViewById(R.id.rl_search);
        fl_search_top = (FrameLayout) findViewById(R.id.fl_search_top);
        search_input = (EditText) findViewById(R.id.et_search_input);
        search_input.setOnClickListener(this);
        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);
        clear_history = (TextView) findViewById(R.id.clear_history);
        clear_history.setOnClickListener(this);
        search_history = (TextView) findViewById(R.id.tv_search_history);
        lv_history = (ListView) findViewById(R.id.lv_history);
        adapter = new SearchHistoryAdapter(list, this);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);
        clear_history_layout = (LinearLayout) findViewById(R.id.clear_history_layout);
        search_null = (FrameLayout) findViewById(R.id.search_null);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(searchHotAdapter);

        searchHotAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<HOTSEARCH>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                try {
                    Intent intent = new Intent(mActivity, GoodsListActivity.class);
                    intent.putExtra("filter", filter.toJson().toString());
                    intent.putExtra(IntentKeywords.KEYWORDS, hotSearchList.get(position).getName());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        search_input.setFocusable(true);
        search_input.setFocusableInTouchMode(true);
        search_input.requestFocus();

        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Resources resources = getBaseContext().getResources();
                String please_input = resources.getString(R.string.search_please_input);
                CloseKeyBoard();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputstr = search_input.getText().toString();
                    CommonMethod.getInstance(SearchActivity.this).setSeachStringToShared(inputstr);
                    list = CommonMethod.getInstance(SearchActivity.this).getSeachStringFromShared();

                    Intent intent = new Intent(SearchActivity.this, GoodsListActivity.class);
                    //为空时不能跳转
                    if (inputstr == null || "".equals(inputstr)) {
                        ToastView toast = new ToastView(SearchActivity.this, please_input);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        try {
                            intent.putExtra("filter", filter.toJson().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra(IntentKeywords.KEYWORDS, inputstr);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });


        // 刷新适配器前面调setListData（）吧适配器里面的list值改变了 然后刷新一下
        list = CommonMethod.getInstance(this).getSeachStringFromShared();

        adapter.setListData(list);
        adapter.notifyDataSetChanged();
        lv_history.setAdapter(adapter);


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        Resources resources = getBaseContext().getResources();
        switch (v.getId()) {
            case R.id.search_back:
                CloseKeyBoard();
                finish();
                break;
            case R.id.clear_history:
                adapter.setListData(null);
                SharedPreferences sahrePreferences = this.getSharedPreferences("my_shared", 0);
                Editor editor = sahrePreferences.edit();
                editor.clear();
                editor.commit();
                adapter.notifyDataSetChanged();
                CloseKeyBoard();
                clear_history_layout.setVisibility(View.GONE);
                search_history.setVisibility(View.GONE);
                layout_search.setVisibility(View.GONE);
                search_null.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_search_cancel:
                CloseKeyBoard();
                y = fl_search_top.getY();
                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -y);
                animation.setDuration(300);
                animation.setFillAfter(true);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        finish();
                        overridePendingTransition(R.anim.animation_4, 0);

                    }
                });
                rl_search.startAnimation(animation);


                break;

            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_input, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(search_input, 0);
            }
        }, 600);

        // 更新数据
        if (needRefresh) {
            list = CommonMethod.getInstance(this).getSeachStringFromShared();
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        }
        // 做一些显示隐藏的判断
        if (list == null || list.size()<=0) {
            clear_history_layout.setVisibility(View.GONE);
            search_history.setVisibility(View.GONE);
            layout_search.setVisibility(View.GONE);
            search_null.setVisibility(View.VISIBLE);
        } else {
            clear_history_layout.setVisibility(View.VISIBLE);
            search_history.setVisibility(View.VISIBLE);
            layout_search.setVisibility(View.VISIBLE);
            search_null.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        needRefresh = true;
    }


    // 关闭键盘
    public void CloseKeyBoard() {
        search_input.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_input.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            CloseKeyBoard();
            finish();
            overridePendingTransition(R.anim.animation_4, 0);
            return false;
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

}
