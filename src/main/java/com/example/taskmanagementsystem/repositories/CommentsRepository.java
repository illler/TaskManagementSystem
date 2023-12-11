package com.example.taskmanagementsystem.repositories;

import com.example.taskmanagementsystem.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByTaskId(String taskId);

}
