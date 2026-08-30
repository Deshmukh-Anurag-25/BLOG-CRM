package com.blogsphere.blogsphere.event;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PostEventListener {

    @RabbitListener(queues = RabbitMQConfig.POST_CREATED_QUEUE)
    public void handlePostCreated(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Post ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.POST_UPDATED_QUEUE)
    public void handlePostUpdated(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Post ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.POST_DELETED_QUEUE)
    public void handlePostDeleted(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Post ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.POST_PUBLISHED_QUEUE)
    public void handlePostPublished(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Post ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.POST_UNPUBLISHED_QUEUE)
    public void handlePostUnpublished(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Post ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.POST_ARCHIVED_QUEUE)
    public void handlePostArchived(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Post ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }
}