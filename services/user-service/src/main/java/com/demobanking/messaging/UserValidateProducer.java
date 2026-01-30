package com.demobanking.messaging;

import com.demobanking.events.UserValidatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidateProducer {

    private final KafkaTemplate<String, Object> template;

    public void publishUserValidateEvents(boolean validated){
        UserValidatedEvent userValidatedEvent = new UserValidatedEvent(0L, validated);
        template.send("USER_VALIDATED_TOPIC", userValidatedEvent);
    }
}
