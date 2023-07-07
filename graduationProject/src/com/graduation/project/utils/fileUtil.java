package com.graduation.project.utils;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author 董琴平
 * @version 1.0
 * 文件处理工具
 * @date 2020/12/30/030 12:17
 */
public class fileUtil {
    public static String getNewFileName(String filName, String addFristStr, String addLastStr) {
        if (null == filName || !filName.contains(".")) return null;
        int pos = filName.lastIndexOf(".");
        StringBuilder stringBuffer = new StringBuilder(filName);
        stringBuffer.insert(pos, "_" + addLastStr);
        stringBuffer.insert(0, addFristStr + "_");
        return stringBuffer.toString();
    }

    /**
     * 创建文件目录
     */
    public static File buildDir(String dir) {
        final File fileDir = new File(dir);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }


    /**
     * 文件或目录是否存在
     */
    public static boolean exists(String path) {
        return new File(path).exists();
    }

    /**
     * 文件是否存在
     */
    public static boolean existsFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }


    /**
     * 文件或目录是否存在
     */
    public static boolean existsAny(String... paths) {
        return Arrays.stream(paths).anyMatch(path -> new File(path).exists());
    }

    /**
     * 删除文件或文件夹
     */
    public static void deleteIfExists(File file) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File temp : files) {
                        deleteIfExists(temp);
                    }
                }
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * 删除文件或文件夹
     */
    public static void deleteIfExists(String path) throws IOException {
        deleteIfExists(new File(path));
    }

    /**
     * 创建文件，如果目标存在则删除
     */
    public static File createFile(String path) throws IOException {
        return createFile(path, false);
    }

    /**
     * 创建文件夹，如果目标存在则删除
     */
    public static File createDir(String path) throws IOException {
        return createDir(path, false);
    }

    /**
     * 创建文件，如果目标存在则删除
     */
    public static File createFile(String path, boolean isHidden) throws IOException {
        File file = createFileSmart(path);
        if (OsUtil.isWindows()) {
            Files.setAttribute(file.toPath(), "dos:hidden", isHidden);
        }
        return file;
    }

    /**
     * 创建文件夹，如果目标存在则删除
     */
    public static File createDir(String path, boolean isHidden) throws IOException {
        File file = new File(path);
        deleteIfExists(file);
        File newFile = new File(path);
        newFile.mkdir();
        if (OsUtil.isWindows()) {
            Files.setAttribute(newFile.toPath(), "dos:hidden", isHidden);
        }
        return file;
    }

    /**
     * 查看文件或者文件夹大小
     */
    public static long getFileSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return file.length();
            } else {
                long size = 0;
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File temp : files) {
                        if (temp.isFile()) {
                            size += temp.length();
                        }
                    }
                }
                return size;
            }
        } else System.out.println("文件不存在");
        return 0;
    }

    public static File createFileSmart(String path) throws IOException {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                createDirSmart(file.getParent());
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            throw new IOException("createFileSmart=" + path, e);
        }
    }

    public static File createDirSmart(String path) throws IOException {
        try {
            File file = new File(path);
            if (!file.exists()) {
                Stack<File> stack = new Stack<>();
                File temp = new File(path);
                while (temp != null) {
                    stack.push(temp);
                    temp = temp.getParentFile();
                }
                while (stack.size() > 0) {
                    File dir = stack.pop();
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                }
            }
            return file;
        } catch (Exception e) {
            throw new IOException("createDirSmart=" + path, e);
        }
    }

    /**
     * 获取目录所属磁盘剩余容量
     */
    public static long getDiskFreeSize(String path) {
        File file = new File(path);
        return file.getFreeSpace();
    }

    public static void unmap(MappedByteBuffer mappedBuffer) throws IOException {
        try {
            Class<?> clazz = Class.forName("sun.nio.ch.FileChannelImpl");
            Method m = clazz.getDeclaredMethod("unmap", MappedByteBuffer.class);
            m.setAccessible(true);
            m.invoke(clazz, mappedBuffer);
        } catch (Exception e) {
            throw new IOException("LargeMappedByteBuffer close", e);
        }
    }


    public static void initFile(String path, boolean isHidden) throws IOException {
        initFile(path, null, isHidden);
    }

    public static void initFile(String path, InputStream input, boolean isHidden) throws IOException {
        if (exists(path)) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(path, "rws")
            ) {
                raf.setLength(0);
            }
        } else {
            fileUtil.createFile(path, isHidden);
        }
        if (input != null) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(path, "rws")
            ) {
                byte[] bts = new byte[8192];
                int len;
                while ((len = input.read(bts)) != -1) {
                    raf.write(bts, 0, len);
                }
            } finally {
                input.close();
            }
        }
    }

    public static boolean canWrite(String path) {
        File file = new File(path);
        File test;
        if (file.isFile()) {
            test = new File(
                    file.getParent() + File.separator + UUID.randomUUID().toString() + ".test");
        } else {
            test = new File(file.getPath() + File.separator + UUID.randomUUID().toString() + ".test");
        }
        try {
            test.createNewFile();
            test.delete();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static void unzip(String zipPath, String toPath, String... unzipFile) throws IOException {
        try (
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipPath))
        ) {
            toPath = toPath == null ? new File(zipPath).getParent() : toPath;
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                final String entryName = entry.getName();
                if (entry.isDirectory() || (unzipFile != null && unzipFile.length > 0
                        && Arrays.stream(unzipFile)
                        .noneMatch((file) -> entryName.equalsIgnoreCase(file)))) {
                    zipInputStream.closeEntry();
                    continue;
                }
                File file = createFileSmart(toPath + File.separator + entryName);
                try (
                        FileOutputStream outputStream = new FileOutputStream(file)
                ) {
                    byte[] bts = new byte[8192];
                    int len;
                    while ((len = zipInputStream.read(bts)) != -1) {
                        outputStream.write(bts, 0, len);
                    }
                }
            }
        }
    }

    /**
     * 判断文件存在是重命名
     */
    public static String renameIfExists(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            int index = file.getName().lastIndexOf(".");
            String name = file.getName().substring(0, index);
            String suffix = index == -1 ? "" : file.getName().substring(index);
            int i = 1;
            String newName;
            do {
                newName = name + "(" + i + ")" + suffix;
                i++;
            }
            while (existsFile(file.getParent() + File.separator + newName));
            return newName;
        }
        return file.getName();
    }

    /**
     * 创建指定大小的Sparse File
     */
    public static void createFileWithSparse(String filePath, long length) throws IOException {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
            try (
                    SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.SPARSE)
            ) {
                channel.position(length - 1);
                channel.write(ByteBuffer.wrap(new byte[]{0}));
            }
        } catch (IOException e) {
            throw new IOException("create spares file fail,path:" + filePath + " length:" + length, e);
        }
    }

    /**
     * 使用RandomAccessFile创建指定大小的File
     */
    public static void createFileWithDefault(String filePath, long length) throws IOException {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
            try (
                    RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");
            ) {
                randomAccessFile.setLength(length);
            }
        } catch (IOException e) {
            throw new IOException("create spares file fail,path:" + filePath + " length:" + length, e);
        }
    }

    public static String getSystemFileType(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file = file.getParentFile();
        }
        return Files.getFileStore(file.toPath()).type();
    }


    public static String fileReName(String path) {
        File oldFile = new File(path);
        String parentPathString = oldFile.getParent();
        String oldFileName = oldFile.getName();
        System.out.println(oldFileName);
        System.out.println(parentPathString);
        if (OsUtil.isWindows()) parentPathString = parentPathString + "\\";
        else parentPathString = parentPathString + "/";
        System.out.println(parentPathString);
        String newFileName = stringUtils.apkNameillegalCharacter(oldFileName);
        System.out.println(newFileName);
        String newFilePath = parentPathString + newFileName;
        if (newFilePath.equals(parentPathString)) return path;
        File newFile = new File(newFilePath);
        if (oldFile.renameTo(newFile)) {
            System.out.println("已重命名");
        } else {
            System.out.println("Error");
        }
        return newFilePath;
    }

    public static String fileReNameZip(String javaPath) {
        File oldFile = new File(javaPath);
        String oldFileName = oldFile.getName();
        System.out.println(javaPath);
        System.out.println(oldFileName);
        String newFilePath = javaPath.replace(".apk", ".zip");
        System.out.println(oldFileName);
        File newFile = new File(newFilePath);
        if (oldFile.renameTo(newFile)) {
            System.out.println("已重命名");
        } else {
            System.out.println("Error");
        }
        return newFilePath;
    }

    /**
     * 文件大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + units[digitGroups];
    }

    /**
     * 获取文件后缀
     */
    public static String getSuffix(File file) {
        String fileName = null;
        String suffix = null;
        if (file != null) {
            fileName = file.getName();
            if (fileName != null && !fileName.isEmpty() && fileName.contains("."))
                suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        //System.out.println("suffix = " + suffix);
        return suffix;
    }


    /**
     * 判断是否为apk文件
     */
    public static boolean isApk(File file) {
        return "apk".equals(getSuffix(file));
    }

    /**
     * 判断是否为apk文件
     */
    public static String getFileName(String path) {
        if (!exists(path)) return null;
        return new File(path).getName();
    }


    /**
     * 判断是否为apk文件
     */
    public static String getFileParent(String path) {
        if (!exists(path)) return null;
        return new File(path).getParent();
    }

    /**
     * 判断是否为apk文件
     */
    public static String getFileNameNoSuffixByPath(String path) {
        if (!existsFile(path)) return null;
        return getFileNameNoSuffix(new File(path).getName());
    }

    /**
     * 去掉后缀名
     */
    public static String getFileNameNoSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return fileName;
    }


    private static void getFileDirOrName(String path) {
        File dirFile = new File(path);
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File fileChildDir : files) {
                    //输出文件名或者文件夹名
                    System.out.print(fileChildDir.getName());
                    if (fileChildDir.isDirectory()) {
                        System.out.println(" :  此为目录名");
                        //通过递归的方式,可以把目录中的所有文件全部遍历出来
                        getFileDirOrName(fileChildDir.getAbsolutePath());
                    }
                    if (fileChildDir.isFile()) {
                        System.out.println(" :  此为文件名");
                    }
                }
            }
        } else {
            System.out.println("你想查找的文件不存在");
        }
    }


    public static fileDetails numberOfFiles(String path) {
        fileDetails fileDetails = new fileDetails();
        File folder = new File(path);
        File[] list = folder.listFiles();
        if (list != null)
            for (File file : list) {
                if (file.isFile()) {
                    fileDetails.setFileLength(fileDetails.getFileLength() + file.length());
                    fileDetails.setFileCount(fileDetails.getFileCount() + 1);
                } else {
                    fileDetails.setFolderCount(fileDetails.getFolderCount() + 1);
                    fileDetails fileDetails1 = numberOfFiles(file.getPath());
                    fileDetails.setFolderCount(fileDetails.getFolderCount() + fileDetails1.getFolderCount());
                    fileDetails.setFileCount(fileDetails.getFileCount() + fileDetails1.getFileCount());
                    fileDetails.setFileLength(fileDetails.getFileLength() + fileDetails1.getFileLength());
                }
            }
        //System.out.println("文件夹的数目: " + fileDetails.getFolderCount() + " 文件的数目: " + fileDetails.getFileCount() + " 目录大小: " + fileDetails.getFileLength());
        return fileDetails;
    }

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

                ArrayList<String> jar = fileUtil.getFileList(path);
                for (int i = 0; i < jar.size(); i++) {
                    file = new File(jar.get(i));
                    if (file.exists()) {
                        if (file.getName().equals(name)) {
                            path = jar.get(i);
                            break;
                        }
                        if (name.equals("keystore") && jar.get(i).contains("keystore")) {
                            path = jar.get(i);
                            break;
                        }
                    }
                }

            }

            if (path.contains("bin")) {
                path = path.replace("bin", "lib");
                ArrayList<String> jar = fileUtil.getFileList(path);
                for (int i = 0; i < jar.size(); i++) {
                    File file = new File(jar.get(i));
                    if (file.exists()) {
                        if (file.getName().equals(name)) {
                            path = jar.get(i);
                            break;
                        }
                        if (name.equals("keystore") && jar.get(i).contains("keystore")) {
                            path = jar.get(i);
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


    // 获取文件夹
    public static ArrayList<String> getDirectoryList(String path) {
        ArrayList<String> directoryList = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isDirectory()) {
                // System.out.println("文件夹：" + tempList[i]);
                directoryList.add(tempList[i].toString());
            }
        }
        return directoryList;
    }

    // 获取文件
    public static ArrayList<String> getFileList(String path) {
        ArrayList<String> fileList = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                // System.out.println("文件：" + tempList[i]);
                fileList.add(tempList[i].toString());
            }
        }
        return fileList;
    }
}
