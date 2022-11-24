package service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pojo.Question;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 17:20
 */
public interface ExcelService {
    void showRecords();

    void recording(String out);

    void getCellByCaseName(String caseName, int caseCellNum, int errCellNum, double opt, int styleCell, Question que);

    Sheet getSheet();

    void setSheet(Sheet sheet);

    Workbook getWorkbook();

    void setWorkbook(Workbook workbook);

    public void writeExcel(FileOutputStream fos) throws IOException;


}