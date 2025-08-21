package com.ornek2.demo.repository;

import com.ornek2.demo.model.ApplicationTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationTableRepository extends JpaRepository<ApplicationTable, Long>
{

}
