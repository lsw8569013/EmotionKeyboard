package com.example.sunxiaodong.library.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <br>类描述:用正则表达式判断字串格式工具类
 * 
 * @author  xiaodong
 * @date  [2015-3-21]
 */
public class RegexFormatUtil {

    //判断手机格式是否正确
    public static boolean isPhoneNumber(String phone) {
    	Pattern p = Pattern.compile("^((1[3,8][0-9])|(14[5,7])|(15[^4,\\D])|(17[0,6-8]))\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    //判断是否全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * <br>功能简述: 判断输入密码是否符合密码格式
     * <br>注意:密码格式：包含A-Z,a-z,0-9
     * @param str
     */
    public static boolean isPassword(String str) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]{6,}");
        Matcher isPassword = pattern.matcher(str);
        return isPassword.matches();
    }
    
    /**
     * <br>功能简述:是否是url
     * @param str
     */
    public static boolean isUrl(String str) {
        boolean ret = false;
        if (str == null || TextUtils.isEmpty(str)) {
            return ret;
        }
        String regex = "^((https|http|ftp)?://)" + ".*";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(str);
        final boolean isUrl = mat.find();
        if (isUrl) {
            ret = true;
        }
        return ret;
    }
    
}
