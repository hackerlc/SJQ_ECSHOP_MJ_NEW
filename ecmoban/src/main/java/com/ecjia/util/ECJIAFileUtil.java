package com.ecjia.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by Adam on 2016/7/22.
 */
public class ECJIAFileUtil {

    public static void saveFile(String filePath, String data, String fileName) throws IOException {

        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(filePath + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(file2);
        PrintStream ps = new PrintStream(fos);
        ps.print(data);
        ps.close();
        fos.close();

    }


    public static String readFile(String filePath, String fileName) throws IOException {
        String path = filePath + "/" + fileName;
        File f1 = new File(path);
        String result = null;
        if (f1.exists()) {
            InputStream is = new FileInputStream(f1);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader bf = new BufferedReader(input);
            result = bf.readLine();
            bf.close();
            input.close();
            is.close();
        }
        return result;
    }
}
