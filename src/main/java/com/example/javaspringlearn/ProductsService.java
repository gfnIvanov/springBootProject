package com.example.javaspringlearn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductsService {
    @Autowired
    private ProductsRepository repo;

    public List<Products> listAll() {
        return repo.findAll();
    }

    public void save(Products products) {
        repo.save(products);
    }

    public Products get(Long id) {
        return repo.findById(id).get();
    }

    public Products getWithProductName(String name) {
        return repo.search(name);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public int getActualQuant(Long id) {
        return repo.getActualQuant(id);
    }
}
