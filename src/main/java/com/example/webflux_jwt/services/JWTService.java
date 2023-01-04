package com.example.webflux_jwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTService {
    final private SecretKey key;
    final private JwtParser parser;


    public JWTService() {
        this.key = Keys.hmacShaKeyFor("324325reafar3qrqfdsjfhdsifghdsfghdsgfhdsgfhdsaf3r3r3".getBytes());
        this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }


    //return a jwt token based on the username
    public String generate(String userName){
        JwtBuilder builder = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
                .signWith(key);
        return builder.compact();
    }

    public String getUserName(String token){
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validate (UserDetails user, String token){
        Claims claims = parser.parseClaimsJws(token).getBody();
        boolean unexpired = claims.getExpiration().after(Date.from(Instant.now()));
        return unexpired && user.getUsername() == claims.getSubject();
    }


}
