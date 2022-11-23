package service;

import org.apache.poi.ss.usermodel.Sheet;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 17:20
 */
public interface ExcelService {
    public void showRecords(Sheet sheet);
    public void recording(String out);
    public void getCellByCaseName(String caseName, int caseCellNum, int errCellNum, double opt, int styleCell);


}