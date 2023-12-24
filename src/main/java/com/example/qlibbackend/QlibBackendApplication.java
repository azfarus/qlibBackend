package com.example.qlibbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QlibBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(QlibBackendApplication.class, args);
    }

}
