package com.example.demo4.SecurityApp.filters;

import com.example.demo4.SecurityApp.entities.UserEntity;
import com.example.demo4.SecurityApp.services.JwtService;
import com.example.demo4.SecurityApp.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter // Extending this guarantees the filter executes exactly once per API request
{
    private final JwtService jwtService; // Injected service used to decode, parse, and extract data from JwtService
    private final UserService userService; // Injected service used to fetch user details from the database using an ID

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, // Represents the incoming HTTP request (headers, body, URI)
            HttpServletResponse response, // Represents the outgoing HTTP response sent back to the client
            FilterChain filterChain)
            throws ServletException, IOException // Represents the chain of other security filters to execute next
    {
        // Looks up and extracts the value of the HTTP header named "Authorization" , it is key , we fetch value
        final String requestTokenHeader =  request.getHeader("Authorization");

        // Checks if the header is completely missing OR if it doesn't start with the standard prefix "Bearer "
        if(requestTokenHeader == null    ||      !requestTokenHeader.startsWith("Bearer "))
        {
            // Hands the request over to the next filter in the security chain without authenticating anyone
            filterChain.doFilter(request, response);
            // Exits this method early so the rest of the token parsing logic below is skipped
            return;
        }

        // Strips out the word "Bearer " leaving behind only the raw, encoded cryptographic JWT string
        String token = requestTokenHeader.replace("Bearer ", "");

        // Decodes the raw token using your JWT service to extract the user's primary key (userId)
        Long userId = jwtService.getUserIdFromToken(token);

        // Checks if a valid user ID was found AND confirms that this user isn't already logged into the current request security context
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            // Queries your database to get the full UserEntity object corresponding to the extracted ID
            UserEntity userEntity = userService.getUserById(userId);

            // Creates Spring's internal "identity card", holding the user object, credentials (null/hidden), and their roles/authorities
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userEntity, null, userEntity.getAuthorities());

            // Extracts extra web details from the current request (like IP address or session details) and attaches them to the token
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Officially authenticates the user by saving their identity card into Spring Security's context for this specific request
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Tells Spring to move forward and execute the next security filter or hand the request to your Controller
        filterChain.doFilter(request, response);

    }
}