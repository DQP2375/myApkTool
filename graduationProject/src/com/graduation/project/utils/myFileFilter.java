package com.graduation.project.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class myFileFilter extends javax.swing.filechooser.FileFilter {
    private String fileType;

    myFileFilter(String type) {
        this.fileType = type;
    }

    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        return fileType.equals(fileUtil.getSuffix(f));
    }

    public String getDescription() {
        return fileType;
    }

    public static String myJFileChooser(String type) {
        String path = null;
        myFileFilter fileFilter = new myFileFilter(type); // 创建过滤器对象
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(fileFilter); // 对JFileChooser设置过滤器
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            path = file.getPath();
        }
        return path;
    }

    public static String myJDirectoriesChooser() {
        String path = null;
        int result = 0;
        JFileChooser fileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
        fileChooser.setDialogTitle("选择目录");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        result = fileChooser.showOpenDialog(null);
        if (JFileChooser.APPROVE_OPTION == result) {
            path = fileChooser.getSelectedFile().getPath();
        }
        return path;
    }


}
