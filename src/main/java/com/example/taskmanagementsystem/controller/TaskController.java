package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.error.UnauthorizedException;
import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.services.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "TaskController")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<?> receiveAllTasks(){
        return ResponseEntity.ok(taskService.getAllTask());
    }

    @GetMapping("/authors/{userId}")
    public ResponseEntity<?> getAuthorsTasks(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getAllTaskByAuthor(userId));
    }
    @GetMapping("/executors/{userId}")
    public ResponseEntity<?> getExecutorsTasks(@PathVariable String userId){
        return ResponseEntity.ok(taskService.getTaskByExecutor(userId));
    }

    @GetMapping("/{userId}/{taskId}")
    public ResponseEntity<?> getOneTask(@PathVariable String userId,
                                        @PathVariable String taskId){
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PostMapping("/save-new-task")
    public ResponseEntity<?> saveNewTask(@RequestBody Task task) {
        try {
            taskService.saveTask(task);
            return ResponseEntity.ok("Task saved");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: " + e.getMessage());
        }
    }

    @PostMapping("/update-task/{taskId}")
    public ResponseEntity<?> updateExistingTask(@PathVariable String taskId,
                                                @RequestBody Task task) {
        try {
            taskService.updateTask(taskId, task);
            return ResponseEntity.ok("Task updated");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-task/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("Task deleted");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: " + e.getMessage());
        }
    }

    @PatchMapping("/update-task-status/{taskId}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable String taskId,
                                                @RequestBody Status status) {
        try {
            taskService.updateTaskStatus(status, taskId);
            return ResponseEntity.ok("Task status updated");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: " + e.getMessage());
        }
    }

    @PatchMapping("/set-task-executor/{taskId}")
    public ResponseEntity<?> setTaskExecutor(@PathVariable String taskId,
                                              @RequestBody String executorId) {
        try {
            taskService.setNewExecutor(taskId, executorId);
            return ResponseEntity.ok("Task executor set");
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: " + e.getMessage());
        }
    }

}
