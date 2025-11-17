package vn.fu_ohayo.service.impl;

import com.google.api.services.youtube.YouTubeScopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import vn.fu_ohayo.dto.response.ExtractTokenResponse;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.Provider;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.service.JwtService;

import javax.sound.midi.InvalidMidiDataException;
import java.security.Key;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j(topic = "JwtServiceImp")
@Service
public class JwtServiceImp implements JwtService {
    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Override
    public String generateAccessToken(Long userId, String email, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        if (authorities == null) {
            authorities = List.of();
        }
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        roles.add(YouTubeScopes.YOUTUBE_UPLOAD
        );
        claims.put("id", userId);
        roles.add(YouTubeScopes.YOUTUBE_UPLOAD
        );
        claims.put("scope", roles);
        return generateAccessToken(claims, email);
    }

    @Override
    public String generateRefreshToken(Long userId, String email, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = new HashMap<>();
        if (authorities == null) {
            authorities = List.of();
        }
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("id", userId);
        roles.add(YouTubeScopes.YOUTUBE_UPLOAD
        );
        claims.put("scope", roles);
        return generateRefreshToken(claims, email);
    }

    @Override
    public ExtractTokenResponse extractUserInformation(String token, TokenType type) {
        if(extractAllClaims(token, type) != null) {
            Claims claims = extractAllClaims(token, type);
            String email = claims.getSubject();
            Long id = claims.get("id", Long.class);
            List<String> scope = (List<String>) claims.get("scope");
            return new ExtractTokenResponse(email, id, scope);
        }
        return null;
    }

    private Claims extractAllClaims(String token, TokenType type) {
        if(extractToken(token, type)) {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(type))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        return null;
    }

    public boolean extractToken(String token, TokenType type) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey(type))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            throw new AppException(ErrorEnum.UNAUTHORIZED);
        } catch (SignatureException e) {
            log.error("Invalid token signature: {}", e.getMessage());
            throw new AppException(ErrorEnum.FORBIDDEN);
        }
    }

    private String generateAccessToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryMinutes))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }
    private String generateRefreshToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("D0oN9tEJlPfctdhy2mHJlMpiDiBB7QSYQ4PqNADNhI4="));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode("rciTbyStKhFLnSCnbgJnQK5a1l7vVKZIKWDZZXnlVCE="));
            }
            default -> throw new RuntimeException();
        }
    }
}
