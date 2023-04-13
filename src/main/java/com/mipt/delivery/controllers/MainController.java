package com.mipt.delivery.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.rabbitmq.client.Connection;
import com.mipt.delivery.configuration.BrokerConfiguration;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class MainController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Data
    private static class OrderRequest {
        String city;
    }

    @PostMapping("/order")
    @ResponseBody
    void RedirectDelivery(@NotNull @RequestBody OrderRequest orderRequest) throws IOException, TimeoutException {
        String city = orderRequest.getCity();
        log.info("Get message from " + city);
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.basicPublish("", BrokerConfiguration.MOSCOW_NAME, null, city.getBytes());
        }
    }

}
