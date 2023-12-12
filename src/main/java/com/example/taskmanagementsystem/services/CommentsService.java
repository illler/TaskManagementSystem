package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.DTO.CommentDTO;
import com.example.taskmanagementsystem.error.NotFoundException;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.repositories.CommentsRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final DTOService dtoService;
    private final TaskRepository taskRepository;

    public List<CommentDTO> getAllCommentsByTask(String taskId) {
        if (taskId == null || taskId.isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found with ID: " + taskId));

        List<Comment> comments = commentsRepository.findAllByTaskId(task);
        if (comments.isEmpty()) {
            throw new NotFoundException("No comments found for task with ID: " + taskId);
        }

        return comments.stream().map(dtoService::convertToCommentDTO).toList();
    }

    @Transactional
    public void addComment(CommentDTO commentDTO) {
        if (commentDTO == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        Comment comment = dtoService.convertToComment(commentDTO);
        commentsRepository.save(comment);
    }
}
