package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.PageRequest;
import com.blogsphere.blogsphere.model.Page;
import com.blogsphere.blogsphere.model.PageStatus;
import com.blogsphere.blogsphere.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public Page createPage(PageRequest request){
        Page page = new Page();
        page.setTitle(request.getTitle());
        page.setSlug(request.getSlug());
        page.setContent(request.getContent());

        return pageRepository.save(page);
    }

    public List<Page> getAllPages() {
        return pageRepository.findAll();
    }

    public Page getPageById(Long id) {
        return pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found with id: " + id));
    }

    public Page updatePage(Long id, PageRequest request) {
        Page page = getPageById(id);
        page.setTitle(request.getTitle());
        page.setSlug(request.getSlug());
        page.setContent(request.getContent());
        return pageRepository.save(page);
    }

    public void deletePage(Long id) {
        Page page = getPageById(id);
        pageRepository.delete(page);
    }

    public Page publishPage(Long id) {
        Page page = getPageById(id);
        page.setStatus(PageStatus.PUBLISHED);
        return pageRepository.save(page);
    }
}
