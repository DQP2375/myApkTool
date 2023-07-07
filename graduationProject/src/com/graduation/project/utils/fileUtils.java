package com.graduation.project.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/8/008 20:12
 */
public class fileUtils {
    private static FileOutputStream fos;

    // 获取Lib的文件目录
    public String getLibFilePath(String name) {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            path = java.net.URLDecoder.decode(path, "utf-8");
            if (System.getProperty("os.name").contains("dows")) {
                path = path.substring(1, path.length());
            }
            if (path.contains("jar")) {
                File file = new File(path);
                if (file.exists()) {
                    path = path.substring(0, path.lastIndexOf("jar") + 3);
                    String jarName = file.getName();
                    path = path.replace(jarName, "");

                }
                ArrayList<String> jar = fileUtils.getFileList(path);
                for (String s : jar) {
                    file = new File(s);
                    if (file.exists()) {
                        if (file.getName().equals(name)) {
                            path = s;
                            break;
                        }
                        if (name.equals("keystore") && s.contains("keystore")) {
                            path = s;
                            break;
                        }
                    }
                }

            }
            if (path.contains("bin")) {
                path = path.replace("bin", "lib");
                ArrayList<String> jar = fileUtils.getFileList(path);
                for (String s : jar) {
                    File file = new File(s);
                    if (file.exists()) {
                        if (file.getName().equals(name)) {
                            path = s;
                            break;
                        }
                        if (name.equals("keystore") && s.contains("keystore")) {
                            path = s;
                            break;
                        }
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // System.out.println(path);
        return path;
    }

    // 获取路径下所有文件
    public static ArrayList<String> getDirectoryList(String path) {
        ArrayList<String> directoryList = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList != null) {
            for (File value : tempList) {
                if (value.isDirectory()) {
//                System.out.println("文件夹：" + value.getPath());
                    directoryList.add(value.getPath());
                }
            }
        }
        return directoryList;
    }


    // 获取路径下所有目录和文件
    public static ArrayList<String> getFileAndDirList(String path) {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) list.add(file.getPath());
            if (file.isDirectory()) {
                File[] tempList = file.listFiles();
                if (tempList != null) {
                    for (File value : tempList) {
//            System.out.println("文件夹：" + value.getPath());
                        list.add(value.toString());

                    }
                }
            }
        }
        return list;
    }

    //递归获取所有获取文件
    public static ArrayList<String> recursionFileList(String path) {
        ArrayList<String> fileList = new ArrayList<String>();
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) fileList.add(file.getPath());
            if (file.isDirectory()) {
                File[] tempList = file.listFiles();
                if (tempList != null) {
                    for (File value : tempList) {
                        if (value.isFile()) {
                            //System.out.println("文件：" + value.getPath());
                            fileList.add(value.getPath());
                        }
                        if (value.isDirectory()) {
                            fileList.addAll(recursionFileList(value.getPath()));
                        }
                    }
                }
            }
        }
        return fileList;
    }

    // 获取文件
    public static ArrayList<String> getFileList(String path) {
        ArrayList<String> fileList = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File value : tempList) {
            if (value.isFile()) {
                //System.out.println("文件：" + value.getPath());
                fileList.add(value.getPath());
            }
        }
        return fileList;
    }

    // 删除文件
    public static boolean deleteFile(String pathname) {
        boolean result = false;
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
            result = true;
            System.out.println(pathname + "成功删除！");
        } //else
        //System.out.println(pathname + "删除失败！");
        return result;
    }

    // 复制文件
    public static boolean copy(String srcPathStr, String desPathStr, String fileName) {
        // 获取源文件的名称
        boolean result = false;
        // File file=new File(srcPathStr);
        // String fileName=file.getName();
        desPathStr = desPathStr + File.separator + fileName; // 源文件地址
        try {
            FileInputStream fis = new FileInputStream(srcPathStr);// 创建输入流对象
            fos = new FileOutputStream(desPathStr);
            byte datas[] = new byte[1024 * 8];// 创建搬运工具
            int len = 0;// 创建长度
            while ((len = fis.read(datas)) != -1)// 循环读取数据
            {
                fos.write(datas, 0, len);
            }
            File files = new File(desPathStr);
            if (files.exists()) {
                result = true;
                System.out.println("\t" + files.getPath() + "替换成功!");
            }
            fis.close();// 释放资源
            fis.close();// 释放资源
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 剪切文件 将文件1剪切入文件2中（并删除源文件）
    public static void cutFile(String inputFilePath, String outFilePath) {
        File inputFile = new File(inputFilePath);
        String fileName = inputFile.getName();
        System.out.println("文件名:" + fileName);
        // String
        // inParent=inputFile.getParent().substring(inputFile.getParent().indexOf("lib")+3).trim();
        // System.out.println("inParent:"+inParent);
        ArrayList<String> directoryList = fileUtils.getDirectoryList(outFilePath);
        System.out.println("文件夹:" + directoryList.toString());

        for (String s : directoryList) {
            File dir = new File(s);
            String Parent = s.replace(dir.getParent(), "");
            System.out.println("Parent:" + Parent);

            if (inputFile.getParent().endsWith(Parent)) {
                File outFile = new File(outFilePath + File.separator + Parent + File.separator + fileName);
                byte[] bytes = new byte[1024];
                int temp = 0;
                try (InputStream mInputStream = new FileInputStream(inputFile); FileOutputStream mFileOutputStream = new FileOutputStream(outFile)) {
                    while ((temp = mInputStream.read(bytes)) != -1) {
                        mFileOutputStream.write(bytes, 0, temp);
                        mFileOutputStream.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(outFile.getName() + "文件移动成功。。。");
                // inputFile.delete(); //删除
            }

        }

    }

    // 文件路径+名称
    private static String filenameTemp;

    /**
     * 创建文件
     *
     * @param fileName    文件名称
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean createFile(String path, String fileName, String filecontent) {
        Boolean bool = false;
        filenameTemp = path + fileName;// 文件路径+名称+文件类型
        File file = new File(filenameTemp);

        try {
            // 如果文件不存在，则创建新的文件
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
                System.out.println("success create file,the file is " + filenameTemp);
                // 创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, filecontent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     *
     * @param filepath 文件路径与名称
     * @param newstr   写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath, String newstr) {
        Boolean bool = false;
        String filein = newstr + "\r\n";// 新写入的行，换行
        String temp = "";

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);// 文件路径(包括文件名称)
            // 将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            // 文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            // 不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bool;
    }

}
