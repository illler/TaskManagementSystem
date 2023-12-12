package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.DTO.TaskDTO;
import com.example.taskmanagementsystem.error.UnauthorizedException;
import com.example.taskmanagementsystem.model.Role;
import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final DTOService dtoService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else {
            return null;
        }
    }

    public TaskDTO getTaskById(String taskId){
        return dtoService.convertToTaskDTO(taskRepository.findById(taskId).orElseThrow());
    }

    public List<TaskDTO> getAllTask(){
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(dtoService::convertToTaskDTO).toList();
    }

    @Transactional
    public void saveTask(TaskDTO taskDTO) {
        Task task = dtoService.convertToTask(taskDTO);
        User currentUser = getCurrentUser();

        if (currentUser != null && currentUser.getId().equals(task.getAuthor().getId())) {
            task.setAuthor(currentUser);
            taskRepository.save(task);
        } else {
            throw new UnauthorizedException("You are not authorized to save new task");
        }
    }


    @Transactional
    public void updateTask(TaskDTO taskDTO) {
        Task task = dtoService.convertToTask(taskDTO);
        User currentUser = getCurrentUser();

        if (currentUser != null && currentUser.getId().equals(task.getAuthor().getId())) {
            taskRepository.save(task);
        } else {
            throw new UnauthorizedException("You are not authorized to update this task");
        }
    }

    @Transactional
    public void deleteTask(String taskId) {
        User currentUser = getCurrentUser();

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (currentUser != null && currentUser.getId().equals(existingTask.getAuthor().getId())) {
            taskRepository.deleteById(taskId);
        } else {
            throw new UnauthorizedException("You are not authorized to delete this task");
        }
    }

    @Transactional
    public void updateTaskStatus(Status status, String taskId) {
        User currentUser = getCurrentUser();

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (currentUser != null && currentUser.getId().equals(existingTask.getExecutor().getId()) &&
                currentUser.getRole().equals(Role.EXECUTOR)) {
            taskRepository.updateTaskStatus(status, taskId);
        } else {
            throw new UnauthorizedException("You are not authorized as executor who have permission to this task");
        }
    }

    @Transactional
    public void setNewExecutor(String taskId, String executorId) {
        User currentUser = getCurrentUser();

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (currentUser != null && currentUser.getId().equals(existingTask.getAuthor().getId())) {
            User executor = userRepository.findById(executorId).orElseThrow();
            if (executor.getRole().equals(Role.EXECUTOR)) {
                existingTask.setExecutor(executor);
            }
        } else {
            throw new UnauthorizedException("You are not authorized to set a new executor for this task");
        }
    }


    public List<TaskDTO> getAllTaskByAuthor(String authorId){
        User user = userRepository.findById(authorId).orElseThrow();
        List<Task> tasks = taskRepository.findAllByAuthor(user);
        return tasks.stream().map(dtoService::convertToTaskDTO).toList();
    }

    public List<TaskDTO> getTaskByExecutor(String executorId){
        User user = userRepository.findById(executorId).orElseThrow();
        List<Task> tasks = taskRepository.findAllByExecutor(user);
        return tasks.stream().map(dtoService::convertToTaskDTO).toList();
    }
}
