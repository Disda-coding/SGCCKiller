package utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * @program: QnA
 * @description:
 * @author: Disda
 * @create: 2022-11-23 15:48
 */
public class PathUtils {

    public static List<String> getTestNames(){
        String path = PathUtils.getPath();
        File dir = new File(path);
        String[] name = dir.list(new ExcelFilter());
        List<String> names = Arrays.asList(name);
        Collections.sort(names);
        System.out.println(names);
        return names;
    }


    public static String getPath(){
        String path = "src/main/resources/";
        try {
            PathUtils.class.getClassLoader().getResource(".").getPath();
        } catch (NullPointerException e) {
            System.out.println("可能在jar包环境");
            StringJoiner targetPath = new StringJoiner("/");
            try {
                String jar_path = PathUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                String[] pathArray = jar_path.split("/");
                for (int i = 0; i < pathArray.length - 1; i++) {
                    System.out.println(targetPath);
                    targetPath = targetPath.add(pathArray[i]);
                }
                path = targetPath.toString()+'/';
            } catch (Exception ex) {
                System.out.println("无法读取jar包下的目录");
            }
        }
        return path;
    }

}