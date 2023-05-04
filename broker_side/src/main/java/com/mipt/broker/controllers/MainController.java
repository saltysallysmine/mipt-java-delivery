package com.mipt.broker.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
@RestController
@RequestMapping("/delivery-broker")
public class MainController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Data
    private static class OrderRequest {
        String city;
    }

    @PostMapping("/log")
    @ResponseBody
    public void RedirectDelivery(@NotNull @RequestBody OrderRequest orderRequest) {
        log.info("Get message in broker");
    }

}
