package com.in5bv.rourb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RourbApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RourbApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        System.out.println("API FUNCIONANDO TEN DE TEN");
    }
}
