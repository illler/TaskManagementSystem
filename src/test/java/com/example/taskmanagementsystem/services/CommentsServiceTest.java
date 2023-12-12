package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.DTO.CommentDTO;
import com.example.taskmanagementsystem.error.NotFoundException;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.repositories.CommentsRepository;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentsServiceTest {

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private DTOService dtoService;

    @InjectMocks
    private CommentsService commentsService;

    @Test
    void getAllCommentsByTask_ValidTaskId_ReturnsCommentDTOList() {
        String taskId = "validTaskId";
        Task task = new Task();
        task.setTaskId(taskId);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comments.add(comment);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentsRepository.findAllByTaskId(task)).thenReturn(comments);
        when(dtoService.convertToCommentDTO(comment)).thenReturn(new CommentDTO());
        List<CommentDTO> result = commentsService.getAllCommentsByTask(taskId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(taskRepository, times(1)).findById(taskId);
        verify(commentsRepository, times(1)).findAllByTaskId(task);
        verify(dtoService, times(1)).convertToCommentDTO(comment);
    }

    @Test
    void getAllCommentsByTask_InvalidTaskId_ThrowsNotFoundException() {
        String invalidTaskId = "invalidTaskId";

        when(taskRepository.findById(invalidTaskId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> commentsService.getAllCommentsByTask(invalidTaskId));

        verify(taskRepository, times(1)).findById(invalidTaskId);
        verify(commentsRepository, never()).findAllByTaskId(any());
        verify(dtoService, never()).convertToCommentDTO(any());
    }

    @Test
    void addComment_ValidCommentDTO_SavesComment() {
        CommentDTO commentDTO = new CommentDTO();
        Comment comment = new Comment();

        when(dtoService.convertToComment(commentDTO)).thenReturn(comment);

        commentsService.addComment(commentDTO);

        verify(commentsRepository, times(1)).save(comment);
        verify(dtoService, times(1)).convertToComment(commentDTO);
    }


    @Test
    void addComment_NullCommentDTO_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> commentsService.addComment(null));

        verify(commentsRepository, never()).save(any());
        verify(dtoService, never()).convertToComment(any());
    }
}
