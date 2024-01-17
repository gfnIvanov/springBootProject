package com.example.javaspringlearn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OperationsService {
    @Autowired
    private OperationsRepository repo;

    public List<Operations> listAll() {
        return repo.findAll();
    }

    public void save(Operations operation) {
        repo.save(operation);
    }

    public void delByProduct(Long productId) {
        repo.delByProduct(productId);
    }
}
