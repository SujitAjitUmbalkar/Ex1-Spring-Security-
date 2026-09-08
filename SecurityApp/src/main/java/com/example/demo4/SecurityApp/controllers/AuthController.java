package com.example.demo4.SecurityApp.controllers;

import com.example.demo4.SecurityApp.dto.LoginDto;
import com.example.demo4.SecurityApp.dto.LoginResponseDto;
import com.example.demo4.SecurityApp.dto.SignUpDto;
import com.example.demo4.SecurityApp.dto.UserDto;
import com.example.demo4.SecurityApp.services.AuthService;
import com.example.demo4.SecurityApp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{

    private final UserService userService;
    private final AuthService authService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto)
    {
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")          // returns token
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto,HttpServletRequest request , HttpServletResponse response)
    {
        LoginResponseDto loginResponseDto  = authService.login(loginDto);
        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());    // in cookies store refresh token only
        cookie.setHttpOnly(true);
        cookie.setSecure("development".equals(deployEnv));
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request)
    {
        // Extract the refresh token string from the HTTP request cookies
        String refreshToken = Arrays.stream(request.getCookies())

                // Filter the stream of cookies to find the one named "refreshToken"
                .filter(cookie -> "refreshToken".equals(cookie.getName()))

                // Get the first matching cookie from the filtered stream
                .findFirst()

                // Extract the string value stored inside the cookie
                .map(Cookie::getValue)


                // Throw an exception if no cookie named "refreshToken" was present in the request
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        // Delegate token validation and generation of new JWT tokens to the authentication service
        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

        // Return an HTTP 200 OK response containing the refreshed login DTO payload
        return ResponseEntity.ok(loginResponseDto);
    }

}
