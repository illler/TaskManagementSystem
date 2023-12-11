package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.error.NotFoundException;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.repositories.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;

    public List<Comment> getAllCommentsByTask(String taskId) {
        if (taskId == null || taskId.isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        List<Comment> comments = commentsRepository.findAllByTaskId(taskId);
        if (comments.isEmpty()) {
            throw new NotFoundException("No comments found for task with ID: " + taskId);
        }
        return comments;
    }

    @Transactional
    public void addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        commentsRepository.save(comment);
    }
}
