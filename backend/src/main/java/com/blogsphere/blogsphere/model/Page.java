package com.blogsphere.blogsphere.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "pages")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    //runs this method just before a new user is inserted into the database
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    //run this method just before an existing user is updated in the database
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private PageStatus status = PageStatus.DRAFT;
}
