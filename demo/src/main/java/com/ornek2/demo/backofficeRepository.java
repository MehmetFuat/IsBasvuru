package com.ornek2.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface backofficeRepository extends JpaRepository<backofficeTask, String>
{
    backofficeTask findByTaskId(String taskId);
}
