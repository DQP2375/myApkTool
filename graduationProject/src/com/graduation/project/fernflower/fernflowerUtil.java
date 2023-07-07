package com.graduation.project.fernflower;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.pathUtil;

import java.io.IOException;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/9/009 22:17
 */
public class fernflowerUtil {
    public static boolean class2java (String in, String out) throws IOException, InterruptedException {
//        java -jar fernflower.jar classes-dex2jar.jar G:\apktool\output
        String cmd = "java -jar %s  %s  %s";
        cmd = String.format(cmd, new String[]{pathUtil.getFernflowerPath(), in, out});
        return CmdExecutor.executor(cmd);
    }


}
