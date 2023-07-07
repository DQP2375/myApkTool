package com.graduation.project.apksigner;

import com.graduation.project.cmd.CmdExecutor;
import com.graduation.project.utils.fileUtil;
import com.graduation.project.utils.pathUtil;

import java.io.*;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/14/014 22:28
 */
public class apksignerUtil {
    public static boolean sign(String apk, String keystore) throws IOException, InterruptedException {
        String cmd = "java -jar %s sign --ks  + %s  + %s";
        cmd = String.format(cmd, new String[]{pathUtil.getBakSmaliPath(), keystore, apk});
        CmdExecutor.executor(cmd);
        return true;
    }

    // 签名校验
    public static boolean verify(String apk_path) {
        Process process = null;
        boolean flag = false;
        String cmd = "java -jar %s verify -v --print-certs " + apk_path;
        cmd = String.format(cmd, new String[]{pathUtil.getApksignerPath(), apk_path});
        try {
            process = Runtime.getRuntime().exec(cmd);
            printMessage(process, process.getInputStream());
            printMessage(process, process.getErrorStream());
            int value = process.waitFor();
            if (value == 0) {
                flag = true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return flag;

    }

    // 签名
    public static boolean apksigner(String apk, String keystore, byte[] pwdbyte) {
        Process process = null;
        boolean flag = false;
        String cmd = "java -jar %s sign --ks   %s   %s ";
        cmd = String.format(cmd, new String[]{pathUtil.getApksignerPath(), keystore, apk});
        System.out.println("\tUse: " + keystore + "\n");
        try {
            process = Runtime.getRuntime().exec(cmd);
            OutputStream OutputStream = process.getOutputStream();
            OutputStream.write(pwdbyte);
            OutputStream.flush();
            OutputStream.close();
            printMessage(process, process.getInputStream());
            printMessage(process, process.getErrorStream());
            int value = process.waitFor();
            if (value == 0) {
                System.out.println("\tSubmit keystore password...\n");
                flag = true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return flag;
    }


    // 打印运行信息
    static void printMessage(Process process, InputStream inputStream) {
        new Thread(() -> {
            try {
                Reader reader = new InputStreamReader(inputStream);
                BufferedReader bf = new BufferedReader(reader);
                String line = "";
                while ((line = bf.readLine()) != null) {
                    System.out.println("\t" + line + "\n");
                }
                reader.close();
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
