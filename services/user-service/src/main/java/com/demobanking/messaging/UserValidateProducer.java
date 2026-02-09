package com.demobanking.messaging;

import com.demobanking.dto.UserDTO;
import com.demobanking.events.Users.UserValidatedEvent;
import com.google.protobuf.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidateProducer {

    private final KafkaTemplate<String, Message> template;

    public void publishUserValidated(UserDTO userDTO, String sagaId){
        UserValidatedEvent userValidatedEvent = UserValidatedEvent.newBuilder()
                .setUserId(userDTO.getId())
                .setSagaId(sagaId)
                .setValidated(true)
                .build();
        template.send("USER_VALIDATED_TOPIC", userValidatedEvent);
    }

    public void publishUserNotValidated(boolean validated){
        UserValidatedEvent userValidatedEvent = UserValidatedEvent.newBuilder()
                .setUserId(0L)
                .setValidated(validated)
                .build();
        template.send("USER_NOT_VALIDATED_TOPIC", userValidatedEvent);
    }
}
