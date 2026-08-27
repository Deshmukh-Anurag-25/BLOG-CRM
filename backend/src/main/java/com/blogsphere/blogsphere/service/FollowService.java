package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Follow;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.FollowRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public Follow followUser(Long followingId) {
        if (followingId.equals(1L)) {
            throw new RuntimeException("Cannot follow yourself");
        }

        if (followRepository.findByFollowerIdAndFollowingId(1L, followingId).isPresent()) {
            throw new RuntimeException("Already following");
        }

        User follower = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + followingId));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        return followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long followingId) {
        followRepository.deleteByFollowerIdAndFollowingId(1L, followingId);
    }

    public long getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
}