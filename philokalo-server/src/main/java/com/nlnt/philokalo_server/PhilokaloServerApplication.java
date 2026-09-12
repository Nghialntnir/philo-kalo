package com.nlnt.philokalo_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhilokaloServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhilokaloServerApplication.class, args);
    }
}
