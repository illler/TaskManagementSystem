package com.example.taskmanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String commentId;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    @NotNull(message = "User ID cannot be null")
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User userId;

    @NotNull(message = "Task ID cannot be null")
    @ManyToOne()
    @JoinColumn(name = "task_id")
    private Task taskId;
}
