package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.model.Like;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.LikeRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Like likePost(Long postId){
        if(likeRepository.findByUserIdAndPostId(1L, postId).isPresent()){
            throw new RuntimeException("Already Liked");
        }

        User user = userRepository.findById(1L).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        return likeRepository.save(like);
    }

    @Transactional
    public void unlikePost(Long postId){
        likeRepository.deleteByUserIdAndPostId(1L, postId);
    }

    public long getLikeCount(Long postId){
        return likeRepository.countByPostId(postId);
    }
}
