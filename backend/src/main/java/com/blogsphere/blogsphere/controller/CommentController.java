package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.CommentRequest;
import com.blogsphere.blogsphere.model.Comments;
import com.blogsphere.blogsphere.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comments createComment(@RequestBody CommentRequest request){
        return commentService.createComment(request);
    }

    @GetMapping("/post/{postId}")
    public List<Comments> getCommentsByPostId(@PathVariable Long postId){
        return commentService.getCommentsByPostId(postId);
    }
}
