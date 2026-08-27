package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.TagRequest;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Tags;
import com.blogsphere.blogsphere.repository.TagRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tags createTag(TagRequest request){
        Tags tag = new Tags();
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        return tagRepository.save(tag);
    }

    public List<Tags> getAllTags(){
        return tagRepository.findAll();
    }

    public Tags getTagById(Long id){
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
    }

    public Tags updateTag(Long id, TagRequest request) {
        Tags tag = getTagById(id);

        if (request.getName() != null) {
            tag.setName(request.getName());
        }
        if (request.getSlug() != null) {
            tag.setSlug(request.getSlug());
        }

        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        Tags tag = getTagById(id);
        tagRepository.delete(tag);
    }
}
