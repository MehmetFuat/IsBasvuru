package com.ornek2.demo;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    private RuntimeService runtimeService;

    @PostMapping("/start")
    public String startProcess(@RequestParam String basvuruId) {
        runtimeService.startProcessInstanceByKey("BasvuruSureci",
                Map.of("basvuruId", basvuruId));
        return "Süreç başlatıldı. Başvuru ID: " + basvuruId;
    }
}
