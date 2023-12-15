package com.example.javaspringlearn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UsersRepository extends JpaRepository<Users, Long> {
    @Query("select u from Users u where u.login = ?1")
    Users search(String keyword);
}
