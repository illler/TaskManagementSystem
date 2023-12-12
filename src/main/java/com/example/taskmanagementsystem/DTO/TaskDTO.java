package com.example.taskmanagementsystem.DTO;

import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private String taskId;

    private String heading;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private String authorId;

    private String executorId;
}
