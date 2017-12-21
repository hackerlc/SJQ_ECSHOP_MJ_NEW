package com.ecjia.entity.responsemodel;

import com.ecjia.consts.ClassName;

/**
 * Created by Adam on 2016-06-14.
 */
public class FUNCTION {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private int icon_white; //图片资源文件
    private int icon_colors;//彩图
    private int name;//功能名称资源
    ClassName.ActivityName action;//功能的事件
    boolean isNeedLogin;//是否需要登陆

    public int getLoginRequestcode() {
        return loginRequestcode;
    }

    public void setLoginRequestcode(int loginRequestcode) {
        this.loginRequestcode = loginRequestcode;
    }

    int loginRequestcode;

    boolean isSupport;

    public boolean isSupport() {
        return isSupport;
    }

    public void setIsSupport(boolean isSupport) {
        this.isSupport = isSupport;
    }

    public int getIcon_white() {
        return icon_white;
    }

    public void setIcon_white(int icon_white) {
        this.icon_white = icon_white;
    }

    public int getIcon_colors() {
        return icon_colors;
    }

    public void setIcon_colors(int icon_colors) {
        this.icon_colors = icon_colors;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public ClassName.ActivityName getAction() {
        return action;
    }

    public void setAction(ClassName.ActivityName action) {
        this.action = action;
    }

    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setIsNeedLogin(boolean isNeedLogin) {
        this.isNeedLogin = isNeedLogin;
    }

    public FUNCTION() {

    }

    /**
     * @param code             功能代号
     * @param icon_white       透明图标id
     * @param icon_colors      彩色图标id
     * @param name             功能名称id
     * @param action           需要去的页面枚举对象
     * @param isNeedLogin      是否需要登录
     * @param isSupport        是否是支持的功能
     * @param loginRequestcode 登录请求码
     */
    public FUNCTION(String code, int icon_white, int icon_colors, int name, ClassName.ActivityName action, boolean
            isNeedLogin,
                    boolean isSupport, int loginRequestcode) {
        this.code = code;
        this.icon_white = icon_white;
        this.icon_colors = icon_colors;
        this.name = name;
        this.action = action;
        this.isNeedLogin = isNeedLogin;
        this.isSupport = isSupport;
        this.loginRequestcode = loginRequestcode;
    }
}
