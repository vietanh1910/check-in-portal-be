package com.example.hunter_point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HunterPointApplication {

	public static void main(String[] args) {
		SpringApplication.run(HunterPointApplication.class, args);
	}

}
