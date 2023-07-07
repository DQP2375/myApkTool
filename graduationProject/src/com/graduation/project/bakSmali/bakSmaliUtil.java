package com.graduation.project.bakSmali;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.pathUtil;

import java.io.IOException;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/14/014 13:24
 */
public class bakSmaliUtil {
    public static boolean dex2Smail(String in, String out) throws IOException, InterruptedException {
        String cmd = "java -jar %s d -o %s %s ";
        cmd = String.format(cmd, new String[]{pathUtil.getBakSmaliPath(), out, in});
        return CmdExecutor.executor(cmd);
    }
}
