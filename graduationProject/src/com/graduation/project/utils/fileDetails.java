package com.graduation.project.utils;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/13/013 13:50
 */
public class fileDetails {
    private int fileCount = 0, folderCount = 0;
    private long fileLength=0;

    public long getFileLength() {
        return fileLength;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getFolderCount() {
        return folderCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public void setFolderCount(int folderCount) {
        this.folderCount = folderCount;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
}
