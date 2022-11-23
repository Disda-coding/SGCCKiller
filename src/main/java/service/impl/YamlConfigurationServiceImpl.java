package service.impl;

import org.modelmapper.ModelMapper;
import org.yaml.snakeyaml.Yaml;
import pojo.Configuration;
import service.ConfigurationService;
import service.UIService;
import utils.ConfigFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @program: QnA
 * @description: solving yaml files
 * @author: Disda
 * @create: 2022-11-22 16:02
 */
public class YamlConfigurationServiceImpl implements ConfigurationService {
    final Yaml yaml = new Yaml();
    ModelMapper modelMapper = new ModelMapper();
    FileInputStream fileInputStream;
    UIService uiService = UIServiceImpl.getInstance();

    private YamlConfigurationServiceImpl(){}

    private static class SingleYamlConfigImpl{
        private static final ConfigurationService INSTANCE = new YamlConfigurationServiceImpl();
    }
    public static ConfigurationService getInstance(){
        return SingleYamlConfigImpl.INSTANCE;
    }

    public String getConfigPath(){
        StringJoiner targetPath = new StringJoiner("/");
        String propPath = null;
        try {
            propPath = YamlConfigurationServiceImpl.class.getClassLoader().getResource(".").getPath(); //读取target下面的excel
        } catch (NullPointerException e) {
            System.out.println("jar包环境");
            String jar_path = null;
            try {
                jar_path = YamlConfigurationServiceImpl.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            } catch (URISyntaxException ex) {
                System.err.println("yaml目录不存在或无法解析！");
                ex.printStackTrace();
            }
            String[] pathArray = jar_path.split("/");
            for (int i = 0; i < pathArray.length - 1; i++) {
                targetPath = targetPath.add(pathArray[i]);
            }
            propPath = targetPath.toString();
        }
        File dir = new File(propPath);
        String[] names = dir.list(new ConfigFilter());
        String path = uiService.selectConfigPath(names);
        return propPath + '/' + path;
    }



    public Configuration getConfiguration(){

        String url = getConfigPath();

        Map<String, Object> map = null;
        try {
            map = yaml.load(new FileInputStream(url));
        } catch (FileNotFoundException e) {
            System.err.println("未找到配置文件，或配置文件路径错误！");
            e.printStackTrace();
        }
        Map<String, Object> ymlMap = new HashMap();
        Map attr = (Map<String, Object>) map.get("attributes");
        Map para = (Map<String, Object>) map.get("parameters");
        ymlMap.putAll(attr);
        ymlMap.putAll(para);
        Configuration configuration  = modelMapper.map(ymlMap,Configuration.class);
//        System.out.println("attr:"+ymlMap);
//        System.out.println(configuration);
        return configuration;
    }

    /**
     * 单元测试没问题
     * @param args
     */
    public static void main(String[] args) {
//        ConfigurationService configurationService = new YamlConfigurationServiceImpl();
        ConfigurationService configurationService = YamlConfigurationServiceImpl.getInstance();
        System.out.println(configurationService.getConfiguration());

    }
}