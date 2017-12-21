package com.ecjia.view.activity.goodsdetail.fragment.interfaces;

/**
 * Created by Kevin Song on 2016/9/25.
 */
public interface TabsHelper {

    /**
     * 选中Item,这是点击item执行的方法
     *
     * @param tabId
     */
    void selectItem(int tabId);

    /**
     * 这个是选中item后的视图变化效果
     *
     * @param tabId
     */
    void setItemView(int tabId);

    /**
     * 这个初始化item的视图
     */
    void initItemView();

    /**
     * 这个是item选中后的逻辑变化执行的方法
     *
     * @param tabId
     */
    void tabChanged(int tabId);
}
