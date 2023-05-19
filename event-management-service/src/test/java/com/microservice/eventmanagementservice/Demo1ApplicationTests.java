package com.microservice.eventmanagementservice;

import com.microservice.eventmanagementservice.config.EventMessagingConfigurationData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableConfigurationProperties(EventMessagingConfigurationData.class)
class Demo1ApplicationTests {

    @Test
    void contextLoads() {
    }

}
