package com.ornek2.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeTask {

    @Id
    private String taskId;

    private String taskType;
    private String name;
    private String email;
    private String status;
    private String processInstanceId;
    private LocalDateTime createdDate;
    private LocalDateTime dueDate;
    private LocalDateTime warningDate;
}
