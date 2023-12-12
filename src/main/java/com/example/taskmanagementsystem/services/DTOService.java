package com.example.taskmanagementsystem.services;


import com.example.taskmanagementsystem.DTO.TaskDTO;
import com.example.taskmanagementsystem.DTO.UserDTO;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import com.example.taskmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DTOService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public TaskDTO convertToTaskDTO(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setAuthorId(task.getAuthor().getId());
        if (task.getExecutor() != null) {
            taskDTO.setExecutorId(task.getExecutor().getId());
        }
        return taskDTO;
    }

    public Task convertToTask(TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        if (taskDTO.getAuthorId() != null) {
            task.setAuthor(userRepository.findById(taskDTO.getAuthorId()).orElseThrow());
        }
        if (taskDTO.getExecutorId() != null) {
            task.setAuthor(userRepository.findById(taskDTO.getExecutorId()).orElseThrow());
        }
        return task;
    }
}
