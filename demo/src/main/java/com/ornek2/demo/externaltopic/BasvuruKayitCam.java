package com.ornek2.demo.externaltopic;

import com.ornek2.demo.model.ApplicationTable;
import com.ornek2.demo.model.UsersTableDeneme;
import com.ornek2.demo.repository.ApplicationTableRepository;
import com.ornek2.demo.repository.UsersTableRepository;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@ExternalTaskSubscription(topicName = "applicationSave")
public class BasvuruKayitCam implements ExternalTaskHandler {

    @Autowired
    private UsersTableRepository usersTableRepository;

    @Autowired
    private ApplicationTableRepository applicationTableRepository;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String email = task.getVariable("email");


        Long customerId = task.getVariable("customerId") != null
                ? ((Number) task.getVariable("customerId")).longValue()
                : null;

        if (customerId == null) {
            Optional<UsersTableDeneme> userOpt = usersTableRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                customerId = userOpt.get().getId();
            } else {
                System.out.println("❌ Kullanıcı bulunamadı, kayıt atılamadı.");
                service.complete(task);
                return;
            }
        }

        ApplicationTable app = new ApplicationTable();
        app.setUserId(customerId);
        app.setDepartment(task.getVariable("department"));
        app.setNote(task.getVariable("note"));
        app.setCvFileName(task.getVariable("cvFileName"));
        app.setStatus("PENDING");

        app.setApplyDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        app.setApplicationCode(UUID.randomUUID().toString().substring(0, 8));

        applicationTableRepository.save(app);

        Map<String, Object> variables = new HashMap<>();
        variables.put("customerId", customerId);

        service.complete(task, variables);

        System.out.println("📥 Application kaydı atıldı → userId: " + customerId);
    }
}
