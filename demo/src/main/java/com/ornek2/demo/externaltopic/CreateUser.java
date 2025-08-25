package com.ornek2.demo.externaltopic;

import com.ornek2.demo.model.AppTable;
import com.ornek2.demo.model.UsersTableDeneme;
import com.ornek2.demo.repository.AppTableRepository;
import com.ornek2.demo.repository.UsersTableRepository;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription(topicName = "createUser")
public class CreateUser implements ExternalTaskHandler {

    @Autowired
    private UsersTableRepository usersTableRepository;

    @Autowired
    private AppTableRepository appTableRepository;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        try {
            String name = task.getVariable("name");
            String surname = task.getVariable("surname");
            String email = task.getVariable("email");
            String phone = task.getVariable("phone");
            
            UsersTableDeneme user = new UsersTableDeneme();
            user.setName(name);
            user.setSurname(surname);
            user.setEmail(email);
            user.setPhone(phone);

            UsersTableDeneme savedUser = usersTableRepository.save(user);

            Integer age = null;
            if (task.getVariable("age") != null) {
                age = ((Number) task.getVariable("age")).intValue();
            }
            Integer experience = null;
            if (task.getVariable("experience") != null) {
                experience = ((Number) task.getVariable("experience")).intValue();
            }
            String education = task.getVariable("education");
            String languageFromForm = task.getVariable("language");

            AppTable app = new AppTable();
            app.setUserId(savedUser.getId());
            app.setAge(age);
            app.setExperience(experience);
            app.setEducation(education);
            app.setLanguageLevel(languageFromForm);

            appTableRepository.save(app);

            Map<String, Object> variables = new HashMap<>();
            variables.put("userId", savedUser.getId());
            variables.put("customerId", savedUser.getId());
            service.complete(task, variables);

            System.out.println("🆕 Kullanıcı ve AppTable kaydedildi → userId: " + savedUser.getId());
        } catch (Exception e) {
            service.handleFailure(task, "CreateUser hata", e.getMessage(), 0, 0);
        }
    }
}
