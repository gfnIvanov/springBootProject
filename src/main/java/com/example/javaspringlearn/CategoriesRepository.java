package com.example.javaspringlearn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CategoriesRepository extends JpaRepository<Categories, Long> {
    @Query("select c from Categories c where c.name = ?1")
    Categories search(String keyword);
}
