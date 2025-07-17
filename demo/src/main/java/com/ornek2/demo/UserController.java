package com.ornek2.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.HashMap;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")

public class UserController {

    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RuntimeService runtimeService;
    


    @PostMapping("/admin-login")
    public boolean loginAdmin(@RequestBody Admin admin) {
        Admin found = adminRepository.findByUsernameAndPassword(admin.getUsername(), admin.getPassword());
        return found != null;
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public String registerUser(
            @RequestPart("user") UserRequest userRequest,
            @RequestPart("cv") MultipartFile cvFile
    ) {
        try {
            long count = userRequestRepository.countByEmail(userRequest.getEmail());
            if (count >= 2) {
                return "Aynı e-posta adresi ile en fazla 2 kez başvuru yapabilirsiniz.";
            }

            String fileName = System.currentTimeMillis() + "_" + cvFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(cvFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            userRequest.setApplyDate(LocalDateTime.now().format(formatter));
            userRequest.setCvFileName(fileName);
            userRequest.setStatus("PENDING");
            userRequest.setApplicationCode(generateRandomCode(8));

            userRequestRepository.save(userRequest);

            // Camunda Süreci Başlat
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", userRequest.getName());
            variables.put("surname", userRequest.getSurname());
            variables.put("email", userRequest.getEmail());
            variables.put("phone", userRequest.getPhone());
            variables.put("department", userRequest.getDepartment());
            variables.put("note", userRequest.getNote());
            variables.put("cvFileName", fileName);

            CamundaStartProcessRequest request = new CamundaStartProcessRequest();
            request.setVariables(variables);
            CamundaStartProcessResponse  = camundaClient.startProcessWithVariables("BasvuruSureci", request);

            return "Başvuru alındı. Başvuru Kodunuz: " + userRequest.getApplicationCode();
        } catch (IOException e) {
            return "Dosya yüklenirken hata oluştu: " + e.getMessage();
        }
    }


    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder bd = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            bd.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return bd.toString();
    }

    @GetMapping("/pending")
    public List<UserRequest> getPendingUsers() {
        return userRequestRepository.findByStatus("PENDING");
    }


    @PostMapping("/approve")
    public String approveUser(@RequestParam String email, @RequestParam(required = false) String note) {
        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null && user.getStatus().equals("PENDING")) {
            user.setStatus("APPROVED");
            userRequestRepository.save(user);
            return "User approved: " + email + (note != null ? " (Note: " + note + ")" : "");
        }
        return "User not found or already processed.";
    }

    @PostMapping("/reject")
    public String rejectUser(@RequestParam String email) {
        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null && user.getStatus().equals("PENDING")) {
            user.setStatus("REJECTED");
            userRequestRepository.save(user);
            return "User rejected: " + email;
        }
        return "User not found or already processed.";
    }

    @GetMapping("/approved")
    public List<UserRequest> getApprovedUsers() {
        return userRequestRepository.findByStatus("APPROVED");
    }

    @GetMapping("/rejected")
    public List<UserRequest> getRejectedUsers() {
        return userRequestRepository.findByStatus("REJECTED");
    }
    @GetMapping("/check-status")
    public UserRequest checkStatus(@RequestParam String code) {
        return userRequestRepository.findByApplicationCode(code);
    }

}
