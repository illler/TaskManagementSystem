package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.DTO.CommentDTO;
import com.example.taskmanagementsystem.DTO.TaskDTO;
import com.example.taskmanagementsystem.model.Comment;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class DTOServiceTest {

    @Mock
    ModelMapper modelMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    DTOService dtoService;


    @Test
    void convertToTaskDTO() {
        // Arrange
        Task task = new Task();
        task.setTaskId("taskId");
        task.setHeading("Task Title");
        task.setAuthor(new User());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId("taskId");
        taskDTO.setHeading("Task Title");
        task.setAuthor(new User());

        when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO);

        // Act
        TaskDTO result = dtoService.convertToTaskDTO(task);

        // Assert
        assertEquals(taskDTO, result);
        verify(modelMapper, times(1)).map(task, TaskDTO.class);
    }

    @Test
    void convertToTask() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId("taskId");
        taskDTO.setHeading("Task Title");
        taskDTO.setAuthorId("authorId");
        taskDTO.setExecutorId("executorId");

        Task task = new Task();
        task.setTaskId("taskId");
        task.setHeading("Task Title");

        when(modelMapper.map(taskDTO, Task.class)).thenReturn(task);
        when(userRepository.findById("authorId")).thenReturn(Optional.of(new User()));
        when(userRepository.findById("executorId")).thenReturn(Optional.of(new User()));

        // Act
        Task result = dtoService.convertToTask(taskDTO);

        // Assert
        assertEquals(task, result);
        verify(modelMapper, times(1)).map(taskDTO, Task.class);
        verify(userRepository, times(1)).findById("authorId");
        verify(userRepository, times(1)).findById("executorId");
    }

    @Test
    void convertToCommentDTO() {
        // Arrange
        Comment comment = new Comment();
        comment.setCommentId("commentId");
        comment.setComment("Comment Content");
        comment.setUserId(new User());
        comment.setTaskId(new Task());

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId("commentId");
        commentDTO.setCommentId("Comment Content");
        comment.setUserId(new User());

        when(modelMapper.map(comment, CommentDTO.class)).thenReturn(commentDTO);

        // Act
        CommentDTO result = dtoService.convertToCommentDTO(comment);

        // Assert
        assertEquals(commentDTO, result);
        verify(modelMapper, times(1)).map(comment, CommentDTO.class);
    }

    @Test
    void convertToComment() {
        // Arrange
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId("commentId");
        commentDTO.setComment("Comment Content");
        commentDTO.setUserId("userId");
        commentDTO.setTaskId("taskId");

        Comment comment = new Comment();
        comment.setCommentId("commentId");
        comment.setComment("Comment Content");

        when(modelMapper.map(commentDTO, Comment.class)).thenReturn(comment);
        when(userRepository.findById("userId")).thenReturn(Optional.of(new User()));
        when(taskRepository.findById("taskId")).thenReturn(Optional.of(new Task()));

        // Act
        Comment result = dtoService.convertToComment(commentDTO);

        // Assert
        assertEquals(comment, result);
        verify(modelMapper, times(1)).map(commentDTO, Comment.class);
        verify(userRepository, times(1)).findById("userId");
        verify(taskRepository, times(1)).findById("taskId");
    }
}
