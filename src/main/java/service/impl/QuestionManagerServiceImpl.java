package service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import pojo.Configuration;
import pojo.Question;
import service.ConfigurationService;
import service.ExcelService;
import service.QuestionManagerService;
import utils.Utils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-22 23:16
 */
public class QuestionManagerServiceImpl implements QuestionManagerService{
    Configuration configuration;
    ExcelService excelService;
    private int cursor;
    private char[] seq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    private QuestionManagerServiceImpl() {
        this.cursor = 1;
        ConfigurationService configurationService = YamlConfigurationServiceImpl.getInstance();
        configuration = configurationService.getConfiguration();
    }

    private static class SingleQuesManagerSerImpl{
        private static final QuestionManagerService INSTANCE =  new QuestionManagerServiceImpl();
    }

    public QuestionManagerService getInstance(){
        return SingleQuesManagerSerImpl.INSTANCE;
    }


    /**
     * 加载题库内的题目传入Question对象数组
     * @param questions
     * @param sheet
     */
    public void getTestData(ArrayList<Question> questions,Sheet sheet) {
        double max_err = 0;
        int rows = sheet.getPhysicalNumberOfRows();

        for (int i = configuration.getqStart(); i <= rows && (sheet.getRow(i) != null && sheet.getRow(i).getCell(configuration.getTitleCell()) != null && !sheet.getRow(i).getCell(configuration.getTitleCell()).toString().trim().equals("")); i++) {
            Row row = sheet.getRow(i);
            String queTitle = "";
            if (row.getCell(configuration.getTitleCell()) != null && !row.getCell(configuration.getTitleCell()).toString().equals(""))
                queTitle = row.getCell(configuration.getTitleCell()).toString();
            //因为全角空白会导致trim不掉的情况,因此需要重写trim
            String queAns = "";
            if (row.getCell(configuration.getAns()) != null && !row.getCell(configuration.getAns()).toString().equals(""))
                queAns = Utils.trim(row.getCell(configuration.getAns()).toString().toUpperCase());
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

            String queType = "";
            if (row.getCell(configuration.getTypeCell()) != null && !row.getCell(configuration.getTypeCell()).toString().equals(""))
                queType = row.getCell(configuration.getTypeCell()).toString();
            ArrayList<String> ops = new ArrayList<>();

            String[] opsInExcel = new String[configuration.getOptEnd() - configuration.getOptBeg() + 1];
            for (int j = configuration.getOptBeg(); j <= configuration.getOptEnd(); j++) {
                if (row.getCell(j) == null || row.getCell(j).toString().trim().equals(""))
                    opsInExcel[j - configuration.getOptBeg()] = "";
                else
                    opsInExcel[j - configuration.getOptBeg()] = row.getCell(j).toString();
            }
            for (int j = 0; j < configuration.getOptEnd() - configuration.getOptBeg() + 1; j++) {
                if (!Utils.isNull(opsInExcel[j])) {
                    ops.add(opsInExcel[j].trim());
                }
            }
            if ( (queType.equals("问答题") || queType.equals("填空题")))
                continue;
            else {
                if (configuration.getIsOrder() == 1 || queType.equals("判断题")) {
                    questions.add(new Question(queTitle, queAns, queType, ops, explains, errTimes, configuration.getDel()));
                } else {
                    questions.add(mixOrder(queTitle, queAns, queType, ops, explains, errTimes, configuration.getDel()));
                }
            }
        }
    }

    /**
     * 将选项打乱顺序
     * @param queTitle
     * @param queAns
     * @param queType
     * @param ops
     * @param explains
     * @param errTimes
     * @param del
     * @return
     */
    public Question mixOrder(String queTitle, String queAns, String queType, ArrayList<String> ops, String explains, double errTimes, String del) {
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
        return new Question(queTitle, sb.toString(), queType, ops, explains, errTimes, del);
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

    /**
     * @Method printTotal
     * @Author disda
     * @Description 用于返回错误大于times的题目数量
     * @Return void
     * @Exception
     * @Date 2021/12/3 2:30 下午
     */

    public static void printTotal(Double times, ArrayList<Question> questions) {
        int size = questions.size();
        int total = 0;
        for (int i = 1; i < size; i++) {
            Question que = questions.get(i);
            if (que.getErrTimes() < times) continue;
            total++;
        }
        System.out.println("错误大于等于" + times + "的题目一共有" + total + "道");
    }

    public void testError(Double times, ArrayList<Question> questions) throws InterruptedException {
        /**
         * @Method testError
         * @Author disda
         * @Description 用于测试错误次数大于times的方法
         * @Return void
         * @Exception
         * @Date 2021/12/3 2:32 下午
         */
        printTotal(times,questions);
        test(1, questions.size() - 1, times,false, questions);
    }


    public void test(int beg, int end, Double times,boolean random,ArrayList<Question> questions) throws InterruptedException {
        long sTime = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        int all = 0;
        int err = 0;
        ArrayList<Integer> indexx;
        if(!random)
            indexx = (ArrayList<Integer>) Stream.iterate(beg, item -> item + 1).limit(end-beg+1).collect(Collectors.toList());
        else
            indexx = Utils.randomSeq(beg,questions.size());
//
        for (int i:indexx){
            Question que = questions.get(i - 1);
            if((que.getType().equals("是非题") || que.getType().equals("判断题"))&configuration.getIncludes_judge()==0) continue;
            if (que.getErrTimes() < times) continue;
            all++;
            System.out.print(+i + ".\033[34m[" + que.getType() + "]\033[m");
            // 太长解决方案
            Utils.printLongStuff(que.getTitle(), configuration.getLinesize());
            for (int j = 0; j < que.ops.size() && !que.ops.get(j).isEmpty(); j++) {
                System.out.println(seq[j] + ": " + que.ops.get(j));
            }
            System.out.println("请输入你的答案：（可以使用a/A/1代表第一个选项,s可以跳过并重置错误次数）");
            String ans = input.nextLine().toUpperCase();

            if (Utils.isInteger(ans)) {
                StringBuilder tmp = new StringBuilder();
                for (int j = 0; j < ans.length(); j++) {
//                    System.out.println(Integer.valueOf(ans.charAt(j)));
                    if(Integer.valueOf(ans.charAt(j))-'0'<=0||Integer.valueOf(ans.charAt(j))-'0'>=seq.length)
                        tmp.append(seq[1]);
                    else
                        tmp.append(seq[Integer.valueOf(ans.charAt(j)) - '0' - 1]);

                }
                ans = tmp.toString();
                //  System.out.println(ans);

            }
            if (que.getType().equals("是非题") || que.getType().equals("判断题")) {
                if (ans.equals("A")) ans = "T";
                else if (!ans.equals("S")||!ans.equals("s"))
                    ans = "F";
            }
            if (ans.equals(que.getAnswer())) {
                System.out.println("\033[32m回答正确\033[m");
                System.out.println();
                excelService.getCellByCaseName(que.getTitle(), configuration.getTitleCell(), configuration.getErrCell(), -configuration.getRatio(), configuration.getStyleCell());
                if (configuration.getEnable_explain() == -1) {
                    printExplains(que);
                }
            } else if (ans.equals("S")||ans.equals("s")) {
                System.out.println("\033[1:32m正确答案：" + que.getAnswer()+"\033[m");
                System.out.println("跳过并重置错误计数器\n");
                excelService.getCellByCaseName(que.getTitle(), configuration.getTitleCell(), configuration.getErrCell(), -2.0, configuration.getStyleCell());
            } else {
                System.out.println("\033[1:31m正确答案：" + que.getAnswer()+"\033[m");
                Thread.sleep(10);
                if (configuration.getEnable_explain() == 1 || configuration.getEnable_explain() == -1) {
                    printExplains(que);
                }
                System.out.println();
                err++;
                excelService.getCellByCaseName(que.getTitle(), configuration.getTitleCell(), configuration.getErrCell(), 1.0, configuration.getStyleCell());
                que.increaseErrTimes();
            }
        }
        long eTime = System.currentTimeMillis();
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

}