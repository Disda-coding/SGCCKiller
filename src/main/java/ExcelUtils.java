import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yaml.snakeyaml.Yaml;
import utils.ConfigFilter;
import utils.ExcelFilter;
import utils.Utils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

enum quesType {
    OBJ,
    OBJnSUB
}

public class ExcelUtils {

    private XSSFSheet sheet;
    private XSSFWorkbook workbook;
    int styleCell, titleCell, typeCell, ans, optBeg, optEnd, explain, errCell, linesize, easy,median, hard, qStart;
    String T, F, del;


    quesType qType = quesType.OBJ;

    public void writeExcel(FileOutputStream fos) throws IOException {
        workbook.write(fos);
    }

    public ExcelUtils(String filePath, Integer index) throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            //获取sheet
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Yaml yaml = new Yaml();


        File dir = new File(ExcelUtils.class.getClassLoader().getResource(".").getPath());
        String[] names = dir.list(new ConfigFilter());
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        String path = names[0];
        while (flag && names.length > 1) {
            System.out.println("请选择要打开的配置文件");
            for (int i = 0; i < names.length; i++)
                System.out.println(i + 1 + ". " + names[i]);
            int select = Integer.parseInt(input.nextLine());
            if (select >= 1 && select <= names.length) {
                path = names[select - 1];
                flag = false;
            }
        }

        URL url = ExcelUtils.class.getClassLoader().getResource(path);

        if (url != null) {
            Map<String, Object> map = yaml.load(new FileInputStream(url.getFile()));

            Map attr = (Map<String, Object>) map.get("attributes");
            styleCell = (Integer) attr.get("styleCell");
            titleCell = (Integer) attr.get("titleCell");
            typeCell = (Integer) attr.get("quesType");
            ans = (Integer) attr.get("ans");
            optBeg = (Integer) attr.get("optBeg");
            optEnd = (Integer) attr.get("optEnd");
            explain = (Integer) attr.get("explain");
            errCell = (Integer) attr.get("errCell");
            Map para = (Map<String, Object>) map.get("parameters");
            T = (String) para.get("T");
            F = (String) para.get("F");
            del = (String) para.get("delimeter");
            linesize = (Integer) para.get("linesize");
            easy = (Integer) para.get("easy");
            median= (Integer) para.get("median");
            hard = (Integer) para.get("hard");
            qStart = (Integer) para.get("qStart");
        }
    }

    public double getRemData(ArrayList<Text> texts) {
        double max_err = 0;
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            String queTitle = row.getCell(0).toString();
            String queAns = row.getCell(1).toString();
            double errTimes = 0.0;
            if (row.getCell(3) == null) {
                row.createCell(3).setCellValue("0");
            } else errTimes = Double.valueOf(row.getCell(3).toString());
            max_err = errTimes > max_err ? errTimes : max_err;
            texts.add(new Text(queTitle, queAns, errTimes));
        }
        return max_err;
    }

    public double getTestData(ArrayList<Question> questions) {
        double max_err = 0;
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = qStart; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            String queTitle = "";
            if (row.getCell(titleCell)!=null&&!row.getCell(titleCell).toString().equals(""))
                queTitle = row.getCell(titleCell).toString();
            //因为全角空白会导致trim不掉的情况,因此需要重写trim
            String queAns ="";
            if(row.getCell(titleCell)!=null&&!row.getCell(ans).toString().equals(""))
                 queAns = Utils.trim(row.getCell(ans).toString());
            String explains = "";
            if(row.getCell(explain) !=null&& !row.getCell(explain).toString().equals(""))
                    row.getCell(explain).toString();
            // new!!
            double errTimes = 0.0;
            if (row.getCell(errCell) == null || row.getCell(errCell).toString().equals("")) {
                row.createCell(errCell).setCellValue("0");
            } else{
                errTimes = Double.valueOf(row.getCell(errCell).toString());
            }
            max_err = errTimes > max_err ? errTimes : max_err;
            String queType="";
            if (row.getCell(typeCell)!=null&&!row.getCell(typeCell).toString().equals(""))
                 queType = row.getCell(typeCell).toString();
            ArrayList<String> ops = new ArrayList<>();

            String[] opsInExcel = new String[optEnd - optBeg + 1];
            for (int j = optBeg; j <= optEnd; j++) {
                if (row.getCell(j) ==null || row.getCell(j).toString().equals(""))
                    opsInExcel[j - optBeg] = "";
                else
                    opsInExcel[j - optBeg] = row.getCell(j).toString();
            }
            for (int j = 0; j < optEnd - optBeg + 1; j++) {
                if (!Utils.isNull(opsInExcel[j])) ops.add(opsInExcel[j]);
            }
            if (qType == quesType.OBJ && (queType.equals("问答题") || queType.equals("填空题")))
                continue;
            else
                questions.add(new Question(queTitle, queAns, queType, ops, explains, errTimes, del));

        }
        return max_err;
    }

    public void fillCell(XSSFRow row, CellStyle style, int styleCell, short color) {
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        Cell cel = row.getCell(styleCell);
        cel.setCellStyle(style);
    }


    public void getCellByCaseName(String caseName, int caseCellNum, int errCellNum, double opt, int styleCell) {
        /**
         * @Method getCellByCaseName
         * @Author disda
         * @Description 用于标记错误的题目
         * @params [caseName, caseCellNum, errCellNum, opt, styleCell]
         * @Return void
         * @Exception
         * @Date 2021/12/3 2:35 下午
         */
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            String cell = row.getCell(caseCellNum).toString(); // 2 for que
            if (cell.equals(caseName)) {
                double errTimes = Double.valueOf(row.getCell(errCellNum).toString()) + opt; // 11 for que
                row.getCell(errCellNum).setCellValue(errTimes);
                CellStyle style = workbook.createCellStyle();
                if (errTimes <= easy) {
                    fillCell(row, style, styleCell, new HSSFColor.GOLD().getIndex());
                } else if (errTimes > easy && errTimes <= median) {
                    fillCell(row, style, styleCell, new HSSFColor.LIGHT_ORANGE().getIndex());
                } else if(errTimes > median && errTimes <= hard ){
                    fillCell(row, style, styleCell, new HSSFColor.ORANGE().getIndex());
                }else if (errTimes >hard){
                    fillCell(row, style, styleCell, new HSSFColor.RED().getIndex());
                }
                break;
            }
        }
    }

}
