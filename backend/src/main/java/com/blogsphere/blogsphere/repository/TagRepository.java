package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tags, Long> {
}
