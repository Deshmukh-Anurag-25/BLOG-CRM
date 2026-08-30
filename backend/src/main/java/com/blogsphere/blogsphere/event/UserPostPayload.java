package com.blogsphere.blogsphere.event;

public class UserPostPayload {

    private Long userId;
    private Long postId;

    public UserPostPayload() {
    }

    public UserPostPayload(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public Long getUserId() { return userId; }
    public Long getPostId() { return postId; }
}
