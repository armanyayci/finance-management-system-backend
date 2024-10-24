package com.sau.swe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("financemngmnt")
    private String SECRET_KEY;
    @Value("${security.jwt.expire}")
    private Long Expire;

    public String findUsername(String token) {

        return exportToken(token, Claims::getSubject);
    }

    private <T> T exportToken(String token, Function<Claims,T> claimsTFunction) {
        final Claims claims= Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();

        return claimsTFunction.apply(claims);
    }
    public Key getKey() {
        byte[] key= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }
    public boolean tokenControl(String jwt, UserDetails userDetails) {
        final String username=findUsername(jwt);
        return(username.equals(userDetails.getUsername()) && !exportToken(jwt,Claims::getExpiration).before(new Date()));
    }
    public String generateToken(UserDetails userDetails, List<String> roles) {
        try {
            // Create a map of claims to store roles
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles",roles);
            // Generate the token with the claims and the username
            return doGenerateToken(claims, userDetails.getUsername());
        } catch (Exception e) {
            // Log a warning if token generation fails
            throw e;
        }
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        try {
            // Build and return the JWT token
            return Jwts.builder()
                    .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + Expire * 1000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        } catch (Exception e) {
            throw e;
        }
    }
    public void validateToken(final String token){
        Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
    }
}
