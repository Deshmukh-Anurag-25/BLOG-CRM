package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    List<Like> findByPostId(Long postId);
    List<Like> findByUserId(Long userId);
    long countByPostId(Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
    void deleteByPostId(Long postId);
    void deleteByUserId(Long userId);
}