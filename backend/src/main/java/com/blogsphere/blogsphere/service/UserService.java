package com.blogsphere.blogsphere.service;

import com.blogsphere.blogsphere.dto.UserRequest;
import com.blogsphere.blogsphere.model.User;
import com.blogsphere.blogsphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setDisplayName(request.getDisplayName());

        return userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with this id is not found" + id));
    }
}
