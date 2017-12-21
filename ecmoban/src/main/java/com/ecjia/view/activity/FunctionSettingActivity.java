package com.ecjia.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.widgets.DragGridView;
import com.ecjia.widgets.ECJiaTopView;
import com.ecjia.entity.responsemodel.FUNCTION;
import com.ecjia.util.FunctionUtil;
import com.ecjia.util.LG;
import com.umeng.message.PushAgent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by Adam on 2016-06-14.
 */
public class FunctionSettingActivity extends BaseActivity implements DragGridView.TagPositionListener, DragGridView.OnRearrangeListener {

    private SharedPreferences spf;
    private ArrayList<FUNCTION> supprotFunctions = new ArrayList<>();
    private ArrayList<FUNCTION> unsupportFunctions = new ArrayList<>();


    private DragGridView mDragView;
    ArrayList<FUNCTION> functionHashMap = new ArrayList<>();

    int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_functionsetting);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        FunctionUtil.getDefault().register();
        initView();
        initData();
        addData();
        initViewGroupItem();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        spf = getSharedPreferences("function_setting", 0);
        supprotFunctions.clear();
        if (spf.getBoolean("isfirst", true)) {
            addFirstData();
            LG.i("isfirst" + "true");
            spf.edit().putBoolean("isfirst", false).commit();
        } else {
            LG.i("isfirst" + "false");
            JSONArray array = null;
            try {
                array = new JSONArray(spf.getString("support", new JSONArray().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /**
             * 加载支持的功能的集合
             */
            LG.i("array == " + array.toString());


            String[] supports = new String[]{
                    "newgoods",
                    "promotion",
                    "mobilebuy",
                    "groupbuy",
                    "todayhot",
                    "message",
                    "feedback",
                    "map"};

            functionHashMap.clear();
            for (int i = 0; i < supports.length; i++) {
                functionHashMap.add(FunctionUtil.getDefault().getFunctions().get(supports[i]));
            }

            if (array != null) {

                /**
                 * 支持的功能顺序列表
                 */
                for (int i = 0; i < array.length(); i++) {
                    String code = array.opt(i).toString();
                    for (int j = 0; j < functionHashMap.size(); j++) {
                        if (functionHashMap.get(j).getCode().equals(code)) {
                            functionHashMap.get(j).setIsSupport(true);
                            supprotFunctions.add(functionHashMap.get(j));
                            break;
                        }
                    }
                }

                /**
                 * 不支持的功能列表
                 */
                for (int i = 0; i < functionHashMap.size(); i++) {
                    String code = functionHashMap.get(i).getCode();
                    boolean support = false;
                    for (int j = 0; j < array.length(); j++) {
                        if (code.equals(array.opt(j).toString())) {
                            support = true;
                            break;
                        }
                    }
                    if (!support) {
                        functionHashMap.get(i).setIsSupport(false);
                        unsupportFunctions.add(functionHashMap.get(i));
                    }
                }
            }
        }
    }

    void addData() {
        functionHashMap.clear();
        for (int i = 0; i < supprotFunctions.size(); i++) {
            functionHashMap.add(supprotFunctions.get(i));
        }
        functionHashMap.add(new FUNCTION("tag", 0, 0, 0, null, false, false,0));
        for (int i = 0; i < unsupportFunctions.size(); i++) {
            functionHashMap.add(unsupportFunctions.get(i));
        }
        tagPosition = supprotFunctions.size();
    }

    private void initViewGroupItem() {

        mDragView.removeAllViews();
        for (int position = 0; position < functionHashMap.size(); position++) {
            if (functionHashMap.get(position).getCode().equals("tag")) {
                View text = LayoutInflater.from(this).inflate(R.layout.function_setting_text, null);
                text.setTag("tag");
                if (position == functionHashMap.size() - 1) {
                    text.findViewById(R.id.barring).setVisibility(View.INVISIBLE);
                } else {
                    text.findViewById(R.id.barring).setVisibility(View.VISIBLE);
                }
                mDragView.addView(text);
            } else {
                View convertView = LayoutInflater.from(this).inflate(R.layout.item_function_setting, null);
                View support = convertView.findViewById(R.id.support);
                View unsupport = convertView.findViewById(R.id.unsupport);

                ImageView image = (ImageView) convertView.findViewById(R.id.icon);
                TextView name = (TextView) convertView.findViewById(R.id.name);
                ImageView checkBox = (ImageView) convertView.findViewById(R.id.check);
                ImageView checkBox_false = (ImageView) convertView.findViewById(R.id.check_false);
                View middleline = convertView.findViewById(R.id.middle_line);

                final int i = position;
                image.setImageResource(functionHashMap.get(i).getIcon_colors());
                name.setText(functionHashMap.get(i).getName());
                checkBox.setSelected(functionHashMap.get(i).isSupport());

                if (functionHashMap.get(position).isSupport()) {
                    support.setVisibility(View.VISIBLE);
                    unsupport.setVisibility(View.GONE);
                } else {
                    support.setVisibility(View.GONE);
                    unsupport.setVisibility(View.VISIBLE);
                }
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int oldPosition = 0;
                        int tagPosition = FunctionSettingActivity.this.tagPosition;
                        for (int i = 0; i < functionHashMap.size(); i++) {
                            if (functionHashMap.get(i).getCode().equals(((View) v.getParent().getParent().getParent().getParent()).getTag())) {
                                oldPosition = i;
                                break;
                            }
                        }
                        LG.i("view.tag:" + ((View) v.getParent().getParent().getParent().getParent()).getTag() + " oldPosition:" + oldPosition + "  tagPosition:" + tagPosition);
                        if (tagPosition > oldPosition) {
                            FUNCTION function = functionHashMap.remove(oldPosition);
                            function.setIsSupport(false);
                            functionHashMap.add(function);
                        } else {
                            FUNCTION function = functionHashMap.remove(oldPosition);
                            function.setIsSupport(true);
                            functionHashMap.add(tagPosition, function);
                        }
                        initViewGroupItem();
                        mHandler.postDelayed(mRunnable, 350);
                    }
                });

                checkBox_false.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int oldPosition = 0;
                        int tagPosition = FunctionSettingActivity.this.tagPosition;
                        for (int i = 0; i < functionHashMap.size(); i++) {
                            if (functionHashMap.get(i).getCode().equals(((View) v.getParent().getParent().getParent().getParent()).getTag())) {
                                oldPosition = i;
                                break;
                            }
                        }
                        LG.i("view.tag:" + ((View) v.getParent().getParent().getParent().getParent()).getTag() + " oldPosition:" + oldPosition + "  tagPosition:" + tagPosition);
                        if (tagPosition > oldPosition) {
                            FUNCTION function = functionHashMap.remove(oldPosition);
                            function.setIsSupport(false);
                            functionHashMap.add(function);
                        } else {
                            FUNCTION function = functionHashMap.remove(oldPosition);
                            function.setIsSupport(true);
                            functionHashMap.add(tagPosition, function);
                        }
                        initViewGroupItem();
                        mHandler.postDelayed(mRunnable, 350);
                    }
                });
                convertView.setTag(functionHashMap.get(i).getCode());
                mDragView.addView(convertView);
            }
        }
    }

    /**
     * 首次进来刷新布局的
     */
    void resetLine() {

        for (int position = 0; position < functionHashMap.size(); position++) {

            if (position == tagPosition) {

            } else {
                if (tagPosition == 0) {
                    if (position == functionHashMap.size() - 1) {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.GONE);
                    } else {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
                    }
                } else if (tagPosition == functionHashMap.size() - 1) {
                    if (position == tagPosition - 1) {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.GONE);
                    } else {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
                    }
                } else {
                    if (position == tagPosition - 1) {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.GONE);
                        LG.i("gone");
                    } else if (position == functionHashMap.size() - 1) {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.GONE);
                    } else {
                        mDragView.getChildAt(position).findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


    private void initView() {
        initTopView();
        mDragView = (DragGridView) findViewById(R.id.dragviewgroup);
        mDragView.setOnDataExchangeListener(new DragGridView.OnDataExchangeListener() {
            @Override
            public void exchange(int from, int to) {
                mHandler.postDelayed(mRunnable, 100);
            }
        });
        mDragView.setOnTagPositionListener(this);
        mDragView.setOnRearrangeListener(this);
    }

    @Override
    public void initTopView() {
        super.initTopView();
        topView = (ECJiaTopView) findViewById(R.id.functionsetting_topview);
        topView.setLeftBackImage(BACKIMAGE_ID, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topView.setTitleText(R.string.function_manage);
    }


    /**
     * 用来刷新布局的
     */
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            LG.i("functionHashMap.size()=" + functionHashMap.size());
            int flag = 0;
            for (int i = 0; i < functionHashMap.size(); i++) {
                if (functionHashMap.get(i).getCode().equals("tag")) {
                    flag = i;
                }
            }
            if (flag == 0) {
                mDragView.getChildAt(flag).findViewById(R.id.barring).setVisibility(View.VISIBLE);
                for (int i = 0; i < functionHashMap.size(); i++) {
                    if (i != flag) {
                        if (i == functionHashMap.size() - 1) {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.GONE);
                        } else {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (flag == functionHashMap.size() - 1) {
                mDragView.getChildAt(flag).findViewById(R.id.barring).setVisibility(View.INVISIBLE);
                for (int i = 0; i < functionHashMap.size(); i++) {
                    if (i != flag) {
                        if (i == tagPosition - 1) {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.GONE);
                        } else {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else {
                mDragView.getChildAt(flag).findViewById(R.id.barring).setVisibility(View.VISIBLE);
                for (int i = 0; i < functionHashMap.size(); i++) {
                    if (i != flag) {
                        if (i == tagPosition - 1) {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.GONE);
                        } else if (i == functionHashMap.size() - 1) {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.GONE);
                        } else {
                            mDragView.getChildAt(i).findViewById(R.id.middle_line).setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    };


    @Override
    protected void onPostResume() {
        super.onPostResume();
        resetLine();
    }

    @Override
    protected void onPause() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < functionHashMap.size(); i++) {
            if (functionHashMap.get(i).getCode().equals("tag")) {

            } else {
                if (functionHashMap.get(i).isSupport()) {
                    array.put(functionHashMap.get(i).getCode());
                }
            }
        }
        LG.i("array 存==" + array.toString());
        spf.edit().putString("support", array.toString()).commit();
        super.onPause();
    }

    /**
     * 首次进入页面，需要初始化数据
     */
    void addFirstData() {
        String[] supports = new String[]{
                "newgoods",
                "promotion",
                "mobilebuy",
                "groupbuy",
                "todayhot",
                "message",
                "feedback",
                "map"};

        for (int i = 0; i < supports.length; i++) {
            supprotFunctions.add(FunctionUtil.getDefault().getFunctions().get(supports[i]));
        }
    }

    @Override
    protected void onDestroy() {
        FunctionUtil.getDefault().unregister();
        super.onDestroy();
    }

    int tagPosition = -1;//不包括这一条数据的位置

    @Override
    public void getTagPosition(int position) {
        this.tagPosition = position;
        LG.i("tagPosition:" + tagPosition);
    }


    @Override
    public void onRearrange(int oldIndex, int newIndex) {
        if (oldIndex == newIndex) {
            return;
        } else if (mDragView.getChildAt(newIndex).getTag().equals("tag")) {
            FUNCTION word = functionHashMap.remove(oldIndex);
            if (oldIndex < newIndex) {
                word.setIsSupport(false);
                functionHashMap.add(newIndex, word);
            } else {
                word.setIsSupport(true);
                functionHashMap.add(newIndex, word);
            }
            if (word.isSupport()) {
                mDragView.getChildAt(oldIndex).findViewById(R.id.support).setVisibility(View.VISIBLE);
                mDragView.getChildAt(oldIndex).findViewById(R.id.unsupport).setVisibility(View.GONE);
            } else {
                mDragView.getChildAt(oldIndex).findViewById(R.id.support).setVisibility(View.GONE);
                mDragView.getChildAt(oldIndex).findViewById(R.id.unsupport).setVisibility(View.VISIBLE);
            }
        } else {
            FUNCTION word = functionHashMap.remove(oldIndex);
            if ((oldIndex - tagPosition) * (newIndex - tagPosition) > 0) {
                functionHashMap.add(newIndex, word);
            } else {
                if (oldIndex < newIndex) {
                    word.setIsSupport(false);
                    functionHashMap.add(newIndex, word);
                } else {
                    word.setIsSupport(true);
                    functionHashMap.add(newIndex, word);
                }
            }
            if (word.isSupport()) {
                mDragView.getChildAt(oldIndex).findViewById(R.id.support).setVisibility(View.VISIBLE);
                mDragView.getChildAt(oldIndex).findViewById(R.id.unsupport).setVisibility(View.GONE);
            } else {
                mDragView.getChildAt(oldIndex).findViewById(R.id.support).setVisibility(View.GONE);
                mDragView.getChildAt(oldIndex).findViewById(R.id.unsupport).setVisibility(View.VISIBLE);
            }
        }

    }
}
