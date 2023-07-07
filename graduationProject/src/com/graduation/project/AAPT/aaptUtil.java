package com.graduation.project.AAPT;


import com.graduation.project.utils.ExcelUtil;
import com.graduation.project.utils.fileUtil;
import com.graduation.project.utils.pathUtil;
import com.graduation.project.utils.stringUtils;

import javax.lang.model.element.Name;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/1/21/021 16:37
 * apk工具类。封装了获取Apk信息的方法。
 * 包名/版本号/版本名/应用程序名/支持的android最低版本号/支持的SDK版本/建议的SDK版本/所需设备特性等
 * * aapt d strings *.apk ---> 打印的内容资源表字符串池APK文件。
 * * aapt d badging *.apk ---> 打印的标签和图标应用APK中声明。
 * * aapt d permissions *.apk ---> 从APK文件打印的权限
 * * aapt d resources *.apk ---> 从APK文件打印资源表。
 * * aapt d configurations *.apk ---> APK文件打印的配置。
 * * aapt d xmltree *.apk res/***.xml---> 以树形结构打印xml信息。
 * * aapt d xmlstrings *.apk res/***.xml---> 打印xml文件中所有的字符串信息。
 */

public class aaptUtil {
    public static final String package_Name = "name";
    public static final String VERSION_CODE = "versionCode";
    public static final String VERSION_NAME = "versionName";
    public static final String SDK_VERSION = "sdkVersion";
    public static final String min_Sdk_Version = "minSdkVersion";
    public static final String compile_Sdk_Version = "compileSdkVersion";
    public static final String compile_Sdk_Version_Codename = "compileSdkVersionCodename";
    public static final String TARGET_SDK_VERSION = "targetSdkVersion";
    public static final String USES_PERMISSION = "uses-permission";
    public static final String APPLICATION_LABEL = "application-label";
    public static final String APPLICATION_ICON = "application-icon";
    public static final String USES_FEATURE = "uses-feature";
    public static final String USES_IMPLIED_FEATURE = "uses-implied-feature";
    public static final String SUPPORTS_SCREENS = "supports-screens";
    public static final String SUPPORTS_ANY_DENSITY = "supports-any-density";
    public static final String DENSITIES = "densities";
    public static final String PACKAGE = "package";
    public static final String APPLICATION = "application:";
    public static final String LAUNCHABLE_ACTIVITY = "launchable-activity";
    private final ProcessBuilder mBuilder;
    private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";
    private static final String FEATURE_SPLIT_REGEX = "(:')|(',')|'";
    private String mAaptPath = pathUtil.getAaptPath();     // aapt所在的目录。

    public aaptUtil() {
        mBuilder = new ProcessBuilder();
        mBuilder.redirectErrorStream(true);
    }

    /**
     * 返回一个apk程序的信息。
     *
     * @param apkPath apk的路径。
     * @return apkInfo 一个Apk的信息。
     */
    public ApkInfo getApkInfo(String apkPath) throws Exception {
        //通过命令调用aapt工具解析apk文件
        if (!fileUtil.existsFile(apkPath)) System.out.println("文件不存在！");
        Process process = mBuilder.command(mAaptPath, "d", "badging", apkPath).start();
        InputStream is = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String tmp = br.readLine();
        try {
            if (tmp == null || !tmp.startsWith("package"))
                System.out.println("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "...");
            ApkInfo apkInfo = new ApkInfo();
            apkInfo.setSize(fileUtil.getFileSize(apkPath));
            do {
                setApkInfoProperty(apkInfo, tmp);
            } while ((tmp = br.readLine()) != null);
            return apkInfo;
        } finally {
            process.destroy();
            closeIO(is);
            closeIO(br);
        }
    }


    /**
     * 返回一个apk程序权限
     *
     * @param apkPath apk的路径。
     * @return apkInfo 一个Apk的信息。
     */
    public void getPermission(String apkPath) throws Exception {
        //通过命令调用aapt工具解析apk文件
        if (!fileUtil.existsFile(apkPath)) System.out.println("文件不存在！");
        Process process = mBuilder.command(pathUtil.getAaptPath(), "d", "permissions", apkPath).start();
        InputStream is = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String tmp = br.readLine();
        try {
            if (tmp == null || !tmp.startsWith("package"))
                System.out.println("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp + "...");
            int i = 0;
            while ((tmp = br.readLine()) != null) {
                ++i;
                permissionAnalysis(tmp, i);
            }
        } finally {
            process.destroy();
        }
    }


    /**
     * 设置APK的属性信息。
     *
     * @param apkInfo
     * @param source
     */
    private void setApkInfoProperty(ApkInfo apkInfo, String source) {
        while (source.startsWith(" ")) source = source.substring(1);
        if (source.startsWith(PACKAGE)) {
            splitPackageInfo(apkInfo, source);
        } else if (source.startsWith(LAUNCHABLE_ACTIVITY)) {
            apkInfo.setLaunchableActivity(getPropertyInQuote(source));
        } else if (source.startsWith(SDK_VERSION)) {
            apkInfo.setSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(TARGET_SDK_VERSION)) {
            apkInfo.setTargetSdkVersion(getPropertyInQuote(source));
        } else if (source.startsWith(USES_PERMISSION)) {
            apkInfo.addToUsesPermissions(getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION_LABEL)) {
            apkInfo.setApplicationLable(getPropertyInQuote(source)); //windows下获取应用名称
        } else if (source.startsWith(APPLICATION_ICON)) {
            apkInfo.addToApplicationIcons(getKeyBeforeColon(source), getPropertyInQuote(source));
        } else if (source.startsWith(APPLICATION)) {
            String[] rs = source.split("( icon=')|'");
            apkInfo.setApplicationIcon(rs[rs.length - 1]);
            apkInfo.setApplicationLable(rs[1]); //linux下获取应用名称
        } else if (source.startsWith(USES_FEATURE)) {
            apkInfo.addToFeatures(getPropertyInQuote(source));
        } else if (source.startsWith(USES_IMPLIED_FEATURE)) {
            apkInfo.addToImpliedFeatures(getFeature(source));
        }
    }

    private void permissionAnalysis(String source, int i) {
        String permission = getPropertyInQuote(source);
        if (stringUtils.isEmpty(permission, 10)) return;
        System.out.println(i + "\t权限：\t" + permission);
        String suffix = stringUtils.getSuffix(permission, ".");
        if (stringUtils.isEmpty(suffix)) return;
        ExcelUtil sheet1 = new ExcelUtil(pathUtil.getPermissionXlsxPath(), "Sheet");
        String cell3 = sheet1.getCellByCaseName(suffix, 1, 2);
        System.out.println("\t说明：\t" + cell3);
        String cell4 = sheet1.getCellByCaseName(suffix, 1, 3);
        if (cell4.contains("Dangerous"))
            System.out.println("\t类别：\t【" + cell4 + "】!");
        else System.out.println("\t类别：\t" + cell4);
        System.out.println();
    }

    private ImpliedFeature getFeature(String source) {
        String[] result = source.split(FEATURE_SPLIT_REGEX);
        ImpliedFeature impliedFeature = new ImpliedFeature(result[1], result[2]);
        return impliedFeature;
    }

    /**
     * 返回出格式为name: 'value'中的value内容。
     *
     * @param source
     * @return
     */
    private String getPropertyInQuote(String source) {
        if (stringUtils.isEmpty(source) || !source.contains("'")) return "";
        int index = source.indexOf("'") + 1;
        return source.substring(index, source.indexOf('\'', index));
    }


    /**
     * 返回冒号前的属性名称
     *
     * @param source
     * @return
     */
    private String getKeyBeforeColon(String source) {
        return source.substring(0, source.indexOf(':'));
    }

    /**
     * 分离出包名、版本等信息。
     *
     * @param apkInfo
     * @param packageSource
     */
    private void splitPackageInfo(ApkInfo apkInfo, String packageSource) {
        String[] packageInfo = packageSource.split(SPLIT_REGEX);
        // for (String s : packageInfo) System.out.println(s);
        for (int i = 0; i < packageInfo.length; ++i) {
            //     System.out.println(packageInfo.length + "-" + i + ":" + packageInfo[i]);
            if (packageInfo[i].startsWith(package_Name))
                apkInfo.setPackageName(packageInfo[++i]);
            else if (packageInfo[i].startsWith(VERSION_CODE))
                apkInfo.setVersionCode(packageInfo[++i]);
            else if (packageInfo[i].startsWith(VERSION_NAME))
                apkInfo.setVersionName(packageInfo[++i]);
            else if (packageInfo[i].contains(min_Sdk_Version))
                apkInfo.setMinSdkVersion(packageInfo[++i]);
            else if (packageInfo[i].startsWith(compile_Sdk_Version_Codename))
                apkInfo.setCompileSdkVersionCodename(packageInfo[++i]);
            else if (packageInfo[i].startsWith(compile_Sdk_Version))
                apkInfo.setCompileSdkVersion(packageInfo[++i]);

        }
    }

    /**
     * 释放资源。
     *
     * @param c 将关闭的资源
     */
    private final void closeIO(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getmAaptPath() {
        return mAaptPath;
    }

    public void setmAaptPath(String mAaptPath) {
        this.mAaptPath = mAaptPath;
    }


    /**
     * "出现第几次的位置
     */
    public int getCharacterPosition(String string, int x) {
        //这里是获取"\""符号的位置
        Matcher slashMatcher = Pattern.compile("'").matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == x) {
                break;
            }
        }
        return slashMatcher.start();
    }

    public String getCharByindex(String string, int start, int end) {
        int one = getCharacterPosition(string, start);
        int two = getCharacterPosition(string, end);
        return string.substring(one + 1, two);
    }

}