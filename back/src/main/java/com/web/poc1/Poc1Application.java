package com.web.poc1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.web")
public class Poc1Application {

    public static void main(String[] args) {
        SpringApplication.run(Poc1Application.class, args);
    }

}
