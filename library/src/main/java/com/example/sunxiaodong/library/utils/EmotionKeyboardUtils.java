package com.example.sunxiaodong.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sunxiaodong on 16/4/13.
 */
public class EmotionKeyboardUtils {

    private static final String DEF_KEYBOARD_HEIGHT_KEY = "def_keyboard_height_key";
    private static final int DEF_KEYBOARD_HEIGHT_DP = 300;
    private static int sDefKeyboardHeight = -1;

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getDefKeyboardHeight(Context context) {
        if (sDefKeyboardHeight < 0) {
            sDefKeyboardHeight = dip2px(context, DEF_KEYBOARD_HEIGHT_DP);
        }
        int height = PreferenceManager.getDefaultSharedPreferences(context).getInt(DEF_KEYBOARD_HEIGHT_KEY, 0);
        return sDefKeyboardHeight = height > 0 && sDefKeyboardHeight != height ? height : sDefKeyboardHeight;
    }

    public static void setDefKeyboardHeight(Context context, int height) {
        if (sDefKeyboardHeight != height) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(DEF_KEYBOARD_HEIGHT_KEY, height).commit();
            sDefKeyboardHeight = height;
        }
    }

    /**
     * 开启软键盘
     */
    public static void openSoftKeyboard(EditText et) {
        if (et != null) {
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
        }
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context context) {
        if (context == null || !(context instanceof Activity) || ((Activity) context).getCurrentFocus() == null) {
            return;
        }
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getFontHeight(TextView textView) {
        Paint paint = new Paint();
        paint.setTextSize(textView.getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top);
    }
}
