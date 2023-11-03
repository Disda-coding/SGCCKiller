package service.impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.Configuration;
import pojo.Question;
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

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

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
        //!!!
        configuration = configurationService.getConfiguration();
    }

    public void removeErrTimes(){
        deleteColumn(sheet,configuration.getErrCell());
        deleteColumn(sheet,configuration.getRecord());
        int rows = sheet.getPhysicalNumberOfRows();
        CellStyle style = workbook.createCellStyle();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(configuration.getTitleCell()) != null); i++) {
            Row row = sheet.getRow(i);
            this.fillCell(row, style, configuration.getStyleCell(), (new HSSFColor.WHITE()).getIndex());
        }
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
//    String caseName, int caseCellNum, int errCellNum, double opt, int styleCell,
    public void getCellByCaseName(Question que,double opt) {
        int rows = sheet.getPhysicalNumberOfRows();
        CellStyle style = workbook.createCellStyle();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(configuration.getTitleCell()) != null); i++) {

            Row row = sheet.getRow(i);
            String cell = row.getCell(configuration.getTitleCell()).toString(); // 2 for que

            if (cell.trim().equals(que.getTitle())) {
                if (opt >= -1.0) {
//                    System.out.println("recorded");
                    double errTimes = Double.valueOf(row.getCell(configuration.getErrCell()).toString()) + opt < 0 ? 0 : Double.valueOf(row.getCell(configuration.getErrCell()).toString()) + opt; // 11 for que
                    row.getCell(configuration.getErrCell()).setCellValue(errTimes);
                    //到时候解耦合
                    que.setErrTimes(errTimes);
                    if (errTimes <= configuration.getEasy()) {
                        fillCell(row, style, configuration.getStyleCell(), new HSSFColor.GOLD().getIndex());
                    } else if (errTimes > configuration.getEasy() && errTimes <= configuration.getMedian()) {
                        fillCell(row, style, configuration.getStyleCell(), new HSSFColor.LIGHT_ORANGE().getIndex());
                    } else if (errTimes > configuration.getMedian() && errTimes <= configuration.getHard()) {
                        fillCell(row, style, configuration.getStyleCell(), new HSSFColor.ORANGE().getIndex());
                    } else if (errTimes > configuration.getHard()) {
                        fillCell(row, style, configuration.getStyleCell(), new HSSFColor.RED().getIndex());
                    }
                    break;
                } else if (opt == -2.0) {
                    row.getCell(configuration.getErrCell()).setCellValue(0.0);
                    this.fillCell(row, style, configuration.getStyleCell(), (new HSSFColor.WHITE()).getIndex());
                }

            }
        }
    }

    @Override
    public void setDate(int index){
        int date = configuration.getDate();
        Row row = sheet.getRow(index);
        row.getCell(date).setCellValue(System.currentTimeMillis());
    }

    @Override
    public void getCellByIndex(int index, double opt) {
        int qStart = configuration.getqStart();
        CellStyle style = workbook.createCellStyle();
        Row row = sheet.getRow(index);
        if (opt >= -1.0) {
            double errTimes = 0.0+opt;
            if(row.getCell(configuration.getErrCell())!=null){
                errTimes = Double.valueOf(row.getCell(configuration.getErrCell()).toString()) + opt < 0 ? 0 : Double.valueOf(row.getCell(configuration.getErrCell()).toString()) + opt; // 11 for que

            }else{
                row.createCell(configuration.getErrCell());
            }
            row.getCell(configuration.getErrCell()).setCellValue(errTimes);
            //到时候解耦合

        } else if (opt == -2.0) {
            row.getCell(configuration.getErrCell()).setCellValue(0.0);

        }
    }

    //删掉指定column及其后面的columns
    public void deleteColumn( Sheet sheet, int columnToDelete ){
        int maxColumn = 0;
        for ( int r=0; r < sheet.getLastRowNum()+1; r++ ){
            Row row = sheet.getRow( r );

            // if no row exists here; then nothing to do; next!
            if ( row == null ) {
                continue;
            }

            // if the row doesn't have this many columns then we are good; next!
            int lastColumn = row.getLastCellNum();
            if ( lastColumn > maxColumn ) {
                maxColumn = lastColumn;
            }

            if ( lastColumn < columnToDelete ) {
                continue;
            }

            for ( int x=columnToDelete+1; x < lastColumn + 1; x++ ){
                Cell oldCell    = row.getCell(x-1);
                if ( oldCell != null ) {
                    row.removeCell( oldCell );
                }

                Cell nextCell   = row.getCell( x );
                if ( nextCell != null ){
                    Cell newCell    = row.createCell( x-1, nextCell.getCellType() );
                    cloneCell(newCell, nextCell);
                }
            }
        }


        // Adjust the column widths
        for ( int c=0; c < maxColumn; c++ ){
            sheet.setColumnWidth( c, sheet.getColumnWidth(c+1) );
        }
    }


    /*
     * Takes an existing Cell and merges all the styles and forumla
     * into the new one
     */
    private void cloneCell( Cell cNew, Cell cOld ){
        cNew.setCellComment( cOld.getCellComment() );
        cNew.setCellStyle( cOld.getCellStyle() );

        switch ( cNew.getCellType() ){
            case Cell.CELL_TYPE_BOOLEAN:{
                cNew.setCellValue( cOld.getBooleanCellValue() );
                break;
            }
            case Cell.CELL_TYPE_NUMERIC:{
                cNew.setCellValue( cOld.getNumericCellValue() );
                break;
            }
            case Cell.CELL_TYPE_STRING:{
                cNew.setCellValue( cOld.getStringCellValue() );
                break;
            }
            case Cell.CELL_TYPE_ERROR:{
                cNew.setCellValue( cOld.getErrorCellValue() );
                break;
            }
            case Cell.CELL_TYPE_FORMULA:{
                cNew.setCellFormula( cOld.getCellFormula() );
                break;
            }
        }

    }


    public void fillCell(Row row, CellStyle style, int styleCell, short color) {
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        Cell cel = row.getCell(styleCell);
        cel.setCellStyle(style);
    }


    public void writeExcel(String url) throws IOException {
        FileOutputStream fos = new FileOutputStream(url);
        workbook.write(fos);
    }

    /**
     * 记录成绩
     *  @param out
     *
     */
    public void recording(String out) {
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
     */
    public void showRecords() {
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