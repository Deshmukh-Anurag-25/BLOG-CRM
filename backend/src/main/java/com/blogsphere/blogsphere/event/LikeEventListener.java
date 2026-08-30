package com.blogsphere.blogsphere.event;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LikeEventListener {

    @RabbitListener(queues = RabbitMQConfig.LIKE_CREATED_QUEUE)
    public void handleLikeCreated(EventEnvelope<UserPostPayload> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("User ID: " + event.getPayload().getUserId());
        System.out.println("Post ID: " + event.getPayload().getPostId());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.LIKE_DELETED_QUEUE)
    public void handleLikeDeleted(EventEnvelope<UserPostPayload> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("User ID: " + event.getPayload().getUserId());
        System.out.println("Post ID: " + event.getPayload().getPostId());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }
}
