package com.graduation.project.shell;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.pathUtil;

import java.io.IOException;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/10/010 15:03
 */
public class shellUtil {

    public static boolean shellScan(String in) throws IOException, InterruptedException {
//        String cmd = "%s -f -p  %s -o %s";
        String cmd = "java -jar %s %s";
        cmd = String.format(cmd, new String[]{pathUtil.getShellScanPath(), in});

        return CmdExecutor.executor(cmd);
    }
}
