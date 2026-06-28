package com.enviro.assessment.junior.reitumetse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvestmentPortfolioApplication {

	public static void main(String[] args) {
		/*
		Creates the application context
		Starts the embedded web server (Tomcat)
		Loads all your Spring components
		Makes your APIs available
		*/
		SpringApplication.run(InvestmentPortfolioApplication.class, args);
	}

}
