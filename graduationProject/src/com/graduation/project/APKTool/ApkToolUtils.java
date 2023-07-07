package com.graduation.project.APKTool;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.pathUtil;

import java.io.IOException;

/**
 * Created by dqp
 */
public class ApkToolUtils {
    //    private static final String APKTOOL_PATH = "C:\\Users\\23752\\Desktop\\test\\apktool\\apktool.jar";
    public static boolean Decompile(String apkPath, String outPath) throws IOException, InterruptedException {
        String cmd = "java -jar %s d -s -f %s -o %s";
        cmd = String.format(cmd, new String[]{pathUtil.getApktoolPath(), apkPath, outPath});
        return CmdExecutor.executor(cmd);
    }

    public static boolean Recompile(String apkDir, String outPath) throws IOException, InterruptedException {
        String cmd = "java -jar %s b %s -o %s";
        cmd = String.format(cmd, new String[]{pathUtil.getApktoolPath(), apkDir, outPath});
        return CmdExecutor.executor(cmd);
    }

}
