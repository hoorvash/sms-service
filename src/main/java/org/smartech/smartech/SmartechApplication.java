package org.smartech.smartech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartechApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartechApplication.class, args);
	}

}
