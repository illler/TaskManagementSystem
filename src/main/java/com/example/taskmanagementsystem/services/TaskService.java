package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.error.UnauthorizedException;
import com.example.taskmanagementsystem.model.Role;
import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.TaskRepository;
import com.example.taskmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        } else {
            return null;
        }
    }

    public Task getTaskById(String taskId){
        return taskRepository.findById(taskId).orElseThrow();
    }

    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    @Transactional
    public void saveTask(Task task) {
        User currentUser = getCurrentUser();

        if (currentUser != null && currentUser.equals(task.getAuthor())) {
            taskRepository.save(task);
        } else {
            throw new UnauthorizedException("You are not authorized to save new task");
        }
    }


    @Transactional
    public void updateTask(String taskId, Task task) {
        User currentUser = getCurrentUser();

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (currentUser != null && currentUser.equals(existingTask.getAuthor())) {
            task.setTaskId(taskId);
            taskRepository.save(task);
        } else {
            throw new UnauthorizedException("You are not authorized to update this task");
        }
    }

    @Transactional
    public void deleteTask(String taskId) {
        User currentUser = getCurrentUser();

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (currentUser != null && currentUser.equals(existingTask.getAuthor())) {
            taskRepository.deleteById(taskId);
        } else {
            throw new UnauthorizedException("You are not authorized to delete this task");
        }
    }

    @Transactional
    public void updateTaskStatus(Status status, String taskId) {
        User currentUser = getCurrentUser();

        Task existingTask = taskRepository.findById(taskId).orElseThrow();

        if (currentUser != null &&
                currentUser.equals(existingTask.getExecutor()) &&
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

        if (currentUser != null && currentUser.equals(existingTask.getAuthor())) {
            User executor = userRepository.findById(executorId).orElseThrow();
            existingTask.setExecutor(executor);
        } else {
            throw new UnauthorizedException("You are not authorized to set a new executor for this task");
        }
    }


    public List<Task> getAllTaskByAuthor(String authorId){
        User user = userRepository.findById(authorId).orElseThrow();
        return taskRepository.findAllByAuthor(user);
    }

    public List<Task> getTaskByExecutor(String executorId){
        User user = userRepository.findById(executorId).orElseThrow();
        return taskRepository.findAllByExecutor(user);
    }
}
