package com.mipt.delivery.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokerConfiguration {

    public static final String MOSCOW_NAME = "moscow_queue";
    public static final String SAINT_PETERSBURG_NAME = "saint_petersburg_queue";

    @Bean
    public Queue SaintPetersburgQueue() {
        return new Queue(SAINT_PETERSBURG_NAME, true);
    }

    @Bean
    public Queue MoscowQueue() {
        return new Queue(MOSCOW_NAME, true);
    }

}
