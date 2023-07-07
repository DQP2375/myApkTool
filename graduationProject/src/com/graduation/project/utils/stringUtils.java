package com.graduation.project.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 董琴平
 * @version 1.0
 * 字符串工具
 * @date 2021/1/18/018 9:13
 */
public class stringUtils {

    /**
     * 判断字符串是否为空 长度是否合法
     */
    public static boolean isEmpty(String str, int len) {
        return isEmpty(str) || str.length() < len;
    }

    public static boolean maxTolength(String str, int len) {
        return !isEmpty(str) && str.length() > len;
    }

    public static boolean equallength(String str, int len) {
        return !isEmpty(str) && str.length() == len;
    }

    public static boolean lengthInterval(String str, int minLen, int maxLen) {
        if (minLen < 1) maxLen = 1;
        return !isEmpty(str) && str.length() >= minLen && str.length() <= maxLen;
    }

    /**
     * 判断多个字符串是否为空 长度是否合法
     */
    public static boolean isEmpty(int len, String... str) {
        for (String s : str) {
            if (isEmpty(s, len)) return true;
        }
        return false;
    }

    /**
     * 判断多个字符串是否为空
     */
    public static boolean isEmpty(String... str) {
        for (String s : str) {
            if (isEmpty(s)) return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || str.isEmpty();
    }

    /**
     * object是否为空
     */
    public static boolean isEmpty(Object obj) {
        return null == obj;
    }


    /**
     * 检测邮箱地址是否合法
     *
     * @param email
     * @return true合法 false不合法
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email)) return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean verifyCodeEquals(String verifyCode, String userCode) {
        if (isEmpty(verifyCode) || isEmpty(userCode)) return false;
        return verifyCode.toUpperCase().equals(userCode.toUpperCase());
    }

    public static String apkNameillegalCharacter(String name) {
        if (name.contains("-")) {
            name = name.replace("-", "");
        }
        return name;
    }


    /**
     * 密码强度
     *
     * @return Z = 字母 S = 数字 T = 特殊字符
     */

	/*  一、假定密码字符数范围6-16，除英文数字和字母外的字符都视为特殊字符：
    弱：^[0-9A-Za-z]{6,16}$
    中：^(?=.{6,16})[0-9A-Za-z]*[^0-9A-Za-z][0-9A-Za-z]*$
    强：^(?=.{6,16})([0-9A-Za-z]*[^0-9A-Za-z][0-9A-Za-z]*){2,}$
    二、假定密码字符数范围6-16，密码字符允许范围为ASCII码表字符：
    弱：^[0-9A-Za-z]{6,16}$
    中：^(?=.{6,16})[0-9A-Za-z]*[\x00-\x2f\x3A-\x40\x5B-\xFF][0-9A-Za-z]*$
    强：^(?=.{6,16})([0-9A-Za-z]*[\x00-\x2F\x3A-\x40\x5B-\xFF][0-9A-Za-z]*){2,}$*/
    public static String checkPasswordComplexity(String passwordStr) {
        String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
        String regexZT = "\\D*";
        String regexST = "[\\d\\W]*";
        String regexZS = "\\w*";
        String regexZST = "[\\w\\W]*";

        if (passwordStr.matches(regexZ)) {
            return "弱";
        }
        if (passwordStr.matches(regexS)) {
            return "弱";
        }
        if (passwordStr.matches(regexT)) {
            return "弱";
        }
        if (passwordStr.matches(regexZT)) {
            return "中";
        }
        if (passwordStr.matches(regexST)) {
            return "中";
        }
        if (passwordStr.matches(regexZS)) {
            return "中";
        }
        if (passwordStr.matches(regexZST)) {
            return "强";
        }
        return passwordStr;

    }

    public static boolean easyPassword(String passwordStr) {
        return passwordStr.contains("弱");
    }

    /**
     * 获取左后一个出现的c字符后面的所有字符串
     */
    public static String getSuffix(String str, String c) {
        if (str == null) return null;
        int pos = str.lastIndexOf(c);
        return str.substring(pos + 1);
    }
}
