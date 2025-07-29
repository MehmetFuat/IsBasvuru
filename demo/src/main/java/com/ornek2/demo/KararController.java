package com.ornek2.demo;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class KararController {

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping("/admin-karar-gonder")
    public ResponseEntity<String> adminKararGonder(
            @RequestParam String processInstanceId,
            @RequestParam String karar
    ) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("adminKarar", karar);

        try {
            runtimeService.createMessageCorrelation("adminOnayMesaji")
                    .processInstanceId(processInstanceId)
                    .setVariables(variables)
                    .correlate();

            return ResponseEntity.ok("✅ Admin kararı iletildi ve süreç devam etti.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Hata: " + e.getMessage());
        }
    }
}
