package se.comhem.quantum;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@EnableScheduling
public class QuantumApplication {
	public static void main(final String... args) {
		new SpringApplication(QuantumApplication.class).run(args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


	@Bean
	public Module jdk8Module() {
		return new Jdk8Module();
	}

	@Bean
	public Module parameterNamesModule() {
		return new ParameterNamesModule();
	}

	@Bean
	public Module javaTimeModule() {
		return new JavaTimeModule();
	}
}
