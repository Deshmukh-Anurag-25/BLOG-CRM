package com.blogsphere.blogsphere.controller;

import com.blogsphere.blogsphere.dto.PageRequest;
import com.blogsphere.blogsphere.model.Page;
import com.blogsphere.blogsphere.service.PageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pages")
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @PostMapping
    public Page createPage(@RequestBody PageRequest request) {
        return pageService.createPage(request);
    }

    @GetMapping
    public List<Page> getAllPages() {
        return pageService.getAllPages();
    }

    @GetMapping("/{id}")
    public Page getPageById(@PathVariable Long id) {
        return pageService.getPageById(id);
    }

    @PutMapping("/{id}")
    public Page updatePage(@PathVariable Long id, @RequestBody PageRequest request) {
        return pageService.updatePage(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePage(@PathVariable Long id) {
        pageService.deletePage(id);
    }

    @PostMapping("/{id}/publish")
    public Page publishPage(@PathVariable Long id) {
        return pageService.publishPage(id);
    }
}