package com.blogsphere.blogsphere.event;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener {

    @RabbitListener(queues = RabbitMQConfig.COMMENT_CREATED_QUEUE)
    public void handleCommentCreated(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Comment ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.COMMENT_UPDATED_QUEUE)
    public void handleCommentUpdated(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Comment ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.COMMENT_DELETED_QUEUE)
    public void handleCommentDeleted(EventEnvelope<Long> event) {
        System.out.println("=========================================");
        System.out.println("RECEIVED EVENT: " + event.getEventType());
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Comment ID: " + event.getPayload());
        System.out.println("Timestamp: " + event.getTimestamp());
        System.out.println("=========================================");
    }
}
