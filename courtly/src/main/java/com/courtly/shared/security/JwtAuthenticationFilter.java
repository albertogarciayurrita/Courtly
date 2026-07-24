package com.courtly.shared.security;

import java.io.IOException;
import java.net.Authenticator;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.courtly.auth.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization"); 
        //if doen't have authorization header, just pass the request to the next filter in the chain

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; //ends here, doesn't continue to the next lines
        }

        String token = authorizationHeader.substring(7); // Remove "Bearer " prefix, so we can get the actual token only without the "bearer" part

        if (!jwtService.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; //ends here, doesn't continue to the next lines
        }

        String email = jwtService.extractEmail(token);
        String role = jwtService.extractRole(token);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role); 

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        filterChain.doFilter(request, response);
    }
}