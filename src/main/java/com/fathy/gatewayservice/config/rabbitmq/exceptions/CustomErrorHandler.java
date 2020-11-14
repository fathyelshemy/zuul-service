package com.fathy.gatewayservice.config.rabbitmq.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

public class CustomErrorHandler implements ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("kibana-logger");

    @Override
    public void handleError(Throwable t) {
            LOGGER.error("Error sending message check dead.letter.queue for details");
            throw new AmqpRejectAndDontRequeueException("Error Handler converted exception to fatal",t);
    }
}

