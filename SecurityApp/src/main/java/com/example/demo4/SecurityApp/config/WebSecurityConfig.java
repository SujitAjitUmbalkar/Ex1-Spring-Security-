package com.example.demo4.SecurityApp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity          // enable configuring security filter chain
public class WebSecurityConfig
{
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                .authorizeHttpRequests(auth->auth
//                        .requestMatchers("/posts/**").permitAll()      // /posts and future child endpoints will be permitted to all , no need to authenticate
                        .requestMatchers("/posts").permitAll()
                        .requestMatchers("/posts/**").hasAnyRole("ADMIN")        // if you are using InMemoryDB, create users and save in InMemoryDatabase
                        .anyRequest().authenticated())                                  // all paths must be authenticated
                .csrf(csrfConfig-> csrfConfig.disable())            // csrf generation
                .sessionManagement(sessionManagementConfig ->
                        sessionManagementConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   ;      // session creation Policy
//                .formLogin(Customizer.withDefaults());                           // form login page default

        return  httpSecurity.build();                   // build object
    }
}

// here you will notice repeatations while logging in
//formLogin() requires a session to remember the logged-in user.
//STATELESS forbids creating that session.
//        Therefore, after login, Spring has nothing to remember the user, so it redirects back to the login page.


//from now we are dealing with JWT so , no need for sessions  , csrf and form login , comment them

// now , form login page wont be there , login via JWT

// ////////////////////////////////////////////////////////////////////////////////////////////
//OTHER CONCEPTS

/*

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

        // Configure authorization rules for URLs
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()            // Allow everyone to access /public/**
                .requestMatchers("/admin/**").hasRole("ADMIN")        // Only users with ADMIN role
                .requestMatchers("/read/**").hasAuthority("READ")     // Only users with READ authority
                .anyRequest().authenticated()                         // All remaining requests require login
        )

        // Configure form-based login
        .formLogin(login -> login
                .loginPage("/login")                                 // Custom login page URL
                .defaultSuccessUrl("/home")                          // Redirect after successful login
                .failureUrl("/login?error")                          // Redirect if login fails
        )

        // Enable HTTP Basic Authentication
        .httpBasic(Customizer.withDefaults())                        // Allow Basic Auth

        // Configure logout
        .logout(logout -> logout
                .logoutUrl("/logout")                                // URL to trigger logout
                .logoutSuccessUrl("/login")                          // Redirect after logout
        )

        // Configure CSRF protection
        .csrf(csrf -> csrf.disable())                                // Disable CSRF (mainly for REST APIs)

        // Configure session management
        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Never create HTTP Session
        )

        // Configure exception handling
        .exceptionHandling(exception ->
                exception.accessDeniedPage("/access-denied")          // Redirect when access is denied (403)
        )

        // Configure security headers
        .headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin())     // Allow frames from same origin
        )

        // Register custom filters
        .addFilter(new CustomFilter())                               // Add custom filter at default position
        .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class) // Execute JWT filter before login filter
        .addFilterAfter(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class); // Execute logging filter after login filter

        return httpSecurity.build();                                 // Build and register SecurityFilterChain
    }
}
```



 */