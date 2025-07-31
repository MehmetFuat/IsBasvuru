package com.ornek2.demo.repository;

import com.ornek2.demo.model.BackofficeTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackofficeRepository extends JpaRepository<BackofficeTask, String>
{
    BackofficeTask findByTaskId(String taskId);
}
