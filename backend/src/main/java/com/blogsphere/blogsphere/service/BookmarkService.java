package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.exception.ResourceNotFoundException;
import com.blogsphere.blogsphere.model.Bookmark;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.BookmarkRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.security.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final CurrentUserProvider currentUserProvider;

    public BookmarkService(BookmarkRepository bookmarkRepository, PostRepository postRepository, CurrentUserProvider currentUserProvider) {
        this.bookmarkRepository = bookmarkRepository;
        this.postRepository = postRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Bookmark bookmarkPost(Long postId){
        User user = currentUserProvider.getUser();
        if(bookmarkRepository.findByUserIdAndPostId(user.getId(), postId).isPresent()){
            throw new RuntimeException("Already Added to bookmark");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setPost(post);

        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long postId){
        User user = currentUserProvider.getUser();
        bookmarkRepository.deleteByUserIdAndPostId(user.getId(), postId);
    }

    public long getBookmarkCount(Long postId){
        return bookmarkRepository.countByPostId(postId);
    }
}
