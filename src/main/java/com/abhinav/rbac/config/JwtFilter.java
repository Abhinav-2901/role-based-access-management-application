package com.abhinav.rbac.config;

import com.abhinav.rbac.application.users.UserService;
import com.abhinav.rbac.domain.model.User;
import com.abhinav.rbac.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    	System.out.println("[JwtFilter] Filtering request: " + request.getMethod() + " " + request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            System.out.println("JWT extracted: " + jwt);
            System.out.println("Username from JWT: " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                // Extract roles from JWT
                Set<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(jwt).stream()
                        .map(SimpleGrantedAuthority::new)   // Spring Security expects "ROLE_USER", etc.
                        .collect(Collectors.toSet());

                // Create auth token with roles
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("[JwtFilter] Authenticated user: " + username + " with roles: " + authorities);
            } else {
                System.out.println("[JwtFilter] Invalid JWT token");
            }
        }

        filterChain.doFilter(request, response);
    }
}