package com.ecjia.util;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ClassName;
import com.ecjia.consts.RequestCodes;
import com.ecjia.entity.responsemodel.FUNCTION;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adam on 2016-06-14.
 */
public class FunctionUtil {


    public static final String[] ALLFUNCTIONCODE = new String[]{
            "theme",
            "newgoods",
            "promotion",
            "mobilebuy",
            "groupbuy",
            "qrcode",
            "qrshare",
            "map",
            "todayhot",
            "message",
            "feedback"
    };

    public void setFunctions(HashMap<String, FUNCTION> functions) {
        instance.functions = functions;
    }

    private HashMap<String, FUNCTION> functions;


    public HashMap<String, FUNCTION> getFunctions() {
        return functions;
    }

    public FUNCTION getFunctionsByCode(String code) {

        return functions.get(code);
    }

    private FunctionUtil() {

    }

    static FunctionUtil instance;

    public static FunctionUtil getDefault() {
        if (instance == null) {
            synchronized (FunctionUtil.class) {
                if (instance == null) {
                    instance = new FunctionUtil();
                }
            }
        }
        return instance;
    }

    public void register() {
        functions = new HashMap<>();

        /**
         * 扫码
         */
        functions.put("qrcode", new FUNCTION("qrcode", R.drawable.icon_find_zxing_white, R.drawable
                .icon_find_zxing, R.string.function_qrcode, ClassName.ActivityName.QRCODE, false, true,0));

        /**
         * 推广
         */
        functions.put("qrshare", new FUNCTION("qrshare", R.drawable.icon_sliding_suggest, R.drawable
                .icon_find_qrcode_share, R
                .string.my_suggest, ClassName.ActivityName.QRSHARE, true, true, RequestCodes.MAIN_TO_SHARE));

        /**
         * 主题街
         */
        functions.put("theme", new FUNCTION("theme", R.drawable.icon_find_theme_white, R.drawable
                .icon_find_theme, R.string.function_toptic, ClassName.ActivityName.THEME, false, true,0));

        /**
         * 手机专享
         */
        functions.put("mobilebuy", new FUNCTION("mobilebuy", R.drawable.icon_find_mobile_buy_white, R.drawable.icon_find_mobile_buy,
                R.string.function_mobilebuy, ClassName.ActivityName.MOBILEBUY, false, true,0));

        /**
         * 促销商品
         */
        functions.put("promotion", new FUNCTION("promotion", R.drawable.icon_find_promotion_white, R.drawable.icon_find_promotion,
                R.string.function_promotion, ClassName.ActivityName.PROMOTIONAL, false, true,0));

        functions.put("newgoods", new FUNCTION("newgoods", R.drawable.icon_find_new_white, R.drawable
                .icon_find_new, R.string.newgoods, ClassName.ActivityName.NEWGOODS, false, true,0));

        // TODO: 2016/8/16
        /**
         * 限时团购
         */
        functions.put("groupbuy", new FUNCTION("groupbuy", R.drawable.icon_find_groupbuy_white, R.drawable.icon_find_groupbuy,
                R.string.function_groupbuy, ClassName.ActivityName.GROUPBUY, false, true,0));


        /**
         * 地图
         */
        functions.put("map", new FUNCTION("map", R.drawable.icon_find_map_white, R.drawable.icon_find_map, R.string
                .function_map, ClassName.ActivityName.MAP, false, true,0));
        /**
         * 今日热点
         */
        functions.put("todayhot", new FUNCTION("todayhot", R.drawable.icon_find_todayhot_white, R.drawable.icon_find_todayhot, R.string
                .function_todayhot, ClassName.ActivityName.TODAYHOT, false, true,0));
        /**
         * 消息中⼼
         */
        functions.put("message", new FUNCTION("message", R.drawable.icon_find_push_white, R.drawable.icon_find_push,
                R.string.function_message, ClassName.ActivityName.MESSAGE, false, true,0));
        /**
         * 客服中心
         */
        functions.put("feedback", new FUNCTION("feedback", R.drawable.icon_find_consult_white, R.drawable
                .icon_find_consult, R
                .string.function_feedback, ClassName.ActivityName.FEEDBACK, false, true,0));

    }

    public ArrayList<FUNCTION> getAllFunctions() {
        ArrayList<FUNCTION> functionses = new ArrayList<>();
        for (int i = 0; i < ALLFUNCTIONCODE.length; i++) {
            functionses.add(functions.get(ALLFUNCTIONCODE[i]));
        }
        return functionses;
    }

    public void unregister() {
        if (functions != null) {
            functions.clear();
            functions = null;
        }
    }
}
