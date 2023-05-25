package com.microservice.eventanalysingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.microservice.clients")
@PropertySources(
        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
)
public class EventAnalysingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventAnalysingServiceApplication.class, args);
    }

}
