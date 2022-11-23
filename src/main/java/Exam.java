import pojo.Question;
import service.impl.UIServiceImpl;
import utils.ExcelFilter;
import utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void testError(Double times) throws InterruptedException {
        /**
         * @Method testError
         * @Author disda
         * @Description 用于测试错误次数大于times的方法
         * @Return void
         * @Exception
         * @Date 2021/12/3 2:32 下午
         */
        printTotal(times);
        test(1, questions.size() - 1, times,false);
    }

    public void testAll() throws InterruptedException {
        int size = questions.size();
        int beg = 1, end = size;
        Scanner input = new Scanner(System.in);
        System.out.println("请输入题号范围，必须小于等于\033[36m" + size + "\033[m并用空格or-相连，回车从\033[34m"+eu.getCurrentBeg()+"\033[m开始默认\033[35m"+eu.sample+"\033[m题！");
        String in = input.nextLine();
        String[] nums = in.split(" |-");
        if (nums.length == 2) {
            beg = Integer.valueOf(nums[0]) <=0 ? 1:Integer.valueOf(nums[0]);
            end = Integer.valueOf(nums[1]) > size ? size : Integer.valueOf(nums[1]);
            eu.setDefaultBeg(end+1>size?1:end+1);
        } else if (nums.length == 1) {
            if (nums[0].equals("")) {

                beg = eu.getAndSetDefaultBeg(size);
                int newBeg = beg+eu.sample;
                end = newBeg-1>size?size:newBeg-1;
                test(beg, end, 0.0,false);
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
        test(beg, end, 0.0,false);

    }

    public void test(int beg, int end, Double times,boolean random) throws InterruptedException {
        long sTime = System.currentTimeMillis();
        Scanner input = new Scanner(System.in);
        int all = 0;
        int err = 0;
        ArrayList<Integer> indexx;
        if(!random)
            indexx = (ArrayList<Integer>) Stream.iterate(beg, item -> item + 1).limit(end-beg+1).collect(Collectors.toList());
        else
            indexx = Utils.randomSeq(beg,questions.size());
//        ArrayList<Integer> seq = new ArrayList<>();
            for (int i:indexx){
//
            Question que = questions.get(i - 1);
            if((que.getType().equals("是非题") || que.getType().equals("判断题"))&eu.includes_judge==0) continue;
            if (que.getErrTimes() < times) continue;
            all++;
            System.out.print(+i + ".\033[34m[" + que.getType() + "]\033[m");
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
                eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, -eu.ratio, eu.styleCell);
                if (eu.enable_explain == -1) {
                    printExplains(que);
                }
            } else if (ans.equals("S")||ans.equals("s")) {
                System.out.println("\033[1:32m正确答案：" + que.getAnswer()+"\033[m");
                System.out.println("跳过并重置错误计数器\n");
                eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, -2.0, eu.styleCell);
            } else {
                System.out.println("\033[1:31m正确答案：" + que.getAnswer()+"\033[m");
                Thread.sleep(10);
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
        if(que.getExplains()==null||que.getExplains().length()==0){
            System.out.println("\033[m");
            return ;
        }

        System.out.print("\033[35m[解析]\033[m ");
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
            System.err.println("\033[31m正确答案：" + que.getAnswer()+"\033[m");
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
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
        System.out.println("Author: disda-coding.github.io/");
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
                    UIServiceImpl.getInstance().sayGoodBye();
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
                System.out.println("\033[31m*********  1.测试全部题目  **********");
                System.out.println("\033[32m********   2. 随机抽样     *********");
                System.out.println("\033[33m*********  3.测试错误题目  **********");
                System.out.println("\033[34m********   4.显示题目与答案  ********");
                System.out.println("\033[35m*********  5.查看历史得分  **********");
                System.out.println("\033[36m********   6.  重置题库   ***********");
                System.out.println("\033[m*********  7.   退出     ***********");
                String opt;
                opt = input.nextLine();
                double max_err = ex.eu.getMaxErrTimes(ex.questions);

                if (opt == null || opt.equals("1") || opt.equals("")) {
                    ex.testAll();
                } else if (opt.equals("3")) {
                    System.out.println("最多错了 " + (int) max_err + " 次");
                    System.out.println("选择错误次数大于等于x的题目");
                    String times = input.nextLine();
                    if (times == null || times.equals("")) times = "0.0001";
                    ex.testError(Double.valueOf(times));
                } else if (opt.equals("4")) {
                    ex.printAll();
                } else if (opt.equals("5")) {
                    ex.eu.showRecords();
                } else if (opt.equals("6")) {
                    ex.removeErrTimes();
                }else if(opt.equals("2")){
                    ex.randomTest();
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

    private void randomTest() throws InterruptedException {
        int size = questions.size();
        System.out.println("请输入采样大小，必须小于等于" + size + " 并用空格or-相连，回车默认"+eu.sample+"题！");
        Scanner input = new Scanner(System.in);
        String in = input.nextLine();
        int beg = eu.sample;
        if(in.equals("")){
            System.out.println("默认抽取 "+eu.sample+" 道题\ndfadfd");
        }else if(!Utils.isNumeric(in)){
            System.out.println("请输入数字");
        }else{
            beg = Integer.valueOf(in)<=0?eu.sample:Integer.valueOf(in);
            System.out.println("抽取 "+beg+" 道题\n");
        }
        test(beg, beg, 0.0,true);
    }

    private void removeErrTimes() {
        for (int i = 1; i < questions.size(); i++) {
            Question que = questions.get(i);
            eu.getCellByCaseName(que.getTitle(), eu.titleCell, eu.errCell, -1.0, eu.styleCell);
        }
        System.out.println("已重置！！！");
    }


}
