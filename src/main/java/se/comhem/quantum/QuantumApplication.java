package se.comhem.quantum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class QuantumApplication {

	public static void main(final String... args) {
		new SpringApplication(QuantumApplication.class).run(args);
	}

}
