package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Like;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.LikeRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.security.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CurrentUserProvider currentUserProvider;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, CurrentUserProvider currentUserProvider) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Like likePost(Long postId){
        User user = currentUserProvider.getUser();

        if(likeRepository.findByUserIdAndPostId(user.getId(), postId).isPresent()){
            throw new RuntimeException("Already Liked");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        return likeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long postId){
        User user = currentUserProvider.getUser();
        likeRepository.deleteByUserIdAndPostId(user.getId(), postId);
    }

    public long getLikeCount(Long postId){
        return likeRepository.countByPostId(postId);
    }
}
