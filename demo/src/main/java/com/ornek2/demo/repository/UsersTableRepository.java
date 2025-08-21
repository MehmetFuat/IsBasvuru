package com.ornek2.demo.repository;

import com.ornek2.demo.model.UsersTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersTableRepository extends JpaRepository<UsersTable, Long> {
    Optional<UsersTable> findByEmail(String email);
}
