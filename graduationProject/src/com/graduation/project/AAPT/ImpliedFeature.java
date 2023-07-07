package com.graduation.project.AAPT;

/**
 * @author 董琴平
 * @version 1.0
 * 特性实体
 * @date 2021/1/21/021 16:37
 */

public class ImpliedFeature {

    /**
     * 所需设备特性名称。
     */
    private String feature;

    /**
     * 表明所需特性的内容。
     */
    private String implied;

    public ImpliedFeature() {
        super();
    }

    public ImpliedFeature(String feature, String implied) {
        super();
        this.feature = feature;
        this.implied = implied;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getImplied() {
        return implied;
    }

    public void setImplied(String implied) {
        this.implied = implied;
    }

    @Override
    public String toString() {
        return "feature=" + feature + "\t implied=" + implied ;
    }
}

