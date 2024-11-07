package com.sau.swe.security.config;

import com.sau.swe.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (header == null || !header.startsWith("Bearer ")){

            filterChain.doFilter(request,response);
            log.info("header is null or not starts with bearer.!");
            return;
        }
        jwt = header.substring(7);

        username = jwtService.getUsernameFromToken(jwt);

        if (username!= null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails =userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(jwt,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                        (userDetails,null,userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            else{
                log.warn("token is not valid for filter");
            }
        }
        else{
            log.warn("username is null yada mevcut oturum var ");
        }

        filterChain.doFilter(request,response);
    }







}

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        log.debug("JwtSecurityFilter started filtering...");
//
//        String authorizationToken = request.getHeader("Authorization");
//
//        if (StringUtils.isEmpty(authorizationToken) || !(authorizationToken.startsWith("Bearer "))) {
//            log.info("Token is empty or does not start with 'Bearer'");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        authorizationToken = authorizationToken.split(" ")[1].trim();
//        String username = jwtService.getUsernameFromToken(authorizationToken);
//
//        log.debug("Username was extracted from authorization token: {}", username);
//
//        if (!jwtService.validateToken(authorizationToken)) {
//            log.info("Authorization token was not validated for {}", username);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!StringUtils.isEmpty(username) && (authentication == null || !authentication.getName().equals(username))) {
//
//            UserDetails storedUserDetails = userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                    storedUserDetails, null, storedUserDetails.getAuthorities());
//            usernamePasswordAuthenticationToken
//                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            //set authentication with token
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//            log.debug("Authentication is set for username: {}", username);
//        }
//        filterChain.doFilter(request, response);
//    }
//
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        return "/api/authenticate".equals(request.getServletPath()) ||
//                request.getServletPath().startsWith("/api/report");
//    }