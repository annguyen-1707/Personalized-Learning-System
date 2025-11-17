package vn.fu_ohayo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.service.impl.PasswordForgotImp;
import vn.fu_ohayo.repository.UserRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {

    private final PasswordForgotImp passwordForgotImp;
    private final UserRepository userRepository;

    //    public ForgotPasswordController(PasswordForgotService passwordForgotService, UserRepository userRepository) {
//        this.passwordForgotService = passwordForgotService;
//        this.userRepository = userRepository;
//    }
    //kiem tra email co ton tai trong he thong hay khong
    //neu ton tai thi gui ma reset password den email do
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        if (!userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(404).body("Email is not registered");
        }
        passwordForgotImp.createAndSendToken(request.getEmail());
        return ResponseEntity.ok("Reset code sent if email exists");
    }

    //khi nhan duoc token nguoi dung se duong chuyen den trang reset password
    //xu ly token va cho phep nguoi dung nhap mat khau moi
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        //otp token validated here
        boolean result = passwordForgotImp.resetPassword(request.getToken(), request.getNewPassword());
        if (result) {
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.badRequest().body("Failed to reset password");
        }
    }
    @Getter
    @Setter
    public static class ForgotPasswordRequest {
        private String email;
    }
    @Getter
    @Setter
    public static class ResetPasswordRequest {
        private String token;
        private String newPassword;
        private String confirmPassword;
    }
}
