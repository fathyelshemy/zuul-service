package com.fathy.gatewayservice.config.rabbitmq;

import brave.Tracer;
import com.fathy.gatewayservice.dtos.DeletedUserDto;
import com.fathy.gatewayservice.dtos.UpdatedTokenDto;
import com.fathy.gatewayservice.entities.User;
import com.fathy.gatewayservice.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger("kibana-logger");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Tracer tracer;

    @RabbitListener(queues = Config.QUEUE_COMBO_USER_TO_BE_DELETED)
    public void receiveDeletedUser(final DeletedUserDto deletedUserDto, @Header(value = "traceId", required = false) String traceId) {
        LOGGER.info(String.format("AMQP, CONSUME, deleted user to be unauthorized, %s", deletedUserDto.getEmail()));
        final long spanId = tracer.startScopedSpan("span" + UUID.randomUUID().toString()).context().spanId();
        MDC.put("X-B3-TraceId", traceId);
        MDC.put("X-B3-SpanId", Long.toHexString(spanId));
        User user = userRepository.findByEmail(deletedUserDto.getEmail());
        if(Objects.nonNull(user)) //Existing user
            user.setDeleted(deletedUserDto.isDeleted());
        else //New user
            user = new User(deletedUserDto.getEmail(), null, deletedUserDto.isDeleted());
        userRepository.save(user);

    }

    @RabbitListener(queues = Config.QUEUE_COMBO_UPDATED_TOKEN)
    public void receiveUpdatedToken(UpdatedTokenDto updatedTokenDto) {
        LOGGER.info("AMQP, CONSUME, token for user {} was last updated on {}",  updatedTokenDto.getUserEmail(),
                updatedTokenDto.getLastUpdated());
        User user = userRepository.findByEmail(updatedTokenDto.getUserEmail());
        if(Objects.nonNull(user)) //Existing user
            user.setTokenLastUpdated(updatedTokenDto.getLastUpdated());
        else //New user
            user = new User(updatedTokenDto.getUserEmail(), updatedTokenDto.getLastUpdated());
        userRepository.save(user);
    }
}


