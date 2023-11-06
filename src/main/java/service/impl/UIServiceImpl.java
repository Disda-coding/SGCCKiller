package service.impl;

import service.TestManagerService;
import service.UIService;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Scanner;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-23 10:50
 */
public class UIServiceImpl implements UIService {
    // 初始化QuestionManagerService
    static TestManagerService testManagerService;


    private UIServiceImpl() {

    }

    public static UIService getInstance() {
        testManagerService = TestManagerServiceImpl.getInstance();
        return UIServiceImpl.SingleUIServiceImpl.INSTANCE;
    }

    public String selectConfigPath(String[] names) {
        String path = names[0];
        Scanner input = new Scanner(System.in);
        boolean flag = true;
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
        return path;
    }

    @Override
    public void helloWorld() {
        System.out.println("*********************************************************************************************************************************************************");
        System.out.println("\n" +
                "   _____________________ ___ ____       \n" +
                "  / __/ ___/ ___/ ___/ //_(_) / /__ ____\n" +
                " _\\ \\/ (_ / /__/ /__/ ,< / / / / -_) __/\n" +
                "/___/\\___/\\___/\\___/_/|_/_/_/_/\\__/_/   \n" +
                "                                        \n");
        System.out.println("                                                                                                                      Author: disda-coding.github.io/");
        System.out.println("                                                                                                                      Version: 1.2.1");
        System.out.println("*********************************************************************************************************************************************************");
    }

    public void sayGoodBye() {
        String bye =
                "                       _oo0oo_\n" +
                        "                      o8888888o\n" +
                        "                      88\" . \"88\n" +
                        "                      (| -_- |)\n" +
                        "                      0\\  =  /0\n" +
                        "                    ___/`---'\\___\n" +
                        "                  .' \\\\|     | '.\n" +
                        "                 / \\\\|||  :  |||// \\\n" +
                        "                / _||||| -:- |||||- \\\n" +
                        "               |   | \\\\\\  -  /// |   |\n" +
                        "               | \\_|  ''\\---/''  |_/ |\n" +
                        "               \\  .-\\__  '-'  ___/-. /\n" +
                        "             ___'. .'  /--.--\\  `. .'___\n" +
                        "          .\"\" '<  `.___\\_<|>_/___.' >' \"\".\n" +
                        "         | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                        "         \\  \\ `_.   \\_ __\\ /__ _/   .-` /  /\n" +
                        "     =====`-.____`.___ \\_____/___.-`___.-'=====\n" +
                        "                       `=---='\n" +
                        "\n" +
                        "\n" +
                        "     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                        "\n" +
                        "               佛祖保佑         永无BUG\n" +
                        "\n" +
                        "\n";
        System.out.println(bye);
    }

    @Override
    public void mainMenu(String path, List<String> names) throws InterruptedException, IOException {
        String url = URLDecoder.decode(path + names.get(0), "utf-8");
        Scanner input = new Scanner(System.in);
        double[] maxDouble = new double[2];
        boolean flag = true;
        while (flag) {
            boolean exitFlag = false;
            if (names.size() > 1) {
                System.out.println("请选择要打开的题库");
                System.out.println("0. 退出题库！");
                for (int i = 0; i < names.size(); i++)
                    System.out.println(i + 1 + ". " + names.get(i));
                int select = Integer.parseInt(input.nextLine());
                if (select >= 1 && select <= names.size()) {
                    //解决中文乱码问题
                    url = URLDecoder.decode(path + names.get(select - 1), "utf-8");
                    testManagerService.setExcelService(url, 0);
                    maxDouble = testManagerService.getTestData();
                } else {
                    flag = false;
                    exitFlag = true;
                    UIServiceImpl.getInstance().sayGoodBye();
                }
            } else {
                //解决中文乱码问题
                url = URLDecoder.decode(path + names.get(0), "utf-8");
                testManagerService.setExcelService(url, 0);
                maxDouble = testManagerService.getTestData();
            }

            while (!exitFlag) {
                System.out.println("************* 选择功能 *************");
                System.out.println("\033[31m*********  1.测试全部题目  **********");
                System.out.println("\033[32m********   2. 随机抽样     *********");
                System.out.println("\033[33m*********  3.测试错误题目  **********");
                System.out.println("\033[34m*********  4.智能模式测试  **********");
                System.out.println("\033[35m********   5.显示题目与答案  ********");
                System.out.println("\033[36m*********  6.查看历史得分  **********");
                System.out.println("\033[37m********   7.  重置题库   ***********");
                System.out.println("\033[m*********  8.   退出     ***********");
                String opt;
                opt = input.nextLine();
                double max_err = testManagerService.getMaxErrTimes();

                if (opt == null || opt.equals("1") || opt.equals("")) {
                    testManagerService.testAll();
                } else if (opt.equals("4")) {
                    System.out.println(11);
                    testManagerService.
                            testByIntelligence(maxDouble);
                } else if (opt.equals("3")) {
                    System.out.println("最多错了 " + (int) max_err + " 次");
                    System.out.println("选择错误次数大于等于x的题目");
                    String times = input.nextLine();
                    if (times == null || times.equals("")) {
                        times = "0.0001";
                    }
                    testManagerService.testError(Double.valueOf(times));
                } else if (opt.equals("5")) {
                    testManagerService.printAll();
                } else if (opt.equals("6")) {
                    testManagerService.getExcelService().showRecords();
                } else if (opt.equals("7")) {
                    testManagerService.removeErrTimes();
                } else if (opt.equals("2")) {
                    testManagerService.randomTest();
                } else {
                    System.out.println("Bye Bye!");
                    testManagerService.resetCursor();
                    exitFlag = true;
                }
                //写回Excel
                testManagerService.getExcelService().writeExcel(url);
            }

        }

    }

    private static class SingleUIServiceImpl {

        private static final UIService INSTANCE = new UIServiceImpl();
    }

}