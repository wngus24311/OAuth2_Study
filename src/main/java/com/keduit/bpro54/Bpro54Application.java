package com.keduit.bpro54;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Bpro54Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Bpro54Application.class, args);
	}
	
}
