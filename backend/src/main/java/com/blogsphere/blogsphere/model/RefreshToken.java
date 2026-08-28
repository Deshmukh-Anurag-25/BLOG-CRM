package com.blogsphere.blogsphere.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "refresh_token")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    private Instant createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        if(token == null){
            token = UUID.randomUUID().toString();
        }
    }
}
