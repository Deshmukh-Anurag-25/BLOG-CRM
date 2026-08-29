package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.AutosaveRequest;
import com.blogsphere.blogsphere.dto.PostRequest;
import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.*;
import com.blogsphere.blogsphere.repository.*;
import com.blogsphere.blogsphere.security.CurrentUserProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final RevisionRepository revisionRepository;
    private final CurrentUserProvider currentUserProvider;

    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, TagRepository tagRepository, RevisionRepository revisionRepository, CurrentUserProvider currentUserProvider){
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.revisionRepository = revisionRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Post createPost(PostRequest request){
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setContent(request.getContent());
        post.setExcerpt(request.getExcerpt());

        User author = currentUserProvider.getUser();
        post.setAuthor(author);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        }

        if (request.getTagIds() != null) {
            Set<Tags> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            post.setTags(tags);
        }

        return postRepository.save(post);
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public Post getPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    public Post updatePost(Long id, PostRequest request){
        Post post = getPostById(id);
        User currentUser = currentUserProvider.getUser();

        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to edit this post");
        }

        Revision revision = new Revision();
        revision.setPost(post);
        revision.setTitle(post.getTitle());
        revision.setContent(post.getContent());
        revision.setEditedBy(currentUser);
        revisionRepository.save(revision);

        if (request.getTitle() != null) {
            post.setTitle(request.getTitle());
        }
        if (request.getSlug() != null) {
            post.setSlug(request.getSlug());
        }
        if (request.getContent() != null) {
            post.setContent(request.getContent());
        }
        if (request.getExcerpt() != null) {
            post.setExcerpt(request.getExcerpt());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            post.setCategory(category);
        }
        if (request.getTagIds() != null) {
            Set<Tags> tags = new HashSet<>(tagRepository.findAllById(request.getTagIds()));
            post.setTags(tags);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long id){
        Post post = getPostById(id);
        User currentUser = currentUserProvider.getUser();

        boolean isOwner = post.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    public Post publishPost(Long id){
        Post post = getPostById(id);
        post.setStatus(PostStatus.PUBLISHED);
        return postRepository.save(post);
    }

    public Post unpublishPost(Long id){
        Post post = getPostById(id);
        post.setStatus(PostStatus.DRAFT);
        return postRepository.save(post);
    }

    public Post archivePost(Long id){
        Post post = getPostById(id);
        post.setStatus(PostStatus.ARCHIVED);
        return postRepository.save(post);
    }

    public List<Revision> getRevisions(Long postId) {
        return revisionRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    public Post autosavePost(Long id, AutosaveRequest request) {
        Post post = getPostById(id);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    public Post schedulePost(Long id, LocalDateTime scheduledAt) {
        Post post = getPostById(id);
        post.setScheduledAt(scheduledAt);
        post.setStatus(PostStatus.SCHEDULED);
        return postRepository.save(post);
    }
}
