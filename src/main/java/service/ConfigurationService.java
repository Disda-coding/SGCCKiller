package service;

import pojo.Configuration;

/**
 * @program: QnA
 * @description: load configuration
 * @author: Disda
 * @create: 2022-11-22 16:00
 */
public interface ConfigurationService {
    public Configuration getConfiguration();

    String getConfigPath();
}