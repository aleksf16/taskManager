package com.example.taskManager.repository;

import com.example.taskManager.entity.Task;
import com.example.taskManager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findAllByStatus(TaskStatus status);
    List<Task> findAllByAuthor_Id(Long authorId);
    List<Task> findAllByAssignee_Id(Long assigneeId);
}
