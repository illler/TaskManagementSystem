package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.DTO.TaskDTO;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    DTOService dtoService;

    @InjectMocks
    TaskService taskService;

    @Test
    public void testGetCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User mockUser = new User();
        when(authentication.getPrincipal()).thenReturn(mockUser);

        User currentUser = taskService.getCurrentUser();
        assertEquals(mockUser, currentUser);

        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    public void testGetTaskById() {
        String taskId = "testTaskId";
        Task mockTask = new Task();
        TaskDTO mockTaskDTO = new TaskDTO();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        when(dtoService.convertToTaskDTO(mockTask)).thenReturn(mockTaskDTO);

        TaskDTO resultDTO = taskService.getTaskById(taskId);
        assertEquals(mockTaskDTO, resultDTO);

        verify(taskRepository, times(1)).findById(taskId);
        verify(dtoService, times(1)).convertToTaskDTO(mockTask);
    }


    @Test
    public void testGetAllTask() {
        // Mock data
        List<Task> mockTasks = List.of(new Task(), new Task()); // You need to create mock Task objects
        List<TaskDTO> mockTaskDTOs = List.of(new TaskDTO(), new TaskDTO()); // You need to create mock TaskDTO objects

        // Mock TaskRepository
        when(taskRepository.findAll()).thenReturn(mockTasks);

        // Mock DTOService
        when(dtoService.convertToTaskDTO(any(Task.class))).thenReturn(new TaskDTO()); // You may need to adjust this depending on your DTO creation logic

        // Test getAllTask
        List<TaskDTO> resultDTOs = taskService.getAllTask();
        assertNotNull(resultDTOs);
        assertEquals(mockTasks.size(), resultDTOs.size());

        // Verify that the method was called
        verify(taskRepository, times(1)).findAll();
        verify(dtoService, times(mockTasks.size())).convertToTaskDTO(any(Task.class));
    }

    @Test
    public void testSaveTask() {
        TaskDTO mockTaskDTO = new TaskDTO();

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User mockUser = new User();
        when(authentication.getPrincipal()).thenReturn(mockUser);

        Task mockTask = new Task();
        when(dtoService.convertToTask(mockTaskDTO)).thenReturn(mockTask);

        assertDoesNotThrow(() -> taskService.saveTask(mockTaskDTO));

        verify(dtoService, times(1)).convertToTask(mockTaskDTO);
        verify(taskService, times(1)).getCurrentUser();
        verify(taskRepository, times(1)).save(mockTask);

        assertNotNull(mockTask.getAuthor());
        assertNotNull(mockTask.getAuthor().getId());
    }


    @Test
    public void testUpdateTask() {
        // Mock data
        TaskDTO mockTaskDTO = new TaskDTO(); // You need to create a mock TaskDTO object
        Task mockTask = new Task(); // You need to create a mock Task object
        User mockCurrentUser = new User(); // You need to create a mock User object

        // Mock DTOService
        when(dtoService.convertToTask(mockTaskDTO)).thenReturn(mockTask);

        // Mock getCurrentUser
        when(taskService.getCurrentUser()).thenReturn(mockCurrentUser);

        // Test updateTask
        assertDoesNotThrow(() -> taskService.updateTask(mockTaskDTO));

        // Verify that the method was called
        verify(dtoService, times(1)).convertToTask(mockTaskDTO);
        verify(taskService, times(1)).getCurrentUser();
        verify(taskRepository, times(1)).save(mockTask);
    }
}
