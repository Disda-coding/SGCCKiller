package service.impl;

import service.UIService;

import java.util.Scanner;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-23 10:50
 */
public class UIServiceImpl implements UIService {
    private UIServiceImpl(){}

    private static class SingleUIServiceImpl{
        private static final UIService INSTANCE = new UIServiceImpl();
    }
    public static UIService getInstance(){
        return UIServiceImpl.SingleUIServiceImpl.INSTANCE;
    }

    public String selectConfigPath(String[] names){
        String path = names[0];
        Scanner input = new Scanner(System.in);
        boolean flag = true;
        while (flag && names.length > 1) {
            System.out.println("请选择要打开的配置文件");
            for (int i = 0; i < names.length; i++)
                System.out.println(i + 1 + ". " + names[i]);
//            System.out.println(0 + " 自动解析");
            int select = Integer.parseInt(input.nextLine());
            if (select >= 1 && select <= names.length) {
                path = names[select - 1];
                flag = false;
            }
        }
        return path;
    }
}