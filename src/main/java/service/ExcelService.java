package service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 17:20
 */
public interface ExcelService {
    public void showRecords(Sheet sheet);
    public void recording(String out, Sheet sheet);
    public void getCellByCaseName(Workbook workbook, Sheet sheet, String caseName, int caseCellNum, int errCellNum, double opt, int styleCell);


}