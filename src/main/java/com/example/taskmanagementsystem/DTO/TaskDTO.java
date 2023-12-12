package com.example.taskmanagementsystem.DTO;

import com.example.taskmanagementsystem.model.Priority;
import com.example.taskmanagementsystem.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Heading cannot be blank")
    @Size(min = 3, max = 255, message = "Heading must be between 3 and 255 characters")
    private String heading;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotBlank(message = "Author ID cannot be blank")
    private String authorId;

    private String executorId;
}
