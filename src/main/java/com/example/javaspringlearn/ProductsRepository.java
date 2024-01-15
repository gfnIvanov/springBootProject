package com.example.javaspringlearn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductsRepository extends JpaRepository<Products, Long> {
    @Query("select p from Products p where p.name = ?1")
    Products search(String keyword);

    @Query(value="""
        select coalesce(sum(oo.quant) - coalesce((select sum(oa.quant)
                                                    from Operations oa
                                                   where oa.product = oo.product
                                                     and oa.action in ('write-off', 'to-order')), 0), 0) r
          from Operations oo
         where oo.product = ?1
           and oo.action = 'additional'
           """,
           nativeQuery = true)
    int getActualQuant(Long productId);
}