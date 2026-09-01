package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.config.RabbitMQConfig;
import com.blogsphere.blogsphere.dto.AutosaveRequest;
import com.blogsphere.blogsphere.dto.PostRequest;
import com.blogsphere.blogsphere.event.EventEnvelope;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.*;
import com.blogsphere.blogsphere.repository.*;
import com.blogsphere.blogsphere.security.CurrentUserProvider;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final RevisionRepository revisionRepository;
    private final FollowRepository followRepository;
    private final CurrentUserProvider currentUserProvider;
    private final RabbitTemplate rabbitTemplate;
    private final EmailService emailService;

    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, TagRepository tagRepository,
                       RevisionRepository revisionRepository, FollowRepository followRepository,
                       CurrentUserProvider currentUserProvider, RabbitTemplate rabbitTemplate, EmailService emailService){
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.revisionRepository = revisionRepository;
        this.followRepository = followRepository;
        this.currentUserProvider = currentUserProvider;
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
    }

    public Post createPost(PostRequest request){
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setContent(request.getContent());
        post.setExcerpt(request.getExcerpt());

        User author = currentUserProvider.getUser();
        post.setAuthor(author);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        }

        if (request.getTagIds() != null) {
            Set<Tags> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            post.setTags(tags);
        }

        Post savedPost = postRepository.save(post);

        emailService.sendPostCreatedEmail(author.getEmail(), author.getDisplayName(), savedPost.getTitle());

        EventEnvelope<Long> event = new EventEnvelope<>("POST_CREATED", savedPost.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_CREATED_ROUTING_KEY,
                event
        );

        return savedPost;
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Post getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public Post updatePost(Long id, PostRequest request){
        Post post = getPostById(id);
        User currentUser = currentUserProvider.getUser();

        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to edit this post");
        }

        Revision revision = new Revision();
        revision.setPost(post);
        revision.setTitle(post.getTitle());
        revision.setContent(post.getContent());
        revision.setEditedBy(currentUser);
        revisionRepository.save(revision);

        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getSlug() != null) {
            post.setSlug(request.getSlug());
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getExcerpt() != null) {
            post.setExcerpt(request.getExcerpt());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            post.setCategory(category);
        }
        if (request.getTagIds() != null) {
            Set<Tags> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            post.setTags(tags);
        }

        Post updatedPost = postRepository.save(post);

        EventEnvelope<Long> updatedEvent = new EventEnvelope<>("POST_UPDATED", updatedPost.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_UPDATED_ROUTING_KEY,
                updatedEvent
        );

        return updatedPost;
    }

    @Transactional
    public void deletePost(Long id){
        Post post = getPostById(id);
        User currentUser = currentUserProvider.getUser();

        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }

        Long deletedPostId = post.getId();

        revisionRepository.deleteByPostId(deletedPostId);
        postRepository.delete(post);

        EventEnvelope<Long> deletedEvent = new EventEnvelope<>("POST_DELETED", deletedPostId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_DELETED_ROUTING_KEY,
                deletedEvent
        );
    }

    public Post publishPost(Long id){
        Post post = getPostById(id);
        post.setStatus(PostStatus.PUBLISHED);
        Post publishedPost = postRepository.save(post);

        notifyFollowersOfPublish(publishedPost);

        EventEnvelope<Long> event = new EventEnvelope<>("POST_PUBLISHED", publishedPost.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_PUBLISHED_ROUTING_KEY,
                event
        );

        return publishedPost;
    }

    public Post unpublishPost(Long id){
        Post post = getPostById(id);
        post.setStatus(PostStatus.DRAFT);
        Post unpublishedPost = postRepository.save(post);

        EventEnvelope<Long> event = new EventEnvelope<>("POST_UNPUBLISHED", unpublishedPost.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_UNPUBLISHED_ROUTING_KEY,
                event
        );

        return unpublishedPost;
    }

    public Post archivePost(Long id){
        Post post = getPostById(id);
        post.setStatus(PostStatus.ARCHIVED);
        Post archivedPost = postRepository.save(post);

        EventEnvelope<Long> event = new EventEnvelope<>("POST_ARCHIVED", archivedPost.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_ARCHIVED_ROUTING_KEY,
                event
        );

        return archivedPost;
    }

    public List<Revision> getRevisions(Long postId) {
        return revisionRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    public Post autosavePost(Long id, AutosaveRequest request) {
        Post post = getPostById(id);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    public Post schedulePost(Long id, LocalDateTime scheduledAt) {
        Post post = getPostById(id);
        post.setScheduledAt(scheduledAt);
        post.setStatus(PostStatus.SCHEDULED);
        return postRepository.save(post);
    }

    /**
     * Notifies everyone who follows the post's author that a new post is live.
     * Called both from the direct publish path and from PostSchedulerService
     * when a scheduled post actually goes live.
     */
    @Transactional
    public void notifyFollowersOfPublish(Post post) {
        User author = post.getAuthor();
        String authorDisplayName = author.getDisplayName();
        String authorUsername = author.getUsername();
        String postTitle = post.getTitle();

        for (Follow follow : followRepository.findByFollowingId(author.getId())) {
            User follower = follow.getFollower();
            emailService.sendNewPostFromFollowedUserEmail(
                    follower.getEmail(), follower.getDisplayName(),
                    authorDisplayName, authorUsername, postTitle
            );
        }
    }

    public void publishDueScheduledPostEvent(Post post) {
        EventEnvelope<Long> event = new EventEnvelope<>("POST_PUBLISHED", post.getId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_EXCHANGE,
                RabbitMQConfig.POST_PUBLISHED_ROUTING_KEY,
                event
        );
    }
}
