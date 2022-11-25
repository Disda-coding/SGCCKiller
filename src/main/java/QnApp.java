import service.TestManagerService;
import service.UIService;
import service.impl.TestManagerServiceImpl;
import service.impl.UIServiceImpl;
import utils.PathUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.List;

public class QnApp {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        TestManagerService questionManagerService = TestManagerServiceImpl.getInstance();
        // 初始化UIService
        UIService uiService = UIServiceImpl.getInstance();
        uiService.helloWorld();
        // 获取题库所在
        String path = PathUtils.getPath();
        // 获取所有排序后的题库
        List<String> names   = PathUtils.getTestNames();
        String url = URLDecoder.decode(path  + names.get(0), "utf-8");

        uiService.mainMenu(path,names);
//        Scanner input = new Scanner(System.in);
//        boolean flag = true;


//        while (flag) {
//            boolean exitFlag = false;
//            if (names.size() > 1) {
//                System.out.println("请选择要打开的题库");
//                System.out.println("0. 退出题库！");
//                for (int i = 0; i < names.size(); i++)
//                    System.out.println(i + 1 + ". " + names.get(i));
//                int select = Integer.parseInt(input.nextLine());
//                if (select >= 1 && select <= names.size()) {
//                    //解决中文乱码问题
//                    url = URLDecoder.decode(path + names.get(select - 1), "utf-8");
//                    questionManagerService.setExcelService(url,0);
//                    questionManagerService.getTestData();
//                } else {
//                    flag = false;
//                    exitFlag = true;
//                    UIServiceImpl.getInstance().sayGoodBye();
//                }
//            } else {
//                //解决中文乱码问题
//                url = URLDecoder.decode(path  + names.get(0), "utf-8");
//                questionManagerService.setExcelService(url,0);
//                questionManagerService.getTestData();
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
//                double max_err = questionManagerService.getMaxErrTimes();
//
//                if (opt == null || opt.equals("1") || opt.equals("")) {
//                    questionManagerService.testAll();
//                } else if (opt.equals("3")) {
//                    System.out.println("最多错了 " + (int) max_err + " 次");
//                    System.out.println("选择错误次数大于等于x的题目");
//                    String times = input.nextLine();
//                    if (times == null || times.equals("")) times = "0.0001";
//                    questionManagerService.testError(Double.valueOf(times));
//                } else if (opt.equals("4")) {
//                    questionManagerService.printAll();
//                } else if (opt.equals("5")) {
//                    questionManagerService.getExcelService().showRecords();
//                } else if (opt.equals("6")) {
//                    questionManagerService.removeErrTimes();
//                }else if(opt.equals("2")){
//                    questionManagerService.randomTest();
//                } else {
//                    System.out.println("Bye Bye!");
//                    exitFlag = true;
//                }
//                //写回Excel
//                questionManagerService.getExcelService().writeExcel(url);
//            }
//
//        }

     }

}
