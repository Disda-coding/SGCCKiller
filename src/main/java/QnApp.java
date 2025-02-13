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
        List<String> names = PathUtils.getTestNames();
        String url = URLDecoder.decode(path + names.get(0), "utf-8");

        uiService.mainMenu(path, names);


    }

}
