package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SpringBootSelfTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSelfTestApplication.class,args);
    }
}
