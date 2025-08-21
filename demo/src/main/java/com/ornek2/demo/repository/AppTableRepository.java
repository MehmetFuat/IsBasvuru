package com.ornek2.demo.repository;

import com.ornek2.demo.model.AppTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppTableRepository extends JpaRepository<AppTable, Long> {
}
