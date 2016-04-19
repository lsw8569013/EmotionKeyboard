package com.example.sunxiaodong.emotionkeyboard.utils;

import android.os.Environment;

/**
 * Created by sunxiaodong on 16/4/15.
 */
public class FilePathUtil {

    private static final String SDCARD = Environment.getExternalStorageDirectory().getPath();//SD卡

    private static final String IYOURCAR = SDCARD + "/emotionkeyboard";

    //传输图片存储路径
    public static final String IMAGE = IYOURCAR + "/image/";
    public static final String IMAGE_CACHE = IMAGE + "/cache/";

}
