package com.ecjia.base;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

/**
 * activit 管理
 *
 * @author DD
 */
public class ActivityManager {

    private static ActivityManager manager;
    private Stack<Activity> activityStack;

    public static ActivityManager getManager() {
        if (manager == null) {
            synchronized (ActivityManager.class) {
                if (manager == null) {
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    /**
     * 获取activityStack
     *
     * @return
     */
    private Stack<Activity> getActivityStack() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return activityStack;
    }

    private boolean isActivity(Activity activity) {
        return activity != null;
    }

    /**
     * 获取下一个activity
     *
     * @param mActivityStack
     * @return
     */
    private Activity ActivityStackLastElement(Stack<Activity> mActivityStack) {
        try {
            return mActivityStack.lastElement();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 添加
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        getActivityStack().add(activity);
    }

    /**
     * 获取当前activity
     *
     * @return
     */
    public Activity getActivity() {
        Activity activity = ActivityStackLastElement(getActivityStack());
        if (isActivity(activity)) {
            return activity;
        }
        return null;
    }

    /**
     * 关闭当前界面
     */
    public void finish() {
        Activity activity = ActivityStackLastElement(getActivityStack());
        if (isActivity(activity)) {
            finish(activity);
        }
    }

    /**
     * 关闭指定界面
     *
     * @param activity 要关闭的Activity
     */
    public void finish(Activity activity) {
        if (activity != null) {
            activity.finish();
            getActivityStack().remove(activity);
            activity = null;
        }
    }

    /**
     * 逐层关闭到指定界面
     *
     * @param cls
     */
    public void finishExceptOne(Class cls) {
        while (true) {
            Activity activity = ActivityStackLastElement(getActivityStack());
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            finish(activity);
        }
    }

    /**
     * 逐层关闭退出APP
     *
     * @param
     */
    public void exit() {
        while (true) {
            Activity activity = ActivityStackLastElement(getActivityStack());
            if (activity == null) {
                break;
            }
            finish(activity);
        }
    }

    public void finishOthers(Class<?> cls) {
        try {
            Iterator itr = activityStack.iterator();
            while (itr.hasNext()) {
                Activity activity = (Activity) itr.next();
                if (!activity.getClass().equals(cls)) {
                    activity.finish();
                    activityStack.remove(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            Iterator itr = activityStack.iterator();
            while (itr.hasNext()) {
                Activity activity = (Activity) itr.next();
                if (activity.getClass().equals(cls)) {
                    activity.finish();
                    activityStack.remove(activity);
                }
                activity = null;
            }
            itr = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
