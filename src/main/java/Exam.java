
import com.sun.tools.javac.util.StringUtils;
import org.apache.poi.util.StringUtil;
import utils.ExcelFilter;
import utils.Utils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

public class Exam {
    private ArrayList<Question> questions = new ArrayList();
    private char[] seq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};


    ExcelUtils eu;

    public void printTotal(Double times) {
        /**
         * @Method printTotal
         * @Author disda
         * @Description 用于返回错误大于times的题目数量
         * @Return void
         * @Exception
         * @Date 2021/12/3 2:30 下午
         */
        int size = questions.size();
        int total = 0;
        for (int i = 1; i < size; i++) {
            Question que = questions.get(i);
            if (que.getErrTimes() < times) continue;
            total++;
        }
        System.out.println("错误大于等于" + times + "的题目一共有" + total + "道");
    }

    public void testError(Double times) {
        /**
         * @Method testError
         * @Author disda
         * @Description 用于测试错误次数大于times的方法
         * @Return void
         * @Exception
         * @Date 2021/12/3 2:32 下午
         */
        printTotal(times);
        test(1, questions.size() - 1, times);
    }

    public void testAll() {
        int size = questions.size();
        int beg = 1, end = size;
        Scanner input = new Scanner(System.in);
        System.out.println("请输入题号范围，必须小于等于" + size + " 并用空格or-相连，回车默认全部！");
        String in = input.nextLine();
        String[] nums = in.split(" |-");
        if (nums.length == 2) {
            beg = Integer.valueOf(nums[0]) <=0 ? 1:Integer.valueOf(nums[0]);
            end = Integer.valueOf(nums[1]) > size ? size : Integer.valueOf(nums[1]);

        } else if (nums.length == 1) {
            if (nums[0].equals("")) {
                test(beg, end, 0.0);
                return;
            } else if (Utils.isNumeric(nums[0])){
                int tmp = Integer.valueOf(nums[0]);
                if(tmp>size||tmp<1){
                    System.out.println("超过范围");
                    tmp = 1;
                }
                beg = end = tmp;

            }

        }
        test(beg, end, 0.0);
    }

    public void test(int beg, int end, Double times) {
        long sTime = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        int all = 0;
        int err = 0;
        for (int i = beg; i <= end; i++) {
            Question que = questions.get(i - 1);
            if (que.getErrTimes() < times) continue;
            all++;
            System.out.print(i + ".[" + que.getType() + "]");
            // 太长解决方案
            Utils.printLongStuff(que.getTitle(), eu.linesize);
            for (int j = 0; j < que.ops.size() && !que.ops.get(j).isEmpty(); j++) {
                System.out.println(seq[j] + ": " + que.ops.get(j));
            }
            System.out.println("请输入你的答案：（可以使用a/A/1代表第一个选项,s可以跳过并重置错误次数）");
            String ans = input.nextLine().toUpperCase();

            if (Utils.isInteger(ans)) {
                StringBuilder tmp = new StringBuilder();
                for (int j = 0; j < ans.length(); j++) {
                    tmp.append(seq[Integer.valueOf(ans.charAt(j)) - '0' - 1]);
                }
                ans = tmp.toString();
                //  System.out.println(ans);

            }

            if (que.getType().equals("是非题") || que.getType().equals("判断题")) {
                if (ans.equals("A")) ans = "T";
                else ans = "F";
            }
            if (ans.equals(que.getAnswer())) {
                System.out.println("回答正确");
                System.out.println();
                eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, -eu.ratio, eu.styleCell);
                if (eu.enable_explain == -1) {
                    printExplains(que);
                }
            } else if (ans.equals("S")||ans.equals("s")) {
                System.out.println("跳过并重置错误计数器");
                eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, -2.0, eu.styleCell);
            } else {
                System.err.println("正确答案：" + que.getAnswer());
                if (eu.enable_explain == 1 || eu.enable_explain == -1) {
                    printExplains(que);
                }
                System.out.println();
                err++;
                eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, 1.0, eu.styleCell);
                que.increaseErrTimes();
            }
        }
        long eTime = System.currentTimeMillis();
        double res = (all - err + 0.0) / (all + 0.0) * 100;
        System.out.println("\n您的得分： " + res);
        String out = "";
        if (eu.calTime == 1) {
            System.out.println("做题时长约为" + (eTime - sTime) / 1000 / 60.0 + "Min");
            out = "\t做题时长约为" + (eTime - sTime) / 1000 / 60.0 + "Min";

        }
        out = "您一共做了: " + all + "题\t您的得分： " + res + out;
        eu.recording(out);


    }

    public void printExplains(Question que) {
        System.out.print("[解析]");
        Utils.printLongStuff(que.getExplains(), eu.linesize);
        System.out.println();
    }

    private void printAll() {
        int size = questions.size() - 1;
        for (int i = 1; i <= size; i++) {
            Question que = questions.get(i);
            System.out.print(i + ". ");
            Utils.printLongStuff(que.getTitle(), eu.linesize);
            for (int j = 0; j < que.ops.size() && !que.ops.get(j).isEmpty(); j++) {
                System.out.println(seq[j] + ": " + que.ops.get(j));
            }
            System.out.println("正确答案：" + que.getAnswer());
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String path = null;
        try {

            path = ExcelUtils.class.getClassLoader().getResource(".").getPath();
            System.out.println("path: " + path);
            path = "src/main/resources/";

//            dir = new File(ExcelUtils.class.getClassLoader().getResource(".").getPath());
        } catch (NullPointerException e) {
            System.out.println("可能在jar包环境");

            StringJoiner targetPath = new StringJoiner("/");
            try {
                String jar_path = Exam.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                String[] pathArray = jar_path.split("/");
                for (int i = 0; i < pathArray.length - 1; i++) {
                    targetPath = targetPath.add(pathArray[i]);
                }
//                dir = new File(targetPath.toString());
                path = targetPath.toString();
//                System.out.println();
            } catch (Exception ex) {
                System.out.println("fail due to jar");
            }
        }

//        System.out.println(ExcelUtils.class.getClass().getResource("."));
        System.out.println("Path!!"+path);
        File dir = new File(path);
        String[] name = dir.list(new ExcelFilter());
        List<String> names = Arrays.asList(name);
        System.out.println(names);
        Collections.sort(names);
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        String url = URLDecoder.decode(path + "/" + name[0], "utf-8");
        while (flag) {
            boolean exitFlag = false;
            Exam ex = new Exam();
            if (names.size() > 1) {
                System.out.println("请选择要打开的题库");
                System.out.println("0. 退出题库！");
                for (int i = 0; i < names.size(); i++)
                    System.out.println(i + 1 + ". " + names.get(i));
                int select = Integer.parseInt(input.nextLine());
                if (select >= 1 && select <= names.size()) {
//                    url = ExcelUtils.class.getClassLoader().getResource(names.get(select-1)).getPath();


                    //解决中文乱码问题
                    url = URLDecoder.decode(path + "/" + names.get(select - 1), "utf-8");

                    ex.eu = new ExcelUtils(url, 0);
                    ex.eu.getTestData(ex.questions);
                } else {
                    flag = false;
                    exitFlag = true;
                }
            } else {
//                url = ExcelUtils.class.getClassLoader().getResource(names.get(0)).getPath();
                //解决中文乱码问题
                url = URLDecoder.decode(path + "/" + names.get(0), "utf-8");
                ex.eu = new ExcelUtils(url, 0);
                System.out.println(url);
                ex.eu.getTestData(ex.questions);
            }


            while (!exitFlag) {
                System.out.println("************* 选择功能 *************");
                System.out.println("*********  1.测试全部题目  **********");
                System.out.println("*********  2.测试错误题目  **********");
                System.out.println("********   3.显示题目与答案  ********");
                System.out.println("*********  4.查看历史得分  **********");
                System.out.println("********   5.  重置题库    *********");
                System.out.println("*********  6.   退出     **********");
                String opt;
                opt = input.nextLine();
                double max_err = ex.eu.getMaxErrTimes(ex.questions);

                if (opt == null || opt.equals("1") || opt.equals("")) {
                    ex.testAll();
                } else if (opt.equals("2")) {
                    System.out.println("最多错了 " + (int) max_err + " 次");
                    System.out.println("选择错误次数大于等于x的题目");
                    String times = input.nextLine();
                    if (times == null || times.equals("")) times = "0.0001";
                    ex.testError(Double.valueOf(times));
                } else if (opt.equals("3")) {
                    ex.printAll();
                } else if (opt.equals("4")) {
                    ex.eu.showRecords();
                } else if (opt.equals("5")) {
                    ex.removeErrTimes();
                } else {
                    System.out.println("Bye Bye!");
                    exitFlag = true;
                }
                //ex.printAns();

                FileOutputStream fos = new FileOutputStream(url);
                ex.eu.writeExcel(fos);
            }

        }


    }

    private void removeErrTimes() {
        for (int i = 1; i < questions.size(); i++) {
            Question que = questions.get(i);
            eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, -1.0, eu.styleCell);
        }
        System.out.println("已重置！！！");
    }


}
