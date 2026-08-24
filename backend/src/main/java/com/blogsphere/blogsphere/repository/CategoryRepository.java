package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
