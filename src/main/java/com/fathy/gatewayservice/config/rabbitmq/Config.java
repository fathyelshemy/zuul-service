package com.fathy.gatewayservice.config.rabbitmq;

import com.fathy.gatewayservice.config.rabbitmq.exceptions.CustomErrorHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.util.ErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config implements RabbitListenerConfigurer {
    //set dead letter queue and exchange
    public final static String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
    public final static String DEAD_LETTER_QUEUE = "dead.letter.queue";
    public final static String DEAD_LETTER_ROUTING_KEY = "dead.letter.key";
    public static Map<String, Object> deadLetterArgs = new HashMap<String, Object>();

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }
    @Bean
    Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
    }
    //init deadLetter args
    static {
        deadLetterArgs.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        deadLetterArgs.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
    }

    public static final String EXCHANGE_COMBO_USER_TO_BE_DELETED = "combo.user.to.be.deleted.exchange";
    public static final String QUEUE_COMBO_USER_TO_BE_DELETED = "combo.user.to.be.deleted.queue";
    public static final String ROUTING_KEY_COMBO_USER_TO_BE_DELETED = "combo.user.to.be.deleted.key";

    public static final String EXCHANGE_COMBO_UPDATED_TOKEN = "combo.updated.token.exchange";
    public static final String QUEUE_COMBO_UPDATED_TOKEN = "combo.updated.token.qeueu";
    public static final String ROUTING_KEY_COMBO_UPDATED_TOKEN = "combo.updated.token.key";

    @Bean
    DirectExchange userToBeDeletedExchange() {
        return new DirectExchange(EXCHANGE_COMBO_USER_TO_BE_DELETED);
    }

    @Bean
    Queue userToBeDeletedQueue() {
        return new Queue(QUEUE_COMBO_USER_TO_BE_DELETED, true,false,false,deadLetterArgs);
    }

    @Bean
    Binding bindingUserToBeDeleted(Queue userToBeDeletedQueue, DirectExchange userToBeDeletedExchange) {
        return BindingBuilder.bind(userToBeDeletedQueue).to(userToBeDeletedExchange).with(ROUTING_KEY_COMBO_USER_TO_BE_DELETED);
    }

    @Bean
    DirectExchange updatedTokenExchange() {
        return new DirectExchange(EXCHANGE_COMBO_UPDATED_TOKEN);
    }

    @Bean
    Queue updatedTokenQueue() {
        return new Queue(QUEUE_COMBO_UPDATED_TOKEN,true,false,false,deadLetterArgs);
    }

    @Bean
    Binding bindingUpdatedToken(Queue updatedTokenQueue, DirectExchange updatedTokenExchange) {
        return BindingBuilder.bind(updatedTokenQueue).to(updatedTokenExchange).with(ROUTING_KEY_COMBO_UPDATED_TOKEN);
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    //set errorHandler
    @Bean
    public ErrorHandler errorHandler() {
        return new CustomErrorHandler();
    }
    //add errorHandler to container factory listener
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setErrorHandler(errorHandler());
        return factory;
    }

}
