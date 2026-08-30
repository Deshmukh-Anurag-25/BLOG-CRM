package com.blogsphere.blogsphere.event;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FollowEventListener {

    @RabbitListener(queues = RabbitMQConfig.FOLLOW_CREATED_QUEUE)
    public void handleFollowCreated(EventEnvelope<FollowPayload> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Follower ID: " + event.getPayload().getFollowerId());
        System.out.println("Following ID: " + event.getPayload().getFollowingId());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.FOLLOW_DELETED_QUEUE)
    public void handleFollowDeleted(EventEnvelope<FollowPayload> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Follower ID: " + event.getPayload().getFollowerId());
        System.out.println("Following ID: " + event.getPayload().getFollowingId());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }
}
