package utils;/**
 * @Author disda
 * @Date 2021/12/5
 */

import java.io.File;
import java.io.FilenameFilter;

/**
 * @program: QnA
 * @description: 用于过滤配置文件
 * @author: liu yan
 * @create: 2021-12-05 18:36
 */
public class ConfigFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }
}