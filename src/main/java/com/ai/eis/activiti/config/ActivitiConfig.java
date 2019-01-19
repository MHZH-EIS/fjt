package com.ai.eis.activiti.config;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.stereotype.Component;

/**
 * AbstractProcessEngineAutoConfiguration.baseSpringProcessEngineConfiguration
 *  构建流程引擎配置信息时会注入ProcessEngineConfigurationConfigurer子类，通过此处自定义配置信息
 */

@Component("customProcessEngineConfigurationConfigurer")
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {
    private static final String FONT_NAME = "宋体";

    @Override
    public void configure(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        springProcessEngineConfiguration.setActivityFontName(FONT_NAME);
        springProcessEngineConfiguration.setLabelFontName(FONT_NAME);
        springProcessEngineConfiguration.setAnnotationFontName(FONT_NAME);
        springProcessEngineConfiguration.setDatabaseSchema("ACTIVITI");
        springProcessEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
 
    }
}
 