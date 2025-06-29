package com.sau.swe.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TWO_FACTOR_EMAIL_QUEUE = "two-factor-email-queue";
    public static final String TWO_FACTOR_EMAIL_EXCHANGE = "two-factor-email-exchange";
    public static final String TWO_FACTOR_EMAIL_ROUTING_KEY = "two-factor-email-routing-key";

    @Bean
    public Queue twoFactorEmailQueue() {
        return QueueBuilder.durable(TWO_FACTOR_EMAIL_QUEUE).build();
    }

    @Bean
    public DirectExchange twoFactorEmailExchange() {
        return new DirectExchange(TWO_FACTOR_EMAIL_EXCHANGE);
    }

    @Bean
    public Binding twoFactorEmailBinding() {
        return BindingBuilder
                .bind(twoFactorEmailQueue())
                .to(twoFactorEmailExchange())
                .with(TWO_FACTOR_EMAIL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 