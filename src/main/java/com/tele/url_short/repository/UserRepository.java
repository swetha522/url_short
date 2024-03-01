package com.tele.url_short.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tele.url_short.dto.User;

public interface UserRepository extends JpaRepository<User,Integer>
{

    boolean existsByEmail(String email);

    User findByEmail(String email);
    
}
