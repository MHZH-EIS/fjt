package com.ai.eis;

import javax.servlet.MultipartConfigElement;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ai.eis.mapper")
@EnableAutoConfiguration
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EisApplication.class, args);
	}
	
	
	@Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //单个文件最大  
        factory.setMaxFileSize("102400KB"); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("102400KB");  
        return factory.createMultipartConfig();  
    }  

}

