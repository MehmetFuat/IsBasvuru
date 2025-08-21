package com.ornek2.demo.externaltopic;

import com.ornek2.demo.model.UserRequest;
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
@ExternalTaskSubscription(topicName = "basvuruRed")
public class BasvuruRedCam implements ExternalTaskHandler {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String email = task.getVariable("email");
        System.out.println("📥 RED task alındı: " + email);

        Integer denemeSayisi = task.getVariable("denemeSayisi");
        if (denemeSayisi == null) denemeSayisi = 0;

        denemeSayisi++;

        String hak = denemeSayisi >= 2 ? "YOK" : "VAR";
        System.out.println("🎯 Deneme sayısı: " + denemeSayisi + " → Hak: " + hak);

        Map<String, Object> variables = new HashMap<>();
        variables.put("hak", hak);
        variables.put("denemeSayisi", denemeSayisi);


        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null) {
            user.setStatus("REJECTED");
            userRequestRepository.save(user);
            System.out.println("❌ Kullanıcı reddedildi: " + email);
        } else {
            System.out.println("🛑 Kullanıcı bulunamadı: " + email);
        }

        service.complete(task, variables);
    }
}
