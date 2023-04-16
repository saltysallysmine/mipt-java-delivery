package com.mipt.delivery.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokerConfiguration {

    public static final String MOSCOW = "MOSCOW";
    public static final String SAINT_PETERSBURG = "SAINT_PETERSBURG";

    @Bean
    public Queue SaintPetersburgQueue() {
        return new Queue(SAINT_PETERSBURG, true);
    }

    @Bean
    public Queue MoscowQueue() {
        return new Queue(MOSCOW, true);
    }

}
