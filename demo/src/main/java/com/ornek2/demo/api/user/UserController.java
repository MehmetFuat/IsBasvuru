package com.ornek2.demo.api.user;

import com.ornek2.demo.model.AppTable;
import com.ornek2.demo.repository.AdminRepository;
import com.ornek2.demo.model.UserRequest;
import com.ornek2.demo.repository.AppTableRepository;
import com.ornek2.demo.repository.UserRequestRepository;
import com.ornek2.demo.repository.BackofficeRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController implements UserApi
{
    @Autowired
    private UserRequestRepository userRequestRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private BackofficeRepository backofficeTaskRepository;

    @Autowired
    private AppTableRepository appTableRepository;


    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }
    @PostMapping("/apply")
    public String apply(@RequestBody AppTable req) {
        AppTable app = new AppTable();
        app.setAge(req.getAge());
        app.setExperience(req.getExperience());
        app.setEducation(req.getEducation());
        app.setLanguageLevel(req.getLanguageLevel());
        appTableRepository.save(app);

        Map<String, Object> vars = new HashMap<>();
        vars.put("age", req.getAge());
        vars.put("experience", req.getExperience());
        vars.put("education", req.getEducation());
        vars.put("languageLevel", req.getLanguageLevel());

        runtimeService.startProcessInstanceByKey("BasvuruSureci", vars);
        return "Başvuru süreci başlatıldı";
    }


    @Override
    public String registerUser(UserRequest userRequest, MultipartFile cvFile) {
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


            Map<String, Object> variables = new HashMap<>();
            variables.put("name", userRequest.getName());
            variables.put("surname", userRequest.getSurname());
            variables.put("email", userRequest.getEmail());
            variables.put("phone", userRequest.getPhone());
            variables.put("department", userRequest.getDepartment());
            variables.put("note", userRequest.getNote());
            variables.put("cvFileName", fileName);

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("BasvuruSureci", variables);

            return "Başvuru alındı. Başvuru Kodunuz: " + userRequest.getApplicationCode();
        } catch (IOException e) {
            return "Dosya yüklenirken hata oluştu: " + e.getMessage();
        }
    }


    @Override
    public List<UserRequest> getPendingUsers() {
        return userRequestRepository.findByStatus("PENDING");
    }

    @Override
    public String approveUser(String email, String note) {
        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null && user.getStatus().equals("PENDING")) {
            user.setStatus("APPROVED");
            userRequestRepository.save(user);

            return "User approved: " + email + (note != null ? " (Note: " + note + ")" : "");
        }
        return "User not found or already processed.";
    }

    @Override
    public String rejectUser(String email) {
        UserRequest user = userRequestRepository.findFirstByEmailOrderByIdDesc(email);
        if (user != null && user.getStatus().equals("PENDING")) {
            user.setStatus("REJECTED");
            userRequestRepository.save(user);
            return "User rejected: " + email;
        }
        return "User not found or already processed.";
    }

    @Override
    public List<UserRequest> getApprovedUsers() {
        return userRequestRepository.findByStatus("APPROVED");
    }

    @Override
    public List<UserRequest> getRejectedUsers() {
        return userRequestRepository.findByStatus("REJECTED");
    }

    @Override
    public UserRequest checkStatus(String code) {
        return userRequestRepository.findByApplicationCode(code);
    }
}
