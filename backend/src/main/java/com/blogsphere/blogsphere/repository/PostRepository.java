package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatusAndScheduledAtBefore(PostStatus status, LocalDateTime time);
}
