package com.example.demo4.SecurityApp.repositories;

import com.example.demo4.SecurityApp.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> 
{
    Optional<UserDetails> findByEmail(String username);
}
