package com.javaguides.sps.helper;

import com.javaguides.sps.model.LoginCreds;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    private Key key;
//
//    private final long ACCESS_EXP = 1000 * 60 * 15;
//    private final long REFRESH_EXP = 1000 * 60 * 60 * 24 * 7;
//
//    @PostConstruct
//    public void init() {
//        this.key = Keys.hmacShaKeyFor(
//                Decoders.BASE64.decode(jwtSecret)
//        );
//    }
//
//    public String generateAccessToken(LoginCreds user) {
//        return Jwts.builder()
//                .setSubject(user.getCd())
//                .claim("role", user.getRole())
//                .claim("userId", user.getUserId())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
//                .signWith(key)
//                .compact();
//    }
//
//    public String generateRefreshToken(LoginCreds user) {
//        return Jwts.builder()
//                .setSubject(user.getCd())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
//                .signWith(key)
//                .compact();
//    }
//
//    public Claims validate(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
}