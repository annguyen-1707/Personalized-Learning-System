package vn.fu_ohayo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.User;
import vn.fu_ohayo.repository.UserRepository;
import vn.fu_ohayo.service.PasswordChangeService;

@Service
@AllArgsConstructor
public class PasswordChangeImp implements PasswordChangeService {
    // dung bien final vi userRepository la mot dependency
    private final UserRepository userRepository;

    @Override
    //khi nguoi dung dang nhap vao profile cua minh va muon doi mat khau
    //doi mat khau cho nguoi dung trong profile
    public boolean changePassword(User user, String currentPassword, String newPassword, String confirmPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New passwords do not match.");
        }
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters.");
        }
        if (newPassword.equals(currentPassword)) {
            throw new IllegalArgumentException("New password must be different from the current password.");
        }
        String newHashed = passwordEncoder.encode(newPassword);
        user.setPassword(newHashed);
        userRepository.save(user);
        return true;
    }
}
