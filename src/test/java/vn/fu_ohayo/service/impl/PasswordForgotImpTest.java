package vn.fu_ohayo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordForgotImpTest {

   // mock is a way to create a fake object that simulates the behavior of a real object
    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    // InjectMocks is used to create an instance of the class under test and inject the mocks into it
    @InjectMocks
    private PasswordForgotImp passwordForgotService;

    private User testUser;
    private final String testEmail = "test@example.com";

    //run before each test
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword("oldPassword");
    }

    @Test
    void createAndSendToken_WithExistingUser_SendsEmail() {
        // Arrange
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        // Act
        passwordForgotService.createAndSendToken(testEmail);

        // Assert
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(testEmail, sentMessage.getTo()[0]);
        assertEquals("Your Password Reset Code", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Your password reset code is:"));

        // Verify token was stored
        Map<String, PasswordForgotImp.TokenInfo> tokenStore = passwordForgotService.getTokenStore();
        assertEquals(1, tokenStore.size());
    }

    @Test
    void createAndSendToken_WithNonExistingUser_DoesNotSendEmail() {
        // Arrange
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        // Act
        passwordForgotService.createAndSendToken(testEmail);

        // Assert
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
        assertTrue(passwordForgotService.getTokenStore().isEmpty());
    }

    @Test
    void resetPassword_WithValidTokenAndPassword_UpdatesPassword() {
        // Arrange
        String token = "123456";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedPassword";

        // Setup token in store
        PasswordForgotImp.TokenInfo tokenInfo = new PasswordForgotImp.TokenInfo(testEmail, LocalDateTime.now().plusMinutes(10));
        passwordForgotService.getTokenStore().put(token, tokenInfo);

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        // Act
        boolean result = passwordForgotService.resetPassword(token, newPassword);

        // Assert
        assertTrue(result);
        verify(userRepository).save(testUser);
        assertEquals(encodedPassword, testUser.getPassword());
        assertTrue(passwordForgotService.getTokenStore().isEmpty());
    }

    @Test
    void resetPassword_WithInvalidToken_ReturnsFalse() {
        // Act
        boolean result = passwordForgotService.resetPassword("invalidToken", "newPassword123");

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void resetPassword_WithShortPassword_ReturnsFalse() {
        // Arrange
        String token = "123456";
        String shortPassword = "short";

        // Setup token in store
        PasswordForgotImp.TokenInfo tokenInfo = new PasswordForgotImp.TokenInfo(testEmail, LocalDateTime.now().plusMinutes(10));
        passwordForgotService.getTokenStore().put(token, tokenInfo);

        // Act
        boolean result = passwordForgotService.resetPassword(token, shortPassword);

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void resetPassword_WithEmptyPassword_ReturnsFalse(){
        // Arrange
        String token = "123456";
        passwordForgotService.getTokenStore().put(token,
                new PasswordForgotImp.TokenInfo(testEmail, LocalDateTime.now().plusMinutes(10)));

        // Act
        boolean result = passwordForgotService.resetPassword(token, "");

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void resetPassword_WithValidTokenButNoUser_ReturnsFalse() {
        // Arrange
        String token = "123456";
        String nonExistingEmail = "nonexistent@example.com";
        passwordForgotService.getTokenStore().put(token,
                new PasswordForgotImp.TokenInfo(nonExistingEmail, LocalDateTime.now().plusMinutes(10)));

        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // Act
        boolean result = passwordForgotService.resetPassword(token, "newPassword123");

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void generateToken_ReturnsValidSixDigitToken() {
        // Act
        String token = passwordForgotService.generateToken(testEmail);

        // Assert
        assertNotNull(token);
        assertEquals(6, token.length());
        assertTrue(token.matches("\\d{6}"));
    }
}