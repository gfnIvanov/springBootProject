package com.example.javaspringlearn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface OperationsRepository extends JpaRepository<Operations, Long> {
    @Modifying
    @Transactional
    @Query(value="delete from Operations p where p.product = ?1",
           nativeQuery = true)
    void delByProduct(Long productId);
}
