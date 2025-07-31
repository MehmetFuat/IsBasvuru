package com.ornek2.demo.service;

import com.ornek2.demo.model.UserRequest;
import com.ornek2.demo.repository.BackofficeRepository;
import com.ornek2.demo.repository.UserRequestRepository;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(topicName = "basvuruKayit")
public class BasvuruKayitCam implements ExternalTaskHandler {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private BackofficeRepository backofficeRepository;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String email = task.getVariable("email");

        System.out.println("📥 basvuruKayit alındı: " + email);

        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null) {
            user.setStatus("PENDING");
            userRequestRepository.save(user);
            System.out.println("✅ Status güncellendi: " + user.getStatus());


            long denemeSayisi = userRequestRepository.countByEmail(email);
            String hak = denemeSayisi >= 2 ? "YOK" : "VAR";
            System.out.println("🔁 Deneme sayısı: " + denemeSayisi + " → Hak: " + hak);

            Map<String, Object> variables = new HashMap<>();
            variables.put("hak", hak);
            service.complete(task, variables);

        } else {
            System.out.println("❌ Email ile kullanıcı bulunamadı: " + email);
            service.complete(task);
        }
    }
}
