package com.merchant.services.offersservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories
@EnableScheduling
@SpringBootApplication
@Profile("!test")
public class OffersServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(OffersServiceApplication.class).run(args);
	}
}
