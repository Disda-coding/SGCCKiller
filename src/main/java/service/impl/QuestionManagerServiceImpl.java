package service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import pojo.Configuration;
import pojo.Question;
import service.ConfigurationService;

import java.util.List;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 23:16
 */
public class QuestionManagerServiceImpl {
    Configuration configuration;
    private int cursor;

    public QuestionManagerServiceImpl() {
        this.cursor = 1;
        ConfigurationService configurationService = YamlConfigurationServiceImpl.getInstance();

        configuration = configurationService.getConfiguration();

    }

    public int setDefaultBeg(int newBeg, Sheet sheet) {
        Row row = sheet.getRow(configuration.getTitleCell() + 1);
        Cell cell = row.getCell(configuration.getMemory());
        if (cell == null || cell.toString().equals("")) {
            row.createCell(configuration.getMemory());
        }
        row.getCell(configuration.getMemory()).setCellValue(newBeg);
        return getAndSetMem(cursor, newBeg);
    }

    public int getAndSetMem(int temp, int newBeg) {
        this.cursor = newBeg;
        return temp;
    }

    public int getCurrentBeg(Sheet sheet) {
        Row row = sheet.getRow(configuration.getTitleCell() + 1);
        Cell cell = row.getCell(configuration.getMemory());
        if (cell != null && !cell.toString().equals(""))
            cursor = (int) cell.getNumericCellValue();
        return cursor;
    }

    public int getNextBeg(int size, Sheet sheet) {
        getCurrentBeg(sheet);
        int newBeg = cursor + configuration.getSample() > size ? 1 : cursor + configuration.getSample();
        return newBeg;
    }

    public int getAndSetDefaultBeg(int size, Sheet sheet) {
        int newBeg = getNextBeg(size, sheet);
        return setDefaultBeg(newBeg, sheet);
    }

    public double getMaxErrTimes(List<Question> questions) {
        double max_err = 0;
        for (Question q : questions) {
            max_err = max_err >= q.getErrTimes() ? max_err : q.getErrTimes();
        }
        return max_err;
    }
}