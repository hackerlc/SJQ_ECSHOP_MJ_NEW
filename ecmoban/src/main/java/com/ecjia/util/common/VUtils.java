package com.ecjia.util.common;

import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VUtils {

   /**
    * 软键盘显示 隐藏
    *
    * @param editText 触发的输入框
    * @param isShow   true is show
    */
   public static void showHideSoftInput(final EditText editText, boolean isShow) {
      if (editText == null) {
         return;
      }
      if (isShow) {
         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               editText.requestFocus();
               InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
               //imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            }
         }, 100);
      } else {
         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
         }, 100);
      }
   }

   interface keyBoardListener {
      void onKeyIsShow(boolean isShow);
   }

   public static boolean isMobileNO(String mobiles) {
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
      Pattern p = Pattern.compile("(1)\\d{10}$");
      Matcher m = p.matcher(mobiles);
      System.out.println(m.matches() + "---");
      return m.matches();
   }
}
