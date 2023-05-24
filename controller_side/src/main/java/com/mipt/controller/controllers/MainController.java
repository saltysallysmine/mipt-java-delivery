package com.mipt.controller.controllers;

import com.mipt.controller.service.City;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class MainController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String MOSCOW = "MOSCOW";
    private static final String SAINT_PETERSBURG = "SAINT_PETERSBURG";

    private final Counter requestsFromMoscow;
    private final Counter requestsFromSaintPetersburg;
    private final Counter errors;

    public MainController(MeterRegistry meterRegistry) {
        requestsFromMoscow = meterRegistry.counter("requests_total");
        requestsFromSaintPetersburg = meterRegistry.counter("requests_saint_petersburg");
        errors = meterRegistry.counter("errors");
    }

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
        City city = City.of(cityName);
        if (city.equals(City.CityEnum.MOSCOW)) {
            log.info("Send message to Moscow queue");
            requestsFromMoscow.increment();
            rabbitTemplate.convertAndSend(MOSCOW, cityName);
        } else if (city.equals(City.CityEnum.SAINT_PETERSBURG)) {
            log.info("Send message to Saint-Petersburg queue");
            requestsFromSaintPetersburg.increment();
            rabbitTemplate.convertAndSend(SAINT_PETERSBURG, cityName);
        } else {
            log.warn("Unknown city " + cityName + ". Request denied.");
            errors.increment();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("This city does not yet have our delivery. Maybe you made a typo? " +
                            "Be careful to replace the hyphen with an underscore in compound names.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
