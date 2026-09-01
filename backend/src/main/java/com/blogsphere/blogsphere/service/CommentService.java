package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import com.blogsphere.blogsphere.dto.CommentRequest;
import com.blogsphere.blogsphere.event.EventEnvelope;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Comments;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.Role;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.CommentRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.security.CurrentUserProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CurrentUserProvider currentUserProvider;
    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CurrentUserProvider currentUserProvider, RabbitTemplate rabbitTemplate, EmailService emailService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.currentUserProvider = currentUserProvider;
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
    }

    public Comments createComment(CommentRequest request){
        Comments comment = new Comments();
        comment.setContent(request.getContent());

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + request.getPostId()));
        comment.setPost(post);

        User author = currentUserProvider.getUser();
        comment.setAuthor(author);

        Comments savedComment = commentRepository.save(comment);

        User postAuthor = post.getAuthor();
        if (!postAuthor.getId().equals(author.getId())) {
            emailService.sendNewCommentEmail(postAuthor.getEmail(), postAuthor.getDisplayName(),
                    author.getDisplayName(), author.getUsername(), post.getTitle());
        }

        EventEnvelope<Long> event = new EventEnvelope<>("COMMENT_CREATED", savedComment.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMENT_EXCHANGE,
                RabbitMQConfig.COMMENT_CREATED_ROUTING_KEY,
                event
        );

        return savedComment;
    }

    public List<Comments> getCommentsByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }

    public Comments updateComment(Long id, CommentRequest request) {
        Comments comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        User currentUser = currentUserProvider.getUser();
        boolean isOwner = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to edit this comment");
        }

        if (request.getContent() != null) {
            comment.setContent(request.getContent());
        }
        Comments updatedComment = commentRepository.save(comment);

        EventEnvelope<Long> event = new EventEnvelope<>("COMMENT_UPDATED", updatedComment.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMENT_EXCHANGE,
                RabbitMQConfig.COMMENT_UPDATED_ROUTING_KEY,
                event
        );

        return updatedComment;
    }

    public void deleteComment(Long id) {
        Comments comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        User currentUser = currentUserProvider.getUser();
        boolean isOwner = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this comment");
        }

        Long deletedCommentId = comment.getId();

        commentRepository.delete(comment);

        EventEnvelope<Long> event = new EventEnvelope<>("COMMENT_DELETED", deletedCommentId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.COMMENT_EXCHANGE,
                RabbitMQConfig.COMMENT_DELETED_ROUTING_KEY,
                event
        );
    }
}
