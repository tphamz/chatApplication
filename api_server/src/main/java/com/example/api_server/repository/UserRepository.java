package com.example.api_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.api_server.model.*;
public interface UserRepository extends JpaRepository <User, Integer>{
    public User findByEmail(String email);
    public User findByUsername(String email);

}
