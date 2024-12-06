package com.kym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KymApplication {

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(KymApplication.class, args)));
    }

}
