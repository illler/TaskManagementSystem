package com.example.taskmanagementsystem.repositories;

import com.example.taskmanagementsystem.model.Status;
import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByAuthor(User user);
    List<Task> findAllByExecutor(User user);

    @Query(value = "update Task set status=:newStatus where taskId=:taskId")
    Task updateTaskStatus(Status newStatus, String taskId);

}
