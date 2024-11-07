package com.sau.swe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expire}")
    private Long EXPIRE_TIME;

    public String getUsernameFromToken(String token) {
        try {
            System.out.println(SECRET_KEY + EXPIRE_TIME);
            return getClaimFromToken(token, Claims::getSubject);
        } catch (Exception UsernameNotFoundException) {
            log.warn("Invalid username while getting from token");
            throw UsernameNotFoundException;
        }

    }

    public Date getExpirationDateFromToken(String token) {

        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (Exception e) {

            log.warn("the expirations couldn't get from the token");
            throw e;
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getAllClaimsFromToken(token);
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.warn("claims couldnt get from the token with t function");
            throw e;
        }

    }
    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.warn("couldnt get the informations from token with secret key");
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.warn("");
            throw e;
        }

    }

    public String generateToken(UserDetails userDetails) {
        try {
            Map<String, Object> claims = new HashMap<>();
            return doGenerateToken(claims, userDetails.getUsername());
        } catch (Exception e) {
            log.warn("couldn't generate token with user details ");
            throw e;
        }

    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        try {
            return Jwts.builder()
                    .setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME * 1000))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        } catch (Exception e) {
            log.warn("couldnt dogeneratetoken ");
            throw e;
        }

    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            log.warn("couldnt validatetoken");
            throw e;
        }
    }
}
