package com.example.taskManager.repository;

import com.example.taskManager.entity.Task;
import com.example.taskManager.entity.TaskStatus;
import com.example.taskManager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void findAllByStatus_ShouldReturnOnlyTasksWithStatus() {
        User author = new User();
        author.setUsername("author");
        author = entityManager.persist(author);

        Task task1 = new Task();
        task1.setTitle("High priority");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setAuthor(author);
        entityManager.persist(task1);

        Task task2 = new Task();
        task2.setTitle("Todo task");
        task2.setStatus(TaskStatus.TODO);
        task2.setAuthor(author);
        entityManager.persist(task2);

        List<Task> inProgress = taskRepository.findAllByStatus(TaskStatus.IN_PROGRESS);

        assertThat(inProgress).hasSize(1);
        assertThat(inProgress.get(0).getTitle()).isEqualTo("High priority");
    }
}