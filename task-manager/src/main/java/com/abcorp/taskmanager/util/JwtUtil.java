package com.abcorp.taskmanager.util;

import com.abcorp.taskmanager.model.response.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class JwtUtil {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    @Value("${taskmanager.jwt.secret}")
    private String secret;
    @Value("${taskmanager.jwt.expiration}")
    private Long expiration;

    public String generateToken(UserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDto.getId());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDto.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SIGNATURE_ALGORITHM, secret).compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public List<GrantedAuthority> generateAuthoritiesFromClaims(Claims claims) {
        return new ArrayList<>(generateAuthoritiesFromName(List.of("task")));
    }

    private List<GrantedAuthority> generateAuthoritiesFromName(List<String> auth) {
        return auth.stream().map(
                r -> (GrantedAuthority) () -> r
        ).toList();
    }

}
