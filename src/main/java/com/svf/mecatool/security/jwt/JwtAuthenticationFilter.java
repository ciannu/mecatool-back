package com.svf.mecatool.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        System.out.println("DEBUG: Received Authorization Header: " + authHeader); // TEMPORARY LOG

        final String jwt;
        final String userEmail;

        System.out.println("\n=== JwtAuthenticationFilter ===");
        System.out.println("Request URI: " + request.getRequestURI());

        // Publicly accessible paths (e.g., auth, swagger)
        if (request.getServletPath().startsWith("/auth") ||
            request.getServletPath().startsWith("/v3/api-docs") ||
            request.getServletPath().startsWith("/swagger-ui")) {
            System.out.println("Skipping JWT filter for public endpoint: " + request.getServletPath());
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No JWT token found or invalid header.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out.println("Extracted JWT: " + jwt);

        userEmail = jwtService.extractUsername(jwt);
        System.out.println("Extracted user email: " + userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Loading user details for: " + userEmail);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("User details loaded. Authorities: " + userDetails.getAuthorities());
            System.out.println("User details class: " + userDetails.getClass().getName());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                System.out.println("JWT token is valid.");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set in SecurityContextHolder.");
                System.out.println("Current authentication: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("Current authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            } else {
                System.out.println("JWT token is NOT valid.");
            }
        } else if (userEmail == null) {
            System.out.println("User email could not be extracted from JWT.");
        } else {
            System.out.println("User already authenticated in SecurityContextHolder.");
        }
        filterChain.doFilter(request, response);
    }
} 