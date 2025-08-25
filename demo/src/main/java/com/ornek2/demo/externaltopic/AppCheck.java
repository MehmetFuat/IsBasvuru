package com.ornek2.demo.externaltopic;

import com.ornek2.demo.repository.UsersTableRepository;
import com.ornek2.demo.model.UsersTableDeneme;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@ExternalTaskSubscription(topicName = "applicationCheck")
public class AppCheck implements ExternalTaskHandler {

    @Autowired
    private UsersTableRepository usersTableRepository;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        String email = task.getVariable("email");
        Optional<UsersTableDeneme> existingUser = usersTableRepository.findByEmail(email);

        boolean userCheck = existingUser.isPresent();

        Map<String, Object> variables = new HashMap<>();
        variables.put("userCheck", userCheck);

        if (userCheck) {
            Long customerId = existingUser.get().getId();
            variables.put("customerId", customerId);
        }

        service.complete(task, variables);
    }
}
