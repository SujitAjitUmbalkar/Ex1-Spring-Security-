package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.dto.LoginDto;
import com.example.demo4.SecurityApp.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String login(LoginDto loginDto)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        UserEntity user = (UserEntity) authentication.getPrincipal();       // get user
        return jwtService.generateToken(user);
    }

}

// dont inject dependencies of UserDetailsService and AuthenticationManager in each other , cyclic exception forms
