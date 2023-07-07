package com.graduation.project.smali;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.pathUtil;

import java.io.IOException;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/14/014 13:21
 */
public class smaliUtil {
    public static boolean smali2Dex(String in, String out) throws IOException, InterruptedException {
        String cmd = "java -jar %s assemble  %s -o %s";
        cmd = String.format(cmd, new String[]{pathUtil.getSmaliPath(), in, out});

        return CmdExecutor.executor(cmd);
    }
}
