package com.graduation.project.api;

import com.graduation.project.loophole.loophole;
import com.graduation.project.utils.ExcelUtil;
import com.graduation.project.utils.pathUtil;
import com.graduation.project.utils.stringUtils;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/15/015 14:08
 */
public class apiType {

    public static api getTypeByJava(String keyword) {
        if (stringUtils.isEmpty(keyword)) return null;
        keyword = keyword.replace(" ", "").toLowerCase();
        api api = new api();
        ExcelUtil sheet1 = new ExcelUtil(pathUtil.getApiXlsxPath(), "Sheet1");
        String jclass, smali, description;
        jclass = sheet1.getVagueCellByCaseName(keyword, 1, 0);
        smali = sheet1.getVagueCellByCaseName(keyword, 1, 2);
        description = sheet1.getVagueCellByCaseName(keyword, 1, 3);
        api.setDescription(description);
        api.setJclass(jclass);
        api.setSmali(smali);
        api.setjFunction(keyword);
        return api;
    }

    public static api getTypeBySmali(String keyword) {
        if (stringUtils.isEmpty(keyword)) return null;
        keyword = keyword.replace(" ", "").toLowerCase();
        api api = new api();
        ExcelUtil sheet1 = new ExcelUtil(pathUtil.getApiXlsxPath(), "Sheet1");
        String jclass, jFunction, description;
        jclass = sheet1.getVagueCellByCaseName(keyword, 2, 0);
        jFunction = sheet1.getVagueCellByCaseName(keyword, 2, 1);
        description = sheet1.getVagueCellByCaseName(keyword, 2, 3);
        api.setDescription(description);
        api.setJclass(jclass);
        api.setSmali(keyword);
        api.setjFunction(jFunction);
        return api;
    }
}
