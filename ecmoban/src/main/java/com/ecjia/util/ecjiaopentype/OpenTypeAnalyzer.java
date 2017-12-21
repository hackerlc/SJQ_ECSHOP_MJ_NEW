package com.ecjia.util.ecjiaopentype;


import java.util.ArrayList;

/**
 * Created by Adam on 2016/7/12.
 */
public class OpenTypeAnalyzer {

    /***
     * 分解openType
     *
     * @param url 指的是原始的字符串
     *            head 一般指标识位，如 “ecjiaopen://app”，用来判断类型
     *            arg0 一般指一级分隔符，如“&”
     *            arg1 一般指二级分隔符，如“=”
     * @return 返回String[] 存储openType 以及所带的参数
     * @throws Exception
     */
    public static ArrayList<String[]> initOpenType(String url, String head, String arg0, String arg1) throws Exception {
        String paramaString = null;
        if (url.indexOf(head) == 0) {
            paramaString = url.replace(head, "");
        }
        return initOpenType(paramaString, arg0, arg1);
    }


    /***
     * 分解openType
     *
     * @param paramaString 指的是原始的字符串
     *                     arg0 一般指一级分隔符，如“&”
     *                     arg1 一般指二级分隔符，如“=”
     * @return 返回String[] 存储openType 以及所带的参数
     * @throws Exception
     */
    private static ArrayList<String[]> initOpenType(String paramaString, String arg0, String arg1) throws Exception {

        if (paramaString == null || arg0 == null || arg1 == null) {
            return null;
        }

        String[] paramas = paramaString.split(arg0);
        ArrayList<String[]> array = null;

        if (paramas.length > 0) {
            array = new ArrayList<>();
            for (int i = 0; i < paramas.length; i++) {
                String[] paramas2 = paramas[i].split(arg1);
                if (paramas2 != null && paramas2.length == 2) {
                    array.add(paramas2);
                }
            }
        }

        return array;
    }

}
