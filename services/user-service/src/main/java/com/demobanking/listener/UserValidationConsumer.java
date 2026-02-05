package com.demobanking.listener;

import com.demobanking.events.Users.UserValidatedEvent;
import com.demobanking.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidationConsumer {
    private final IUserService userService;

    @KafkaListener(topics = "USER_VALIDATED_CMD_TOPIC", groupId = "USER_EVENT_GROUP")
    public void onUserValidateEvent(UserValidatedEvent event){

        if(event.getValidated()) userService.registerLogActivity(event.getUserId());
        // rollback
        else userService.retrieveUserById(event.getUserId());
    }
}
