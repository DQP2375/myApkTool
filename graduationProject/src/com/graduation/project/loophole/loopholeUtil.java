package com.graduation.project.loophole;

import com.graduation.project.fernflower.SundayTest;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/14/014 23:02
 */
public class loopholeUtil {
    public static void manifestAnalysis(String path) {
        System.out.println("\t【AndroidManifest配置相关的风险或漏洞】");
        String[] keyWord = new String[]{"android:debuggable=\"true\"","android:allowBackup=\"true\"","<service,android:exported=\"true\"","<activity,android:exported=\"true\"","<provider,android:exported=\"true\"","<receiver,android:exported=\"true\"","android:scheme"};
        for (String key : keyWord) {
            loophole loophole = loopholeType.getType(key);
            SundayTest.findManifestLoophole(path, key, loophole);
        }
    }


    public static void webViewAnalysis(String dir) {
        System.out.println("\t【WebView组件漏洞】");
        String[] keyWord = new String[]{"addJavascriptInterface", "getClassLoader", "proceed", "setAllowFileAccess(true)", "checkServerTrusted", "ALLOW_ALL_HOSTNAME_VERIFIER"};
        for (String key : keyWord) {
            loophole loophole = loopholeType.getType(key);
            SundayTest.findLoophole(dir, key, loophole);
        }
    }

    public static void dateAnalysis(String dir) {
        System.out.println("\t【数据安全风险】");
        String[] keyWord = new String[]{"getExternalStorageDirectory", "MODE_WORLD_READABLE", "MODE_WORLD_WRITEABLE", "NoPadding", "PKCS5padding", "setSeed"};
        for (String key : keyWord) {
            loophole loophole = loopholeType.getType(key);
            SundayTest.findLoophole(dir, key, loophole);
        }
    }

    public static void fileAnalysis(String dir) {
        System.out.println("\t【数据安全风险】");
        String[] keyWord = new String[]{"getName", "openFile()"};
        for (String key : keyWord) {
            loophole loophole = loopholeType.getType(key);
            SundayTest.findLoophole(dir, key, loophole);
        }
    }

    public static void loopholeAnalysis(String path) {
        dateAnalysis(path);
        fileAnalysis(path);
        webViewAnalysis(path);
    }


}
