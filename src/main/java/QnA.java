/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-23 15:14
 */
public class QnA {

//    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
//        String path = PathUtils.getPath();
//        System.out.println("Author: disda-coding.github.io");
//        File dir = new File(path);
//        String[] name = dir.list(new ExcelFilter());
//        List<String> names = Arrays.asList(name);
//        System.out.println(names);
//        Collections.sort(names);
//        Scanner input = new Scanner(System.in);
//        boolean flag = true;
//        String url = URLDecoder.decode(path + "/" + name[0], "utf-8");
//        while (flag) {
//            boolean exitFlag = false;
//            Exam ex = new Exam();
//            if (names.size() > 1) {
//                System.out.println("请选择要打开的题库");
//                System.out.println("0. 退出题库！");
//                for (int i = 0; i < names.size(); i++)
//                    System.out.println(i + 1 + ". " + names.get(i));
//                int select = Integer.parseInt(input.nextLine());
//                if (select >= 1 && select <= names.size()) {
//                    //解决中文乱码问题
//                    url = URLDecoder.decode(path + "/" + names.get(select - 1), "utf-8");
//
//                    ex.eu = new ExcelUtils(url, 0);
//                    ex.eu.getTestData(ex.questions);
//                } else {
//                    flag = false;
//                    exitFlag = true;
//                    UIServiceImpl.getInstance().sayGoodBye();
//                }
//            } else {
////                url = ExcelUtils.class.getClassLoader().getResource(names.get(0)).getPath();
//                //解决中文乱码问题
//                url = URLDecoder.decode(path + "/" + names.get(0), "utf-8");
//                ex.eu = new ExcelUtils(url, 0);
//                System.out.println(url);
//                ex.eu.getTestData(ex.questions);
//            }
//
//
//            while (!exitFlag) {
//                System.out.println("************* 选择功能 *************");
//                System.out.println("\033[31m*********  1.测试全部题目  **********");
//                System.out.println("\033[32m********   2. 随机抽样     *********");
//                System.out.println("\033[33m*********  3.测试错误题目  **********");
//                System.out.println("\033[34m********   4.显示题目与答案  ********");
//                System.out.println("\033[35m*********  5.查看历史得分  **********");
//                System.out.println("\033[36m********   6.  重置题库   ***********");
//                System.out.println("\033[m*********  7.   退出     ***********");
//                String opt;
//                opt = input.nextLine();
//                double max_err = ex.eu.getMaxErrTimes(ex.questions);
//
//                if (opt == null || opt.equals("1") || opt.equals("")) {
//                    ex.testAll();
//                } else if (opt.equals("3")) {
//                    System.out.println("最多错了 " + (int) max_err + " 次");
//                    System.out.println("选择错误次数大于等于x的题目");
//                    String times = input.nextLine();
//                    if (times == null || times.equals("")) times = "0.0001";
//                    ex.testError(Double.valueOf(times));
//                } else if (opt.equals("4")) {
//                    ex.printAll();
//                } else if (opt.equals("5")) {
//                    ex.eu.showRecords();
//                } else if (opt.equals("6")) {
//                    ex.removeErrTimes();
//                }else if(opt.equals("2")){
//                    ex.randomTest();
//                } else {
//                    System.out.println("Bye Bye!");
//                    exitFlag = true;
//                }
//                //ex.printAns();
//
//                FileOutputStream fos = new FileOutputStream(url);
//                ex.eu.writeExcel(fos);
//            }
//
//        }
//
//
//    }
}