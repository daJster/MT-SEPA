package com.sepa.payment.system.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.transaction.name:transaction-processing-queue}")
    private String transactionQueueName;

    @Value("${rabbitmq.exchange.name:transaction-exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key:transaction.process}")
    private String routingKey;

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(transactionQueueName).build();
    }

    @Bean
    public DirectExchange transactionExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder
                .bind(transactionQueue())
                .to(transactionExchange())
                .with(routingKey);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}