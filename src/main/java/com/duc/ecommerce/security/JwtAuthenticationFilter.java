package com.duc.ecommerce.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtService jwtService;
    CustomUserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("JWT FILTER RUN");

        String authHeader = request.getHeader("Authorization");

        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            try {
                String username = jwtService.extractUsername(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        userDetails.getAuthorities()
                );

                System.out.println(authentication.getAuthorities());

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "SECURITY CONTEXT = "
                                + SecurityContextHolder.getContext().getAuthentication()
                );
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }
        filterChain.doFilter(request,response);

    }
}
