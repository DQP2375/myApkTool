package com.graduation.project.APKTool;

import com.graduation.project.cmd.SysShell;

import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/8/008 21:28
 */
public class apkUtil {

    public static void decompress(String zipPath, String targetPath) throws IOException {
        File file = new File(zipPath);
        if (!file.isFile()) {
            throw new FileNotFoundException("file not exist!");
        }
        if (targetPath == null || "".equals(targetPath)) {
            targetPath = file.getParent();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> files = zipFile.entries();
        ZipEntry entry = null;
        File outFile = null;
        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        while (files.hasMoreElements()) {
            entry = files.nextElement();
            outFile = new File(targetPath + File.separator + entry.getName());
            // 如果条目为目录，则跳向下一个
            if (entry.isDirectory()) {
                outFile.mkdirs();
                continue;
            }
            // 创建目录
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            // 创建新文件
            outFile.createNewFile();
            // 如果不可写，则跳向下一个条目
            if (!outFile.canWrite()) {
                continue;
            }
            try {
                bin = new BufferedInputStream(zipFile.getInputStream(entry));
                bout = new BufferedOutputStream(new FileOutputStream(outFile));
                byte[] buffer = new byte[1024];
                int readCount = -1;
                while ((readCount = bin.read(buffer)) != -1) {
                    bout.write(buffer, 0, readCount);
                }
            } finally {
                try {
                    bin.close();
                    bout.flush();
                    bout.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static InputStream getInputStream(String apk,String filename) throws IOException {
        InputStream is = null;
        ZipFile zip = new ZipFile(apk);
        // search for file with given filename
        Enumeration<?> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String entryName = entry.getName();
            System.out.println(entryName);
            if (entryName.equals(filename)) {
                is = zip.getInputStream(entry);
                break;
            }
        }
        return is;
    }

    /**
     * 将inputStream转化为file
     * @param is
     * @param file 要输出的文件目录
     */
    public static void inputStream2File (InputStream is, File file) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buffer = new byte[8192];

            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } finally {
            os.close();
            is.close();
        }
    }
//
//    public static void main(String[] args) throws IOException {
//        inputStream2File( getInputStream("G:\\apktool\\apk\\zh.apk","classes.dex"),new File("G:\\apktool\\apk\\Base64.dex"));
//    }


}
