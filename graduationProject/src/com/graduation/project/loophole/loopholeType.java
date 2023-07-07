package com.graduation.project.loophole;

import com.graduation.project.utils.ExcelUtil;
import com.graduation.project.utils.pathUtil;
import com.graduation.project.utils.stringUtils;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/14/014 23:16
 */
public class loopholeType {

    public static loophole getType(String keyword) {
        if (stringUtils.isEmpty(keyword)) return null;
        loophole loophole = new loophole();
        ExcelUtil sheet1 = new ExcelUtil(pathUtil.getLoopholeXlsxPath(), "Sheet1");
        String type, mold, risk;
        type = sheet1.getVagueCellByCaseName(keyword, 3, 0);
        mold = sheet1.getVagueCellByCaseName(keyword, 3, 1);
        risk = sheet1.getVagueCellByCaseName(keyword, 3, 2);
        loophole.setKeyword(keyword);
        loophole.setMold(mold);
        loophole.setRisk(risk);
        loophole.setType(type);
        return loophole;
    }

}
