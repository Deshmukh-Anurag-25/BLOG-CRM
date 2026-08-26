package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.PostStatus;
import com.blogsphere.blogsphere.repository.PostRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostSchedulerService {

    private final PostRepository postRepository;

    public PostSchedulerService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void publishScheduledPosts() {
        List<Post> duePosts = postRepository.findByStatusAndScheduledAtBefore(
                PostStatus.SCHEDULED, LocalDateTime.now());

        for (Post post : duePosts) {
            post.setStatus(PostStatus.PUBLISHED);
            postRepository.save(post);
        }
    }
}