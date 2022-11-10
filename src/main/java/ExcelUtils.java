import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yaml.snakeyaml.Yaml;
import sun.lwawt.macosx.CPrinterDevice;
import utils.ConfigFilter;
import utils.Utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

enum quesType {
    OBJ,
    OBJnSUB
}


public class ExcelUtils {

    static boolean isSelect = true;


    private Sheet sheet;
    private Workbook workbook;
    int styleCell, enable_explain,titleCell, typeCell, ans, optBeg, optEnd, explain, errCell, linesize, easy, median, hard, qStart, calTime, isOrder, record,sample,includes_judge,memory;
    String del;
    double ratio;

    quesType qType = quesType.OBJ;


    private int mem;

    public void writeExcel(FileOutputStream fos) throws IOException {
        workbook.write(fos);
    }
    public int setDefaultBeg(int newBeg){
        Row row = sheet.getRow(titleCell+1);
        Cell cell =  row.getCell(memory);
        if (cell == null || cell.toString().equals("")) {
            row.createCell(memory);
        }
        row.getCell(memory).setCellValue(newBeg);
        return getAndSetMem(mem,newBeg);
    }
    public int getCurrentBeg(){
        Row row = sheet.getRow(titleCell+1);
        Cell cell =  row.getCell(memory);
        if (cell != null && !cell.toString().equals(""))
            mem = (int) cell.getNumericCellValue();
        return mem;
    }
    public int getNextBeg(int size){
        getCurrentBeg();
        int newBeg=mem+sample>size?1:mem+sample;
        return newBeg;
    }
    public int getAndSetDefaultBeg(int size){
        int newBeg = getNextBeg(size);
        return setDefaultBeg(newBeg);
    }
//    public int getAndSetDefaultBeg(int size){
//        int newBeg = 1;
//        Row row = sheet.getRow(titleCell+1);
//        Cell cell =  row.getCell(memory);
//        if (cell == null || cell.toString().equals("")) {
//            newBeg=mem+sample>size?1:mem+sample;
//            row.createCell(memory);
//            row.getCell(memory).setCellValue(newBeg);
//        }else{
//            mem = (int) cell.getNumericCellValue();
//            newBeg=mem+sample>size?1:mem+sample;
//            System.out.println(newBeg);
//            row.getCell(memory).setCellValue(newBeg);
//        }
//        return getAndSetMem(mem,newBeg);
//    }
    public int getAndSetMem(int temp,int newBeg){
        this.mem = newBeg;
        return temp;
    }
    public ExcelUtils(String filePath, Integer index) throws FileNotFoundException, UnsupportedEncodingException, URISyntaxException {
        this.mem = 1;
        System.out.println("选项为: "+filePath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);

            if (filePath.endsWith("xls")) {
                workbook = new HSSFWorkbook(fileInputStream);

            }

            else if (filePath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fileInputStream);
            }
            //获取sheet
            sheet = workbook.getSheetAt(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Yaml yaml = new Yaml();
        String propPath = null;
        StringJoiner targetPath = new StringJoiner("/");
        try{
//             propPath = "src/main/resources/";
//            System.out.println(111);
            propPath = ExcelUtils.class.getClassLoader().getResource(".").getPath(); //读取target下面的excel
        }catch (NullPointerException e){
            System.out.println("jar包环境");
            String jar_path = Exam.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String[] pathArray= jar_path.split("/");
            for (int i = 0; i < pathArray.length-1; i++) {
                targetPath = targetPath.add(pathArray[i]);
            }
            propPath = targetPath.toString();
//            System.out.println('p'+propPath);
        }

        File dir = new File(propPath);

        String[] names = dir.list(new ConfigFilter());
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        String path = names[0];
        while (flag && names.length > 1 && isSelect) {
            System.out.println("请选择要打开的配置文件");
            for (int i = 0; i < names.length; i++)
                System.out.println(i + 1 + ". " + names[i]);
            System.out.println(0+" 自动解析");
            int select = Integer.parseInt(input.nextLine());
            if (select >= 1 && select <= names.length) {
                path = names[select - 1];
                flag = false;
            }else if(select==0){
                isSelect=false;
            }

        }
        String url = null;
        if (path!=null)
            url = propPath+'/'+path;

        if (url != null && isSelect) {
            Map<String, Object> map = yaml.load(new FileInputStream(url));

            Map attr = (Map<String, Object>) map.get("attributes");
            styleCell = (Integer) attr.get("styleCell");
            titleCell = (Integer) attr.get("titleCell");
            typeCell = (Integer) attr.get("quesType");
            ans = (Integer) attr.get("ans");
            optBeg = (Integer) attr.get("optBeg");
            optEnd = (Integer) attr.get("optEnd");
            explain = (Integer) attr.get("explain");
            errCell = (Integer) attr.get("errCell");
            record = (Integer) attr.get("record");
            memory = (Integer) attr.get("memory");
            Map para = (Map<String, Object>) map.get("parameters");
            del = (String) para.get("delimeter");
            linesize = (Integer) para.get("linesize");
            easy = (Integer) para.get("easy");
            median = (Integer) para.get("median");
            hard = (Integer) para.get("hard");
            qStart = (Integer) para.get("qStart");
            calTime = (Integer) para.get("calTime");
            isOrder = (Integer) para.get("isOrder");
            ratio = (Double) para.get("ratio");
            enable_explain = (Integer) para.get("enable_explain");
            sample = (Integer) para.get("sample");
            includes_judge = (Integer) para.get("includes_judge");
        }
        else{
            //自动解析
        }
    }

    public double getRemData(ArrayList<Text> texts) {
        double max_err = 0;
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(0) != null); i++) {
            Row row = sheet.getRow(i);
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

    public double getMaxErrTimes(List<Question> questions) {
        double max_err = 0;
        for (Question q : questions) {
            max_err = max_err >= q.getErrTimes() ? max_err : q.getErrTimes();
        }
        return max_err;
    }


    public void getTestData(ArrayList<Question> questions) {
        double max_err = 0;
        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = qStart; i <= rows && (sheet.getRow(i)!=null&&sheet.getRow(i).getCell(titleCell) != null&& !sheet.getRow(i).getCell(titleCell).toString().trim().equals("")); i++) {
            Row row = sheet.getRow(i);
            String queTitle = "";
            if (row.getCell(titleCell) != null && !row.getCell(titleCell).toString().equals(""))
                queTitle = row.getCell(titleCell).toString();
            //因为全角空白会导致trim不掉的情况,因此需要重写trim
            String queAns = "";
            if (row.getCell(ans)!=null &&!row.getCell(ans).toString().equals(""))
                queAns = Utils.trim(row.getCell(ans).toString().toUpperCase());
            String explains = "";
            if (row.getCell(explain) != null && !row.getCell(explain).toString().equals(""))
                explains = row.getCell(explain).toString();
            // new!!
            double errTimes = 0.0;
            if (row.getCell(errCell) == null || row.getCell(errCell).toString().equals("")) {
                row.createCell(errCell).setCellValue("0");
            } else {
                errTimes = Double.valueOf(row.getCell(errCell).toString());
            }

            String queType = "";
            if (row.getCell(typeCell) != null && !row.getCell(typeCell).toString().equals(""))
                queType = row.getCell(typeCell).toString();
            ArrayList<String> ops = new ArrayList<>();

            String[] opsInExcel = new String[optEnd - optBeg + 1];
            for (int j = optBeg; j <= optEnd; j++) {
                if (row.getCell(j) == null || row.getCell(j).toString().trim().equals(""))
                    opsInExcel[j - optBeg] = "";
                else
                    opsInExcel[j - optBeg] = row.getCell(j).toString();
            }
            for (int j = 0; j < optEnd - optBeg + 1; j++) {
                if (!Utils.isNull(opsInExcel[j])) {
                    ops.add(opsInExcel[j].trim());
                }
            }
            if (qType == quesType.OBJ && (queType.equals("问答题") || queType.equals("填空题")))
                continue;
            else {

                if (isOrder == 1|| queType.equals("判断题")) {
                    questions.add(new Question(queTitle, queAns, queType, ops, explains, errTimes, del));
                } else {
                    questions.add(mixOrder(queTitle, queAns, queType, ops, explains, errTimes, del));
                }
            }


        }

    }


    //    HashMap mapping = new HashMap(){
//        {
//            mapping.put(0,'A');
//            mapping.put(1,'B');
//            mapping.put(2,'C');
//            mapping.put(3,'D');
//            mapping.put(4,'E');
//            mapping.put(5,'F');
//            mapping.put(6,'G');
//            mapping.put(7,'H');
//
//        }
//    };
    public Question mixOrder(String queTitle, String queAns, String queType, ArrayList<String> ops, String explains, double errTimes, String del) {
        StringBuilder sb = new StringBuilder(queAns);
//        System.out.println("1"+queAns);
        if (ops.size() != 0) {
            sb = new StringBuilder();
            HashMap<Integer, String> map = new HashMap<>();
            for (int i = 0; i < ops.size(); i++) {
                map.put(i, ops.get(i));
            }
            ArrayList<String> ans = new ArrayList<>();
            for (int i = 0; i < queAns.length(); i++) {
                ans.add(map.get((int) queAns.charAt(i) - 65));
            }
            Collections.shuffle(ops);

            for (int i = 0; i < ops.size(); i++) {

                for (String a : ans) {
                    if (a != null)
                        if (a.equals(ops.get(i) == null ? "xxx" : ops.get(i))) {

                            char val = (char) (i + 65);
                            sb.append(val);
                            break;
                        }
                }
            }
//            System.out.println("sb"+sb);

        }

        return new Question(queTitle, sb.toString(), queType, ops, explains, errTimes, del);
    }

    public void fillCell(Row row, CellStyle style, int styleCell, short color) {
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
        CellStyle style = workbook.createCellStyle();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(titleCell) != null); i++) {

            Row row = sheet.getRow(i);
            String cell = row.getCell(caseCellNum).toString(); // 2 for que

            if (cell.trim().equals(caseName)) {

                if (opt >= -1.0) {
//                    System.out.println("recorded");
                    double errTimes = Double.valueOf(row.getCell(errCellNum).toString()) + opt<0?0:Double.valueOf(row.getCell(errCellNum).toString()) + opt; // 11 for que
                    row.getCell(errCellNum).setCellValue(errTimes);

                    if (errTimes <= easy) {
                        fillCell(row, style, styleCell, new HSSFColor.GOLD().getIndex());
                    } else if (errTimes > easy && errTimes <= median) {
                        fillCell(row, style, styleCell, new HSSFColor.LIGHT_ORANGE().getIndex());
                    } else if (errTimes > median && errTimes <= hard) {
                        fillCell(row, style, styleCell, new HSSFColor.ORANGE().getIndex());
                    } else if (errTimes > hard) {
                        fillCell(row, style, styleCell, new HSSFColor.RED().getIndex());
                    }
                    break;
                } else if(opt == -2.0){
                    row.getCell(errCellNum).setCellValue(0.0);
                    this.fillCell(row, style, styleCell, (new HSSFColor.WHITE()).getIndex());
                    if (row.getCell(record) != null)
                        row.getCell(record).setCellValue("");
                }

            }
        }
    }





    public void recording(String out) {
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows && (sheet.getRow(i).getCell(titleCell) != null); i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(record);
            if (cell == null || cell.toString().trim().equals("")) {
                row.createCell(record);
                row.getCell(record).setCellValue(out);
                break;
            }
        }
    }

    public void showRecords() {
        int rows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            Cell cell = row.getCell(record);
            if (cell != null) {
                System.out.println(cell);
            } else {
                break;
            }
        }
    }
}
