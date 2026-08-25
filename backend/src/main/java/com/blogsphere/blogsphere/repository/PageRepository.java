package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
