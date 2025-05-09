package com.tracktainment.duxmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication(scanBasePackages = {"com.tracktainment.duxmanager", "com.playground"})
@EnableMongoAuditing
public class DuxManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuxManagerApplication.class, args);
	}
}
