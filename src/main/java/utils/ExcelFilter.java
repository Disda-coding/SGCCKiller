package utils;


import java.io.File;
import java.io.FilenameFilter;

public class ExcelFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".xlsx") || name.endsWith(".xls");
    }

}
