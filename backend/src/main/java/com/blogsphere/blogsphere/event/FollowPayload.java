package com.blogsphere.blogsphere.event;

public class FollowPayload {

    private Long followerId;
    private Long followingId;

    public FollowPayload() {
    }

    public FollowPayload(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public Long getFollowerId() { return followerId; }
    public Long getFollowingId() { return followingId; }
}
