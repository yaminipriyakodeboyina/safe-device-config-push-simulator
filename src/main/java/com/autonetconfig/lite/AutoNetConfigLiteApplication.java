package com.autonetconfig.lite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AutoNetConfigLiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoNetConfigLiteApplication.class, args);
    }
}
