package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.AutosaveRequest;
import com.blogsphere.blogsphere.dto.PostRequest;
import com.blogsphere.blogsphere.dto.ScheduleRequest;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.Revision;
import com.blogsphere.blogsphere.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/{id}/publish")
    public Post publishPost(@PathVariable Long id) {
        return postService.publishPost(id);
    }

    @PostMapping("/{id}/unpublish")
    public Post unpublishPost(@PathVariable Long id) {
        return postService.unpublishPost(id);
    }

    @PostMapping("/{id}/archive")
    public Post archivePost(@PathVariable Long id) {
        return postService.archivePost(id);
    }

    @GetMapping("/{id}/revisions")
    public List<Revision> getRevisions(@PathVariable Long id) {
        return postService.getRevisions(id);
    }

    @PatchMapping("/{id}/autosave")
    public Post autosavePost(@PathVariable Long id, @RequestBody AutosaveRequest request) {
        return postService.autosavePost(id, request);
    }

    @PostMapping("/{id}/schedule")
    public Post schedulePost(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        return postService.schedulePost(id, request.getScheduledAt());
    }
}
