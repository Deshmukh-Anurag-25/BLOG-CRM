package com.blogsphere.blogsphere.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email
    private String email;

    private String password;
    private String displayName;
    private String bio;
}
