package org.infracloud.url.shortener.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @EnableScheduling
@EnableJpaRepositories("org.infracloud.url.shortener.repository")
@Configuration
public class AppConfig {

	@Bean
	public Module javaTimeModule() {
		return new JavaTimeModule();
	}

	@Bean
	protected Module hibernateModule() {
		return new Hibernate5Module();
	}


}
