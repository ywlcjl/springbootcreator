package com.example.springbootcreator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springbootcreator.mapper")
public class SpringbootcreatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootcreatorApplication.class, args);
	}

}
