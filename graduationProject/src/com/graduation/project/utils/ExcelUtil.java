package com.graduation.project.utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

/**
 * @author 董琴平
 * @version 1.0
 * @date 2021/4/7/007 14:47
 */
public class ExcelUtil {
    private XSSFSheet sheet;

    /**
     * 构造函数，初始化excel数据
     *
     * @param filePath  excel路径
     * @param sheetName sheet表名
     */
    public ExcelUtil(String filePath, String sheetName) {
        if (!fileUtil.existsFile(filePath)) System.out.println("文件不存在");
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = sheets.getSheet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据行和列的索引获取单元格的数据
     *
     * @param row
     * @param column
     * @return
     */
    public String getExcelDateByIndex(int row, int column) {
        XSSFRow row1 = sheet.getRow(row);
        return row1.getCell(column).toString();
    }

    /**
     * 根据某一列值为“******”的这一行，来获取该行第x列的值
     *
     * @param caseName
     * @param currentColumn 当前单元格列的索引
     * @param targetColumn  目标单元格列的索引
     * @return
     */
    public String getCellByCaseName(String caseName, int currentColumn, int targetColumn) {
        if (stringUtils.isEmpty(caseName)) return null;
        String operateSteps = "";
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; ++i) {
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(currentColumn).toString().toLowerCase();
            if (!stringUtils.isEmpty(cell) && caseName.toLowerCase().equals(cell)) {
                operateSteps = row.getCell(targetColumn).toString().toLowerCase();
                break;
            }
        }
        return operateSteps;
    }

    /**
     * 根据某一列值包含“******”的这一行，来获取该行第x列的值
     *
     * @param caseName
     * @param currentColumn 当前单元格列的索引
     * @param targetColumn  目标单元格列的索引
     * @return
     */
    public String getVagueCellByCaseName(String caseName, int currentColumn, int targetColumn) {
        if (stringUtils.isEmpty(caseName)) return null;
        String operateSteps = "";
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; ++i) {
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(currentColumn).toString();
            if (!stringUtils.isEmpty(cell) && cell.toLowerCase().contains(caseName.toLowerCase())) {
                operateSteps = row.getCell(targetColumn).toString().toLowerCase();
                break;
            }
        }
        return operateSteps;
    }

    //打印excel数据
    public void readExcelData() {
        //获取行数
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            //获取列数
            XSSFRow row = sheet.getRow(i);
            int columns = row.getPhysicalNumberOfCells();
            for (int j = 0; j < columns; j++) {
                String cell = row.getCell(j).toString();
                System.out.println(cell);
            }
        }
    }

}
