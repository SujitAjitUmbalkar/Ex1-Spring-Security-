package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.dto.LoginDto;
import com.example.demo4.SecurityApp.dto.LoginResponseDto;
import com.example.demo4.SecurityApp.entities.UserEntity;
import com.example.demo4.SecurityApp.repositories.SessionRepository;
import com.example.demo4.SecurityApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    public LoginResponseDto login(LoginDto loginDto)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        UserEntity user = (UserEntity) authentication.getPrincipal();       // get user

         String accessToken = jwtService.generateAccessToken(user);
         String refreshToken = jwtService.generateRefreshToken(user);
         sessionService.generateNewSession(user , refreshToken);

         return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken)
    {
        Long userId = jwtService.getUserIdFromToken(refreshToken);      // validate refresh token

        sessionService.validSession(refreshToken);          // validate session also

        UserEntity user = userService.getUserById(userId);

        String accessToken = jwtService.generateAccessToken(user);

        return new  LoginResponseDto(user.getId(), accessToken, refreshToken);
    }
}
//call refresh method when you access token expires , you need to pass cookies