package com.ornek2.demo.service;

import com.ornek2.demo.model.BackofficeTask;
import com.ornek2.demo.model.UserRequest;
import com.ornek2.demo.repository.BackofficeRepository;
import com.ornek2.demo.repository.UserRequestRepository;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@ExternalTaskSubscription(topicName = "backOfficeTaskCreation")
public class BackOfficeTaskCreation implements ExternalTaskHandler {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private BackofficeRepository backofficeRepository;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String email = task.getVariable("email");
        String processInstanceId = task.getProcessInstanceId();

        System.out.println("✅ basvuruOnay alındı: " + email);

        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null) {
            BackofficeTask backofficeTask = new BackofficeTask();
            backofficeTask.setTaskId(UUID.randomUUID().toString());
            backofficeTask.setTaskType("Kayıt");
            backofficeTask.setName(user.getName() + " " + user.getSurname());
            backofficeTask.setEmail(user.getEmail());
            backofficeTask.setProcessInstanceId(processInstanceId);
            backofficeTask.setStatus("NEW");
            backofficeTask.setCreatedDate(LocalDateTime.now());
            backofficeTask.setDueDate(LocalDateTime.now().plusDays(3));
            backofficeTask.setWarningDate(LocalDateTime.now().plusDays(2));

            backofficeRepository.save(backofficeTask);
        } else {
            System.out.println("❌ Email ile kullanıcı bulunamadı: " + email);
        }

        service.complete(task);
    }
}
