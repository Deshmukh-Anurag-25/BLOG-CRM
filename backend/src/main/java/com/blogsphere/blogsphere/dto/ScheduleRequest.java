package com.blogsphere.blogsphere.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequest {
    private LocalDateTime scheduledAt;
}