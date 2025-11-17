package vn.fu_ohayo.controller.admin;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.config.AuthConfig;
import vn.fu_ohayo.dto.request.admin.AdminLoginRequest;
import vn.fu_ohayo.dto.DTO.AdminDTO;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.ExtractTokenResponse;
import vn.fu_ohayo.dto.response.TokenResponse;
import vn.fu_ohayo.entity.Admin;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.TokenType;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.UserMapper;
import vn.fu_ohayo.repository.AdminRepository;
import vn.fu_ohayo.repository.RoleRepository;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.impl.AuthenticationServiceImp;

@Slf4j(topic = "ADMIN_CONTROLLER")
@RestController
@RequestMapping("/admin")
public class AdminAuthController {
    final AuthConfig a;
    final AdminRepository adminRepository;
    final RoleRepository roleRepository;
    final AuthenticationServiceImp authenticationService;
    final UserMapper userMapper;
    final UserRepository userRepository;
    final JwtService jwtService;

    public AdminAuthController(AuthConfig a, AdminRepository adminRepository, RoleRepository roleRepository, AuthenticationServiceImp authenticationService, UserMapper userMapper, UserRepository userRepository, JwtService jwtService) {
        this.a = a;
        this.adminRepository = adminRepository;
        this.roleRepository = roleRepository;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginForAdmin(@RequestBody AdminLoginRequest adminLoginRequest) {
        TokenResponse tokenResponse = authenticationService.getAccessTokenForAdmin(adminLoginRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .build();

        log.info("User logged in successfully: {}", adminLoginRequest.getEmail());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.<TokenResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User logged in successfully")
                        .data(new TokenResponse(tokenResponse.getAccessToken(), null)) // KHÔNG gửi refreshToken trong body
                        .build());

}

//@GetMapping
//    public void login () {
//    String username = "admin2";
//    String password = a.passwordEncoder().encode("123123");
//    Set<Role> role = new HashSet<>();
//    role.add(roleRepository.findByName(RoleEnum.CONTENT_MANAGER));
//    Admin b = Admin.builder().username(username).password(password).roles(role).build();
//    adminRepository.save(b);
//}

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<AdminDTO>> login () {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
       Admin a = adminRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        AdminDTO response = userMapper.toAdmin(a);
       return ResponseEntity.ok().body(ApiResponse.<AdminDTO>builder().data(response).build());
    }
    @GetMapping("/check-login")
    public ResponseEntity<ApiResponse<TokenResponse>> checkLogin(HttpServletRequest request) {
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.ok(
                    ApiResponse.<TokenResponse>builder()
                            .code("401")
                            .status("Unauthorized")
                            .message("No refresh token found")
                            .data(null)
                            .build()
            );
        }
        ExtractTokenResponse response = jwtService.extractUserInformation(refreshToken, TokenType.REFRESH_TOKEN);
        if (response.getEmail() == null) {
            throw new AppException(ErrorEnum.INVALID_TOKEN);
        }
        TokenResponse tokenResponse = authenticationService.getRefreshToken(refreshToken, "admin");
        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User is logged in")
                        .data(new TokenResponse(tokenResponse.getAccessToken(), null)) // KHÔNG gửi refreshToken trong body
                        .build()
        );

    }

}
