package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.validation.PasswordResetValidate;
import vn.fu_ohayo.service.PasswordForgotService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Getter
public class PasswordForgotImp implements PasswordForgotService {
    private final Map<String, TokenInfo> tokenStore = new HashMap<>();
    //    SLF4J (Simple Logging Facade for Java) – A facade (abstraction layer)
//    that allows switching between different logging implementations
    //logger co gui thong tin ra console, file, database, ...
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PasswordForgotImp.class);
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private static final Random random = new Random();

    @AllArgsConstructor
    @Getter
    public static class TokenInfo {
        private final String email;
        private LocalDateTime expiryTime;
    }

    @Override
    // tao va gui ma xac nhan den email cua nguoi dung
    public void createAndSendToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.warn("Email not found.");
            return;
        }
        tokenStore.entrySet().removeIf(entry -> entry.getValue().getEmail().equals(email));

        String token = generateToken(email);
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        tokenStore.put(token, new TokenInfo(email, expiryTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Password Reset Code");
        message.setText("Your password reset code is: " + token);
        message.setFrom("thai110504@gmail.com");
        mailSender.send(message);
    }

    @Override
    // doi mat khau cho nguoi dung khi nhap token
    public boolean resetPassword(String token, String newPassword) {
        TokenInfo tokenInfo = tokenStore.get(token);
        // kiem tra token co hop le va chua het han hay khong
        if (tokenInfo == null || PasswordResetValidate.isTokenValid(token, tokenStore)) {
            logger.warn("Invalid or expired token.");
            return false;
        }
        // neu hop le thi cho phep nguoi dung nhap mat khau moi
        if (!PasswordResetValidate.isPasswordNotEmpty(newPassword)) {
            logger.warn("New password cannot be empty.");
            return false;
        }
        if (!PasswordResetValidate.isPasswordLengthValid(newPassword, 8)) {
            logger.warn("New password must be at least 8 characters long.");
            return false;
        }
        Optional<User> userOpt = userRepository.findByEmail(tokenInfo.email);
        if (userOpt.isEmpty())
            return false;
        User user = userOpt.get();
        String newHashed = passwordEncoder.encode(newPassword);
        user.setPassword(newHashed);
        userRepository.save(user);
        tokenStore.remove(token);
        return true;
    }

    @Override
    // tao ma xac nhan ngau nhien 6 so de gui den email
    public String generateToken(String email) {
        int number = random.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}
