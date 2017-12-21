package com.ecjia.widgets.home;

import android.app.Activity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Adam on 2016-06-23.
 */
public abstract class HomeView<T> {

    protected Activity mActivity;

    protected int width;

    public ArrayList<T> mDataList;

    public HomeView(Activity activity) {
        this.mActivity = activity;
        width = getDisplayMetricsWidth();
        init();
    }

    protected void init() {

    }

    /**
     * 设置数据的显示还是不现实
     */
    public abstract void setVisible();

    /**
     * 添加到listview中
     *
     * @param listView
     */
    public abstract void addToListView(ListView listView);

    /**
     * 创建视图
     */
    public abstract void createView(ArrayList<T> dataList);

    /**
     * 获取屏幕宽度
     */
    public int getDisplayMetricsWidth() {
        int i = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        int j = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

}
