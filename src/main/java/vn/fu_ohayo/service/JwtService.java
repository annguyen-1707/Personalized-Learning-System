package vn.fu_ohayo.service;

import org.springframework.security.core.GrantedAuthority;
import vn.fu_ohayo.dto.response.ExtractTokenResponse;
import vn.fu_ohayo.enums.TokenType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface JwtService {
    String generateAccessToken(Long userId, String email, Collection<? extends GrantedAuthority> authorities);
    String generateRefreshToken(Long userId, String email, Collection<? extends GrantedAuthority> authorities);
    ExtractTokenResponse extractUserInformation(String token, TokenType type);
}
