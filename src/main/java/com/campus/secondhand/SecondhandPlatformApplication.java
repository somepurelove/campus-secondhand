package com.campus.secondhand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.campus.secondhand")
public class SecondhandPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecondhandPlatformApplication.class, args);
    }
}
