package com.example.javaspringlearn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoriesService {
    @Autowired
    private CategoriesRepository repo;

    public List<Categories> listAll() {
        return repo.findAll();
    }

    public void save(Categories category) {
        repo.save(category);
    }

    public Categories get(Long id) {
        return repo.findById(id).get();
    }

    public Categories getWithCategoryName(String name) {
        return repo.search(name);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
