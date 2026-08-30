package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    long countByFollowingId(Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerId(Long followerId);
    void deleteByFollowingId(Long followingId);
}