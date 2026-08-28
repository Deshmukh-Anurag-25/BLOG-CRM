package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Follow;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.FollowRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import com.blogsphere.blogsphere.security.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    public FollowService(FollowRepository followRepository, UserRepository userRepository, CurrentUserProvider currentUserProvider) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Follow followUser(Long followingId) {

        User follower = currentUserProvider.getUser();

        if (followingId.equals(follower.getId())) {
            throw new RuntimeException("Cannot follow yourself");
        }

        if (followRepository.findByFollowerIdAndFollowingId(follower.getId(), followingId).isPresent()) {
            throw new RuntimeException("Already following");
        }

        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + followingId));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        return followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long followingId) {
        User user = currentUserProvider.getUser();
        followRepository.deleteByFollowerIdAndFollowingId(user.getId(), followingId);
    }

    public long getFollowerCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }
}