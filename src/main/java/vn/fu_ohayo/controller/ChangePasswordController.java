package vn.fu_ohayo.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.impl.PasswordChangeImp;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class ChangePasswordController {

    private final UserRepository userRepository;
    private final PasswordChangeImp passwordChangeImp;


    @PostMapping("/change-password")
    //ham principal de lay thong tin nguoi dung dang dang nhap
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, java.security.Principal principal) {
        String email = principal != null ? principal.getName() : request.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        try {
            passwordChangeImp.changePassword(
                    user,
                    request.getCurrentPassword(),
                    request.getNewPassword(),
                    request.getConfirmPassword()
            );
            return ResponseEntity.ok("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Getter
    @Setter
    public static class ChangePasswordRequest {
        private String email;
        private String currentPassword;
        private String newPassword;
        private String confirmPassword;
    }
}