package com.microservice.clients.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order", url = "http://localhost:8082")
public interface OrderClient {

}
