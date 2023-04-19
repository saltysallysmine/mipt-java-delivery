package com.mipt.broker.controllers;

import com.mipt.broker.service.City;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.rabbitmq.client.Connection;
import com.mipt.broker.configuration.BrokerConfiguration;
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

    /*
     * This controller redirects POST-requests from different cities to their Rabbit queues.
     *
     * Be careful to replace the hyphen with an underscore in compound names.
     */
    @PostMapping("/order")
    @ResponseBody
    public ResponseEntity<String> RedirectDelivery(@NotNull @RequestBody OrderRequest orderRequest) throws IOException, TimeoutException {
        String cityName = orderRequest.getCity().toUpperCase();
        log.info("Get message from city " + cityName);
        ConnectionFactory factory = new ConnectionFactory();
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            City city = City.of(cityName);
            if (city.equals(City.CityEnum.MOSCOW)) {
                log.info("Send message to Moscow queue");
                channel.basicPublish("", BrokerConfiguration.MOSCOW, null,
                        cityName.getBytes());
            } else if (city.equals(City.CityEnum.SAINT_PETERSBURG)) {
                log.info("Send message to Saint-Petersburg queue");
                channel.basicPublish("", BrokerConfiguration.SAINT_PETERSBURG, null,
                        cityName.getBytes());
            } else {
                log.warn("Unknown city " + cityName + ". Request denied.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("This city does not yet have our delivery. Maybe you made a typo? " +
                                "Be careful to replace the hyphen with an underscore in compound names.");
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
