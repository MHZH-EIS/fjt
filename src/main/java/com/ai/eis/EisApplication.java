package com.ai.eis;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ai.eis.mapper")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EisApplication {

	public static void main(String[] args) {
		SpringApplication.run(EisApplication.class, args);
	}

}

