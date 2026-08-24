package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.TagRequest;
import com.blogsphere.blogsphere.model.Tags;
import com.blogsphere.blogsphere.service.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public Tags createTag(@RequestBody TagRequest request){
        return tagService.createTag(request);
    }

    @GetMapping
    public List<Tags> getAllTags(){
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Tags getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }
}
