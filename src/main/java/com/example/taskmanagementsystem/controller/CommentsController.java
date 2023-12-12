package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.DTO.CommentDTO;
import com.example.taskmanagementsystem.error.NotFoundException;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.services.CommentsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks/comments")
@RequiredArgsConstructor
@Tag(name = "CommentsController")
public class CommentsController {

    private final CommentsService commentsService;

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getAllCommentsToTask(@PathVariable String taskId) {
        try {
            return ResponseEntity.ok(commentsService.getAllCommentsByTask(taskId));
        } catch (IllegalArgumentException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/add-new-comment")
    public ResponseEntity<?> addNewComment(@RequestBody CommentDTO comment) {
        try {
            commentsService.addComment(comment);
            return ResponseEntity.ok("Comment added successfully");
        } catch (IllegalArgumentException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}