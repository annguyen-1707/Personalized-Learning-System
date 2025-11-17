package vn.fu_ohayo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import vn.fu_ohayo.config.AuthConfig;
import vn.fu_ohayo.dto.request.InitialRegisterRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.user.UserResponse;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.enums.UserStatus;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.impl.UserServiceImp;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
 class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private MailService mailService;

    @Mock
    private AuthConfig configuration;

    @InjectMocks
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(configuration.passwordEncoder()).thenReturn(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "hashedPassword";
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return true;
            }
        });
    }

    @Test
    void registerInitial_successfully() {
        // Arrange
        InitialRegisterRequest request = new InitialRegisterRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("secret123");

        User user = User.builder()
                .userId(1L)
                .email(request.getEmail())
                .status(UserStatus.INACTIVE)
                .password("hashedPassword")
                .build();

        Mockito.when(userRepository.existsByEmailAndStatus(request.getEmail(), UserStatus.INACTIVE)).thenReturn(false);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(jwtService.generateAccessToken(1L, request.getEmail(), null)).thenReturn("fake-jwt");

        // Act
        ApiResponse<?> response = userService.registerInitial(request);

        // Assert
        assertEquals("200", response.getCode());
        assertEquals("OK", response.getStatus());
        assertEquals("User registered successfully", response.getMessage());

        // Verify side effects
        verify(mailService).sendEmail(eq("newuser@example.com"), eq("fake-jwt"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerInitial_fail_whenEmailExists() {
        // Arrange
        InitialRegisterRequest request = new InitialRegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("pass");

        Mockito.when(userRepository.existsByEmailAndStatus(request.getEmail(), UserStatus.INACTIVE)).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> userService.registerInitial(request));
        assertEquals(ErrorEnum.EMAIL_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void findByID_Test() {
        Long userId = 1L;
        User user = User.builder()
                .userId(userId)
                .email("thai110504@gmail.com").build();
        Mockito.when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        User user1 = userService.getUserById(1L);

        assertEquals(user1.getEmail(), user.getEmail());
    }

    @Test
    void findByID_NotFound_Test() {
        Long userId = 1L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        AppException exception = assertThrows(AppException.class, () -> userService.getUserById(userId));
        assertEquals(ErrorEnum.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    void getAllUser_Test() {
    // Arrange
        List<UserResponse> userList = new ArrayList<>();
       userList.add(UserResponse.builder().userId(1L).email("thai110504@gmail.com").build());
       userList.add(UserResponse.builder().userId(2L).email("thai1105044@gmail.com").build());

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        // Act
        List<UserResponse> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
    }

}
