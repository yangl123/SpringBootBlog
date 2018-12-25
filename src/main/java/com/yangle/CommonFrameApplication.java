package com.yangle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @author yangle
 */
@MapperScan("com.yangle.mapper") //启用mapper扫描
@SpringBootApplication
public class CommonFrameApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CommonFrameApplication.class, args);
	}
	@Bean
	public StartUpListener startUpListener(){
		return new StartUpListener();
	};
}
