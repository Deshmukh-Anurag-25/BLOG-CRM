package com.blogsphere.blogsphere.event;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    @RabbitListener(queues = RabbitMQConfig.USER_DELETED_QUEUE)
    public void handleUserDeleted(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("User ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }
}
