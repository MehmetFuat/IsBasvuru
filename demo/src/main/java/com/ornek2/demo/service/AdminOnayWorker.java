package com.ornek2.demo.service;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(topicName = "basvuruOnay")

public class AdminOnayWorker implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String processInstanceId = task.getProcessInstanceId();

        Object kararObj = task.getVariable("adminKarar");
        String adminKarar = kararObj != null ? kararObj.toString() : "YOK";

        System.out.println("📩 Admin kararı geldi: " + adminKarar + " | Process ID: " + processInstanceId);

        Map<String, Object> variables = new HashMap<>();
        variables.put("adminKarar", adminKarar);
        service.complete(task, variables);
    }

}

