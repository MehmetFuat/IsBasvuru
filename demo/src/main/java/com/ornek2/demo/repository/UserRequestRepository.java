package com.ornek2.demo.repository;

import com.ornek2.demo.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {

    List<UserRequest> findByStatus(String status);
    UserRequest findFirstByEmailOrderByIdDesc(String email);
    UserRequest findByApplicationCode(String applicationCode);
    long countByEmail(String email);

}
