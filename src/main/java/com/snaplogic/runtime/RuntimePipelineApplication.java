package com.snaplogic.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@Log4j2
@SpringBootApplication
public class RuntimePipelineApplication {

	private static final Logger logger = LogManager.getLogger(RuntimePipelineApplication.class);

	public static void main(String[] args) {
		logger.info("Starting up RuntimePipelineApplication");
		SpringApplication.run(RuntimePipelineApplication.class, args);
	}

}
