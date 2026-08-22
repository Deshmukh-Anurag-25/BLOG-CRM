package com.blogsphere.blogsphere.repository;

import com.blogsphere.blogsphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
