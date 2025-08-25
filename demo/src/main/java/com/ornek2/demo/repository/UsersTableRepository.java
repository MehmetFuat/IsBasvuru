package com.ornek2.demo.repository;

import com.ornek2.demo.model.UsersTableDeneme;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersTableRepository extends JpaRepository<UsersTableDeneme, Long> {
    Optional<UsersTableDeneme> findByEmail(String email);
}
