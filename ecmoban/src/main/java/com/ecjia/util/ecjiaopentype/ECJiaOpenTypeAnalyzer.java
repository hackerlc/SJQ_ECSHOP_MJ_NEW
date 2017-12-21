package com.ecjia.util.ecjiaopentype;

import java.util.ArrayList;

/**
 * Created by Adam on 2016/8/11.
 */
public class ECJiaOpenTypeAnalyzer {

    /**
     * 由于问题很复杂，暂时先处理2层关系的数据吧
     *
     * @param opentypeString
     * @return
     */
    public static ArrayList<String[]> analyze(String opentypeString) throws Exception {
        return OpenTypeAnalyzer.initOpenType(opentypeString, "ecjiaopen://app?", "&", "=");
    }
}
