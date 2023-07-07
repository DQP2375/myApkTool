package com.graduation.project.dex2jar;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.pathUtil;

import java.io.IOException;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/8/008 15:49
 */
public class dex2jarUtil {

    public static boolean dex2jar(String in, String out) throws IOException, InterruptedException {
//        String cmd = "%s -f -p  %s -o %s";
        String cmd = "%s -f -p  %s -o %s";
        cmd = String.format(cmd, new String[]{pathUtil.getDex2jarPath(), in, out});
        return CmdExecutor.executor(cmd);
    }


}
