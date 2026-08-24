package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.model.Bookmark;
import com.blogsphere.blogsphere.model.Post;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.BookmarkRepository;
import com.blogsphere.blogsphere.repository.PostRepository;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, PostRepository postRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Bookmark bookmarkPost(Long postId){
        if(bookmarkRepository.findByUserIdAndPostId(1L, postId).isPresent()){
            throw new RuntimeException("Already Added to bookmark");
        }

        User user = userRepository.findById(1L).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setPost(post);

        return bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Long postId){
        bookmarkRepository.deleteByUserIdAndPostId(1L, postId);
    }

    public long getBookmarkCount(Long postId){
        return bookmarkRepository.countByPostId(postId);
    }
}
