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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecjia.entity.responsemodel.HOTSEARCH;
import com.ecjia.view.adapter.adapter.MultiItemTypeAdapter;
import com.ecjia.view.adapter.search.SearchHotAdapter;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.network.netmodle.GoodsListModel;
import com.ecjia.widgets.dialog.MyDialog;
import com.ecjia.widgets.ToastView;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.view.adapter.SearchHistoryNewAdapter;
import com.ecjia.entity.responsemodel.FILTER;
import com.ecjia.util.CommonMethod;
import com.ecjia.util.EventBus.MyEvent;
import com.umeng.message.PushAgent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * 独立的搜索界面
 */

@SuppressLint("CommitPrefEdits")
public class SearchNewActivity extends BaseActivity implements OnClickListener {

    private EditText search_input;
    private TextView tv_search_cancel;
    private RelativeLayout rl_search;
    private LinearLayout ll_search_type, ll_type_goods, ll_type_sellers;
    private TextView tv_content_goods, tv_content_sellers;
    private FrameLayout search_null;
    private LinearLayout fl_search_top;
    private View banner_blank;
    private float y;
    FILTER filter = new FILTER();
    private GoodsListModel dataModel;

    //搜索历史
    private FrameLayout fl_search_notnull;
    private LinearLayout clean_seller_history, clean_good_history;
    private LinearLayout ll_good, ll_shop;
    private TextView tv_seller_history, tv_good_history;
    private ListView lv_seller_history, lv_good_history;
    private SearchHistoryNewAdapter adapterSeller;
    private SearchHistoryNewAdapter adapterGood;
    private List<String> listseller;
    private List<String> listgood;

    private RecyclerView mRecyclerView;
    private SearchHotAdapter searchHotAdapter;
    private List<HOTSEARCH> hotSearchList;

    // 标志位
    private boolean needRefresh = false;
    private MyDialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_search_new);
        PushAgent.getInstance(this).onAppStart();
        // 初始化
        hotSearchList = new ArrayList<>();
        searchHotAdapter = new SearchHotAdapter(mActivity, hotSearchList);
        initHistory();

        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        fl_search_top = (LinearLayout) findViewById(R.id.fl_search_top);
        search_input = (EditText) findViewById(R.id.et_search_input);
        search_input.setOnClickListener(this);
        tv_search_cancel = (TextView) findViewById(R.id.tv_search_cancel);
        tv_search_cancel.setOnClickListener(this);
        search_null = (FrameLayout) findViewById(R.id.search_null);

        banner_blank = (View) findViewById(R.id.banner_blank);
        banner_blank.setOnClickListener(this);

        ll_good = (LinearLayout) findViewById(R.id.ll_good);
        ll_shop = (LinearLayout) findViewById(R.id.ll_shop);

        ll_search_type = (LinearLayout) findViewById(R.id.ll_search_type);
        ll_type_goods = (LinearLayout) findViewById(R.id.ll_type_goods);
        ll_type_goods.setOnClickListener(this);
        ll_type_sellers = (LinearLayout) findViewById(R.id.ll_type_sellers);
        ll_type_sellers.setOnClickListener(this);

        tv_content_goods = (TextView) findViewById(R.id.tv_content_goods);

        tv_content_sellers = (TextView) findViewById(R.id.tv_content_sellers);

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
        InputMethodManager inputManager = (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(search_input, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(search_input, 0);
            }
        }, 998);

        search_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
                if (s.length() == 0) {
                    ll_search_type.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    ll_search_type.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                    tv_content_goods.setText(s);
                    tv_content_sellers.setText(s);
                }
            }
        });

        search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Resources resources = getBaseContext().getResources();
                String please_input = resources.getString(R.string.search_please_input);
                CloseKeyBoard();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputstr = search_input.getText().toString();
                    //为空时不能跳转
                    if (inputstr == null || "".equals(inputstr)) {
                        ToastView toast = new ToastView(SearchNewActivity.this, please_input);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Intent intent = new Intent(SearchNewActivity.this, SearchAllActivity.class);
                        intent.putExtra(IntentKeywords.KEYWORDS, inputstr);
                        startActivity(intent);
                        search_input.setText("");
                    }
                }
                return true;
            }

        });

    }

    private void initHistory() {
        listseller = CommonMethod.getInstance(SearchNewActivity.this).getSellersFromShared();
        listgood = CommonMethod.getInstance(SearchNewActivity.this).getGoodsFromShared();

        fl_search_notnull = (FrameLayout) findViewById(R.id.fl_search_notnull);

        tv_seller_history = (TextView) findViewById(R.id.tv_seller_history);
        tv_good_history = (TextView) findViewById(R.id.tv_good_history);

        clean_good_history = (LinearLayout) findViewById(R.id.clean_good_history);
        clean_good_history.setOnClickListener(this);
        clean_seller_history = (LinearLayout) findViewById(R.id.clean_seller_history);
        clean_seller_history.setOnClickListener(this);

        lv_good_history = (ListView) findViewById(R.id.lv_good_history);
        lv_seller_history = (ListView) findViewById(R.id.lv_seller_history);

        adapterSeller = new SearchHistoryNewAdapter(listseller, this);
        adapterGood = new SearchHistoryNewAdapter(listgood, this);

        adapterSeller.setListData(listseller);
        adapterGood.setListData(listgood);

        lv_good_history.setAdapter(adapterGood);
        lv_seller_history.setAdapter(adapterSeller);

        lv_good_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CloseKeyBoard();
                Intent intent = new Intent(SearchNewActivity.this, GoodsListActivity.class);
                try {
                    intent.putExtra("filter", filter.toJson().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra(IntentKeywords.KEYWORDS, adapterGood.list.get(position));
                startActivity(intent);
            }
        });

        lv_seller_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CloseKeyBoard();
                Intent intent = new Intent(SearchNewActivity.this, SearchSellerGoodsActivity.class);
                intent.putExtra(IntentKeywords.KEYWORDS, adapterSeller.list.get(position));
                intent.putExtra("searchseller", true);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            Resources resources = getBaseContext().getResources();
            String please_input = resources.getString(R.string.search_please_input);
            CloseKeyBoard();
            String inputstr = search_input.getText().toString();

            Intent intent = new Intent(SearchNewActivity.this, SearchAllActivity.class);
            //为空时不能跳转
            if (inputstr == null || "".equals(inputstr)) {
                ToastView toast = new ToastView(SearchNewActivity.this, please_input);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                intent.putExtra(IntentKeywords.KEYWORDS, inputstr);
                startActivity(intent);
                search_input.setText("");
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        String please_input = res.getString(R.string.search_please_input);
        String main_exit = res.getString(R.string.lastbrowse_delete);
        String main_exit_content = res.getString(R.string.lasebrowse_delete_sure);
        String main_exit_content2 = res.getString(R.string.lasebrowse_delete_sure2);
        switch (v.getId()) {
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
                    }
                });
                rl_search.startAnimation(animation);


                break;

            case R.id.ll_type_goods:
                CloseKeyBoard();
                String inputstr = search_input.getText().toString();
                CommonMethod.getInstance(SearchNewActivity.this).setGoodsToShared(inputstr);

                //为空时不能跳转
                if (inputstr == null || "".equals(inputstr)) {
                    ToastView toast = new ToastView(SearchNewActivity.this, please_input);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent intent = new Intent(SearchNewActivity.this, GoodsListActivity.class);
                    try {
                        intent.putExtra("filter", filter.toJson().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra(IntentKeywords.KEYWORDS, inputstr);
                    startActivity(intent);
                    search_input.setText("");
                }
                break;

            case R.id.ll_type_sellers:
                CloseKeyBoard();
                String inputstr2 = search_input.getText().toString();
                //为空时不能跳转
                if (inputstr2 == null || "".equals(inputstr2)) {
                    ToastView toast = new ToastView(SearchNewActivity.this, please_input);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent intent2 = new Intent(SearchNewActivity.this, SearchSellerGoodsActivity.class);
                    intent2.putExtra(IntentKeywords.KEYWORDS, inputstr2);
                    intent2.putExtra("searchseller", true);
                    startActivity(intent2);
                    search_input.setText("");
                }

                break;
            case R.id.clean_good_history:
                myDialog = new MyDialog(this, main_exit, main_exit_content);
                myDialog.show();
                myDialog.negative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.positive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                        SharedPreferences sahrePreferences = SearchNewActivity.this.getSharedPreferences(
                                "my_good_shared", 0);
                        Editor editor = sahrePreferences.edit();
                        editor.clear();
                        editor.commit();
                        CloseKeyBoard();
                        refreshUI();
                    }
                });


                break;
            case R.id.clean_seller_history:
                myDialog = new MyDialog(this, main_exit, main_exit_content2);
                myDialog.show();
                myDialog.negative.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();

                    }
                });
                myDialog.positive.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                        SharedPreferences sahrePreferences2 = SearchNewActivity.this.getSharedPreferences(
                                "my_seller_shared", 0);
                        Editor editor2 = sahrePreferences2.edit();
                        editor2.clear();
                        editor2.commit();
                        CloseKeyBoard();
                        refreshUI();
                    }
                });


                break;

            case R.id.banner_blank:
                search_input.setText("");
                break;

            default:
                break;
        }

    }

    private void refreshUI() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) search_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(search_input, 0);
            }
        }, 600);

        // 更新数据
        listseller = CommonMethod.getInstance(SearchNewActivity.this).getSellersFromShared();
        listgood = CommonMethod.getInstance(SearchNewActivity.this).getGoodsFromShared();

        if (null != tv_good_history) {
            tv_good_history.setText("(" + listgood.size() + res.getString(R.string.have_history) + ")");
        }
        if (null != tv_seller_history) {
            tv_seller_history.setText("(" + listseller.size() + res.getString(R.string.have_history) + ")");
        }

        if (null != adapterSeller) {
            adapterSeller.setListData(listseller);
            adapterSeller.notifyDataSetChanged();
        }
        if (null != adapterGood) {
            adapterGood.setListData(listgood);
            adapterGood.notifyDataSetChanged();
        }

        setListViewHeightBasedOnChildren(lv_good_history);
        setListViewHeightBasedOnChildren(lv_seller_history);

        // 做一些显示隐藏的判断
        if ((listseller == null || listseller.size() == 0) && (listgood == null || listgood.size() == 0)) {
            fl_search_notnull.setVisibility(View.GONE);
            search_null.setVisibility(View.VISIBLE);
        } else {
            if (listseller == null || listseller.size() == 0) {
                ll_shop.setVisibility(View.GONE);
            } else {
                ll_shop.setVisibility(View.VISIBLE);
            }
            if (listgood == null || listgood.size() == 0) {
                ll_good.setVisibility(View.GONE);
            } else {
                ll_good.setVisibility(View.VISIBLE);
            }

            fl_search_notnull.setVisibility(View.VISIBLE);
            search_null.setVisibility(View.GONE);
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
            return false;
        }
        return true;
    }


    public void onEvent(MyEvent event) {
        if (event.getMsg().equals("search_back")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        closeKeyBoard(search_input);
    }
}
