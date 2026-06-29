package com.example.demo4.SecurityApp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig
{
    @Bean
    ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    UserDetailsService myInMemoryUserDetailsService()
    {
        UserDetails normalUser = User                         // User from  org.springframework.security.core.userdetails
                .withUsername("Sujit")
                .password(passwordEncoder().encode("1234"))
                .roles("USER")
                .build();

        UserDetails adminUser = User                         // User from  org.springframework.security.core.userdetails
                .withUsername("Ajit")
                .password(passwordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
