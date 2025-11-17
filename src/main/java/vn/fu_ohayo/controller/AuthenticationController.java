package vn.fu_ohayo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.CompleteProfileRequest;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.request.OAuthRequest;
import vn.fu_ohayo.dto.request.SignInRequest;
import vn.fu_ohayo.dto.response.*;
import vn.fu_ohayo.dto.response.user.UserFromProvider;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.*;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.JwtService;
import vn.fu_ohayo.service.MailService;
import vn.fu_ohayo.service.impl.AuthenticationServiceImp;
import vn.fu_ohayo.service.impl.UserServiceImp;

import java.io.IOException;


@Slf4j(topic = "AUTHCONTROLLER")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    MailService mailService;
    UserServiceImp userService;
    UserRepository userRepository;
    AuthenticationServiceImp authenticationService;
    JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> registerInit(@RequestBody InitialRegisterRequest initialRegisterRequest) {
            return ResponseEntity.ok(
                    userService.registerInitial(initialRegisterRequest)
            );
        }

    @GetMapping("/mailAgain")
    public ResponseEntity<String> sendMailAgain(@RequestParam("emailAgain") String email) {
        mailService.sendEmailAgain(email);
        return ResponseEntity.ok("oke");
    }

    @GetMapping("/social-login")
    public ResponseEntity<AuthUrlResponse> socialAuth(@RequestParam("login_type") String type) {
        String url = authenticationService.generateAuthURL(type);
        AuthUrlResponse response = new AuthUrlResponse(url);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<String>> checkUserStatus(@RequestParam("email") String email) {
        User user = userRepository.findByEmailAndProvider(email, Provider.LOCAL).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        if (user.getStatus().equals(UserStatus.ACTIVE)) {
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code("200")
                            .status("OK")
                            .message("User registered")
                            .data("Registered")
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code("200")
                        .status("Failed")
                        .message("User registered")
                        .data("Not Active")
                        .build());
    }

    @PostMapping("/complete-profile")
    public ApiResponse<String> completeProfile( @RequestParam String email,@Valid @RequestBody CompleteProfileRequest completeProfileRequest) {
        log.info(email);

        userService.completeProfile(completeProfileRequest, email);
        return ApiResponse.<String>builder()
                .code("200")
                .status("OK")
                .message("Profile completed successfully")
                .data("Profile completed successfully")
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody SignInRequest signInRequest) {
        if(!userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)).getStatus().equals(UserStatus.ACTIVE)) {
            throw new AppException(ErrorEnum.ACCOUNT_INACTIVE);
        }
        TokenResponse tokenResponse = authenticationService.getAccessToken(signInRequest);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(60 * 60 * 24 * 7L)
                .build();
        log.info("User logged in successfully: {}", signInRequest.getEmail());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.<TokenResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User logged in successfully")
                        .data(new TokenResponse(tokenResponse.getAccessToken(), null))
                        .build());
    }

    @PostMapping("/getOAuthToken")
    public ResponseEntity<ApiResponse<TokenResponse>> getOAuthToken(@RequestBody OAuthRequest request) {
        TokenResponse tokenResponse = authenticationService.getAccessTokenForSocialLogin(request.getEmail(), request.getProvider());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(7L * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.<TokenResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User logged in successfully")
                        .data(new TokenResponse(tokenResponse.getAccessToken(), null))
                        .build());
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByToken() {
        UserResponse userResponse = userService.getUser();
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User retrieved successfully")
                        .data(userResponse)
                        .build()
        );
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
        if (refreshToken == null || !authenticationService.extractToken(refreshToken, TokenType.REFRESH_TOKEN)) {
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
        TokenResponse tokenResponse = authenticationService.getRefreshToken(refreshToken, "user");
        log.info(tokenResponse.getAccessToken());
        return ResponseEntity.ok(
                ApiResponse.<TokenResponse>builder()
                        .code("200")
                        .status("OK")
                        .message("User is logged in")
                        .data(new TokenResponse(tokenResponse.getAccessToken(), null)) // KHÔNG gửi refreshToken trong body
                        .build()
        );

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/code/{provider}")
    public void handleOAuthCallback(@PathVariable String provider,
                                   @RequestParam("code") String code,
                                   HttpServletResponse response) throws IOException {
        String accessToken = authenticationService.getAccessTokenFromProvider(provider, code);
        UserFromProvider user = authenticationService.getUserInfoFromProvider(provider, accessToken);

        String redirectUrl = "http://localhost:5173/oauth-callback?email=" + user.getEmail() + "&exist=" + user.isExist();

        response.sendRedirect(redirectUrl);
    }
}