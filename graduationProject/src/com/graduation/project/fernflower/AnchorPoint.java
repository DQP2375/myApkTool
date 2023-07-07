package com.graduation.project.fernflower;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/10/010 11:01
 */
public class AnchorPoint {
    private int row;
    private int start;
    private int end;
    private String keyWord;
    private String line;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }
}
