//package com.blogsphere.blogsphere.config;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//
//    public static final String POST_EXCHANGE = "post.exchange";
//    public static final String POST_CREATED_QUEUE = "post.created.queue";
//    public static final String POST_CREATED_ROUTING_KEY = "post.created";
//
//    @PostConstruct
//    public void test() {
//        System.out.println("========== RabbitMQConfig LOADED ==========");
//    }
//
//    @Bean
//    public TopicExchange postExchange() {
//        return new TopicExchange(POST_EXCHANGE);
//    }
//
//    @Bean
//    public Queue postCreatedQueue() {
//        return new Queue(POST_CREATED_QUEUE, true);
//    }
//
//    @Bean
//    public Binding postCreatedBinding(Queue postCreatedQueue, TopicExchange postExchange) {
//        return BindingBuilder.bind(postCreatedQueue).to(postExchange).with(POST_CREATED_ROUTING_KEY);
//    }
//
//    @Bean
//    public JacksonJsonMessageConverter jsonMessageConverter() {
//        return new JacksonJsonMessageConverter();
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(jsonMessageConverter());
//        return template;
//    }
//}
package com.blogsphere.blogsphere.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ==============================
    // RabbitMQ Constants
    // ==============================

    public static final String POST_EXCHANGE = "post.exchange";

    public static final String POST_CREATED_QUEUE = "post.created.queue";
    public static final String POST_UPDATED_QUEUE = "post.updated.queue";
    public static final String POST_DELETED_QUEUE = "post.deleted.queue";
    public static final String POST_PUBLISHED_QUEUE = "post.published.queue";
    public static final String POST_UNPUBLISHED_QUEUE = "post.unpublished.queue";
    public static final String POST_ARCHIVED_QUEUE = "post.archived.queue";

    public static final String POST_CREATED_ROUTING_KEY = "post.created";
    public static final String POST_UPDATED_ROUTING_KEY = "post.updated";
    public static final String POST_DELETED_ROUTING_KEY = "post.deleted";
    public static final String POST_PUBLISHED_ROUTING_KEY = "post.published";
    public static final String POST_UNPUBLISHED_ROUTING_KEY = "post.unpublished";
    public static final String POST_ARCHIVED_ROUTING_KEY = "post.archived";

    public static final String COMMENT_EXCHANGE = "comment.exchange";

    public static final String COMMENT_CREATED_QUEUE = "comment.created.queue";
    public static final String COMMENT_UPDATED_QUEUE = "comment.updated.queue";
    public static final String COMMENT_DELETED_QUEUE = "comment.deleted.queue";

    public static final String COMMENT_CREATED_ROUTING_KEY = "comment.created";
    public static final String COMMENT_UPDATED_ROUTING_KEY = "comment.updated";
    public static final String COMMENT_DELETED_ROUTING_KEY = "comment.deleted";

    public static final String LIKE_EXCHANGE = "like.exchange";

    public static final String LIKE_CREATED_QUEUE = "like.created.queue";
    public static final String LIKE_DELETED_QUEUE = "like.deleted.queue";

    public static final String LIKE_CREATED_ROUTING_KEY = "like.created";
    public static final String LIKE_DELETED_ROUTING_KEY = "like.deleted";

    public static final String BOOKMARK_EXCHANGE = "bookmark.exchange";

    public static final String BOOKMARK_CREATED_QUEUE = "bookmark.created.queue";
    public static final String BOOKMARK_DELETED_QUEUE = "bookmark.deleted.queue";

    public static final String BOOKMARK_CREATED_ROUTING_KEY = "bookmark.created";
    public static final String BOOKMARK_DELETED_ROUTING_KEY = "bookmark.deleted";

    public static final String FOLLOW_EXCHANGE = "follow.exchange";

    public static final String FOLLOW_CREATED_QUEUE = "follow.created.queue";
    public static final String FOLLOW_DELETED_QUEUE = "follow.deleted.queue";

    public static final String FOLLOW_CREATED_ROUTING_KEY = "follow.created";
    public static final String FOLLOW_DELETED_ROUTING_KEY = "follow.deleted";


    // ==============================
    // Exchange
    // ==============================

    @Bean
    public TopicExchange postExchange() {
        return new TopicExchange(POST_EXCHANGE);
    }


    // ==============================
    // Queue
    // ==============================

    @Bean
    public Queue postCreatedQueue() {
        return new Queue(
                POST_CREATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue postUpdatedQueue() {
        return new Queue(
                POST_UPDATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue postDeletedQueue() {
        return new Queue(
                POST_DELETED_QUEUE,
                true
        );
    }

    @Bean
    public Queue postPublishedQueue() {
        return new Queue(
                POST_PUBLISHED_QUEUE,
                true
        );
    }

    @Bean
    public Queue postUnpublishedQueue() {
        return new Queue(
                POST_UNPUBLISHED_QUEUE,
                true
        );
    }

    @Bean
    public Queue postArchivedQueue() {
        return new Queue(
                POST_ARCHIVED_QUEUE,
                true
        );
    }


    // ==============================
    // Binding
    // ==============================

    @Bean
    public Binding postCreatedBinding(
            Queue postCreatedQueue,
            TopicExchange postExchange) {

        return BindingBuilder
                .bind(postCreatedQueue)
                .to(postExchange)
                .with(POST_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding postUpdatedBinding(
            Queue postUpdatedQueue,
            TopicExchange postExchange) {

        return BindingBuilder
                .bind(postUpdatedQueue)
                .to(postExchange)
                .with(POST_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding postDeletedBinding(
            Queue postDeletedQueue,
            TopicExchange postExchange) {

        return BindingBuilder
                .bind(postDeletedQueue)
                .to(postExchange)
                .with(POST_DELETED_ROUTING_KEY);
    }

    @Bean
    public Binding postPublishedBinding(
            Queue postPublishedQueue,
            TopicExchange postExchange) {

        return BindingBuilder
                .bind(postPublishedQueue)
                .to(postExchange)
                .with(POST_PUBLISHED_ROUTING_KEY);
    }

    @Bean
    public Binding postUnpublishedBinding(
            Queue postUnpublishedQueue,
            TopicExchange postExchange) {

        return BindingBuilder
                .bind(postUnpublishedQueue)
                .to(postExchange)
                .with(POST_UNPUBLISHED_ROUTING_KEY);
    }

    @Bean
    public Binding postArchivedBinding(
            Queue postArchivedQueue,
            TopicExchange postExchange) {

        return BindingBuilder
                .bind(postArchivedQueue)
                .to(postExchange)
                .with(POST_ARCHIVED_ROUTING_KEY);
    }


    // ==============================
    // Comment Exchange / Queue / Binding
    // ==============================

    @Bean
    public TopicExchange commentExchange() {
        return new TopicExchange(COMMENT_EXCHANGE);
    }

    @Bean
    public Queue commentCreatedQueue() {
        return new Queue(
                COMMENT_CREATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue commentUpdatedQueue() {
        return new Queue(
                COMMENT_UPDATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue commentDeletedQueue() {
        return new Queue(
                COMMENT_DELETED_QUEUE,
                true
        );
    }

    @Bean
    public Binding commentCreatedBinding(
            Queue commentCreatedQueue,
            TopicExchange commentExchange) {

        return BindingBuilder
                .bind(commentCreatedQueue)
                .to(commentExchange)
                .with(COMMENT_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding commentUpdatedBinding(
            Queue commentUpdatedQueue,
            TopicExchange commentExchange) {

        return BindingBuilder
                .bind(commentUpdatedQueue)
                .to(commentExchange)
                .with(COMMENT_UPDATED_ROUTING_KEY);
    }

    @Bean
    public Binding commentDeletedBinding(
            Queue commentDeletedQueue,
            TopicExchange commentExchange) {

        return BindingBuilder
                .bind(commentDeletedQueue)
                .to(commentExchange)
                .with(COMMENT_DELETED_ROUTING_KEY);
    }


    // ==============================
    // Like Exchange / Queue / Binding
    // ==============================

    @Bean
    public TopicExchange likeExchange() {
        return new TopicExchange(LIKE_EXCHANGE);
    }

    @Bean
    public Queue likeCreatedQueue() {
        return new Queue(
                LIKE_CREATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue likeDeletedQueue() {
        return new Queue(
                LIKE_DELETED_QUEUE,
                true
        );
    }

    @Bean
    public Binding likeCreatedBinding(
            Queue likeCreatedQueue,
            TopicExchange likeExchange) {

        return BindingBuilder
                .bind(likeCreatedQueue)
                .to(likeExchange)
                .with(LIKE_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding likeDeletedBinding(
            Queue likeDeletedQueue,
            TopicExchange likeExchange) {

        return BindingBuilder
                .bind(likeDeletedQueue)
                .to(likeExchange)
                .with(LIKE_DELETED_ROUTING_KEY);
    }


    // ==============================
    // Bookmark Exchange / Queue / Binding
    // ==============================

    @Bean
    public TopicExchange bookmarkExchange() {
        return new TopicExchange(BOOKMARK_EXCHANGE);
    }

    @Bean
    public Queue bookmarkCreatedQueue() {
        return new Queue(
                BOOKMARK_CREATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue bookmarkDeletedQueue() {
        return new Queue(
                BOOKMARK_DELETED_QUEUE,
                true
        );
    }

    @Bean
    public Binding bookmarkCreatedBinding(
            Queue bookmarkCreatedQueue,
            TopicExchange bookmarkExchange) {

        return BindingBuilder
                .bind(bookmarkCreatedQueue)
                .to(bookmarkExchange)
                .with(BOOKMARK_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bookmarkDeletedBinding(
            Queue bookmarkDeletedQueue,
            TopicExchange bookmarkExchange) {

        return BindingBuilder
                .bind(bookmarkDeletedQueue)
                .to(bookmarkExchange)
                .with(BOOKMARK_DELETED_ROUTING_KEY);
    }


    // ==============================
    // Follow Exchange / Queue / Binding
    // ==============================

    @Bean
    public TopicExchange followExchange() {
        return new TopicExchange(FOLLOW_EXCHANGE);
    }

    @Bean
    public Queue followCreatedQueue() {
        return new Queue(
                FOLLOW_CREATED_QUEUE,
                true
        );
    }

    @Bean
    public Queue followDeletedQueue() {
        return new Queue(
                FOLLOW_DELETED_QUEUE,
                true
        );
    }

    @Bean
    public Binding followCreatedBinding(
            Queue followCreatedQueue,
            TopicExchange followExchange) {

        return BindingBuilder
                .bind(followCreatedQueue)
                .to(followExchange)
                .with(FOLLOW_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding followDeletedBinding(
            Queue followDeletedQueue,
            TopicExchange followExchange) {

        return BindingBuilder
                .bind(followDeletedQueue)
                .to(followExchange)
                .with(FOLLOW_DELETED_ROUTING_KEY);
    }


    // ==============================
    // JSON Message Converter
    // ==============================

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }


    // ==============================
    // RabbitTemplate
    // ==============================

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory) {

        RabbitTemplate template =
                new RabbitTemplate(connectionFactory);

        template.setMessageConverter(jsonMessageConverter());

        return template;
    }


    // ==============================
    // RabbitAdmin
    // ==============================

    @Bean
    public RabbitAdmin rabbitAdmin(
            ConnectionFactory connectionFactory) {

        RabbitAdmin admin =
                new RabbitAdmin(connectionFactory);

        admin.setAutoStartup(true);

        return admin;
    }


    // ==============================
    // Initialize RabbitMQ
    // ==============================

    @Bean
    public ApplicationRunner rabbitMQInitializer(
            RabbitAdmin rabbitAdmin) {

        return args -> rabbitAdmin.initialize();
    }
}