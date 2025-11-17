package vn.fu_ohayo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.UserServiceDetail;
import vn.fu_ohayo.service.impl.AdminDetailService;
import vn.fu_ohayo.service.impl.AdminServiceDetail;
import vn.fu_ohayo.service.impl.JwtServiceImp;

import java.io.IOException;
@Slf4j(topic = "CustomizeRequestFilter")
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CustomizeRequestFilter extends OncePerRequestFilter {
    JwtServiceImp jwtService;
    UserServiceDetail userServiceDetail;
    AdminDetailService adminDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("Processing request: {} {}", method, path);

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                jwtService.extractToken(token, TokenType.ACCESS_TOKEN);
            } catch (AppException e) {
                if (e.getMessage().equals("Access token expired")) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired");
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                    return;
                }
            }
            try {
                String username = jwtService.extractUserInformation(token, TokenType.ACCESS_TOKEN).getEmail();
                UserDetails user = null;
                try {
                    user = userServiceDetail.UserServiceDetail().loadUserByUsername(username);
                    log.info("Loaded as USER");
                } catch (Exception e1) {
                    try {
                        user = adminDetailService.loadUserByUsername(username);
                        log.info("Loaded as ADMIN");
                    } catch (Exception e2) {
                        log.warn("User not found in both USER and ADMIN services");
                    }
                }

                if (user != null) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            } catch (Exception e) {
                log.error("Error extracting user information from token: {}", e.getMessage());
                // Không throw ở đây nữa để không chặn luồng các API public hoặc OPTIONS
            }
        }

        filterChain.doFilter(request, response);
    }

}
