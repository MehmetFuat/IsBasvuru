package com.ornek2.demo.api.backoffice;

import com.ornek2.demo.repository.BackofficeRepository;
import com.ornek2.demo.model.BackofficeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BackofficeController implements BackofficeApi {

    @Autowired
    private BackofficeRepository backofficeTaskRepository;


    @Override
    public List<BackofficeTask> getAllTasks() {
        return backofficeTaskRepository.findAll();
    }

    @Override
    public void approve(String taskId)
    {
        BackofficeTask task = backofficeTaskRepository.findByTaskId(taskId);
        if (task != null) {
            task.setStatus("APPROVED");
            backofficeTaskRepository.save(task);
        }
    }

    @Override
    public void reject(String taskId)
    {
        BackofficeTask task = backofficeTaskRepository.findByTaskId(taskId);
        if (task != null) {
            task.setStatus("REJECTED");
            backofficeTaskRepository.save(task);
        }
    }


}



