package com.javaguides.bms.helper;

import com.javaguides.bms.model.LoginCreds;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static Key key;

    private static final long ACCESS_EXP = 1000 * 60 * 15;
    private final long REFRESH_EXP = 1000 * 60 * 60 * 24 * 7;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    public static String generateAccessToken(LoginCreds user) {
        return Jwts.builder()
                .setSubject(user.getCd())
                .claim("role", user.getRole())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(LoginCreds user) {
        return Jwts.builder()
                .setSubject(user.getCd())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(key)
                .compact();
    }

    public static Claims validate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static LoginCreds parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();

        LoginCreds user = new LoginCreds();
        user.setUserId(claims.get("userId").toString());
        user.setCd((String) claims.get("userCd"));
        user.setRole(Integer.parseInt(claims.get("role").toString()));

        return user;
    }

}