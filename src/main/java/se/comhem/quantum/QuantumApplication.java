package se.comhem.quantum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
@EnableScheduling
public class QuantumApplication {
	public static void main(final String... args) {
		new SpringApplication(QuantumApplication.class).run(args);
	}
}
