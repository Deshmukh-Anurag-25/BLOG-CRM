package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);
    long countByPostId(Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
    void deleteByPostId(Long postId);
    void deleteByUserId(Long userId);
}