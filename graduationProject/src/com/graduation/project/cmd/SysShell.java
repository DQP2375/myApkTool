package com.graduation.project.cmd;

import com.graduation.project.utils.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

/*
 * jar相关包命令执行
 *董琴平
 */
public class SysShell {
    // ndk
    public static boolean ndk_build() {
        boolean flag = false;
        Process process = null;
        String cmd = "ndk-build";
        try {
            process = Runtime.getRuntime().exec(cmd, null, new File("/home/ncj/Android/hello/jni"));
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

    // 清屏
    public static void clearScreen() {
        Process process = null;
        String cmd;
        if (System.getProperty("os.name").contains("dows")) {
            cmd = "cls";
        } else
            cmd = "clear";
        try {
            process = Runtime.getRuntime().exec(cmd);
            int value = process.waitFor();
            if (value == 0) {
                System.out.println("clearScreen" + value);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    //定位文件
    public static boolean positionFile(String apk_path) {
        Process process = null;
        boolean flag = false;
        String cmd;
        if (System.getProperty("os.name").contains("dows")) {
            cmd = "explorer " + apk_path;
        } else
            cmd = "nautilus " + apk_path;
        try {
            process = Runtime.getRuntime().exec(cmd);
            int value = process.waitFor();
            if (value == 0) {
                flag = true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return flag;
        }
        return flag;

    }

    // 签名
    public static boolean apksigner(String apk_path, byte[] pwdbyte) {
        Process process = null;
        boolean flag = false;
        fileUtils f = new fileUtils();
        String jar = f.getLibFilePath("apksigner.jar");
        String keystore = f.getLibFilePath("keystore");
        String cmd_Unpack = "java -jar " + jar + " sign --ks " + keystore + " " + apk_path;
        System.out.println("\tUse: " + keystore + "\n");
        try {
            process = Runtime.getRuntime().exec(cmd_Unpack);
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
            return flag;
        }
        return flag;

    }

    // 签名校验
    public static boolean verify(String apk_path) {
        Process process = null;
        boolean flag = false;
        fileUtils f = new fileUtils();
        String cmd_Unpack = "java -jar " + f.getLibFilePath("apksigner.jar") + " verify -v --print-certs " + apk_path;
        try {
            process = Runtime.getRuntime().exec(cmd_Unpack);
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

    // apk反编译
    public static boolean Unpack(String Original_apk_path, String Unpack_path) {
        Process process = null;
        boolean flag = false;
        fileUtils f = new fileUtils();
        String cmd_Unpack = "java -jar " + f.getLibFilePath("apktool.jar") + " d -rf " + Original_apk_path + " -o "
                + Unpack_path;
        try {

            process = Runtime.getRuntime().exec(cmd_Unpack);
            printMessage(process, process.getInputStream());
            printMessage(process, process.getErrorStream());
            int value = process.waitFor();
            if (value == 0)
                flag = true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return flag;
        }
        return flag;

    }

    // apk打包
    public static boolean packaging(String Unpack_path, String packaging_path) {
        boolean flag = false;
        fileUtils f = new fileUtils();
        String cmd_Unpack = "java -jar " + f.getLibFilePath("apktool.jar") + "  b -rf " + Unpack_path + " -o "
                + packaging_path;
        try {
            Process process = Runtime.getRuntime().exec(cmd_Unpack);
            printMessage(process, process.getInputStream());
            printMessage(process, process.getErrorStream());
            int value = process.waitFor();
            if (value == 0)
                flag = true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return flag;
        }
        return flag;

    }

    // apk打包
    public static boolean dex2jar(String inPath, String outPath) {
        boolean flag = false;
        String cmd_Unpack = "G:\\apktool\\dex2jar\\d2j-dex2jar.bat -f -p  G:\\apktool\\output\\zh\\classes.dex -o G:\\apktool\\input\\test.rar";
        try {
            Process process = Runtime.getRuntime().exec(cmd_Unpack);
            printMessage(process, process.getInputStream());
            printMessage(process, process.getErrorStream());
            int value = process.waitFor();
            if (value == 0)
                flag = true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return flag;
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
                    System.out.println(line);
                }
                reader.close();
                bf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
