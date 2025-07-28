package com.ornek2.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backoffice")
@CrossOrigin(origins = "http://localhost:3000")
public class backofficeController {

    @Autowired
    private backofficeRepository backofficeTaskRepository;

    @GetMapping
    public List<backofficeTask> getAllTasks() {
        return backofficeTaskRepository.findAll();
    }

    @PostMapping("/approve/{taskId}")
    public void approve(@PathVariable String taskId) {
        backofficeTask task = backofficeTaskRepository.findByTaskId(taskId);
        if (task != null) {
            task.setStatus("APPROVED");
            backofficeTaskRepository.save(task);
        }
    }

    @PostMapping("/reject/{taskId}")
    public void reject(@PathVariable String taskId) {
        backofficeTask task = backofficeTaskRepository.findByTaskId(taskId);
        if (task != null) {
            task.setStatus("REJECTED");
            backofficeTaskRepository.save(task);
        }
    }
}
