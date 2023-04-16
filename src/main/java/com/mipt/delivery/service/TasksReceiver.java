package com.mipt.delivery.service;

import com.mipt.delivery.configuration.BrokerConfiguration;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class TasksReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = BrokerConfiguration.SAINT_PETERSBURG, ackMode = "MANUAL")
    public void receiveFromSaintPetersburg(String text, MessageHeaders headers, Channel channel,
                                           @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException, InterruptedException {
        log.info("Get message from Saint-Petersburg");
        Thread.sleep(1000);
        channel.basicAck(tag, false);
    }

    @RabbitListener(queues = BrokerConfiguration.MOSCOW, ackMode = "MANUAL")
    public void receiveFromMoscow(String text, MessageHeaders headers, Channel channel,
                                  @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException, InterruptedException {
        log.info("Get message from Moscow");
        Thread.sleep(1000);
        channel.basicAck(tag, false);
    }

}