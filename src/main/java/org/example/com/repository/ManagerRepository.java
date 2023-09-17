package org.example.com.repository;

import org.example.com.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Manager entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
