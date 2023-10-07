package com.sparta.team2project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Team2projectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Team2projectApplication.class, args);
	}

}
