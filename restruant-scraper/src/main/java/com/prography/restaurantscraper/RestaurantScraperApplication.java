package com.prography.restaurantscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RestaurantScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantScraperApplication.class, args);
    }

}
