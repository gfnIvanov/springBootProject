package com.example.javaspringlearn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostsService {
    @Autowired
    private PostsRepository repo;

    public List<Posts> listAll() {
        return repo.findAll();
    }

    public void save(Posts post) {
        repo.save(post);
    }
}
