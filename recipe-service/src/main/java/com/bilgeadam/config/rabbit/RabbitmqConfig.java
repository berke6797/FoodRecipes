package com.bilgeadam.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {
    @Value("${rabbitmq.exchange}")
    String exchange;
    @Value("${rabbitmq.favoriteCategoryQueue}")
    String favoriteCategoryQueue;
    @Value("${rabbitmq.favoriteCategoryBindingKey}")
    String favoriteCategoryBindingKey;

    @Bean
    Queue favoriteCategoryQueue() {
        return new Queue(favoriteCategoryQueue);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(final Queue favoriteCategoryQueue, final DirectExchange directExchange) {
        return BindingBuilder.bind(favoriteCategoryQueue).to(directExchange).with(favoriteCategoryBindingKey);
    }
}
