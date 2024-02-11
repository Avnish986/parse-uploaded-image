package com.vitraya.parseuploadimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ParseUploadImageApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParseUploadImageApplication.class, args);
	}

}
