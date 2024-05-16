package com.abcorp.taskmanager.filter;

import com.abcorp.taskmanager.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(AUTHORIZATION_HEADER);
        String username = null;
        Claims claims = null;
        String jwt;
        if (null != authorization && authorization.startsWith("Bearer ")) {
            jwt = authorization.substring(7);
            claims = jwtUtil.extractAllClaims(jwt);
            username = claims.getSubject();
        }
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UUID userId = UUID.fromString(claims.get("userId").toString());
            List<GrantedAuthority> authorities = jwtUtil.generateAuthoritiesFromClaims(claims);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
