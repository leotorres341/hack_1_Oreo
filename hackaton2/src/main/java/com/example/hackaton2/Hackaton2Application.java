package com.example.hackaton2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Hackaton2Application {

    public static void main(String[] args) {
        SpringApplication.run(Hackaton2Application.class, args);
    }

    @Bean
    CommandLineRunner checkSecret(Environment env) {
        return args -> System.out.println("JWT_SECRET: " + env.getProperty("jwt.secret"));
    }
}