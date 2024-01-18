package com.example.javaspringlearn;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

public interface OperationsRepository extends JpaRepository<Operations, Long> {
    @Modifying
    @Transactional
    @Query(value="delete from Operations p where p.product = ?1",
           nativeQuery = true)
    void delByProduct(Long productId);

    @Query(value="""
        select c.name,
               o.action,
               sum(o.quant) as quant
          from Operations o
               join Products p on o.product = p.id
               join Categories c on p.category = c.id
         group by p.category, o.action
            """,
           nativeQuery = true)
    List<Tuple> getDataForGraphs();
}
