package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.PostRequest;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(@RequestBody PostRequest request){
        return postService.createPost(request);
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody PostRequest request){
        return postService.updatePost(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id){
        postService.deletePost(id);
    }
}
