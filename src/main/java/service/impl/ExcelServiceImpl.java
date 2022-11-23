package service.impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.Configuration;
import service.ConfigurationService;
import service.ExcelService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 17:20
 */
public class ExcelServiceImpl implements ExcelService {
    Configuration configuration;
    ConfigurationService configurationService = YamlConfigurationServiceImpl.getInstance();
    private Sheet sheet;
    private Workbook workbook;

    public ExcelServiceImpl(String filePath, Integer index) {
        System.out.println("选项为: " + filePath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            if (filePath.endsWith("xls")) {
                workbook = new HSSFWorkbook(fileInputStream);

            } else if (filePath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fileInputStream);
            }
            //获取sheet
            sheet = workbook.getSheetAt(index);

        } catch (Exception e) {
            e.printStackTrace();
        }

        configuration = configurationService.getConfiguration();

    }

    /**
     * @Method getCellByCaseName
     * @Author disda
     * @Description 用于标记错误的题目
     * @params [caseName, caseCellNum, errCellNum, opt, styleCell]
     * @Return void
     * @Exception
     * @Date 2021/12/3 2:35 下午
     */
    public void getCellByCaseName(Workbook workbook, Sheet sheet, String caseName, int caseCellNum, int errCellNum, double opt, int styleCell) {

        int rows = sheet.getPhysicalNumberOfRows();
        CellStyle style = workbook.createCellStyle();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(configuration.getTitleCell()) != null); i++) {

            Row row = sheet.getRow(i);
            String cell = row.getCell(caseCellNum).toString(); // 2 for que

            if (cell.trim().equals(caseName)) {

                if (opt >= -1.0) {
//                    System.out.println("recorded");
                    double errTimes = Double.valueOf(row.getCell(errCellNum).toString()) + opt < 0 ? 0 : Double.valueOf(row.getCell(errCellNum).toString()) + opt; // 11 for que
                    row.getCell(errCellNum).setCellValue(errTimes);

                    if (errTimes <= configuration.getEasy()) {
                        fillCell(row, style, styleCell, new HSSFColor.GOLD().getIndex());
                    } else if (errTimes > configuration.getEasy() && errTimes <= configuration.getMedian()) {
                        fillCell(row, style, styleCell, new HSSFColor.LIGHT_ORANGE().getIndex());
                    } else if (errTimes > configuration.getMedian() && errTimes <= configuration.getHard()) {
                        fillCell(row, style, styleCell, new HSSFColor.ORANGE().getIndex());
                    } else if (errTimes > configuration.getHard()) {
                        fillCell(row, style, styleCell, new HSSFColor.RED().getIndex());
                    }
                    break;
                } else if (opt == -2.0) {
                    row.getCell(errCellNum).setCellValue(0.0);
                    this.fillCell(row, style, styleCell, (new HSSFColor.WHITE()).getIndex());
                    if (row.getCell(configuration.getRecord()) != null)
                        row.getCell(configuration.getRecord()).setCellValue("");
                }

            }
        }
    }

    public void fillCell(Row row, CellStyle style, int styleCell, short color) {
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        Cell cel = row.getCell(styleCell);
        cel.setCellStyle(style);
    }


    public void writeExcel(FileOutputStream fos, Workbook workbook) throws IOException {
        workbook.write(fos);
    }

    /**
     * 记录成绩
     *
     * @param out
     * @param sheet
     */
    public void recording(String out, Sheet sheet) {
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(configuration.getTitleCell()) != null); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(configuration.getRecord());
            if (cell == null || cell.toString().trim().equals("")) {
                row.createCell(configuration.getRecord());
                row.getCell(configuration.getRecord()).setCellValue(out);
                break;
            }
        }
    }

    /**
     * 显示成绩
     *
     * @param sheet
     */
    public void showRecords(Sheet sheet) {
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(configuration.getRecord());
            if (cell != null) {
                System.out.println(cell);
            } else {
                break;
            }
        }
    }
}