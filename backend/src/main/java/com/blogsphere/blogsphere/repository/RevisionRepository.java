package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Revision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RevisionRepository extends JpaRepository<Revision, Long> {
    List<Revision> findByPostIdOrderByCreatedAtDesc(Long postId);
    void deleteByPostId(Long postId);
    void deleteByEditedById(Long editedById);
}