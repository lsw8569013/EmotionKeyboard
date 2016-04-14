package com.example.sunxiaodong.library.utils;

import android.content.Context;
import android.preference.PreferenceManager;

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

}
