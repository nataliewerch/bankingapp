package org.example.com.repository;

import org.example.com.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Product entities.
 *
 * @author Natalie Werch
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Retrieves a list of products associated with a specific manager.
     *
     * @param id the unique identifier of the manager.
     * @return a list of products associated with the specified manager.
     */
    List<Product> getAllByManager_Id(Long id);
}
