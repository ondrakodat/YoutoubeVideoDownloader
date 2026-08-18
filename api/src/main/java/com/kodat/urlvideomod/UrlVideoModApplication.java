package com.kodat.urlvideomod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class UrlVideoModApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlVideoModApplication.class, args);
    }

}
