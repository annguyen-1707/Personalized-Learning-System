package vn.fu_ohayo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordChangeImpTest {

    @Mock
    UserRepository userRepository;

    PasswordChangeImp passwordChangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordChangeService = new PasswordChangeImp(userRepository);
    }

    @Test
    void changePassword_success() {
        // Given
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String oldPass = "12345678";
        String newPass = "newpassword";

        User user = new User();
        user.setPassword(encoder.encode(oldPass));

        // When
        boolean result = passwordChangeService.changePassword(user, oldPass, newPass, newPass);

        // Then
        assertTrue(result);
        verify(userRepository).save(user);
        assertNotEquals(oldPass, user.getPassword()); // ensure it's hashed and changed
    }

    @Test
    void changePassword_wrongCurrentPassword_shouldThrow() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode("correct-password"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            passwordChangeService.changePassword(user, "wrong-password", "newpass123", "newpass123");
        });

        assertEquals("Current password is incorrect.", ex.getMessage());
    }

    @Test
    void changePassword_passwordsDoNotMatch_shouldThrow() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode("password123"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            passwordChangeService.changePassword(user, "password123", "newpass123", "mismatch123");
        });

        assertEquals("New passwords do not match.", ex.getMessage());
    }

    @Test
    void changePassword_tooShort_shouldThrow() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode("password123"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            passwordChangeService.changePassword(user, "password123", "short", "short");
        });

        assertEquals("New password must be at least 8 characters.", ex.getMessage());
    }

    @Test
    void changePassword_sameAsCurrent_shouldThrow() {
        String password = "password123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setPassword(encoder.encode(password));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            passwordChangeService.changePassword(user, password, password, password);
        });

        assertEquals("New password must be different from the current password.", ex.getMessage());
    }
}
