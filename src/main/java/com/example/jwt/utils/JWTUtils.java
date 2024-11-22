package com.example.jwt.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Function;

@Component
public class JWTUtils {
    @Value("${jwt.secret.key}")
    private String sec_key;

    public String generateToken(UserDetails ud) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, ud.getUsername());
    }

    private String createToken(Map<String, Object> c, String subject) {
        return Jwts.builder().setClaims(c).setSubject(subject).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(SignatureAlgorithm.HS256, sec_key).compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> cr) {
        final Claims c = Jwts.parser().setSigningKey(sec_key).parseClaimsJws(token).getBody();
        return cr.apply(c);
    }

    public boolean isTokenValid(String token, UserDetails ud) {
        final String username = extractUsername(token);
        return username.equals(ud.getUsername()) && !isTokenExpired(token);
    }

    boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
