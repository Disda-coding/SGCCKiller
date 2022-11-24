package service.impl;

import org.modelmapper.ModelMapper;
import org.yaml.snakeyaml.Yaml;
import pojo.Configuration;
import service.ConfigurationService;
import service.UIService;
import utils.ConfigFilter;
import utils.PathUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

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
        String propPath = PathUtils.getPath();
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