package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.CommentRequest;
import com.blogsphere.blogsphere.model.Comments;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.CommentRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comments createComment(CommentRequest request){
        Comments comment = new Comments();
        comment.setContent(request.getContent());

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setPost(post);

        User author = userRepository.findById(1L).orElseThrow();
        comment.setAuthor(author);

        return commentRepository.save(comment);
    }
    public List<Comments> getCommentsByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
}
