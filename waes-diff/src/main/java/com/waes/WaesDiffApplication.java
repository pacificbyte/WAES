package com.waes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * This is the main class. This class executes the application as a stand-alone
 * @author ccamarena
 *
 */
@SpringBootApplication
public class WaesDiffApplication {

	// main method, it calls the Spring application default method run.
	public static void main(String[] args) {
		SpringApplication.run(WaesDiffApplication.class, args);
	}
}
