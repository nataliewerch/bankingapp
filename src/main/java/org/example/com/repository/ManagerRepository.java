package org.example.com.repository;

import org.example.com.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
