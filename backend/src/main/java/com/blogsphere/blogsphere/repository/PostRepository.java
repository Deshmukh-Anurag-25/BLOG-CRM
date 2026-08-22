package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
