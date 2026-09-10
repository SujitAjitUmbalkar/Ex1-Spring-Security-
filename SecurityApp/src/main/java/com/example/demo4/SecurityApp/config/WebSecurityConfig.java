package com.example.demo4.SecurityApp.config;

import com.example.demo4.SecurityApp.entities.enums.Permission;
import com.example.demo4.SecurityApp.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] PUBLIC_ROUTES = {
            "/error",
            "/auth/**",
            "/home.html"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())

                .sessionManagement(sessionManagementConfig ->
                        sessionManagementConfig
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(PUBLIC_ROUTES).permitAll()

                        // Permission-based authorization
                        .requestMatchers(HttpMethod.POST, "/posts/**")
                        .hasAuthority(Permission.POST_CREATE.name())

                        .requestMatchers(HttpMethod.GET, "/posts/**")
                        .hasAuthority(Permission.POST_VIEW.name())

                        .requestMatchers(HttpMethod.PUT, "/posts/**")
                        .hasAuthority(Permission.POST_UPDATE.name())

                        .requestMatchers(HttpMethod.DELETE, "/posts/**")
                        .hasAuthority(Permission.POST_DELETE.name())

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }
}