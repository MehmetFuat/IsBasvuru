package com.ornek2.demo;

import com.ornek2.demo.UserRequest;
import com.ornek2.demo.UserRequestRepository;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "basvuruOnay")
public class BasvuruOnayCam {

    @Autowired
    private UserRequestRepository userRequestRepository;

    public void execute(ExternalTask task, ExternalTaskService service) {
        String email = task.getVariable("email");

        System.out.println("✅ basvuruOnay alındı: " + email);

        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null) {
            user.setStatus("APPROVED");
            userRequestRepository.save(user);
            System.out.println("✔ Kullanıcı onaylandı.");
        } else {
            System.out.println("❌ Email ile kullanıcı bulunamadı: " + email);
        }

        service.complete(task);
    }
}
