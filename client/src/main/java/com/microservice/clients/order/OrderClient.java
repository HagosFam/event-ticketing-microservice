package com.microservice.clients.order;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "orders")
public interface OrderClient {

}
