package com.ornek2.demo;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController

public class CamundaProxyController {

    @PostMapping("/send-admin-message")
    public ResponseEntity<?> sendAdminMessage(@RequestBody Map<String, Object> request) {
        try {
            String processInstanceId = (String) request.get("processInstanceId");
            String karar = (String) request.get("karar");

            RestTemplate restTemplate = new RestTemplate();

            String camundaUrl = "http://localhost:8082/engine-rest/message";

            Map<String, Object> message = new HashMap<>();
            message.put("messageName", "admin_message");
            message.put("processInstanceId", processInstanceId);

            Map<String, Object> processVariables = new HashMap<>();
            Map<String, Object> kararValue = new HashMap<>();
            kararValue.put("value", karar);
            kararValue.put("type", "String");
            processVariables.put("adminKarar", kararValue);

            message.put("processVariables", processVariables);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(message, headers);

            restTemplate.postForEntity(camundaUrl, entity, String.class);

            return ResponseEntity.ok("Camunda mesajı gönderildi.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Hata: " + e.getMessage());
        }
    }
}
