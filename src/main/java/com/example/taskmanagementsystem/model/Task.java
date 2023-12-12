package com.example.taskmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @NotNull(message = "Author cannot be null")
    @ManyToOne()
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne()
    @JoinColumn(name = "executor_id")
    private User executor;

    @OneToMany(mappedBy = "taskId")
    private List<Comment> comments;
}
