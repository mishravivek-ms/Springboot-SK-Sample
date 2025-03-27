package com.sk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sk")
public class SpringbootSkRestAiTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootSkRestAiTutorialApplication.class, args);
	}

}
