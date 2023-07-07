package com.graduation.project;

import com.graduation.project.AAPT.ApkInfo;
import com.graduation.project.AAPT.aaptUtil;
import com.graduation.project.APKTool.ApkToolUtils;
import com.graduation.project.api.apiUtil;
import com.graduation.project.apksigner.apksignerUtil;
import com.graduation.project.bakSmali.bakSmaliUtil;
import com.graduation.project.cmd.SysShell;
import com.graduation.project.dex2jar.dex2jarUtil;
import com.graduation.project.fernflower.ZipUtils;
import com.graduation.project.fernflower.fernflowerUtil;
import com.graduation.project.loophole.loopholeUtil;
import com.graduation.project.shell.shellUtil;
import com.graduation.project.smali.smaliUtil;
import com.graduation.project.utils.*;

import java.io.*;
import java.util.Scanner;

import static com.graduation.project.fernflower.SundayTest.findKeyWord;


public class Main {
    private static final String x = "=============================================================";
    private static String original_apk_path = null;
    private static String recompile_apk_path = null;
    private static String decompile_apk_path = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println(
                              "=============================================================\n\n"
                            + "                   安卓应用逆向编译及应用研究                      \n\n"
                            + "                姓名：董琴平   学号：092617103                   \n\n"
                            + "=============================================================\n"
                            + "\t01.『选择APK』\t\t02.『提取APK信息』\n"
                            + "\t03.『反编译APK』\t04.『回编译APK』\n"
                            + "\t05.『dex转class』\t06.『class转java』\n"
                            + "\t07.『权限分析』\t\t08.『关键字锚点』\n"
                            + "\t09.『dex转smali』\t10.『smail转dex』\n"
                            + "\t11.『APK签名』\t\t12.『漏洞检测』\n"
                            + "\t13.『java API检测』\t14.『smali API检测』\n");
            System.out.print("功能选项(0~14)：");
            Scanner input = new Scanner(System.in);
            if (input.hasNextInt()) {
                int cmd = input.nextInt();
                switch (cmd) {
                    case 1:
                        selectAPK();
                        break;
                    case 2:
                        extractInfo();
                        break;
                    case 3:
                        decompile();
                        break;
                    case 4:
                        recompile();
                        break;
                    case 5:
                        dex2class();
                        break;
                    case 6:
                        class2java();
                        break;
                    case 7:
                        analysisPermission();
                        break;
                    case 8:
                        keyAnchor();
                        break;
                    case 9:
                        dex2smali();
                        break;
                    case 10:
                        smali2dex();
                        break;
                    case 11:
                        apksigner();
                        break;
                    case 12:
                        loophole();
                        break;
                    case 13:
                        javaApi();
                        break;
                    case 14:
                        smaliApi();
                        break;

                }
            }
        }
    }

    public static boolean checkApkPath() {
        if (stringUtils.isEmpty(original_apk_path) || !fileUtil.existsFile(original_apk_path)) {
            System.out.println("源apk地址正确，请重新选择apk文件！");
            return false;
        }
        return true;
    }

    public static void selectAPK() {
        System.out.println(x);
        original_apk_path = myFileFilter.myJFileChooser("apk");
        if (checkApkPath()) {
            File file = new File(original_apk_path);
            String type = fileUtil.getSuffix(file);
            String s = "『选择APK』\n" +
                    "\t名称：\t" + file.getName() + "\n" +
                    "\t大小：\t" + fileUtil.readableFileSize(file.length()) + "\n" +
                    "\t类型：\t" + type + "\n" +
                    "\t路径：\t" + original_apk_path + "\n" +
                    "已选中" + "【" + file.getName() + "】！\n"
                    + x;
            System.out.println(s);
        } else {
            System.out.println("源apk地址正确，请重新选择apk文件！");
        }
    }


    public static void extractInfo() {
        System.out.println(x);
        ApkInfo apkInfo = null;
        if (!checkApkPath()) selectAPK();
        try {
            long start = System.currentTimeMillis();
            apkInfo = new aaptUtil().getApkInfo(original_apk_path);
            System.out.print(apkInfo.toZhString() + "\t加壳状态：");
            shellUtil.shellScan(original_apk_path);
            System.out.println("\t【操作耗时】：\t" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("apk信息提取失败！");
        }

        System.out.println(x);
    }

    public static void decompile() {
        System.out.println(x);
        if (!checkApkPath()) selectAPK();
        System.out.println("『反编译apk』");
        try {
            long start = System.currentTimeMillis();
            decompile_apk_path = pathUtil.getDecompilePath(fileUtil.getFileNameNoSuffixByPath(original_apk_path));
            boolean result = ApkToolUtils.Decompile(original_apk_path, decompile_apk_path);
            if (result) {
                System.out.println("\t【输入路径】：\t" + original_apk_path);
                System.out.println("\t【输出路径】：\t" + decompile_apk_path);
                System.out.println("\t【操作耗时】：\t" + (System.currentTimeMillis() - start) + "ms");
                SysShell.positionFile(decompile_apk_path);
            } else System.out.println("apk反编译失败！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("apk反编译异常！");
        }
        System.out.println(x);
    }

    public static void recompile() {
        System.out.println(x);
        System.out.println("『回编译apk』");
        if (stringUtils.isEmpty(decompile_apk_path))
            decompile_apk_path = myFileFilter.myJDirectoriesChooser();
        try {
            if (stringUtils.isEmpty(decompile_apk_path)) {
                System.out.println("未选择目标文件或目录！");
                return;
            }
            long start = System.currentTimeMillis();
            recompile_apk_path = pathUtil.getRecompilePath(fileUtil.getFileName(decompile_apk_path)) + ".apk";
            boolean result = ApkToolUtils.Recompile(decompile_apk_path, recompile_apk_path);
            if (result) {
                System.out.println("\t【输入路径】：\t" + decompile_apk_path);
                System.out.println("\t【输出路径】：\t" + recompile_apk_path);
                System.out.println("\t【操作耗时】：\t" + (System.currentTimeMillis() - start) + "ms");
                SysShell.positionFile(pathUtil.getRecompilePath());
            } else System.out.println("apk回编译失败！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("apk回编译异常！");
        }
        System.out.println(x);
    }

    public static void dex2class() {
        System.out.println(x);
        System.out.println("『dex->class』");
        String dexPath = null;
        String zipPath = null;
        if (stringUtils.isEmpty(dexPath))
            dexPath = myFileFilter.myJFileChooser("dex");
        try {
            if (stringUtils.isEmpty(dexPath)) {
                System.out.println("未选择目标文件或目录！");
                return;
            }
            long start = System.currentTimeMillis();
            zipPath = pathUtil.getClassPath() + fileUtil.getFileName(fileUtil.getFileParent(dexPath));
            boolean result = dex2jarUtil.dex2jar(dexPath, zipPath + ".zip");
            if (result) {
                System.out.println("\t【输入路径】：\t" + dexPath);
                System.out.println("\t【输出路径】：\t" + zipPath + ".zip");
                System.out.println("\t【操作耗时】：\t" + (System.currentTimeMillis() - start) + "ms");
                SysShell.positionFile(pathUtil.getClassPath());
            } else System.out.println("dex->class失败！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("dex->class异常！");
        }
        System.out.println(x);
    }

    public static void class2java() {
        System.out.println(x);
        System.out.println("『class->java』");
        String zipPath = null;
        String javaPath = null;
        String unZipPath = null;
        if (stringUtils.isEmpty(zipPath))
            zipPath = myFileFilter.myJFileChooser("zip");
        try {
            if (stringUtils.isEmpty(zipPath)) {
                System.out.println("未选择目标文件或目录！");
                return;
            }
            long start = System.currentTimeMillis();
            boolean result = fernflowerUtil.class2java(zipPath, pathUtil.getJavaPath());
            if (result) {
                javaPath = pathUtil.getJavaPath() + fileUtil.getFileName(zipPath);
                unZipPath = pathUtil.getJavaPath() + fileUtil.getFileNameNoSuffixByPath(javaPath);
                ZipUtils.unZip(javaPath, unZipPath);
                fileDetails details = fileUtil.numberOfFiles(unZipPath);
                System.out.println("\t【输入路径】：\t" + zipPath);
                System.out.println("\t【输出路径】：\t" + unZipPath);
                System.out.println("\t【目录数量】：\t" + details.getFolderCount());
                System.out.println("\t【文件数量】：\t" + details.getFileCount());
                System.out.println("\t【目录大小】：\t" + fileUtil.readableFileSize(details.getFileLength()));
                System.out.println("\t【操作耗时】：\t" + (System.currentTimeMillis() - start) + "ms");
                SysShell.positionFile(unZipPath);
            } else System.out.println("class->java失败！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("class->java异常！");
        }
        System.out.println(x);
    }

    public static void analysisPermission() {
        System.out.println(x);
        if (!checkApkPath()) selectAPK();
        System.out.println("『权限分析』");
        try {
            long start = System.currentTimeMillis();
            new aaptUtil().getPermission(original_apk_path);
            System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("分析异常");
        }
        System.out.println(x);
    }

    public static void keyAnchor() {
        String keyWord = null;
        String analysis_path = null;
        Scanner word = new Scanner(System.in);
        System.out.println(x);
        System.out.println("『关键字定位』");
        if (stringUtils.isEmpty(analysis_path))
            analysis_path = myFileFilter.myJDirectoriesChooser();
        if (stringUtils.isEmpty(analysis_path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        while (keyWord == null) {
            System.out.print("\t【关键词(多个关键词用','隔开)】：");
            keyWord = word.nextLine();
        }
        String[] keys = keyWord.split(",");
        long start = System.currentTimeMillis();
        findKeyWord(analysis_path, keys);
        System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(x);
    }


    public static void smali2dex() {
        String smali_Path = null;
        String dex_path = null;
        System.out.println(x);
        System.out.println("『smali2dex』");
        if (stringUtils.isEmpty(smali_Path))
            smali_Path = myFileFilter.myJDirectoriesChooser();
        if (stringUtils.isEmpty(smali_Path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        long start = System.currentTimeMillis();
        try {
            dex_path = pathUtil.getDexPath() + fileUtil.getFileName(fileUtil.getFileParent(smali_Path)) + ".dex";
            boolean result = smaliUtil.smali2Dex(smali_Path, dex_path);
            if (!result) System.out.println("smali2dex失败！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("smali2dex异常");
        }
        System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(x);
    }

    public static void dex2smali() {
        String smali_Path = null;
        String dex_path = null;
        System.out.println(x);
        System.out.println("『dex2smali』");
        if (stringUtils.isEmpty(dex_path))
            dex_path = myFileFilter.myJFileChooser("dex");
        if (stringUtils.isEmpty(dex_path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        long start = System.currentTimeMillis();
        try {
            smali_Path = pathUtil.getSnaliDirPath() + fileUtil.getFileName(fileUtil.getFileParent(dex_path));
            boolean result = bakSmaliUtil.dex2Smail(dex_path, smali_Path);
            if (!result) System.out.println("dex2smali失败！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("dex2smali异常");
        }
        System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(x);
    }

    public static void apksigner() {
        String apk_Path = null;
        String keyStore_path = null;
        String pwd = null;
        byte[] pwdbyte = null;
        System.out.println(x);
        Scanner scanner = new Scanner(System.in);
        System.out.println("『APK签名』");
        if (stringUtils.isEmpty(apk_Path))
            apk_Path = myFileFilter.myJFileChooser("apk");
        if (stringUtils.isEmpty(apk_Path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        if (stringUtils.isEmpty(keyStore_path))
            keyStore_path = myFileFilter.myJFileChooser("keystore");
        if (stringUtils.isEmpty(keyStore_path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        while (stringUtils.isEmpty(pwd)) {
            System.out.print("\t【输入密钥】：");
            pwd = scanner.nextLine();
        }
        pwdbyte = pwd.getBytes();
        long start = System.currentTimeMillis();
        //apksignerUtil.verify(apk_Path);
        if (apksignerUtil.apksigner(apk_Path, keyStore_path, pwdbyte))
            System.out.println("\t【签名完成】");
        System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(x);
    }

    public static void loophole() {
        String loophole_path = null;
        String AndroidManifest = null;
        System.out.println(x);
        System.out.println("『漏洞检测』");
        System.out.print("\t【选择AndroidManifest.xml】：");
        AndroidManifest = myFileFilter.myJFileChooser("xml");
        if (!stringUtils.isEmpty(AndroidManifest)) {
            System.out.println(AndroidManifest);
            loopholeUtil.manifestAnalysis(AndroidManifest);
        } else System.out.println("\t【未选择AndroidManifest.xml】");
        loophole_path = myFileFilter.myJDirectoriesChooser();
        if (!stringUtils.isEmpty(loophole_path)) {
            long start = System.currentTimeMillis();
            loopholeUtil.loopholeAnalysis(loophole_path);
            System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        } else System.out.println("\t【未选择检测Java文件目录！】：");
        System.out.println(x);
    }

    public static void javaApi() {
        String java_path = null;
        System.out.println(x);
        System.out.println("『API检测』");
        if (stringUtils.isEmpty(java_path))
            java_path = myFileFilter.myJDirectoriesChooser();
        if (stringUtils.isEmpty(java_path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        long start = System.currentTimeMillis();
        apiUtil.apiAnalysisByJava(java_path);
        System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(x);
    }

    public static void smaliApi() {
        String smali_path = null;
        System.out.println(x);
        System.out.println("『API检测』");
        if (stringUtils.isEmpty(smali_path))
            smali_path = myFileFilter.myJDirectoriesChooser();
        if (stringUtils.isEmpty(smali_path)) {
            System.out.println("未选择目标文件或目录！");
            return;
        }
        long start = System.currentTimeMillis();
        apiUtil.apiAnalysisBySmali(smali_path);
        System.out.println("\t【操作耗时】：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println(x);
    }

}
