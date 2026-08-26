package com.blogsphere.blogsphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BlogsphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogsphereApplication.class, args);
	}

}
