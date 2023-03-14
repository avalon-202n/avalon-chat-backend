package com.avalon.avalonchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.avalon.avalonchat.domain.profile.domain"})
public class AvalonChatBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvalonChatBackendApplication.class, args);
	}

}
