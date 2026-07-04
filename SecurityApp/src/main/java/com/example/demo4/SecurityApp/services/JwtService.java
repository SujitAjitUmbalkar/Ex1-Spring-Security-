package com.example.demo4.SecurityApp.services;

import com.example.demo4.SecurityApp.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService
{
    @Value("${jwt.secretKey}")
    private String secretKey;

    private SecretKey getSecretKey()            // No parameter is needed because the class already has access to the secret key.
    {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

//    Token generation
    public String generateToken(UserEntity user)
    {
       return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", Set.of("USER", "ADMIN"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60))
                .signWith(getSecretKey())
                .compact();

//compact work -
// Create the JWT header.
//Create the JWT payload (claims).
//Encode the header and payload using Base64URL.
//Generate the signature using your secret key.
//Combine everything into the final JWT string.

    }

//    FIND DETAILS FROM TOKEN
    public Long getUserIdFromToken(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return  Long.parseLong(claims.getSubject());            // subject was string ,convert it in long and return
    }

}
