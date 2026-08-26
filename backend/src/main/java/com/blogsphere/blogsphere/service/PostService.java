package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.AutosaveRequest;
import com.blogsphere.blogsphere.dto.PostRequest;
import com.blogsphere.blogsphere.model.*;
import com.blogsphere.blogsphere.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final RevisionRepository revisionRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository, TagRepository tagRepository, RevisionRepository revisionRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.revisionRepository = revisionRepository;
    }

    public Post createPost(PostRequest request){
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setContent(request.getContent());
        post.setExcerpt(request.getExcerpt());

        User author = userRepository.findById(1L).orElseThrow();
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
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public Post updatePost(Long id, PostRequest request){
        Post post = getPostById(id);

        Revision revision = new Revision();
        revision.setPost(post);
        revision.setTitle(post.getTitle());
        revision.setContent(post.getContent());
        revision.setEditedBy(userRepository.findById(1L).orElseThrow());
        revisionRepository.save(revision);

        post.setTitle(request.getTitle());
        post.setSlug(request.getSlug());
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    public void deletePost(Long id){
        Post post = getPostById(id);
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
