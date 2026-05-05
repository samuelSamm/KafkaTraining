package com.tecnosolution.KafkaTraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KafkaTrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaTrainingApplication.class, args);
	}

}
