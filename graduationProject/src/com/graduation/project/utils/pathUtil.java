package com.graduation.project.utils;

import com.graduation.project.fernflower.fernflowerUtil;
import com.graduation.project.loophole.loophole;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/5/005 19:22
 */
public class pathUtil {
    private static String apktoolPath = "G:\\apktool\\apktool.jar";
    private static String aaptPath = "G:\\apktool\\aapt.exe";
    private static String dex2jarPath = "G:\\apktool\\dex2jar\\d2j-dex2jar.bat";
    private static String fernflowerPath = "G:\\apktool\\fernflower.jar";
    private static String shellScanPath="G:\\apktool\\ShellScan.jar";
    private static String permissionXlsxPath="G:\\apktool\\permissions.xlsx";
    private static String loopholeXlsxPath="G:\\apktool\\loophole.xlsx";
    private static String apiXlsxPath="G:\\apktool\\api.xlsx";
    private static String smaliPath = "G:\\apktool\\smali.jar";
    private static String bakSmaliPath = "G:\\apktool\\bakSmali.jar";
    private static String apksignerPath="G:\\apktool\\apksigner.jar";
    private static String recompilePath = "G:\\apktool\\recompile\\";
    private static String decompilePath = "G:\\apktool\\decompile\\";
    private static String javaPath = "G:\\apktool\\java\\";
    private static String dexPath = "G:\\apktool\\dex\\";
    private static String zipPath = "G:\\apktool\\zip\\";
    private static String classPath = "G:\\apktool\\class\\";

    public static String getApiXlsxPath() {
        return apiXlsxPath;
    }

    public static String getLoopholeXlsxPath() {
        return loopholeXlsxPath;
    }

    private static String smaliDirPath = "G:\\apktool\\smali\\";

    public static String getApksignerPath() {
        return apksignerPath;
    }

    public static String getBakSmaliPath() {
        return bakSmaliPath;
    }

    public static String getSnaliDirPath() {
        if (!fileUtil.existsFile(smaliDirPath)) fileUtil.buildDir(smaliDirPath);
        return smaliDirPath;
    }

    public static String getSmaliPath() {
        return smaliPath;
    }

    public static String getPermissionXlsxPath() {
        return permissionXlsxPath;
    }

    public static String getShellScanPath() {
        return shellScanPath;
    }

    public static String getClassPath() {
        if (!fileUtil.existsFile(classPath)) fileUtil.buildDir(classPath);
        return classPath;
    }

    public static String getFernflowerPath() {
        return fernflowerPath;
    }

    public static String getZipPath() {
        if (!fileUtil.existsFile(zipPath)) fileUtil.buildDir(zipPath);
        return zipPath;
    }

    public static String getDexPath() {
        if (!fileUtil.existsFile(dexPath)) fileUtil.buildDir(dexPath);
        return dexPath;
    }

    public static String getJavaPath() {
        if (!fileUtil.existsFile(javaPath)) fileUtil.buildDir(javaPath);
        return javaPath;
    }

    public static String getRecompilePath(String name) {
        if (!fileUtil.existsFile(recompilePath)) fileUtil.buildDir(recompilePath);
        return recompilePath + name;
    }
    public static String getRecompilePath() {
        if (!fileUtil.existsFile(recompilePath)) fileUtil.buildDir(recompilePath);
        return recompilePath ;
    }


    public static String getDecompilePath(String name) {
        if (!fileUtil.existsFile(decompilePath)) fileUtil.buildDir(decompilePath);
        return decompilePath + name;
    }

    public static String getAaptPath() {
        return aaptPath;
    }

    public static String getApktoolPath() {
        return apktoolPath;
    }



    public static String getDex2jarPath() {
        return dex2jarPath;
    }
}
