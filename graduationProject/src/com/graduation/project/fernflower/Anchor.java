package com.graduation.project.fernflower;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/10/010 11:04
 */
public class Anchor {
    private String className;
    private String path;
    private List<AnchorPoint> anchorPointList = new ArrayList<>();
    private int showWord=0;

    public int getShowWord() {
        return showWord;
    }

    public void setShowWord(int showWord) {
        this.showWord = showWord;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public List<AnchorPoint> getAnchorPointList() {
        return anchorPointList;
    }

    public void setAnchorPointList(List<AnchorPoint> anchorPointList) {
        this.anchorPointList.addAll(anchorPointList);
    }

    public void addAnchorPoint(AnchorPoint anchorPoint) {
        this.anchorPointList.add(anchorPoint);
    }
}
