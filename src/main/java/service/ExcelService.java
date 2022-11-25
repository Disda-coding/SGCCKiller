package service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pojo.Question;

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

    void getCellByCaseName(Question que, double opt);

    void getCellByIndex(int index, double opt);

    Sheet getSheet();

    void setSheet(Sheet sheet);


    public void removeErrTimes();

    Workbook getWorkbook();

    void setWorkbook(Workbook workbook);

    public void writeExcel(String url) throws IOException;


}