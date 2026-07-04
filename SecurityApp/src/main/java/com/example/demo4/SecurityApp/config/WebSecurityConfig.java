package com.example.demo4.SecurityApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable()) // Disable CSRF for stateless REST APIs
                .sessionManagement(sessionManagementConfig ->
                        sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. Publicly accessible authentication endpoints (Sign-up, Login)
                        .requestMatchers("/auth/**").permitAll()

                        // 2. Allow everyone to VIEW posts (GET requests to /posts or /posts/123)
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()

                        // 3. Restrict modifying posts (POST, PUT, DELETE) to ADMIN only
                        .requestMatchers("/posts/**").hasRole("ADMIN")

                        // 4. Everything else requires authentication
                        .anyRequest().authenticated()
                );

        return httpSecurity.build();
    }
}