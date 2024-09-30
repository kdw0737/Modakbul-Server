package com.modakbul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ModakbulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModakbulApplication.class, args);
	}

}
