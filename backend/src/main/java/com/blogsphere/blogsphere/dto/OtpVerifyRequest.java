package com.blogsphere.blogsphere.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @NotBlank(message = "otp is required")
    private String otp;
}
