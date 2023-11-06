package service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import pojo.Configuration;
import pojo.Question;
import service.ConfigurationService;
import service.ExcelService;
import service.QuestionManagerService;
import service.TestManagerService;
import utils.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 23:16
 */
public class TestManagerServiceImpl implements TestManagerService {
    Configuration configuration;
    ExcelService excelService;
    QuestionManagerService questionManagerService = QuestionManagerServiceImpl.getInstance();
    private ArrayList<Question> questions;
    private int cursor;
    private char[] seq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    public void setExcelService(String filePath, int index) {
        excelService = new ExcelServiceImpl(filePath, index);
    }

    public ExcelService getExcelService() {
        return excelService;
    }

    @Override
    public void testByIntelligence(double[] maxVariable) throws InterruptedException {
        test(1, questions.size(), 0.0, false, maxVariable);

    }

    public void resetCursor() {
        this.cursor = 1;
    }

    private TestManagerServiceImpl() {
        this.cursor = 1;
//        this.question = new ArrayList();
        ConfigurationService configurationService = YamlConfigurationServiceImpl.getInstance();
        //!!!!
        configuration = configurationService.getConfiguration();
    }

    private static class SingleTestManagerSerImpl {
        private static final TestManagerService INSTANCE = new TestManagerServiceImpl();
    }

    public static TestManagerService getInstance() {
        return SingleTestManagerSerImpl.INSTANCE;
    }


    /**
     * 加载题库内的题目传入Question对象数组
     *
     * @return
     */
    public double[] getTestData() {
        questions = new ArrayList<>();
        Sheet sheet = excelService.getSheet();
        int rows = sheet.getPhysicalNumberOfRows();
        double maxErrors = 0.0, maxDate = 0.0;
        for (int i = configuration.getqStart(); i <= rows && (sheet.getRow(i) != null && sheet.getRow(i).getCell(configuration.getTitleCell()) != null && !sheet.getRow(i).getCell(configuration.getTitleCell()).toString().trim().equals("")); i++) {

            Row row = sheet.getRow(i);
            String queTitle = "";
            if (row.getCell(configuration.getTitleCell()) != null && !row.getCell(configuration.getTitleCell()).toString().equals(""))
                queTitle = row.getCell(configuration.getTitleCell()).toString();
            //因为全角空白会导致trim不掉的情况,因此需要重写trim
            String queAns = "";
            if (row.getCell(configuration.getAns()) != null && !row.getCell(configuration.getAns()).toString().equals(""))
                queAns = CommonUtils.trim(row.getCell(configuration.getAns()).toString().toUpperCase());
            String explains = "";
            if (row.getCell(configuration.getExplain()) != null && !row.getCell(configuration.getExplain()).toString().equals(""))
                explains = row.getCell(configuration.getExplain()).toString();
            // new!!
            double errTimes = 0.0;
            if (row.getCell(configuration.getErrCell()) == null || row.getCell(configuration.getErrCell()).toString().equals("")) {
                row.createCell(configuration.getErrCell()).setCellValue("0");
            } else {
                errTimes = Double.valueOf(row.getCell(configuration.getErrCell()).toString());
            }
            maxErrors = errTimes > maxErrors ? errTimes : maxErrors;
            // new!!
            double datetime = 0.0;
            if (row.getCell(configuration.getDate()) == null || row.getCell(configuration.getDate()).toString().equals("")) {
                row.createCell(configuration.getDate()).setCellValue("0");
            } else {
                datetime = Double.valueOf(row.getCell(configuration.getDate()).toString());
            }
            maxDate = datetime > maxDate ? datetime : maxDate;
            String queType = "";
            if (row.getCell(configuration.getQuesType()) != null && !row.getCell(configuration.getQuesType()).toString().equals("")) {
                queType = row.getCell(configuration.getQuesType()).toString();
            }
            ArrayList<String> ops = new ArrayList<>();

            String[] opsInExcel = new String[configuration.getOptEnd() - configuration.getOptBeg() + 1];
            for (int j = configuration.getOptBeg(); j <= configuration.getOptEnd(); j++) {
                if (row.getCell(j) == null || row.getCell(j).toString().trim().equals(""))
                    opsInExcel[j - configuration.getOptBeg()] = "";
                else
                    opsInExcel[j - configuration.getOptBeg()] = row.getCell(j).toString();
            }
            for (int j = 0; j < configuration.getOptEnd() - configuration.getOptBeg() + 1; j++) {
                if (!CommonUtils.isNull(opsInExcel[j])) {
                    ops.add(opsInExcel[j].trim());
                }
            }
            if ((queType.equals("问答题") || queType.equals("填空题")))
                continue;
            else {
                if (configuration.getIsOrder() == 1 || queType.equals("判断题")) {
                    questions.add(questionManagerService.createQuestion(queTitle, queAns, queType, ops, explains, errTimes, datetime, maxDate, configuration.getDel()));
                } else {
                    questions.add(mixOrder(queTitle, queAns, queType, ops, explains, errTimes, datetime, maxDate, configuration.getDel()));
                }
            }
        }
        double[] maxVariables=new double[]{maxErrors, maxDate};
        return maxVariables;
    }

    /**
     * 将选项打乱顺序
     *
     * @param queTitle
     * @param queAns
     * @param queType
     * @param ops
     * @param explains
     * @param datetime
     * @param maxDate
     * @param del
     * @return
     */
    public Question mixOrder(String queTitle, String queAns, String queType, ArrayList<String> ops, String explains, double errTimes, double datetime, double maxDate, String del) {
        StringBuilder sb = new StringBuilder(queAns);
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
        }
        return questionManagerService.createQuestion(queTitle, sb.toString(), queType, ops, explains, errTimes, datetime, maxDate, del);
    }


    public int setDefaultBeg(int newBeg) {
        Sheet sheet = excelService.getSheet();
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

    public int getCurrentBeg() {
        Sheet sheet = excelService.getSheet();
        Row row = sheet.getRow(configuration.getTitleCell() + 1);
        Cell cell = row.getCell(configuration.getMemory());
        if (cell != null && !cell.toString().equals(""))
            cursor = (int) cell.getNumericCellValue();
        return cursor;
    }

    public int getNextBeg(int size) {
        getCurrentBeg();
        int newBeg = cursor + configuration.getSample() > size ? 1 : cursor + configuration.getSample();
        return newBeg;
    }

    public int getAndSetDefaultBeg(int size) {
        int newBeg = getNextBeg(size);
        return setDefaultBeg(newBeg);
    }

    public double getMaxErrTimes() {
        double max_err = 0;
        for (Question q : questions) {
            max_err = max_err >= q.getErrTimes() ? max_err : q.getErrTimes();
        }
        return max_err;
    }

    /**
     * @Method printTotal
     * @Author disda
     * @Description 用于返回错误大于times的题目数量
     * @Return void
     * @Exception
     * @Date 2021/12/3 2:30 下午
     */

    public void printTotal(Double times) {
        int size = questions.size();
        int total = 0;
        for (int i = 0; i < size; i++) {
            Question que = questions.get(i);
            if (que.getErrTimes() < times) continue;
            total++;
        }
        System.out.println("错误大于等于" + times + "的题目一共有" + total + "道!");
    }


    /**
     * @Method testError
     * @Author disda
     * @Description 用于测试错误次数大于times的方法
     * @Return void
     * @Exception
     * @Date 2021/12/3 2:32 下午
     */
    public void testError(Double times) throws InterruptedException {
        printTotal(times);
        test(1, questions.size(), times, false, null);
    }


    public void test(int beg, int end, Double times, boolean random, double[] maxVariable) throws InterruptedException {
        long sTime = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        int all = 0;
        int err = 0;
        ArrayList<Integer> indexx;
        if (!random)
            indexx = (ArrayList<Integer>) Stream.iterate(beg, item -> item + 1).limit(end - beg + 1).collect(Collectors.toList());
        else
            indexx = CommonUtils.randomSeq(beg, questions.size());
//
        int k = 1;
        for (int i : indexx) {
            Question que = questions.get(i - 1);
            if ((que.getType().equals("是非题") || que.getType().equals("判断题")) & configuration.getIncludes_judge() == 0)
                continue;
            if (que.getErrTimes() < times) continue;
            if (maxVariable != null) {
                double queCoef = (0.7 * (que.getErrTimes() / maxVariable[0]) + 0.3 * (que.getDate() / maxVariable[1]));
                que.setCoef(queCoef);
                if (queCoef < 0.7) {
                    continue;
                }
            }
            all++;
            if (random) {
                System.out.print(+k + ".\033[34m[" + que.getType() + "]\033[m");
                k++;
            } else {
                System.out.print(+i + ".\033[34m[" + que.getType() + "]\033[m");
                if (maxVariable != null){
                    System.out.print("\033[33m[系数为{" + que.getCoef() + '}' + "]\033[m");
                }
            }

            // 太长解决方案
            CommonUtils.printLongStuff(que.getTitle(), configuration.getLinesize());
            for (int j = 0; j < que.ops.size() && !que.ops.get(j).isEmpty(); j++) {
                System.out.println(seq[j] + ": " + que.ops.get(j));
            }
            System.out.println("请输入你的答案：（可以使用a/A/1代表第一个选项,s可以跳过并重置错误次数）");
            String ans = input.nextLine().toUpperCase();

            if (CommonUtils.isInteger(ans)) {
                StringBuilder tmp = new StringBuilder();
                for (int j = 0; j < ans.length(); j++) {
//                    System.out.println(Integer.valueOf(ans.charAt(j)));
                    if (Integer.valueOf(ans.charAt(j)) - '0' <= 0 || Integer.valueOf(ans.charAt(j)) - '0' >= seq.length)
                        tmp.append(seq[1]);
                    else
                        tmp.append(seq[Integer.valueOf(ans.charAt(j)) - '0' - 1]);

                }
                ans = tmp.toString();
                //  System.out.println(ans);

            }
            if (que.getType().equals("是非题") || que.getType().equals("判断题")) {
                if (ans.equals("A")) ans = "T";
                else if (!ans.equals("S") && !ans.equals("s"))
                    ans = "F";
            }
            if (ans.equals(que.getAnswer())) {
                System.out.println("\033[32m回答正确\033[m");
                System.out.println();
                que.setErrTimes(que.getErrTimes() - configuration.getRatio());
                excelService.getCellByIndex(i, -configuration.getRatio());
//                excelService.getCellByCaseName(que, -configuration.getRatio());
                if (configuration.getEnable_explain() == -1) {
                    printExplains(que);
                }
            } else if (ans.equals("S") || ans.equals("s")) {
                System.out.println("\033[1:32m正确答案：" + que.getAnswer() + "\033[m");
                System.out.println("跳过并重置错误计数器\n");
                questionManagerService.resetErrTimes(que);
                excelService.getCellByIndex(i, -2.0);
//                excelService.getCellByCaseName(que, -2.0);
            } else {
                System.out.println("\033[1:31m正确答案：" + que.getAnswer() + "\033[m");
                Thread.sleep(10);
                if (configuration.getEnable_explain() == 1 || configuration.getEnable_explain() == -1) {
                    printExplains(que);
                }
                System.out.println();
                err++;
                questionManagerService.increaseErrTimes(que);
                excelService.getCellByIndex(i, 1.0);
//                excelService.getCellByCaseName(que, 1.0);
//                que.increaseErrTimes();
            }
            excelService.setDate(i);
        }
        long eTime = System.currentTimeMillis();
        if (all == 0) {
            System.out.println("");
            return;
        }
        double res = (all - err + 0.0) / (all + 0.0) * 100;
        System.out.println("\n您的得分： " + res);
        String out = "";
        if (configuration.getCalTime() == 1) {
            System.out.println("做题时长约为" + (eTime - sTime) / 1000 / 60.0 + "Min");
            out = "\t做题时长约为" + (eTime - sTime) / 1000 / 60.0 + "Min";

        }
        out = "您一共做了: " + all + "题\t您的得分： " + res + out;
        excelService.recording(out);

    }

    public void printExplains(Question que) {
        if (que.getExplains() == null || que.getExplains().length() == 0) {
            System.out.println("\033[m");
            return;
        }

        System.out.print("\033[35m[解析]\033[m ");
        CommonUtils.printLongStuff(que.getExplains(), configuration.getLinesize());
        System.out.println();
    }

    public void testAll() throws InterruptedException {
        int size = questions.size();
        int beg = 1, end = size;
        Scanner input = new Scanner(System.in);
        System.out.println("请输入题号范围，必须小于等于\033[36m" + size + "\033[m并用空格or-相连，回车从\033[34m" + getCurrentBeg() + "\033[m开始默认\033[35m" + configuration.getSample() + "\033[m题！");
        String in = input.nextLine();
        String[] nums = in.split(" |-");
        if (nums.length == 2) {
            beg = Integer.valueOf(nums[0]) <= 0 ? 1 : Integer.valueOf(nums[0]);
            end = Integer.valueOf(nums[1]) > size ? size : Integer.valueOf(nums[1]);
            setDefaultBeg(end + 1 > size ? 1 : end + 1);
        } else if (nums.length == 1) {
            if (nums[0].equals("")) {
                beg = getAndSetDefaultBeg(size);
                int newBeg = beg + configuration.getSample();
                end = newBeg - 1 > size ? size : newBeg - 1;
                test(beg, end, 0.0, false, null);
                return;
            } else if (CommonUtils.isNumeric(nums[0])) {
                int tmp = Integer.valueOf(nums[0]);
                if (tmp > size || tmp < 1) {
                    System.out.println("超过范围");
                    tmp = 1;
                }
                beg = end = tmp;
            }

        }
        test(beg, end, 0.0, false, null);

    }

    @Override
    public void printAll() {
        int size = questions.size() - 1;
        for (int i = 1; i <= size; i++) {
            Question que = questions.get(i);
            System.out.print(i + ". ");
            System.out.println(que.getTitle());
            for (int j = 0; j < que.ops.size() && !que.ops.get(j).isEmpty(); j++) {
                System.out.println(seq[j] + ": " + que.ops.get(j));
            }
            System.out.println("\033[31m正确答案：" + que.getAnswer() + "\033[m");
        }
    }

    public void removeErrTimes() {
        for (int i = 0; i < questions.size(); i++) {
            Question que = questions.get(i);
            questionManagerService.resetErrTimes(que);
            questionManagerService.resetDate(que);
        }
        excelService.removeErrTimes();
        System.out.println("已重置！！！");
    }

    public void randomTest() throws InterruptedException {
        int size = questions.size();
        System.out.println("请输入采样大小，必须小于等于" + size + " 并用空格or-相连，回车默认" + configuration.getSample() + "题！");
        Scanner input = new Scanner(System.in);
        String in = input.nextLine();
        int beg = configuration.getSample();
        if (in.equals("")) {
            System.out.println("默认抽取 " + configuration.getSample() + " 道题\n");
        } else if (!CommonUtils.isNumeric(in)) {
            System.out.println("请输入数字");
        } else {
            beg = Integer.valueOf(in) <= 0 ? configuration.getSample() : Integer.valueOf(in);
            System.out.println("抽取 " + beg + " 道题\n");
        }
        test(beg, beg, 0.0, true, null);
    }

}